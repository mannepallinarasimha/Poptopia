package com.kelloggs.promotions.winnerservice.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.entity.Winner;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromoMetaObject;

public interface WinnerSelectionService {

    ApiResponse<Winner> getWinner(Integer promotionId, Date startDate, Date endDate);

    ApiResponse<Winner> addWinner(Integer entryId);

    Map<Reward, String> selectWinOrLostForMoment(Integer promotionId, Integer profileId, Integer entryId,
			PromoMetaObject promoMetaObject);

	ApiListResponse<?> getRewardForOnlineWinSelection(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep, List<Reward> responseKeyObj);

	ApiListResponse<?> getRewardOnlyForValidUser(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep);

	ApiListResponse<?> getRewardForValidWinUser(Integer promotionId, Integer profileId, Integer entryId,
			String responseValueObj, String rewardStep);

	Map<Reward, String> selectWinOrLostForPercentage(Integer promotionId, Integer profileId, Integer entryId);

	ApiListResponse<?> getResponseForOfflineWinSelection(List<Reward> responseKeyObj, String responseValueObj,
			Integer promotionId, Integer entryId);

	/**
	 * Decide the winner/loser for the given promotion entry based on winning moment and winning probability
	 *
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param hasSpecialEligibility	Determines whether the user has special eligibility criteria or not
	 * @return Return the generated token binded with winner/loser selection details
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @param hasSpecialEligibility 
	 * @since 18th July 2022
	 */
	public Token decideWinnerOrLoser(Integer promotionEntryId, Boolean hasSpecialEligibility);

	/**
	 * Decide the winner/loser for the given promotion entry based on the winning moment. Here an user is selected
	 * as a winner if the user current request time(UTC TZ) is falling under any predefined winning moments otherwise not.
	 * If the expected number of winners are not selected due to any reason for a predefined winning moment then the remaining 
	 * winners count will be selected in upcoming/next winning moments to fulfill the requirements.
	 * 
	 * @param promotionEntry	User current promotion entry details
	 * @param userRequestTime	User request time to be considered to retrieve the appropriate winning moment
	 * @param hasPrizeSelection	Select prize for the winner if the flag is set as true, otherwise no prize selection. 
	 * @return Return the updated promotion entry with selected winner/loser status and prize details
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 24th July 2023
	 */
	PromotionEntry decideWinnerOrLoser(PromotionEntry promotionEntry, LocalDateTime userRequestTime, Boolean hasPrizeSelection);
	
	/**
	 * Select prize card for the given promotion entry. The prize card selection mechanism 
	 * specifies a set of common fixed prizes in the pool for all the users. An user can win
	 * a particular prize only once sequentially one after another according to the prize order
	 * in the pool. Once an user redeemed all the prizes in the pool, then the system will return an
	 * error message on the user's next entry i.e. user entry won't be eligible for the further
	 * prize card selection.
	 * 
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param hasRandomSelection to check if the prize selection is on random basis or not
	 * @return Return the selected prize card details
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 22nd May 2023
	 */
	public PromotionPrize selectPrizeCard(PromotionEntry promotionEntry, Boolean hasRandomSelection);
}
