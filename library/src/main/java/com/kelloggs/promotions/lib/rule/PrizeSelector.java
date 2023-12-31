package com.kelloggs.promotions.lib.rule;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.model.ApiLogger;

/**
 * Prize Selector: 	This is root prize selector,
 * which provide select method to be implement for prize selection
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 28-02-2022
 */
@FunctionalInterface
public interface PrizeSelector {
	
	
	public static final SecureRandom RANDOM = new SecureRandom();
	
	public static final ApiLogger LOGGER = new ApiLogger(PrizeSelector.class);
	
	
	/**
	 * Choose a random prize as per the given probability
	 * 
	 * @param eligiblePrizeConfigs	list of eligible prizes in selection draw
	 * @return	return a random prize 
	 */
	static PromotionPrize randomPrize(List<PromotionPrize> eligiblePrizeConfigs) {
		
		PromotionPrize prize = null;
		
		// Get random probability by counting total probability of eligible prizes
		Integer randomProbability = RANDOM.nextInt(eligiblePrizeConfigs.stream()
				.collect(Collectors.summingInt(PromotionPrize::getWinProbability)));
		
		LOGGER.log(String.format("Generated random probability for prize selection is: %d", randomProbability));

		// Sort the prize list for high accuracy possibilities
		Collections.sort(eligiblePrizeConfigs, (prize1, prize2) ->
			prize1.getWinProbability().compareTo(prize2.getWinProbability()));
		
		Integer sumProbability = 0;
		
		for (PromotionPrize eligiblePrize : eligiblePrizeConfigs) {

			// Sum of previous prize probability
			sumProbability += eligiblePrize.getWinProbability();
			
			if (randomProbability < sumProbability) {
				// Set the selected prize and break 
				prize = eligiblePrize; break;
			}
		}
		
		return prize;	
	}
	
	/**
	 * Select the prize for winner
	 * 
	 * @param defaultPrizeConfigs	default prize configurations for the current promotion
	 * @param wonPrizeCodes		code of the prizes has been already won by user, holds prize code and its occurrence
	 * @return	return the selected prize details
	 */
	PromotionPrize select(List<PromotionPrize> defaultPrizeConfigs, Map<String, Long> wonPrizeCodes);
	
	/**
	 * Filter out eligible prize configurations
	 * 
	 * @param defaultPrizeConfigs	default prize configurations for the current promotion
	 * @param wonPrizeCodes		code of the prizes has been already won by user, holds prize code and its occurrence
	 * @return	return the eligible prizes for selection draw
	 */
	default List<PromotionPrize> getEligiblePrizeConfigs(List<PromotionPrize> defaultPrizeConfigs, Map<String, Long> wonPrizeCodes) {
		return defaultPrizeConfigs;
	}

}
