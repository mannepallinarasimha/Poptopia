package com.kelloggs.promotions.winnerservice.services;

import java.util.List;
import java.util.Map;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.WinnerConfig;

/**
 * PromotionPrize Service: This service is used to handle all the prize related operation
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 24-02-2022
 */
public interface PromotionPrizeService {
	
	
	/**
	 * Check the eligibility of a user whether its applicable for prize selection
	 * 
	 * @param promotion		current promotion details
	 * @param profileId		user unique profile id to check against
	 * @return	return true, if user is eligible, otherwise false
	 */
	public abstract Boolean isEligibleForPrizeSelection(Promotion promotion, Integer profileId);
	
	/**
	 * Check the spin limit of a user for current promotion
	 * 
	 * @param promotion		current promotion details
	 * @param profileId		user unique profile id to check the spin limit
	 * @return	return remain spin limit of the user
	 */
	public abstract WinnerConfig checkSpinLimit(Promotion promotion, Integer profileId);
	
	/**
	 * Decide a prize for the winner
	 * 
	 * @param promotionEntry	the promotion entry used to select prize
	 * @return	return decided prize for the entry 
	 */
	public abstract PromotionPrize decidePrize(PromotionEntry promotionEntry);
	
	/**
	 * Get default prize configurations from databases
	 * 
	 * @param promotionId	current promotion id for the configurations
	 * @param isActive	if set true, then return the active prize configurations, otherwise both
	 * @return return the default prize configurations
	 */
	public abstract List<PromotionPrize> getDefaultPrizeConfigs(Integer promotionId, Boolean isActive);
	
	/**
	 * Get all the entries by user which has already won prize
	 * 
	 * @param promotionId	id of the current promotion
	 * @param profileId	unique user profile id to fetch won entries
	 * @return 	return list of entries, otherwise empty list if no won entries found
	 */
	public abstract List<PromotionEntry> getEntriesHasWonPrize(Integer promotionId, Integer profileId);
	
	/**
	 * Create a new spin promotion entry in database
	 * 
	 * @param entryOption	holds promotion id and profile id for the entry
	 * @return	return new created promotion entry
	 */
	public abstract PromotionEntry createSpinEntry(Map<String, Integer> entryOption);

	/**
	 * Update the selected prize in database, which perform an update of prize code and name 
	 * in promotion entry record and decrease the max winner value by one.
	 * 
	 * @param promotionEntry	entry in which prize details to be update
	 * @param promotionPrize	the selected prize details
	 * @return	return updated prize details after decreasing the max winner value by one
	 */
	public abstract PromotionPrize updatePrizeDetails(PromotionEntry promotionEntry, PromotionPrize promotionPrize);

	/**
	 * Get specific prize configurations from databases
	 * 
	 * @param promotionId	current promotion id for the configurations
	 * @param promoCodeId The promoCodeId of the promoCode entered by the user.
	 * @param isActive	if set true, then return the active prize configurations
	 * @return return the specific prize configurations
	 */
	public abstract List<PromotionPrize> getSpecificPrizeConfigs(Integer promotionId , String promoCodeId, Boolean isActive);

}
