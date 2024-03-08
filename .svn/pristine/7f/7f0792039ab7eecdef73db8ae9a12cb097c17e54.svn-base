package com.kelloggs.promotions.lib.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.rule.Selector;

/**
 * Winning Moment Based Instant Winner Selector: This winner selector uses winning moment based winner selection process, 
 * it selects the winner and loser for the given promotion entry based on provided selection criteria(winner selection configuration) which includes 
 * 
 * <ul>
 * 	<li>If the given winning moment is available and has winner count stock(WinnerConfig.maxWinner > 0) available 
 * 		for the winner selection and the requested user has valid available win occurrence (Promotion.maxLimit > 0) then the particular entry will 
 * 		be selected as a winner otherwise loser.</li>
 * 	<li>Maximum number of winner selection count can be set by the WinnerConfig.maxWinner field in database for a specific winning moment period,
 *  	if maxWinner > 0, then specified number of winners can be selected and if maxWinner = 0, then no winner can be selected.</li>
 * 	<li>A user can win one or more time as per the the value set for Promotion.maxLimit field in database,
 * 		if maxLimit = -1, then one user can win infinite times otherwise specified number of time during entire promotion period.</li>
 * </ul>
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 21st July 2022
 */
@Service
public class WinningMomentBasedInstantWinnerSelector implements Selector<PromotionEntry, WinnerConfig, Boolean> {
	
	@Autowired
	private PromotionEntryRepo promotionEntryRepository;
		
	private static final ApiLogger LOGGER = 
			new ApiLogger(WinningMomentBasedInstantWinnerSelector.class);
	
	/**
	 * Instantiate the selector
	 */
	public WinningMomentBasedInstantWinnerSelector() {
		super();
	}
	
	/**
	 * Perform winning moment based instant winner selection process
	 * 
	 * @param promotionEntry	Promotion entry details to be used for selection process
	 * @param winnerConfig	Winner configuration to be used as criteria for selection process
	 * @return Return true if winner is selected, otherwise false
	 */
	@Override
	public Boolean select(PromotionEntry promotionEntry, WinnerConfig winnerConfig) {
		
		Boolean isWinnerSelected = false;
		Promotion promotion = promotionEntry.getPromotion();
		
		LOGGER.log(":#:   WINNER SELECTOR :: WINNING MOMENT BASED INSTANT WINNER SELECTOR   :#:");
		LOGGER.log(String.format("Winner selection criteria :: {maxWinner: %d, startTime: %s, endTime: %s}", 
				winnerConfig.getMaxWinner(), winnerConfig.getStartTime(), winnerConfig.getEndTime()));
					
		if (
			// Check the availability of max winner stock for the current winning moment
			winnerConfig.getMaxWinner() > 0
			
			// Check the eligibility of requested user win occurrence for current promotion
			&& (promotion.getMaxLimit() < 0 || promotion.getMaxLimit() 
					> promotionEntryRepository.countByProfileIdAndAttr1Value(
							promotionEntry.getProfileId(), Boolean.TRUE.toString()))
		) {

			// Set true, if user selected as winner
			isWinnerSelected = true;
		}
		
		return isWinnerSelected;
	}
}
