package com.kelloggs.promotions.lib.rule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kelloggs.promotions.lib.constants.ErrorCodes;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.rule.PrizeSelector;

/**
 * Default Prize Selector: This is the default prize selector for any spin wheel promotion, which includes 
 * 
 * <ul>
 * 	<li>A user can win one or more prize multiple time as per the the value set for max-win field in database,
 * 		if max-win set to 1, then one prize can only be won once by an user</li>
 * 	<li>Verified entries by Default/Receipt upload/Batch code can only be part of the prize selection.</li>
 * 	<li>Spin limit can be set for day/entire promotion time period to prevent a user</li>
 *  <li>Each prize will participate in selection draw until its not fully used by user, once a user won 
 *  	a prize total number of times set in max-win field, that won't be the part of prize selection again, 
 *      and its probability will be distributed equally among rest of the prizes.</li>
 * 	<li>No prize mode is enabled for the selection, if there is no prize selected it will return an empty prize with code - 4004.</li>
 *  <li>Here, win probability of No-Prize = (100 - sum of win probability of default prize configurations).
 *  	If all the inventory prize already consumed by an user it will throw an error with status - 4001 and says all prize consumed.</li>
 *  <li>If (sum of win probability of default prize >= 100) set in database, then the selector will be act as No-Loss selector.</li>
 * </ul>
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 28-02-2022
 */
public class DefaultPrizeSelector implements PrizeSelector {
	
	
	private static final ApiLogger LOGGER = 
			new ApiLogger(DefaultPrizeSelector.class);
	
	/**
	 * Instantiate the selector
	 */
	public DefaultPrizeSelector() {
		super();
	}

	
	@Override
	public PromotionPrize select(List<PromotionPrize> defaultPrizeConfigs, Map<String, Long> wonPrizeCodes) {
		
		PromotionPrize selectedPrize = null;
		
		LOGGER.log(":#:          PRIZE SELECTOR :: DEFAULT PRIZE SELECTOR           :#:");
		
		// Get eligible prize configuration for selector draw
		List<PromotionPrize> eligiblePrizeConfigs = 
				getEligiblePrizeConfigs(defaultPrizeConfigs, wonPrizeCodes);
		
		// Set No-Prize option in prize selection draw
		PromotionPrize noPrize = new PromotionPrize();
		
		noPrize.setId(101);
		noPrize.setPrizeCode(ErrorCodes.NO_PRIZE.getCode().toString());
		noPrize.setPrizeName("No Prize");
		noPrize.setActive(true);
		noPrize.setWinProbability(100 - eligiblePrizeConfigs.stream()
				.collect(Collectors.summingInt(PromotionPrize::getWinProbability)));
				
		if (!eligiblePrizeConfigs.isEmpty()) {
			
			// Select a random prize by following probability 
			eligiblePrizeConfigs.add(noPrize);
			selectedPrize = PrizeSelector.randomPrize(eligiblePrizeConfigs);
			
		} else {
			// Return no prize, if there are no eligible prize configurations
			selectedPrize = noPrize;
		}

		return selectedPrize;
	}

	@Override
	public List<PromotionPrize> getEligiblePrizeConfigs(
			List<PromotionPrize> defaultPrizeConfigs, Map<String, Long> wonPrizeCodes) {

		List<PromotionPrize> eligiblePrizeConfigs = new ArrayList<>();

		// Get list of prizes, which has been completely used/won by user
		List<PromotionPrize> wonPrizeConfigs = defaultPrizeConfigs.stream()
				.filter(prize -> wonPrizeCodes.containsKey(prize.getPrizeCode()) 
				&& Long.valueOf(prize.getMaxWin()).equals(wonPrizeCodes.get(prize.getPrizeCode())))
				.collect(Collectors.toList());
		
		LOGGER.log(String.format("Total number of prizes won by user :: %d", wonPrizeCodes.size()));

		if (defaultPrizeConfigs.size() - wonPrizeConfigs.size() > 0) {
			
			// Get the average probability to be distribute among eligible prizes
			Integer avgProbability = wonPrizeConfigs.stream()
					.collect(Collectors.summingInt(PromotionPrize::getWinProbability)) 
					/ (defaultPrizeConfigs.size() - wonPrizeConfigs.size());
								
			defaultPrizeConfigs.removeAll(wonPrizeConfigs);
			
			// Get the eligible prize configurations, otherwise return empty list of prize
			eligiblePrizeConfigs = defaultPrizeConfigs.stream().map(prize -> { 
						prize.setWinProbability(prize.getWinProbability() + avgProbability); 
						return prize; 	
					}).collect(Collectors.toList());
		}
		
		LOGGER.log(String.format("Total number of eligible prizes for current selection :: %d", wonPrizeCodes.size()));
		
		return eligiblePrizeConfigs;
	}

}
