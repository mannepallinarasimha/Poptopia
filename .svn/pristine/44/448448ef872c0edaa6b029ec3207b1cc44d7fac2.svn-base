package com.kelloggs.promotions.winnerservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.winnerservice.services.PromotionPrizeService;
import com.kelloggs.promotions.winnerservice.utilities.WinnerUtil;

/**
 * SpinWheel Controller: This controller handle all the spin wheel related APis
 *  
 * @author UDIT NAYAK (M1064560)
 * @since 24-02-2022
 * @version v1
 */
@RestController
@RequestMapping("/api/v1/spinwheel")
public class SpinWheelController {
	
	
	private PromotionPrizeService promotionPrizeService;
	
	private static final ApiLogger LOGGER = 
			new ApiLogger(SpinWheelController.class);
	
	/**
	 * Instantiate the controller
	 * 
	 * @param promotionPrizeService
	 */
	public SpinWheelController(PromotionPrizeService promotionPrizeService) {
		this.promotionPrizeService = promotionPrizeService;
	}
	
	
	/**
	 * Get user eligibility for prize selection
	 * 
	 * @param promotionId	id of the promotion
	 * @param promotionId	id of the promotion
	 * @return	return user eligibility status
	 */
	@GetMapping("/{promotionId}/user/{profileId}/eligibility/status")
	public Map<String, Object>  getEligibilityStatus(
			@PathVariable("promotionId") Optional<Promotion> promotion, @PathVariable Integer profileId) {
		
		Map<String, Object> eligibilityStatus = new HashMap<>();
		
		// Set the user eligibility status 
		eligibilityStatus.put("message", "User prize selection eligibility status!");
		eligibilityStatus.put("isEligible", promotionPrizeService.isEligibleForPrizeSelection(promotion.orElse(null), profileId));
		
		return eligibilityStatus;
	}
	
	/**
	 * Check user remain spin limit info for current promotion	
	 * 
	 * @param promotionId	id of the promotion
	 * @param promotionId	id of the promotion
	 * @return	return user remain spin limit info
	 */
	@GetMapping("/{promotionId}/user/{profileId}/spin/limit")
	public ApiResponse<WinnerConfig> getRemainSpinLimit(
			@PathVariable("promotionId") Optional<Promotion> promotion, @PathVariable Integer profileId) {
		
		// Return user remain spin limit info
		return new ApiResponse<>(String.format("Remain spin limit of user profile id: %d", 
				profileId), promotionPrizeService.checkSpinLimit(promotion.orElse(null), profileId));
	} 
	
	/**
	 * Select a prize for the winner
	 * 
	 * @param entryOption	should hold promotionId and profile Id of user
	 * @return selected prize details for provided entry
	 */
	@PostMapping("/prize/select")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<PromotionPrize> selectWinnerPrize(@RequestBody Map<String, Integer> entryOption) {
		
		// Create a new promotion entry 
		PromotionEntry promotionEntry = promotionPrizeService.createSpinEntry(entryOption);
		
		// Return the selected prize for a winner
		return new ApiResponse<>(String.format("Prize for the given promotion entry id: %d", 
				promotionEntry.getId()), promotionPrizeService.decidePrize(promotionEntry));
	}
	
	/**
	 * Select the prize by promotion entry
	 * 
	 * @param promotionEntryId	id of the user promotion entry
	 * @return selected prize details for provided entry
	 */
	@GetMapping("/entry/{promotionEntryId}/prize/select")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<PromotionPrize> selectWinnerPrize(@PathVariable("promotionEntryId") Optional<PromotionEntry> promotionEntryOptional) {
		
		PromotionPrize selectedPrize = null;
		PromotionEntry promotionEntry = promotionEntryOptional.orElse(null);
		
		if (Boolean.TRUE.equals(WinnerUtil.isValidPromotionEntry(promotionEntry)) && Boolean.TRUE.equals(
				promotionPrizeService.isEligibleForPrizeSelection(promotionEntry.getPromotion(), promotionEntry.getProfileId()))) {
			
			// Select the prize, if entry is eligible for prize selection
			selectedPrize = promotionPrizeService.decidePrize(promotionEntry);
		}
		
		// Return the selected prize for a winner
		return new ApiResponse<>(String.format("Prize for the given promotion entry id: %d", promotionEntry.getId()), selectedPrize);
	}
	 
	
	/**
	 * Get default prize configurations for a promotion
	 * 
	 * @param promotionId	id of the promotion to be search
	 * @param isActive	if set true, returns all the active configurations, otherwise default value is false which returns both
	 * @return	return a list of prize configurations as per the active flag
	 */
	@GetMapping("/{promotionId}/prize/configs")
	public ApiListResponse<PromotionPrize> getPrizeConfigs(@PathVariable Integer promotionId, 
			@RequestParam(name = "active", defaultValue = "false", required = false) Boolean isActive) {
		
		// Get default prize configurations for a promotion
		List<PromotionPrize> prizeConfigs = promotionPrizeService.getDefaultPrizeConfigs(promotionId, isActive);
		
		return new ApiListResponse<>(String.format(
				"All the prize cofigs for given promotion id: %d", promotionId), prizeConfigs, prizeConfigs.size());
	}
	
	/**
	 * Get user won prize entry details for a promotion
	 * 
	 * @param promotionId	id of the promotion to be against
	 * @param profileId		unique profile id of the user
	 * @return	return a list of prize entries which has been won by user
	 */
	@GetMapping("/{promotionId}/user/{profileId}/won/prizes")
	public ApiListResponse<PromotionEntry> getWonPrizeEntries(@PathVariable Integer promotionId, @PathVariable Integer profileId) {
		
		// Get user won prize entry details for a promotion
		List<PromotionEntry> wonPrizeEntries = promotionPrizeService.getEntriesHasWonPrize(promotionId, profileId);
		
		if (wonPrizeEntries.isEmpty()) {
			// Throw exception, if there is no won prize found for the user
			throw new ApiException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), 
					String.format("No prize won by user profile id: %d for the promotion id: %d", profileId, promotionId), LOGGER);
		}
		
		return new ApiListResponse<>(String.format("All the prize entries won by user profile id: %d for the promotion id: %d", 
				profileId, promotionId), wonPrizeEntries, wonPrizeEntries.size());
	}
}
