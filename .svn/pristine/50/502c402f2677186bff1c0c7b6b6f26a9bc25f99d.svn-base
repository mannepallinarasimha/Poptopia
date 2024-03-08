package com.kelloggs.promotions.rewardservice.service.impl;

import static com.kelloggs.promotions.lib.constants.CodeConstants.AVAILABLE;
import static com.kelloggs.promotions.lib.constants.CodeConstants.LOOSERS;
import static com.kelloggs.promotions.lib.constants.CodeConstants.PROMO_CODE_STATUS;
import static com.kelloggs.promotions.lib.constants.CodeConstants.USED;
import static com.kelloggs.promotions.lib.constants.CodeConstants.WINNERS;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALL_REWARDS_USED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_ENTRY_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_PROFILE_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_ELIGIBLE_FOR_REWARD;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NO_PRIZE;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.REDEMPTION_LIMIT_REACHED;
import static com.kelloggs.promotions.lib.constants.PromotionType.PROMO_CODE;
import static com.kelloggs.promotions.lib.constants.PromotionType.RECEIPT;
import static com.kelloggs.promotions.lib.constants.PromotionType.REWARD_CODE;
import static com.kelloggs.promotions.lib.constants.StatusConstants.IN_WINNER_DRAW;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REWARD_CODE_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REWARD_EMAIL_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Item;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.exception.ApiExceptionExtended;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiListResponseExtended;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.lib.model.RedeemRewardMetaData;
import com.kelloggs.promotions.lib.repository.ItemRepo;
import com.kelloggs.promotions.lib.repository.PromotionCodeEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.ReceiptRepo;
import com.kelloggs.promotions.lib.repository.RewardRepo;
import com.kelloggs.promotions.lib.repository.RewardUsedRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.lib.rule.RuleExecutorService;
import com.kelloggs.promotions.lib.rule.impl.RewardRuleExecutorServiceImpl;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.rewardservice.service.RewardService;

@Service
public class RewardServiceImpl implements RewardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RewardServiceImpl.class);

	@Autowired
	private WinnerConfigRepo winnerConfigRepo;

	private final RewardRepo rewardRepo;

	private final RewardUsedRepo rewardUsedRepo;

	private final PromotionEntryRepo promotionEntryRepo;

	private final StatusService statusService;

	private final ReceiptRepo receiptRepo;

	private final ItemRepo itemRepo;
	
	private final PromotionCodeEntryRepo promotionCodeEntryRepo;
	
	private final PromotionRepo promotionRepo;

	public RewardServiceImpl(RewardRepo rewardRepo, RewardUsedRepo rewardUsedRepo,
			PromotionEntryRepo promotionEntryRepo, StatusService statusService, ReceiptRepo receiptRepo,
			ItemRepo itemRepo, PromotionCodeEntryRepo promotionCodeEntryRepo, PromotionRepo promotionRepo) {
		this.rewardRepo = rewardRepo;
		this.rewardUsedRepo = rewardUsedRepo;
		this.promotionEntryRepo = promotionEntryRepo;
		this.statusService = statusService;
		this.receiptRepo = receiptRepo;
		this.itemRepo = itemRepo;
		this.promotionCodeEntryRepo = promotionCodeEntryRepo;
		this.promotionRepo = promotionRepo;
	}

	@Override
	public ApiListResponse<Reward> getRewards(Integer promotionId, Integer entryId, Integer profileId,
			String promotionType, Integer maxRewardPerEntry) {

		List<PromotionEntry> promotionEntries = getPromotionEntries(promotionId, profileId);

		PromotionEntry promotionEntry = promotionEntries.stream() // checking if promotion Id sent is valid for the user
				.filter(entry -> entry.getId().equals(entryId)).findFirst().orElseThrow(() -> {
					LOGGER.info("Invalid Promotion Entry ID: {} for user with profile ID: {}", entryId, profileId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
							"Invalid Promotion Entry ID:: %d for user with profile ID:: %d", entryId, profileId));
				});
		return processEntryForReward(promotionEntries, promotionEntry, promotionType, promotionId, maxRewardPerEntry);
	}

	@Override
	public ApiListResponse<Reward> getRewardsRandomly(Integer promotionId, Integer entryId, Integer profileId,
			String promotionType, Integer maxRewardPerEntry) {

		List<PromotionEntry> promotionEntries = getPromotionEntries(promotionId, profileId);
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
		if (flagToCallProcessEntryForRewardMethod.equals("yes")) {
			return processEntryForReward(promotionEntries, promotionEntry, promotionType, promotionId, maxRewardPerEntry);
		} else if (flagToCallProcessEntryForRewardMethod.equals("no")) {
			if(promotionType.equals(PROMO_CODE.getType())) {
			promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
			promotionEntry.setAttr1Value(LOOSERS.getStatus());
			}
			promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
			promotionEntryRepo.save(promotionEntry);
			LOGGER.info(
					"User {} selected for a different reward than reward code or reward limit reached and {} for random selection. User has entered in daily draw",
					promotionEntry.getProfileId(), promotionEntry.getAttr1Value());
			if(null != promotionEntry.getAttr1Value()) {
			throw new ApiExceptionExtended(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
					"User %d selected for a different reward than reward code or reward limit reached. User has entered in daily draw",
					promotionEntry.getProfileId()), promotionEntry.getAttr1Value());
			}
			else {
			throw new ApiException(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
				   "User %d selected for a different reward than reward code or reward limit reached. User has entered in daily draw",
				   promotionEntry.getProfileId()));	
			}
		} else {
			return processEntryForReward(promotionEntries, promotionEntry, promotionType, promotionId, maxRewardPerEntry);
		}

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

	private List<Reward> generateRewards(PromotionEntry promotionEntry, Integer limit, String promotionType) {

		List<Reward> rewards = rewardRepo.getRewardsByQuantity(promotionEntry.getPromotion().getId(), AVAILABLE.getStatus(), limit)
				.orElseThrow(() -> {
					if(promotionType.equals(PROMO_CODE.getType())) {
					promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
					promotionEntry.setAttr1Value(LOOSERS.getStatus());
					promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
					promotionEntryRepo.save(promotionEntry);
					}
					LOGGER.info("No Reward codes left for promotion with ID: {}", promotionEntry.getPromotion().getId());
					if(null != promotionEntry.getAttr1Value()) {
					return new ApiExceptionExtended(HttpStatus.NOT_FOUND, ALL_REWARDS_USED.getCode(),
							String.format("No Reward codes left for promotion with ID:: %d", promotionEntry.getPromotion().getId()),promotionEntry.getAttr1Value());}
					else {
					return new ApiException(HttpStatus.NOT_FOUND, ALL_REWARDS_USED.getCode(),
							String.format("No Reward codes left for promotion with ID:: %d", promotionEntry.getPromotion().getId()));	
					}
				});

		rewards = rewards.stream().peek(reward -> reward.setStatus(USED.getStatus())).collect(Collectors.toList());

		return rewardRepo.saveAll(rewards);
	}

	private List<Reward> getRewardsForEntry(PromotionEntry promotionEntry, String promotionType, Integer maxRewardPerEntry) {

		if ((promotionType.equals(PROMO_CODE.getType())) || (promotionType.equals(REWARD_CODE.getType()))) {
			return generateRewards(promotionEntry, 1,promotionType);
		} else if (promotionType.equals(RECEIPT.getType())) {

			List<Receipt> receipts = receiptRepo.findByPromotionEntryId(promotionEntry.getId())
					.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(),
							String.format("No receipt found for promotion entry with id: %d", promotionEntry.getId())));

			Integer totalItems = 0;
			for(Receipt r: receipts) {
				List<Item> items = itemRepo.findByReceiptId(r.getId())
						
						.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(),
								String.format("No participating products found in receipt for promotion entry with id: %d",
										promotionEntry.getId())));

				 totalItems = totalItems + items.stream().map(Item::getQty).reduce(0, (qty, total) -> total + qty);
				 
				 // Limit max reward per entry for user
				 totalItems = (maxRewardPerEntry > 0 && maxRewardPerEntry < totalItems) ? maxRewardPerEntry : totalItems;

			}
			
				
			return generateRewards(promotionEntry, totalItems, promotionType);
		}
		throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format("Invalid promotionType: %s", promotionType));
	}

	private List<PromotionEntry> getPromotionEntries(Integer promotionId, Integer profileId) {
		List<PromotionEntry> promotionEntries = promotionEntryRepo.findByPromotionIdAndProfileId(promotionId, profileId) // getting
																															// promotions
																															// entries
																															// of
																															// user
				.orElseThrow(() -> {
					LOGGER.info("No promotion entries found for user with profile ID: {} under promotion with ID: {}",
							profileId, promotionId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_PROFILE_ID.getCode(), String.format(
							"No promotion entries found for user with profile ID:: %d under promotion with ID:: %d",
							profileId, promotionId));
				});

		return promotionEntries;
	}

	private ApiListResponse<Reward> processEntryForReward(List<PromotionEntry> promotionEntries,
			PromotionEntry promotionEntry, String promotionType, Integer promotionId, Integer maxRewardPerEntry) {

		final String entryStatus = promotionEntry.getStatus().getType(); // getting status_id of promotion_entry like
																			// 101(VERIFIED),etc
		final String rewardString = "Reward Code for user with Profile ID:: %d";

		if (entryStatus.equals(REWARD_CODE_SENT.getStatus()) || entryStatus.equals(REWARD_EMAIL_SENT.getStatus())) {

			List<Integer> entries = new ArrayList<>();
			entries.add(promotionEntry.getId()); 

			List<RewardUsed> rewardUsedList = rewardUsedRepo.findByPromotionEntryIdIn(entries); 

			List<Reward> rewards = rewardUsedList.stream() 
					.map(rewardUsed -> rewardRepo.findByRewardCode(rewardUsed.getRewardCode()))
					.collect(Collectors.toList());

		
			if(null != promotionEntry.getAttr1Value()) {
			return new ApiListResponseExtended<>(String.format(rewardString, promotionEntry.getProfileId()), rewards,
					rewards.size(), promotionEntry.getAttr1Value());
			}
			else {
				return new ApiListResponse<>(String.format(rewardString, promotionEntry.getProfileId()), rewards,
						rewards.size());
			}

		} else if (entryStatus.equals(IN_WINNER_DRAW.getStatus())) {

			LOGGER.info(
					"User {} selected for a different reward than reward code or reward limit reached and {} for random selection. User has entered in daily draw",
					promotionEntry.getProfileId(), promotionEntry.getAttr1Value());
			if(null != promotionEntry.getAttr1Value()) {
			throw new ApiExceptionExtended(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
					"User %d selected for a different reward than reward code or reward limit reached. User has entered in daily draw",
					promotionEntry.getProfileId()), promotionEntry.getAttr1Value());}
			else {
				throw new ApiException(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
					"User %d selected for a different reward than reward code or reward limit reached. User has entered in daily draw",
					promotionEntry.getProfileId()));
			}

		} else if (entryStatus.equals(VERIFIED.getStatus())) {

			final Integer MAX_REDEMPTION_LIMIT = promotionEntry.getPromotion().getMaxLimit(); // max redemption limit

			if (MAX_REDEMPTION_LIMIT == 0) { // If no redemption limit

				List<Reward> rewards = getRewardsForEntry(promotionEntry, promotionType, maxRewardPerEntry);
				updateInfoOfRewards(rewards, promotionEntry);
				if(promotionType.equals(PROMO_CODE.getType())) {
				promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
				promotionEntry.setAttr1Value(WINNERS.getStatus());
				promotionEntryRepo.save(promotionEntry);
				}
				if(null != promotionEntry.getAttr1Value()) {
				return new ApiListResponseExtended<>(String.format(rewardString, promotionEntry.getProfileId()),
						rewards, rewards.size(), promotionEntry.getAttr1Value());
				}
				else {
					return new ApiListResponse<>(String.format(rewardString, promotionEntry.getProfileId()),
						rewards, rewards.size());
				}

			} else {

				List<Integer> entryIds = promotionEntries.stream().map(PromotionEntry::getId)
						.collect(Collectors.toList());

				List<RewardUsed> rewardedEntries = rewardUsedRepo.findByPromotionEntryIdIn(entryIds);

				if (rewardedEntries.size() < MAX_REDEMPTION_LIMIT) {

					List<Reward> rewards = getRewardsForEntry(promotionEntry, promotionType, maxRewardPerEntry);
					updateInfoOfRewards(rewards, promotionEntry);
					if(promotionType.equals(PROMO_CODE.getType())) {
					promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
					promotionEntry.setAttr1Value(WINNERS.getStatus());
					promotionEntryRepo.save(promotionEntry);
					}
					if(null != promotionEntry.getAttr1Value()) {
					return new ApiListResponseExtended<>(String.format(rewardString, promotionEntry.getProfileId()),
							rewards, rewards.size(), promotionEntry.getAttr1Value());
					}
					else {
						return new ApiListResponse<>(String.format(rewardString, promotionEntry.getProfileId()),
								rewards, rewards.size());
					}

				} else {

					promotionEntry.setStatus(statusService.getStatus(IN_WINNER_DRAW.getStatus()));
					if(promotionType.equals(PROMO_CODE.getType())) {
					promotionEntry.setAttr1Code(PROMO_CODE_STATUS.getStatus());
					promotionEntry.setAttr1Value(LOOSERS.getStatus());
					}
					promotionEntryRepo.save(promotionEntry);
					
					LOGGER.info(
							"User {} selected for a different reward than reward code or reward limit reached and {} for random selection. User has entered in daily draw",
							promotionEntry.getProfileId(), promotionEntry.getAttr1Value());
					if(null != promotionEntry.getAttr1Value()) {
					throw new ApiExceptionExtended(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
							"User %d selected for a different reward than reward code or reward limit reached . User has entered in daily draw",
							promotionEntry.getProfileId()), promotionEntry.getAttr1Value());
					}
					else {
						throw new ApiException(HttpStatus.OK, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
								"User %d selected for a different reward than reward code or reward limit reached . User has entered in daily draw",
								promotionEntry.getProfileId()));	
					}
				}
			}
		}

		LOGGER.info("User with profile Id :: {} not eligible for reward for the promotion entry ID:: {} ",
				promotionEntry.getProfileId(), promotionEntry.getId());
		throw new ApiException(HttpStatus.OK, NOT_ELIGIBLE_FOR_REWARD.getCode(),
				String.format("User with profile Id :: %d not eligible for reward for the promotion entry ID:: %d ",
						promotionEntry.getProfileId(), promotionEntry.getId()));

	}
	
	@Override
	@Transactional
    public RedeemRewardDTO redeemReward(Integer profileId, Integer promotionId, RedeemRewardMetaData redeemRewardMetaData) {
		
		List<PromotionCodeEntry> validCodes =  promotionCodeEntryRepo.findByProfileIdAndRewardUsedIdIsNull(profileId).orElse(Collections.emptyList());
		Integer exchangeCodePerReward = Objects.nonNull(redeemRewardMetaData.getExchangeCodePerReward()) ? redeemRewardMetaData.getExchangeCodePerReward() : validCodes.size();

		//Check if user has a minimum number of valid codes for reward redemption.
		if(exchangeCodePerReward > validCodes.size()) {
									 
		throw new ApiException(HttpStatus.EXPECTATION_FAILED, NOT_ELIGIBLE_FOR_REWARD.getCode(), String.format(
					"User with profileId: %d does not have enough valid codes for redeeming the reward.",profileId));
						
		}
		
		List<RewardUsed> redeemedRewards = rewardUsedRepo.findByProfileId(profileId).orElse(null);
		
		//Check if the user has already redeemed max number of rewards
		if((Objects.nonNull(redeemRewardMetaData.getMaxRewardPerUser()) && Objects.nonNull(redeemedRewards))  ? 
				redeemRewardMetaData.getMaxRewardPerUser() <= redeemedRewards.size(): Boolean.FALSE) {
			
			throw new ApiException(HttpStatus.BAD_REQUEST, REDEMPTION_LIMIT_REACHED.getCode(), String.format(
					"User with profileId: %d has reached the maximum reward limit.", profileId));
		}	
		
		//Select an available reward for the user
		Reward availableReward = rewardRepo.findAllByPromotionIdAndStatusAndRewardStep(promotionId, AVAILABLE.getStatus().toString(), exchangeCodePerReward.toString(),
				PageRequest.of(0,1)).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, NO_PRIZE.getCode(), String.format(
						"There are no rewards available based on number of codes required for reward redemption."))).get(0);				 
			
			availableReward.setStatus(USED.getStatus());
			rewardRepo.save(availableReward);
			
			RewardUsed redeemedReward = new RewardUsed();
			redeemedReward.setProfileId(profileId);
			redeemedReward.setReward(availableReward);
			redeemedReward.setRewardCode(availableReward.getRewardCode());
			redeemedReward.setPromotion(Objects.nonNull(promotionId) ? promotionRepo.findById(promotionId).orElse(null) : null) ;
			rewardUsedRepo.save(redeemedReward);
			
			validCodes.stream().limit(exchangeCodePerReward).forEach(code -> code.setRewardUsed(redeemedReward));	
			promotionCodeEntryRepo.saveAll(validCodes);
			
			RedeemRewardDTO rewardEntry = new RedeemRewardDTO();
			Set<RewardUsed> rewardSet = new HashSet<>();
			rewardEntry.setProfileId(profileId);
			rewardSet.add(redeemedReward);
			rewardEntry.setRedeemedRewards(rewardSet);		 
		
		 return rewardEntry;
    }


}
