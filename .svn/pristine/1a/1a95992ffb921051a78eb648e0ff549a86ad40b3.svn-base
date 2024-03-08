package com.kelloggs.promotions.promotionservice.service;

import java.util.List;
import java.util.Set;

import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.lib.model.PromoCodeMetaObject;

public interface PromotionCodeEntryService {

    ApiResponse<PromotionCodeEntry> addPromotionCodeEntry(Integer promotionId, Integer promocodeId, PromotionEntry promotionEntry, 
    		Boolean validateForExistingEntry, Boolean isCommonPromoCode, Boolean isMultiUsePromoCode, Boolean hasAttributes);	    
	
    /**
	 * Validate the user input promotion codes
	 * 
	 * @param promotionId	Id of the current promotion
	 * @param promoCodes	user input promotion codes	
	 * @param isCodeValidationRequired	Defines whether promotion code validation is required or not.
	 * 		  If the value is set to true, then all the given promotion codes must be valid 
	 * 		  otherwise throw exception with there respective code statuses and if the value is set to false,
	 * 		  then at least one promotion code must be valid otherwise throw not found exception.
	 * @return Return list of validate promotion codes otherwise throw error with their status
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 15th July 2022
	 */
	public List<PromotionCode> validatePromoCode(Integer promotionId, List<String> promoCodes, Boolean isCodeValidationRequired);
    
	 /**
     * Add new promotion entry for given multiple promotion codes
     *  
     * @param promotionId	Id of the current promotion
     * @param promotionEntry	user input entry details contains profileId and promoCodes 
     * @param isCodeValidationRequired	Whether code validation is required or not
     * @param validateForExistingEntry	Validate the batch code for an existing entry or new 
     * @return return newly created promotion entry details
     * 
     * @author UDIT NAYAK (M1064560) 
     * @since 14th July 2022
     */
    public PromotionEntry addMultiPromoCodeEntry(Integer promotionId, 
    		PromotionEntry promotionEntry, Boolean isCodeValidationRequired, Boolean validateForExistingEntry);

    ApiResponse<PromotionCode> getPromotionCodeByEntryId(Integer promotionId,Integer entryId);
    
    /**
     * GET Redemption data - usedBatchCodes and redeemedRewards for the user
     *  
     * @param promotionIds	Ids of the current promotion
     * @param profileId	Unique Profile id of the user
     * @return Return Redemption data - usedBatchCodes and redeemedRewards for the user
     * 
     * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    public RedeemRewardDTO getRedemptionData(Integer profileId, Set<Integer> promotionIds);
    
    /**
     * Validate the promocodes sent by the user
     *  
     * @param PromoCodeMetaObject - Contains the below details
     * profileId - unique profileId of the user
     * promotionId - Id of the current promotion
     * batchCodes - List of batch codes
     * Regex - regular expression to validate codes 
     * @return Return the saved code details of the users in case of successful validation 
     * @return Return the status of the codes if they are invalidated
     * 
     * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    public RedeemRewardDTO validateBatchCode(PromoCodeMetaObject promoCodeMetaObject);
}
