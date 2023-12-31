package com.kelloggs.promotions.winnerservice.services.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NO_ELIGIBLE_ENTRIES;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NO_PRIZE;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.REDEMPTION_LIMIT_REACHED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.LOST;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.WON;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.INFO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.constants.ErrorCodes;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionPrizeRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.winnerservice.services.PromotionPrizeService;
import com.kelloggs.promotions.winnerservice.utilities.WinnerUtil;

/**
 * PromotionPrize Service: It is used to implement all the promotion prize service functionalities.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 24-02-2022
 */
@Service
public class PromotionPrizeServiceImpl implements PromotionPrizeService {

	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private PromotionRepo promotionRepository;
		
	@Autowired
	private WinnerConfigRepo winnerConfigRepository;
	
	@Autowired
	private PromotionEntryRepo promotionEntryRepository;
	
	@Autowired
	private PromotionPrizeRepo promotionPrizeRepository;
	
	private static final ApiLogger LOGGER = 
			new ApiLogger(PromotionPrizeServiceImpl.class);
	
	
	/**
	 * Instantiate the service
	 * 
	 */
	public PromotionPrizeServiceImpl() {
		super();
	}
	
	
	@Override
	public Boolean isEligibleForPrizeSelection(Promotion promotion, Integer profileId) {

		Boolean isEligibleForPrizeSelection = false;
		
		// Get user spin limit details
		WinnerConfig winnerConfig = checkSpinLimit(promotion, profileId);
		
		if (winnerConfig.getLimit() <= 0) {
					
			// Throw spin limit exceed exception
			throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, REDEMPTION_LIMIT_REACHED.getCode(),
					String.format("Spin limit exceeded for user with profile id: %d for promotion with id: %d", 
							profileId, promotion.getId()), LOGGER.setLogLevel(INFO));
			
		} else if (getDefaultPrizeConfigs(promotion.getId(), true)
				.stream().collect(Collectors.summingLong(PromotionPrize::getInventory)) <= 0) {
			
			// Throw error, if there is no prize remain in inventory
			throw new ApiException(HttpStatus.INSUFFICIENT_STORAGE, HttpStatus.INSUFFICIENT_STORAGE.value(), 
					String.format("Prize out of stock for given promotion id: %d", promotion.getId()), LOGGER.setLogLevel(ERROR));
			
		} else if (getEntriesHasWonPrize(promotion.getId(), profileId).size() >= 
				promotionPrizeRepository.countMaxWinPossibilities(promotion.getId()).orElse(0)) {
			
			// Throw error, if all the prize are already won by the user
			throw new ApiException(HttpStatus.IM_USED, ErrorCodes.ALL_REWARDS_USED.getCode(), 
					String.format( "All the prize are consumed by user with profile id: %d for promotion id: %d", 
							profileId, promotion.getId()), LOGGER.setLogLevel(INFO));
		} else {
			
			// Set true if user is eligible
			isEligibleForPrizeSelection = true;
		}
		
		LOGGER.log(INFO, String.format("Prize selection eligibility status for user with profile id: %d "
				+ "for promotion id: %d is :: %b", profileId, promotion.getId(), isEligibleForPrizeSelection));

		return isEligibleForPrizeSelection;
	}
	
	@Override
	public WinnerConfig checkSpinLimit(Promotion promotion, Integer profileId) {
		
		WinnerConfig winnerConfig = null;
		
		if (promotion != null) {
		
			ZoneId zone = promotion.getLocalTimeZone() != null 
					? ZoneId.of(promotion.getLocalTimeZone()) : ZoneId.of(ZoneOffset.UTC.getId());

			// Get current prize limit configuration from database
			 winnerConfig = winnerConfigRepository.findByPromotionIdAndTime(promotion.getId(), LocalDateTime.now(zone), 
					 Sort.by("endTime")).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(), 
							 String.format("No spin limit set for promotion id: %d", promotion.getId()), LOGGER.setLogLevel(ERROR))).get(0);
			
			// Count user prize entry for the current time range
			 Integer countEntry = promotionEntryRepository.findByPromotionIdAndProfileIdAndCreatedLocalDateTimeBetweenAndPrizeStatusTypeIn(
					 promotion.getId(), profileId, winnerConfig.getStartTime(), winnerConfig.getEndTime(), 
					 Arrays.asList(WON.getStatus(), LOST.getStatus())).orElse(Collections.emptyList()).size();
			 		
			// Update the spin limit of user
			 winnerConfig.setLimit(winnerConfig.getLimit() - countEntry);
			 entityManager.detach(winnerConfig);
			 
			 LOGGER.log(INFO, String.format("Remain spin limit of user with profile id: %d "
			 		+ "is: %d for promotion with id: %d", profileId, winnerConfig.getLimit(), promotion.getId()));
		} else {

			// Throw exception, if invalid promotion entry id found
			throw new ApiException(HttpStatus.NOT_FOUND, 
					HttpStatus.NOT_FOUND.value(), "Invalid Promotion Id!", LOGGER.setLogLevel(ERROR));		
		}
		
		return winnerConfig;
	}
	
	@Override
	public PromotionPrize decidePrize(PromotionEntry promotionEntry) {
									
		Integer promotionId = promotionEntry.getPromotion().getId();
		
		LOGGER.log(INFO, ":::::::::::::::::::::  PRIZE SELECTION START  :::::::::::::::::::::");
		LOGGER.log(INFO, String.format(":: [Promotion Id: %d, Promotion Entry Id: %d, Profile Id: %d] :: ", 
				promotionId, promotionEntry.getId(), promotionEntry.getProfileId()));
		
		// Decide a prize for the winner
		PromotionPrize winnerPrize = WinnerUtil.getPrizeSelector(promotionEntry.getPromotion().getAttr1_value())
				.select(getDefaultPrizeConfigs(promotionId, true).stream().filter(defaultPrize -> defaultPrize.getInventory() > 0)
					.collect(Collectors.toList()), getEntriesHasWonPrize(promotionId, promotionEntry.getProfileId())
						.stream().collect(Collectors.groupingBy(PromotionEntry::getAttr1Value, Collectors.counting())));
	
		LOGGER.log(INFO, String.format("User with profile id: %d has won the prize[%s-%s] for promotion entry id: %d under promotion id: %d", 
				promotionEntry.getProfileId(), winnerPrize.getPrizeCode(), winnerPrize.getPrizeName(), promotionEntry.getId(), promotionId));
		LOGGER.log(INFO, "::::::::::::::::::::::  PRIZE SELECTION END  :::::::::::::::::::::");
		
		// Update the prize details in database		
		return updatePrizeDetails(promotionEntry, winnerPrize);
	}

	@Override
	public List<PromotionPrize> getDefaultPrizeConfigs(Integer promotionId, Boolean isActive) {
				
		Optional<List<PromotionPrize>> defaultPrizeConfigs = null;
				
		if (Boolean.TRUE.equals(isActive)) {
			// If isActive set to true, get all the active prize configurations
			defaultPrizeConfigs =  promotionPrizeRepository.findByPromotionIdAndIsActive(promotionId, true);
		} else {
			// If isActive set to false, get all the prize configurations
			defaultPrizeConfigs = promotionPrizeRepository.findByPromotionId(promotionId);
		}
		
		// Return the prize configurations
		return defaultPrizeConfigs.orElseThrow(() -> 
				new ApiException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), 
				String.format("No prize config found for promotion id: %d", promotionId), LOGGER.setLogLevel(ERROR)));
	}
	
	@Override
	public List<PromotionEntry> getEntriesHasWonPrize(Integer promotionId, Integer profileId) {
		
		// Get all the user entries that has won the prize
		return promotionEntryRepository.findByPromotionIdAndProfileIdAndPrizeStatusType(
				promotionId, profileId, WON.getStatus()).orElse(Collections.emptyList());
	}
	
	@Override 
	public PromotionEntry createSpinEntry(Map<String, Integer> entryOption) {
		
		PromotionEntry promotionEntry = null;
		
		if (entryOption.containsKey("profileId") && entryOption.containsKey("promotionId")) {
			
			// Retrieve entry options
			Integer profileId = entryOption.get("profileId");
			Integer promotionId = entryOption.get("promotionId");
			
			// Get promotion details by id
			Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
			
			if (Boolean.TRUE.equals(isEligibleForPrizeSelection(promotion, profileId))) {

				// If user is eligible for prize selection, then save promotion entry in database
				promotionEntry = promotionEntryRepository.save(WinnerUtil.setSpinEntryOption(profileId, 
						promotion, statusService.getStatus(VERIFIED.getStatus())));
				
				LOGGER.log(INFO, String.format("New promotion entry created with id: %d for user "
						+ "with profile id: %d under promotion id: %d", promotionEntry.getId(), profileId, promotionId));
			}

		} else {
			
			// If invalid required parameter found
			throw new ApiException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), 
					"Both promotion and profile id are required for prize selection!", LOGGER.setLogLevel(ERROR));
		}
		
		return promotionEntry;
	}
	
	@Override 
	public PromotionPrize updatePrizeDetails(PromotionEntry promotionEntry, PromotionPrize promotionPrize) {
		
		if (!promotionPrize.getPrizeCode().equals(NO_PRIZE.getCode().toString())) {
			
			// Set the won status on promotion entry
			promotionEntry.setPrizeStatus(statusService.getStatus(WON.getStatus()));
			
			// Update the prize inventory
			promotionPrize.setInventory(promotionPrize.getInventory() - 1);
			promotionPrize = promotionPrizeRepository.save(promotionPrize);
			
			LOGGER.log(INFO, String.format("Remain prize for PrizeCode-%s is: %d under promotion with id: %d", 
					promotionPrize.getPrizeCode(), promotionPrize.getInventory(), promotionEntry.getPromotion().getId()));
		} else {
			
			// Set the lost status on promotion entry
			promotionEntry.setPrizeStatus(statusService.getStatus(LOST.getStatus()));
		}
		
		// Update the prize entry in database with prize code
		promotionEntry.setAttr1Code("prizeCode");
		promotionEntry.setAttr1Value(promotionPrize.getPrizeCode());
		promotionEntryRepository.save(promotionEntry);
				
		return promotionPrize;
	}
	
	@Override
	public List<PromotionPrize> getSpecificPrizeConfigs(Integer promotionId, String promoCodeId, Boolean isActive) {
				
		Optional<List<PromotionPrize>> specificPrizeConfigs = null;
		
		if(Boolean.TRUE.equals(isActive)) {			
		specificPrizeConfigs =  promotionPrizeRepository.findByPromotionIdAndReferenceAndIsActive(promotionId, promoCodeId, isActive);
		}
		else {
			specificPrizeConfigs = promotionPrizeRepository.findByPromotionIdAndReference(promotionId, promoCodeId);
		}
		
		// Return the prize configurations
		return specificPrizeConfigs.orElseThrow(() -> 
				new ApiException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), 
				String.format("No prize config found for promotion id: %d", promotionId), LOGGER.setLogLevel(ERROR)));
	}
	
}
