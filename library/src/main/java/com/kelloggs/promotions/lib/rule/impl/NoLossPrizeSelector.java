package com.kelloggs.promotions.lib.rule.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.kelloggs.promotions.lib.constants.ErrorCodes;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.rule.PrizeSelector;

/**
 * NoLoss Prize Selector: This is another type of prize selector, 
 * basically its overridden from default selector where user will never lose, which includes 
 * 
 * <ul>
 * 	<li>A user can win one or more prize multiple time as per the the value set for max-win field in database.</li>
 * 	<li>Verified entries by Default/Receipt upload/Batch code can only be part of the prize selection.</li>
 * 	<li>Spin limit can be set for day/entire promotion time period to prevent a user.</li>
 *  <li>Each prize will participate in selection draw until its not fully used by user, once a user won 
 *  	a prize total number of times set in max-win field, that won't be the part of prize selection again, 
 *      and its probability will be distributed equally among rest of the prizes.</li>
 * 	<li>No prize mode is disabled for this selection, if all the inventory prize already consumed by an user
 * 		it will throw an error with status - 4001 and says all prize consumed.</li>
 * </ul>
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 28-02-2022
 */
public class NoLossPrizeSelector extends DefaultPrizeSelector implements PrizeSelector {
	
	
	private static final ApiLogger LOGGER = 
			new ApiLogger(NoLossPrizeSelector.class);
	
	/**
	 * Instantiate the selector
	 */
	public NoLossPrizeSelector() {
		super();
	}

	
	@Override
	public PromotionPrize select(List<PromotionPrize> defaultPrizeConfigs, Map<String, Long> wonPrizeCodes) {
		
		PromotionPrize selectedPrize = null;
		
		LOGGER.log(":#:          PRIZE SELECTOR :: NO-LOSS PRIZE SELECTOR           :#:");

		// Get eligible prize configuration for selection draw
		List<PromotionPrize> eligiblePrizeConfigs = 
				getEligiblePrizeConfigs(defaultPrizeConfigs, wonPrizeCodes);
		
		if (!eligiblePrizeConfigs.isEmpty()) {
			
			// Select a random prize by following probability 
			selectedPrize = PrizeSelector.randomPrize(eligiblePrizeConfigs);
		} else {
			// Throw error, if no eligible prize configuration remain
			throw new ApiException(HttpStatus.IM_USED, 
					ErrorCodes.ALL_REWARDS_USED.getCode(), "All the prize are consumed by user!", LOGGER);
		}
		
		return selectedPrize;
	}

}
