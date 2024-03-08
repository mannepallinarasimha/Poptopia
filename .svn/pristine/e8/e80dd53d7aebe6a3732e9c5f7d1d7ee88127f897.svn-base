package com.kelloggs.promotions.lib.rule.impl;

import static com.kelloggs.promotions.lib.constants.StatusConstants.LOST;
import static com.kelloggs.promotions.lib.constants.StatusConstants.WON;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.lib.rule.Selector;

/**
 * Percentage Based Instant Winner Selector: This winner selector uses probability/percentage based winner selection process, 
 * it selects the winner and loser for the given promotion entry based on provided selection criteria(winner selection configuration) which includes 
 * 
 * <ul>
 *  <li>The selector generates a random number with range between 0 and the value set in WinnerConfig.limit and compares with the increment/fixed 
 *  	win probability set in WinnerConfig.winProbablity for the selection process. If the generatedNumber <= winProbablity then winner otherwise loser.</li>
 * 	<li>It also support automatic increment of winning probability functionality to increase the chance of winning and can be enable by setting 
 * 		the WinnerConfig.winStep field in database which decide the probability increment time by dividing the limit equally. If the value is 
 * 		zero/negative then it acts as fixed/non-incremental probability based winner selection.</li>
 * 	<li>WinnerConfig.limit is used to set the limit/range for selection process and also can be the average number of entries per promotion period.</li>
 *  <li>Probability/possibility can be set by using the WinnerConfig.winProbablity field in database, it indicates the more higher value the more 
 *  	winning chances and value automatically gets increment if the auto increment of winning probability functionality is enabled.</li>
 * 	<li>Maximum number of winner count can be set by the WinnerConfig.maxWinner field in database as per the promotion period,
 *  	if maxWinner = -1, then infinite number of winners can be selected and if maxWinner = 0, then no winner can be selected.</li>
 * 	<li>A user can win one or more time as per the the value set for Promotion.maxLimit field in database,
 * 		if maxLimit = -1, then one user can win infinite times otherwise specified number of time.</li>
 * <li>To apply a special eligibility criteria/condition on a particular promotion entry, the value of PromotionEntry.attr1Value must be true. 
 * 		If the value is true, then one user can have infinite times of chance to win even the Promotion.maxLimit > 0.</li>
 * </ul>
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 21st July 2022
 */
@Service
public class PercentageBasedInstantWinnerSelector implements Selector<PromotionEntry, WinnerConfig, Boolean> {

	@Autowired
	private WinnerConfigRepo winnerConfigRepository;
	
	@Autowired
	private PromotionEntryRepo promotionEntryRepository;
	
	public static final SecureRandom RANDOM = new SecureRandom();
	
	private static final ApiLogger LOGGER = 
			new ApiLogger(PercentageBasedInstantWinnerSelector.class);
	
	/**
	 * Instantiate the selector
	 */
	public PercentageBasedInstantWinnerSelector() {
		super();
	}
	
	/**
	 * Perform percentage based instant winner selection process
	 * 
	 * @param promotionEntry	Promotion entry details to be used for selection process
	 * @param winnerConfig	Winner configuration to be used as criteria for selection process
	 * @return Return true if winner is selected, otherwise false
	 */
	@Override
	public Boolean select(PromotionEntry promotionEntry, WinnerConfig winnerConfig) {
		
		Boolean isWinnerSelected = false;
		Promotion promotion = promotionEntry.getPromotion();
		
		LOGGER.log(":#:   WINNER SELECTOR :: PERCENTAGE BASED INSTANT WINNER SELECTOR   :#:");
		LOGGER.log(String.format("Winner selection criteria :: {maxWinner: %d, limit: %d, winProbablity: %d, winStep: %d}", 
				winnerConfig.getMaxWinner(), winnerConfig.getLimit(), winnerConfig.getWinProbability(), winnerConfig.getWinStep()));
						
		if ((RANDOM.nextInt(winnerConfig.getLimit()) <= getAutoIncrementedWinnerProbability(
				promotionEntry.getProfileId(), winnerConfig, Boolean.parseBoolean(promotionEntry.getAttr1Value()))) 
			
			// Check the availability of max winner stock for the current promotion
			&& (winnerConfig.getMaxWinner() < 0 || winnerConfig.getMaxWinner() > promotionEntryRepository
					.countByPromotionIdAndCreatedLocalDateTimeBetweenAndStatusTypeIn(promotion.getId(), 
							winnerConfig.getStartTime(), winnerConfig.getEndTime(), Arrays.asList(WON.getStatus())))
			
			// Check the eligibility of requested user win occurrence for current promotion
			&& (Boolean.parseBoolean(promotionEntry.getAttr1Value()) || promotion.getMaxLimit() < 0 
					|| promotion.getMaxLimit() > promotionEntryRepository.countByPromotionIdAndProfileIdAndStatusType(
							promotion.getId(), promotionEntry.getProfileId(), WON.getStatus()))
		) {
			
			// Set true, if user selected as winner
			isWinnerSelected = true;
		}
		
		return isWinnerSelected;
	}

	/**
	 * Get automatic incremented winning probability for winner selection
	 * 
	 * @param profileId	Unique profile id of the user
	 * @param winnerConfig	Winner criteria for the selection process
	 * @param hasSpecialEligibility	Determines whether the user has special eligibility or not
	 * @return	Return automatic incremented winning probability
	 */
	private Integer getAutoIncrementedWinnerProbability(Integer profileId, WinnerConfig winnerConfig, Boolean hasSpecialEligibility) {
		
		Integer incrementedProbablity = winnerConfig.getWinProbability();
		
		if (winnerConfig.getWinStep() > 0 && (winnerConfig.getWinProbability() < winnerConfig.getLimit())
				
			// Check probability increment eligibility against total number of users participated for the given time period
			&& IntStream.rangeClosed(1, winnerConfig.getWinStep()).mapToLong(entryStep -> (winnerConfig.getLimit() / winnerConfig.getWinStep()) * entryStep)
				.boxed().collect(Collectors.toList()).contains(promotionEntryRepository.countByPromotionIdAndCreatedLocalDateTimeBetweenAndStatusTypeIn(winnerConfig
						.getPromotion().getId(), winnerConfig.getStartTime(), winnerConfig.getEndTime(), Arrays.asList(WON.getStatus(), LOST.getStatus())))
		) {
			
			// Increment the winning probability
			incrementedProbablity += winnerConfig.getLimit() / winnerConfig.getWinStep();
			incrementedProbablity = incrementedProbablity > winnerConfig.getLimit() ? winnerConfig.getLimit() : incrementedProbablity;
			
			LOGGER.log(LogLevel.INFO, String.format("Automatic incremented winning probability "
					+ ":: {winConfigId: %d, limit: %d, oldProbability: %d, newProbability: %d}", winnerConfig.getId(), 
					winnerConfig.getLimit(), winnerConfig.getWinProbability(), incrementedProbablity));
			
			// Update the incremented winning probability
			winnerConfig.setWinProbability(incrementedProbablity);
			winnerConfigRepository.save(winnerConfig);
		}
		
		if (Boolean.TRUE.equals(hasSpecialEligibility) && promotionEntryRepository
				.countByPromotionIdAndProfileIdAndStatusType(winnerConfig.getPromotion().getId(), profileId, WON.getStatus()) > 0) {
			
			// Temporary Project Striker Requirement: Reduce win probability for already won special user		
			incrementedProbablity = (int) (winnerConfig.getWinStep() > 0 ? (winnerConfig.getLimit() / winnerConfig.getWinStep()) : (winnerConfig.getWinProbability() * 0.1));
			LOGGER.log(LogLevel.INFO, String.format("Updated winning probability for already won special user :: %d", incrementedProbablity));
		}
		
		return incrementedProbablity;
	}
}
