package com.kelloggs.promotions.winnerservice.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.PromoMetaObject;

public interface WinnerService {

    List<PromotionEntry> getValidEntriesForPromotion(Integer promotionId, Date startDate, Date endDate);

    ApiListResponse<PromotionEntry> getEntriesForWinnerSelection(Integer promotionId, Date startDate, Date endDate);

	ApiListResponse<?> getWinnersForSweepstake(Integer promotionId, PromoMetaObject promoMetaObject);
	
	/**
	 * Update the promotion entry with the winner selection status along with the prize.
	 * 
	 * @param promotionEntry	User promotion entry details
	 * @param winnerMap contains the winner selection status along with the prize
	 * @return Return the updated promotion entry details
	 * 
	 * @author ANSHAY SEHRAWAT (M1092350)
	 * @since 24th July 2023
	 */
	public PromotionEntry updateWinnerSelectionResult(PromotionEntry promotionEntry, Map<String, Object> winnerMap);

}
