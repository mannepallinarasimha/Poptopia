package com.kelloggs.promotions.winnerservice.services.impl;

import static com.kelloggs.promotions.lib.constants.CodeConstants.AVAILABLE;
import static com.kelloggs.promotions.lib.constants.CodeConstants.LOOSERS;
import static com.kelloggs.promotions.lib.constants.CodeConstants.PROMO_CODE_STATUS;
import static com.kelloggs.promotions.lib.constants.CodeConstants.USED;
import static com.kelloggs.promotions.lib.constants.CodeConstants.WINNERS;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALL_REWARDS_USED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_ENTRY_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_PROFILE_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NO_ELIGIBLE_ENTRIES;
import static com.kelloggs.promotions.lib.constants.PromotionType.PROMO_CODE;
import static com.kelloggs.promotions.lib.constants.StatusConstants.CLOSED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.IN_WINNER_DRAW;
import static com.kelloggs.promotions.lib.constants.StatusConstants.LOST;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REWARD_CODE_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.WON;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.INFO;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelloggs.promotions.lib.constants.ErrorCodes;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.entity.Winner;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiListResponseExtended;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromoMetaObject;
import com.kelloggs.promotions.lib.model.WinDateConfig;
import com.kelloggs.promotions.lib.repository.PromotionCodeEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.RewardRepo;
import com.kelloggs.promotions.lib.repository.RewardUsedRepo;
import com.kelloggs.promotions.lib.repository.TokenRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.lib.repository.WinnerRepo;
import com.kelloggs.promotions.lib.rule.RuleExecutorService;
import com.kelloggs.promotions.lib.rule.Selector;
import com.kelloggs.promotions.lib.rule.impl.PercentageBasedInstantWinnerSelector;
import com.kelloggs.promotions.lib.rule.impl.RewardRuleExecutorServiceImpl;
import com.kelloggs.promotions.lib.rule.impl.WinningMomentBasedInstantWinnerSelector;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.lib.utilities.ServiceUtil;
import com.kelloggs.promotions.winnerservice.services.PromotionPrizeService;
import com.kelloggs.promotions.winnerservice.services.WinnerSelectionService;
import com.kelloggs.promotions.winnerservice.services.WinnerService;
import com.kelloggs.promotions.winnerservice.utilities.WinnerUtil;

@Service
public class WinnerSelectionServiceImpl implements WinnerSelectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WinnerSelectionServiceImpl.class);

	@Autowired
	private WinnerConfigRepo winnerConfigRepo;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	private PromotionPrizeService promotionPrizeService;

	private WinnerRepo winnerRepo;

	private PromotionEntryRepo promotionEntryRepo;

	private WinnerService winnerService;

	private StatusService statusService;

	private final RewardRepo rewardRepo;

	private final RewardUsedRepo rewardUsedRepo;
	
	private final TokenRepo tokenRepo;
		
	private final PromotionCodeEntryRepo promotionCodeEntryRepo;
	
	private static final ApiLogger API_LOGGER = 
			new ApiLogger(WinnerSelectionServiceImpl.class);

	public WinnerSelectionServiceImpl(WinnerRepo winnerRepo, PromotionEntryRepo promotionEntryRepo,
			WinnerService winnerService, StatusService statusService, RewardRepo rewardRepo,
			RewardUsedRepo rewardUsedRepo, TokenRepo tokenRepo, PromotionCodeEntryRepo promotionCodeEntryRepo) {
		this.winnerRepo = winnerRepo;
		this.promotionEntryRepo = promotionEntryRepo;
		this.winnerService = winnerService;
		this.statusService = statusService;
		this.rewardRepo = rewardRepo;
		this.rewardUsedRepo = rewardUsedRepo;
		this.tokenRepo = tokenRepo;
		this.promotionCodeEntryRepo = promotionCodeEntryRepo;
	}

	@Override
	public ApiResponse<Winner> getWinner(Integer promotionId, Date startDate, Date endDate) {

		List<PromotionEntry> validEntries = winnerService.getValidEntriesForPromotion(promotionId, startDate, endDate);

		Winner winner = generateWinnerForPromotion(validEntries, promotionId);
		return new ApiResponse<>(String.format("Winner selected for promotion Id::  %d", promotionId), winner);
	}

	private Winner generateWinnerForPromotion(List<PromotionEntry> promotionEntries, Integer promotionId) {

		Integer size = promotionEntries.size();

		if (size > 0) {
			Integer winnerId = new SecureRandom().nextInt(size);
			PromotionEntry winnerEntry = promotionEntries.get(winnerId);

			winnerEntry.setStatus(statusService.getStatus(CLOSED.getStatus()));
			winnerEntry = promotionEntryRepo.save(winnerEntry);

			Winner winner = new Winner();
			winner.setPromotionEntry(winnerEntry);

			return winnerRepo.save(winner);
		}

		throw new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(),
				String.format("No eligible entries found for promotion Id: %d", promotionId));
	}

	@Override
	public ApiResponse<Winner> addWinner(Integer entryId) {

		PromotionEntry promotionEntry = promotionEntryRepo.findById(entryId)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(),
						String.format("Invalid Entry Id:: %d", entryId)));

		promotionEntry.setStatus(statusService.getStatus(CLOSED.getStatus()));
		promotionEntry = promotionEntryRepo.save(promotionEntry);

		Winner winner = new Winner();
		winner.setPromotionEntry(promotionEntry);

		Winner selectedWinner = winnerRepo.save(winner);

		return new ApiResponse<>("Winner Selected", selectedWinner);
	}

	private List<PromotionEntry> getAllPromotionEntries(Integer promotionId, Integer profileId) {
		// getting promotions entries of user
		List<PromotionEntry> promotionEntries = promotionEntryRepo.findByPromotionIdAndProfileId(promotionId, profileId)
				.orElseThrow(() -> {
					LOGGER.info("No promotion entries found for user with profile ID: {} under promotion with ID: {}",
							profileId, promotionId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_PROFILE_ID.getCode(), String.format(
							"No promotion entries found for user with profile ID:: %d under promotion with ID:: %d",
							profileId, promotionId));
				});

		return promotionEntries;
	}

	@Override
	public Map<Reward, String> selectWinOrLostForMoment(Integer promotionId, Integer profileId, Integer entryId,
			PromoMetaObject promoMetaObject) {

		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, profileId);

		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});

		Reward rewardObject = new Reward();

		rewardObject.setProfileId(profileId);

		Map<Reward, String> responseObj = new HashMap<>();
		String responseValue = new String();

		WinDateConfig finalMetaObj = new WinDateConfig();
		String trimLocalCreatedDateTime = promotionEntry.getCreatedLocalDateTime().toString().split("\\.")[0];
		LocalDateTime entryLocalDateTime = getParsedLocalDateTime(trimLocalCreatedDateTime);
		Integer currentWinCountBetweenStartAndEnd = 0;
		// exception handled for winDateConfig
		for (WinDateConfig meta : promoMetaObject.getWinDateConfig()) {
			LocalDateTime startDateTime = getParsedLocalDateTime(meta.getStart()); // ex: 2021-10-06 12:56:99.99999
			LocalDateTime endDateTime = getParsedLocalDateTime(meta.getEnd());

			if ((entryLocalDateTime.isEqual(startDateTime))
					|| (promotionEntry.getCreatedLocalDateTime().isAfter(startDateTime))
							&& (promotionEntry.getCreatedLocalDateTime().isBefore(endDateTime))) {
				finalMetaObj.setStart(startDateTime.toString());
				finalMetaObj.setEnd(endDateTime.toString());
				finalMetaObj.setWinCount(meta.getWinCount());
				break;
			}
		}

		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		if ((null != finalMetaObj.getStart()) && (null != finalMetaObj.getEnd())) {
			startDateTime = getParsedLocalDateTime(finalMetaObj.getStart().concat(":00"));
			endDateTime = getParsedLocalDateTime(finalMetaObj.getEnd().concat(":00"));
			try {// current count of winners in the time range ex=2
				currentWinCountBetweenStartAndEnd = promotionEntryRepo
						.currentWinCountBetweenStartAndEnd(promotionId, startDateTime, endDateTime).get();
			} catch (Exception e) {
				throw new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(),
						String.format("No Promotion Entries found for promotion ID:: %d ", promotionId));
			}
		} else {
			LOGGER.error("No win start date and end date present for the entry");
		}

		if (null != finalMetaObj.getStart() && promotionEntry.getStatus().getType().equals(VERIFIED.getStatus())
				&& currentWinCountBetweenStartAndEnd < finalMetaObj.getWinCount()) {

				promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
				promotionEntry.setAttr1Value(WINNERS.getStatus());
				promotionEntryRepo.save(promotionEntry);
				responseValue = WINNERS.getStatus();

				responseObj.put(rewardObject, responseValue);
				return responseObj;

		} else {
			promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
			promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
			promotionEntry.setAttr1Value(LOOSERS.getStatus());
			promotionEntryRepo.save(promotionEntry);
			responseValue = LOOSERS.getStatus();

			responseObj.put(rewardObject, responseValue);
			return responseObj;

		}
	}

	private LocalDateTime getParsedLocalDateTime(String createdLocalDateTime) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime localDateTime = LocalDateTime
				.parse(createdLocalDateTime.toString().replaceAll("[-+' '+:+'T'+.]*", ""), formatter);
		return localDateTime;
	}

	@Override
	public ApiListResponse<?> getRewardForOnlineWinSelection(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep, List<Reward> responseKeyObj) {

		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, profileId);

		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});

		if (responseValueObj.equals("WON")) {
			List<Reward> rewards = getRewardsForEntry(promotionEntry, "promocode", rewardStep);
			updateInfoOfRewards(rewards, promotionEntry);
			return new ApiListResponseExtended<>(
					String.format("Reward Code for win user with Profile ID  :: %d", profileId), rewards,
					rewards.size(), responseValueObj);
		}
		return new ApiListResponseExtended<>(String.format("User lost with Profile ID  :: %d", profileId),
				responseKeyObj, responseKeyObj.size(), responseValueObj);
	}

	private List<Reward> getRewardsForEntry(PromotionEntry promotionEntry, String promotionType, String rewardStep) {

		if (promotionType.equals(PROMO_CODE.getType())) {
			return generateRewards(promotionEntry, 1, rewardStep);
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Invalid promotionType: %s", promotionType));
		}
	}

	private List<Reward> generateRewards(PromotionEntry promotionEntry, Integer limit, String rewardStep) {

		List<Reward> rewards = rewardRepo
				.getRewardsByQuantities(promotionEntry.getPromotion().getId(), AVAILABLE.getStatus(), rewardStep, limit)
				.orElseThrow(() -> {
					LOGGER.info("No Reward codes left for promotion with ID: {}",
							promotionEntry.getPromotion().getId());
					return new ApiException(HttpStatus.NOT_FOUND, ALL_REWARDS_USED.getCode(), String.format(
							"No Reward codes left for promotion with ID:: %d", promotionEntry.getPromotion().getId()));

				});

		rewards = rewards.stream().peek(reward -> {
			reward.setStatus(USED.getStatus());
			reward.setProfileId(promotionEntry.getProfileId());
		}).collect(Collectors.toList());
		return rewardRepo.saveAll(rewards);
	}

	private void updateInfoOfRewards(List<Reward> rewards, PromotionEntry promotionEntry) {

		List<RewardUsed> rewardUsedList = rewards.stream().map(reward -> {
			RewardUsed rewardUsed = new RewardUsed();
			rewardUsed.setPromotionEntry(promotionEntry);
			rewardUsed.setRewardCode(reward.getRewardCode());
			return rewardUsed;
		}).collect(Collectors.toList());

		rewardUsedRepo.saveAll(rewardUsedList);

		promotionEntry.setStatus(statusService.getStatus(REWARD_CODE_SENT.getStatus()));
		promotionEntryRepo.save(promotionEntry);
	}

	@Override
	public ApiListResponse<?> getResponseForOfflineWinSelection(List<Reward> responseKeyObj, String responseValueObj,
			Integer promotionId, Integer entryId) {
		if (responseValueObj.equals("WON")) {
			return new ApiListResponseExtended<Reward>(
					String.format("User Won for promotion Id %d having entry Id  %d:: ",
							promotionId, entryId),
					responseKeyObj, responseKeyObj.size(), responseValueObj);
		} else {
			return new ApiListResponseExtended<Reward>(
					String.format("User Lost for promotion Id %d having entry Id  %d:: ",
							promotionId, entryId),
					responseKeyObj, responseKeyObj.size(), responseValueObj);
		}

	}
	
	@Override
	public ApiListResponse<?> getRewardOnlyForValidUser(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep) {
		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, profileId);

		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});

		List<Reward> rewards = getRewardsForEntry(promotionEntry, "promocode", rewardStep);
		updateInfoOfRewards(rewards, promotionEntry);
		return new ApiListResponseExtended<>(
				String.format("Reward Code for valid user with Profile ID  :: %d", profileId), rewards,
				rewards.size(), responseValueObj);
	}

	@Override
	public ApiListResponse<?> getRewardForValidWinUser(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep) {
		String validObjString = rewardStep.split("\\+")[0];
		String winObjString = rewardStep.split("\\+")[1];
		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, profileId);

		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});

		List<Reward> rewardForValid = getRewardsForEntry(promotionEntry, "promocode", validObjString);
		updateInfoOfRewards(rewardForValid, promotionEntry);

		if (responseValueObj.equals("WON")) {
			List<Reward> combineRewardForValidWin = new ArrayList<>();
			for (Reward reward : rewardForValid) {
				combineRewardForValidWin.add(reward);
			}
			List<Reward> rewardForWin = getRewardsForEntry(promotionEntry, "promocode", winObjString);
			updateInfoOfRewards(rewardForWin, promotionEntry);
			for (Reward reward : rewardForWin) {
				combineRewardForValidWin.add(reward);
			}

			return new ApiListResponseExtended<>(String
					.format("Reward Code for Valid as well as win user with Profile ID  :: %d", profileId),
					combineRewardForValidWin, combineRewardForValidWin.size(), responseValueObj);
		}
		return new ApiListResponseExtended<>(
				String.format("Reward Code for Valid user who lost with Profile ID  :: %d", profileId),
				rewardForValid, rewardForValid.size(), responseValueObj);
	}

	@Override
	public Map<Reward, String> selectWinOrLostForPercentage(Integer promotionId, Integer profileId, Integer entryId) {
		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, profileId);
		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});

		List<PromotionEntry> promotionEntriesList = null;
		try {
			promotionEntriesList = promotionEntryRepo
					.getEntriesHavingWonStatus(promotionId, PROMO_CODE_STATUS.getStatus(), WINNERS.getStatus()).get();
		} catch (Exception exp) {
			LOGGER.info("No promotion entries found under promotion with ID: {} and caused by {}", promotionId,
					exp.getMessage());
		}

        //fetching the required date promotion's data from the winner config table
		List<WinnerConfig> winnerConfigs = null;
		try {
			winnerConfigs = winnerConfigRepo.findWinnerConfigFromDB(promotionId).get();
		} catch (Exception exp) {
			LOGGER.error("********* error in findWinnerConfigFromDB** {}", exp.getMessage());
		}

		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		WinnerConfig winnerConfig = winnerConfigs.stream()
				.filter(entry -> entry.getPromotionDate().toString().equals(promotionEntry.getCreatedLocalDateTime().format(date))).findFirst()
				.orElseThrow(() -> {
					LOGGER.info("No WinnerConfig data present for date: {} with entry ID : {}",
							promotionEntry.getCreatedDate(), entryId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(),
							String.format("No WinnerConfig data present for date: {} with entry ID : {}",
									promotionEntry.getCreatedDate(), entryId));
				});

		//creating and calling RewardRuleExecutorServiceImpl object for executing set of rules 
		RuleExecutorService ruleExecutorServiceObj = new RewardRuleExecutorServiceImpl();
		Map<String, Object> objRuleMap = new HashMap<String, Object>();

		objRuleMap.put("PromotionEntry", promotionEntry); // putting a unique indentifier to identify class name in Map's key
		objRuleMap.put("promotionEntriesList", promotionEntriesList);
		objRuleMap.put("WinnerConfig", winnerConfig);

		Map<String, Object> resultMapFromRuleExecutor = ruleExecutorServiceObj.execute(objRuleMap);
		String flagToCallProcessEntryForRewardMethod = (String) resultMapFromRuleExecutor
				.get("FlagToCallProcessEntryForRewardMethod");
		Reward rewardObject = new Reward();

		rewardObject.setProfileId(profileId);

		Map<Reward, String> responseObj = new HashMap<>();
		String responseValue = new String();

		if (flagToCallProcessEntryForRewardMethod.equals("yes")) {
			final Integer MAX_REDEMPTION_LIMIT = promotionEntry.getPromotion().getMaxLimit(); // max redemption limit

			if (MAX_REDEMPTION_LIMIT == 0) { // If no redemption limit
				promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
				promotionEntry.setAttr1Value(WINNERS.getStatus());
				promotionEntryRepo.save(promotionEntry);
				responseValue = WINNERS.getStatus();
				responseObj.put(rewardObject, responseValue);
				return responseObj;
				
			} else {

				List<Integer> entryIds = promotionEntries.stream().map(PromotionEntry::getId)
						.collect(Collectors.toList());

				List<RewardUsed> rewardedEntries = rewardUsedRepo.findByPromotionEntryIdIn(entryIds);

				if (rewardedEntries.size() < MAX_REDEMPTION_LIMIT) {
					promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
					promotionEntry.setAttr1Value(WINNERS.getStatus());
					promotionEntryRepo.save(promotionEntry);
					responseValue = WINNERS.getStatus();
					responseObj.put(rewardObject, responseValue);
					return responseObj;
				}
				else {
					promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
					promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
					promotionEntry.setAttr1Value(LOOSERS.getStatus());
					promotionEntryRepo.save(promotionEntry);
					responseValue = LOOSERS.getStatus();
					responseObj.put(rewardObject, responseValue);
					LOGGER.info(
							"User {} selected for a different reward than reward code or reward limit reached and {} for random selection. User has entered in daily draw",
							promotionEntry.getProfileId(), responseValue);
					return responseObj;
	
				}
			}
			} else {
				promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
				promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
				promotionEntry.setAttr1Value(LOOSERS.getStatus());
				promotionEntryRepo.save(promotionEntry);
				responseValue = LOOSERS.getStatus();
				responseObj.put(rewardObject, responseValue);
				LOGGER.info(
						"User {} selected for a different reward than reward code or reward limit reached and {} for random selection. User has entered in daily draw",
						promotionEntry.getProfileId(), responseValue);
				return responseObj;		 
		}
	}
	
	
	@Override
	public Token decideWinnerOrLoser(Integer promotionEntryId, Boolean hasSpecialEligibility) {

		Token resultToken = null;
		
		// Get promotion entry details from database
		PromotionEntry promotionEntry = promotionEntryRepo.findById(promotionEntryId).orElse(null);
				
		if (Boolean.TRUE.equals(WinnerUtil.isValidPromotionEntry(promotionEntry))) {
			
			API_LOGGER.log(INFO, ":::::::::::::::::::::  WINNER/LOSER SELECTION START  :::::::::::::::::::::");
			API_LOGGER.log(INFO, String.format(":: [PromotionId: %d, PromotionEntryId: %d, ProfileId: %d, SpecialEligibility: %s] :: ", 
					promotionEntry.getPromotion().getId(), promotionEntry.getId(), promotionEntry.getProfileId(), hasSpecialEligibility));
			
			// Get current winner selection configuration from database
			WinnerConfig winnerConfig = winnerConfigRepo.findByPromotionIdAndTime(promotionEntry.getPromotion().getId(), 
					promotionEntry.getCreatedLocalDateTime(), Sort.by("endTime")).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, 
							NO_ELIGIBLE_ENTRIES.getCode(), String.format("No winner selection config set for promotion with id: %d", 
									promotionEntry.getPromotion().getId()), API_LOGGER.setLogLevel(ERROR))).get(0);
			
			if (Boolean.TRUE.equals(hasSpecialEligibility)) {
				
				// Add the special eligibility criteria for the winner selection
				promotionEntry.setAttr1Code("specialEligibility");
				promotionEntry.setAttr1Value(hasSpecialEligibility.toString());
			}
			
			// Randomly decide the winner/loser for the given promotion entry
			Selector<PromotionEntry, WinnerConfig, Boolean> selector = applicationContext.getBean(PercentageBasedInstantWinnerSelector.class);
			boolean selectionResult = selector.select(promotionEntry, winnerConfig);
					
			// Update the promotion entry with selection result
			promotionEntry.setStatus(statusService.getStatus((selectionResult ? WON : LOST).getStatus()));
			promotionEntryRepo.save(promotionEntry);
	
			// Save the generated token in database
			Token generatedToken = ServiceUtil.generateToken(promotionEntry);
			generatedToken.setAnswer(promotionEntry.getStatus().getType());
            resultToken = tokenRepo.save(generatedToken);
            
			API_LOGGER.log(INFO, String.format("Token Generated with selection result for given promotion entry Id[%d] "
					+ ":: {token: %s, result: %s}", promotionEntry.getId(), resultToken.getHashCode(), resultToken.getAnswer()));
			API_LOGGER.log(INFO, "::::::::::::::::::::::  WINNER/LOSER SELECTION END  :::::::::::::::::::::");
            
		}
		
		return resultToken;
	}
	
	@Override @Transactional
	public PromotionEntry decideWinnerOrLoser(PromotionEntry promotionEntry, LocalDateTime userRequestTime, Boolean hasPrizeSelection) {
        
		Promotion promotion = promotionEntry.getPromotion();
		
		API_LOGGER.log(INFO, String.format("Winner & Prize selecton request :: [PromotionId: %d, PromotionEntryId: %d, "
				+ "ProfileId: %d, UserRequestTime: %s]", promotion.getId(), promotionEntry.getId(), promotionEntry.getProfileId(), userRequestTime));
				
		// Get current winner selection configuration / winning moment from database based on the current request time
		WinnerConfig winnerConfig = winnerConfigRepo.findByPromotionsIdAndTimeAndMaxWinnerGreaterThan(promotion.getId(), 
				userRequestTime, 0, Sort.by("startTime")).orElse(Arrays.asList(new WinnerConfig())).get(0);
		
		// Decide the winner/loser for the given promotion entry based on the winning moment
		Selector<PromotionEntry, WinnerConfig, Boolean> selector = applicationContext.getBean(WinningMomentBasedInstantWinnerSelector.class);
		boolean isWinner = selector.select(promotionEntry, winnerConfig);
		
		if (isWinner) {
			// Decrease the max winner count and update the winner configuration
			winnerConfig.setMaxWinner(winnerConfig.getMaxWinner() - 1);
			winnerConfigRepo.save(winnerConfig);
		}
					
		// Update the promotion entry with winner selection result and prize details
		promotionEntry.setStatus(statusService.getStatus((isWinner ? WON : LOST).getStatus()));
		promotionEntry.setAttr1Code("isWinner"); promotionEntry.setAttr1Value(Boolean.toString(isWinner));
		
		if (Boolean.TRUE.equals(hasPrizeSelection)) {
			// Distribute prize if the given user entry is selected as winner
			promotionEntry.setAttr2Code("prize"); 
			promotionEntry.setAttr2Value(isWinner && Objects.nonNull(promotion.getAttr1_value()) ? promotion.getAttr1_value() : "null");
		}
		
		API_LOGGER.log(INFO, String.format("Winner & Prize selection response for given promotion entry Id[%d] "
				+ ":: {isWinner: %s, Prize: %s}", promotionEntry.getId(), isWinner, promotionEntry.getAttr2Value()));
        
		// Save the updated promotion entry and return
		return promotionEntryRepo.save(promotionEntry);
	}
	
	@Override
	public PromotionPrize selectPrizeCard(PromotionEntry promotionEntry, Boolean hasRandomSelection) {
			
		PromotionPrize selectedPrize = null;
		final String FOURTH_CARD  = "FOURCARDEMITITGPP";
		final String NINTH_CARD  = "NINECARDEMITITGPP";
		
		// Get all the consumed prize card of the user
			Set<String> userWonPrizes = promotionPrizeService.getEntriesHasWonPrize(promotionEntry.getPromotion().getId(), 
					promotionEntry.getProfileId()).stream().map(PromotionEntry::getAttr1Value).collect(Collectors.toSet());
			
		if(Boolean.FALSE.equals(hasRandomSelection)){
			
			//Fetching the promoCodeId tagged to the user entry.
			PromotionCodeEntry promoCodeEntry =  promotionCodeEntryRepo.findAllByPromotionEntryId(promotionEntry.getId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, 
					NO_ELIGIBLE_ENTRIES.getCode(), String.format("No associated promotion code was found for entryId: %d.",promotionEntry.getId()))).get(0);
								
			// Select a prize card for the user entry which is tagged to a specific promoCode.
			selectedPrize = promotionPrizeService.getSpecificPrizeConfigs(promotionEntry.getPromotion().getId(), promoCodeEntry.getPromotionCode().getId().toString(), true)
					.stream().filter(specificPrize -> !userWonPrizes.contains(specificPrize.getPrizeCode())).findFirst().orElse(null);
		}
		
		else {
								
			// Select a prize card for the user entry from all the available eligible prize cards
			    selectedPrize = promotionPrizeService.getDefaultPrizeConfigs(promotionEntry.getPromotion().getId(), true)
					.stream().filter(defaultPrize -> !userWonPrizes.contains(defaultPrize.getPrizeCode())).findFirst().orElse(null);
		}
	
		if (Objects.nonNull(selectedPrize)) {
						
			// Update the user entry with the selected prize card details in database
			promotionEntry.setAttr1Code("prizeCode");
			promotionEntry.setAttr1Value(selectedPrize.getPrizeCode());
			promotionEntry.setPrizeStatus(statusService.getStatus(WON.getStatus()));
			promotionEntryRepo.save(promotionEntry);
			
			if(userWonPrizes.size()+ 1 == 4){
				selectedPrize.setAttribute(FOURTH_CARD);
			}
			else if(userWonPrizes.size()+ 1 == 9){
				selectedPrize.setAttribute(NINTH_CARD);
			}
			
		} else {
			
			// Throw error, if all the prize cards are already won by the user
			throw new ApiException(HttpStatus.IM_USED, ErrorCodes.ALL_REWARDS_USED.getCode(), 
					String.format("All the prize cards are consumed by user with profile id: %d for promotion id: %d", 
							promotionEntry.getProfileId(), promotionEntry.getPromotion().getId()), API_LOGGER.setLogLevel(INFO));
		}
			
		return selectedPrize;
	}
}
