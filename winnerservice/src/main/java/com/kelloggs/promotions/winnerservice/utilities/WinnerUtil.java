package com.kelloggs.promotions.winnerservice.utilities;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.kelloggs.promotions.lib.constants.ErrorCodes;
import com.kelloggs.promotions.lib.constants.PrizeSelectorType;
import com.kelloggs.promotions.lib.constants.StatusConstants;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Status;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.rule.PrizeSelector;
import com.kelloggs.promotions.lib.rule.impl.DefaultPrizeSelector;
import com.kelloggs.promotions.lib.rule.impl.NoLossPrizeSelector;

/**
 * Winner Util: This class is used to provide all the 
 * utility functions for prize/winner operations
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 25-02-2022
 */
public class WinnerUtil {

	
	private static final ApiLogger LOGGER = 
			new ApiLogger(WinnerUtil.class);
	
	/**
	 * Instantiate the utility
	 */
	private WinnerUtil() {
		super();
	}
	

	/**
	 * Validate promotion entry before prize/winner selection
	 * 
	 * @param promotionEntry	user provided promotion entry
	 * @return	return true, if promotion entry is valid, otherwise false
	 */
	public static Boolean isValidPromotionEntry(PromotionEntry promotionEntry) {
		
		Boolean isValidPromotionEntry = false;
	
		if (promotionEntry == null) {
			
			// Throw exception, if invalid promotion entry id found
			throw new ApiException(HttpStatus.NOT_FOUND, 
					ErrorCodes.INVALID_ENTRY_ID.getCode(), "Invalid promotion entry id!", LOGGER.setLogLevel(ERROR));
			
		} else if (promotionEntry.getPrizeStatus() != null && 
				(promotionEntry.getPrizeStatus().getType().equals(StatusConstants.WON.getStatus()) 
				|| promotionEntry.getPrizeStatus().getType().equals(StatusConstants.LOST.getStatus()))) {
			
			//Throw exception, if promotion entry has been already used for prize/winner selection
			throw new ApiException(HttpStatus.IM_USED, ErrorCodes.ALREADY_VALIDATED.getCode(), 
					String.format("Promotion entry id: %d is already used for prize/winner selection under promotion with id: %d", 
							promotionEntry.getId(), promotionEntry.getPromotion().getId()), LOGGER.setLogLevel(ERROR));
		
		} else if (promotionEntry.getStatus() == null 
				|| !(promotionEntry.getStatus().getType().equals(StatusConstants.VERIFIED.getStatus())
				|| promotionEntry.getStatus().getType().equals(StatusConstants.UPLOADED.getStatus())
				|| promotionEntry.getStatus().getType().equals(StatusConstants.UNDER_SNIPP_PROCESSING.getStatus()))) {
			
			// Throw exception, if promotion entry is not eligible for prize/winner selection
			throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorCodes.NOT_ELIGIBLE_FOR_REWARD.getCode(), 
					String.format("Promotion entry id: %d is not eligible for prize/winner selection under promotion with id: %d", 
							promotionEntry.getId(), promotionEntry.getPromotion().getId()), LOGGER.setLogLevel(ERROR));
		} else {
			
			// Set true, if promotion entry is valid
			isValidPromotionEntry = true;
		}
		
		return isValidPromotionEntry;
	}
	
	/**
	 * Check whether the given promotion entry is valid and never used for winner selection
	 * 
	 * @param promotionEntry	user provided promotion entry
	 * @return	return true, if promotion entry is valid and not used, otherwise false
	 */
	public static Boolean isValidEntryForWinnerSelection(PromotionEntry promotionEntry) {

		Boolean isValidEntryForWinnerSelection = false;
		
		if (Objects.isNull(promotionEntry)) {
			
			// Throw exception, if the promotion entry is invalid
			throw new ApiException(HttpStatus.NOT_FOUND, 
					NOT_FOUND.getCode(), "Invalid promotion entry id!", LOGGER.setLogLevel(ERROR));
			
		} else if ((Objects.nonNull(promotionEntry.getStatus()) && 
				(StatusConstants.WON.getStatus().equals(promotionEntry.getStatus().getType()) 
				|| StatusConstants.LOST.getStatus().equals(promotionEntry.getStatus().getType())))
				|| (Objects.nonNull(promotionEntry.getAttr1Code()) && Objects.nonNull(promotionEntry.getAttr1Value()))) {
			
			//Throw exception, if promotion entry has been already used for winner selection
			throw new ApiException(HttpStatus.ALREADY_REPORTED, ErrorCodes.ALREADY_VALIDATED.getCode(), String.format(
					"Given promotion entry id: %d is already used for the winner selection.", promotionEntry.getId()), LOGGER.setLogLevel(ERROR));
		} else {
			
			// Set true, if promotion entry is valid for winner selection
			isValidEntryForWinnerSelection = true;
		}
		
		return isValidEntryForWinnerSelection; 
	}
	
	/**
	 * Get suitable prize selector for a promotion
	 * 
	 * @param prizeSelectorType 	prize selector type of current promotion
	 * @return	return the prize selector object
	 */
	public static PrizeSelector getPrizeSelector(String prizeSelectorType) {
		
		PrizeSelector prizeSelector = null;
		
		if (prizeSelectorType == null 
				|| PrizeSelectorType.TYPE_DEFAULT.getName().equals(prizeSelectorType)) {
			
			// If prize selector type is not set or TYPE_DEFAULT
			prizeSelector = new DefaultPrizeSelector();
			
		} else if (PrizeSelectorType.TYPE_NOLOSS.getName().equals(prizeSelectorType)) {
			
			// If prize selector type is TYPE_NOLOSS
			prizeSelector = new NoLossPrizeSelector();
			
		} else {
			
			// Throw error, if invalid prize selector is set
			throw new ApiException(HttpStatus.NOT_FOUND, ErrorCodes.INVALID_TOKEN.getCode(), 
					String.format("No prize selector found with given type:: %s", prizeSelectorType), LOGGER.setLogLevel(ERROR));
		}
		
		return prizeSelector;
	}

	/**
	 * Set up the new promotion entry option 
	 * 
	 * @param profileId	id of the user
	 * @param promotion	current promotion details to be set
	 * @return	return updated promotion entry 
	 */
	public static PromotionEntry setSpinEntryOption(Integer profileId, Promotion promotion, Status status) {
		
		PromotionEntry promotionEntry = null;
		
		if (promotion != null && profileId != null) {
			
			promotionEntry = new PromotionEntry();
			
			// Set promotion details in entry
	        promotionEntry.setPromotion(promotion);
			promotionEntry.setCountry(promotion.getRegion().getCountry());
		      
			 // Set the initial entry status
			promotionEntry.setStatus(status);
			promotionEntry.setProfileId(profileId);;

	        // Set local date time based on the zone
	        String timeZone = promotion.getLocalTimeZone();
	        
	        if (timeZone != null) { 
	        	promotionEntry.setLocalTimeZone(timeZone);
	            promotionEntry.setCreatedLocalDateTime(LocalDateTime.now(ZoneId.of(timeZone)));
	        } else {
	        	// Set Default/UTC time zone
	        	promotionEntry.setCreatedLocalDateTime(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
	        	LOGGER.log(WARN, String.format("Local time zone is not set for the promotion Id:: %d", promotion.getId()));
	        }
		}
		
		return promotionEntry;
	}
	
}
