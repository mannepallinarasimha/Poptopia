package com.kelloggs.promotions.promotionservice.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromoCodeMetaObject;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.promotionservice.service.PromotionCodeEntryService;

@RestController
@RequestMapping(value = {"/api/v1/promotions", "/api/v1/promotions/{promotionId}"})
public class PromotionCodeEntryController {

    private final PromotionCodeEntryService promotionCodeEntryService;

    public PromotionCodeEntryController(PromotionCodeEntryService promotionCodeEntryService) {
        this.promotionCodeEntryService = promotionCodeEntryService;
    }
    
    @PostMapping(path ="/promocodeEntry/{promocodeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionCodeEntry> createPromotionCodeEntry(@PathVariable(name = "promotionId") Integer promotionId, 
    		@PathVariable(name = "promocodeId") Integer promocodeId, @Valid @RequestBody PromotionEntry promotionEntry,
    		@RequestParam(name = "validateForExistingEntry", required = false, defaultValue = "false") Boolean validateForExistingEntry,
    		@RequestParam(name = "isCommonPromoCode", required = false, defaultValue = "false") Boolean isCommonPromoCode,
    		@RequestParam(name = "isMultiUsePromoCode", required = false, defaultValue = "false") Boolean isMultiUsePromoCode,
            @RequestParam(value = "hasAttributes", required = false, defaultValue = "false") Boolean hasAttributes) {

    	return promotionCodeEntryService.addPromotionCodeEntry(promotionId, promocodeId, promotionEntry, validateForExistingEntry, isCommonPromoCode, isMultiUsePromoCode, hasAttributes);
    }
    
    /**
     * Create promotion entry for multiple promotion code
     *  
     * @param promotionId	Id of the current promotion
     * @param promotionEntry	User input entry details contains profileId and promoCodes
     * @param isCodeValidationRequired	Whether code validation is required or not, default value is true
     * @param validateForExistingEntry	Validate the batch code for an existing entry or new 
     * @return Return new created promotion entry details after promotion code validation
     * 
     * @author UDIT NAYAK (M1064560)  
     * @since 14th July 2022
     */
    @PostMapping(path = "/multi/promocode/entry") 
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionEntry> createMultiPromoCodeEntry(
    		@PathVariable(name = "promotionId") Integer promotionId, @Valid @RequestBody PromotionEntry promotionEntry,
    		@RequestParam(name = "validation", required = false, defaultValue = "true") Boolean isCodeValidationRequired,
    		@RequestParam(name = "validateForExistingEntry", required = false, defaultValue = "false") Boolean validateForExistingEntry) {
    	
    	// Return the newly created promotion details after promotion code validation
    	return new ApiResponse<>(String.format("Promotion entry details for the given profile id[%d] under the promotion id[%d].", 
    			promotionEntry.getProfileId(), promotionId), promotionCodeEntryService.addMultiPromoCodeEntry(
    					promotionId, promotionEntry, isCodeValidationRequired, validateForExistingEntry));
    }


    @GetMapping(path ="/promocode")
    public ApiResponse<PromotionCode> getUsedPromoCode(@PathVariable(name = "promotionId") Integer promotionId,
                                                       @RequestParam(name = "entryId") Integer entryId) {
        return promotionCodeEntryService.getPromotionCodeByEntryId(promotionId,entryId);
    }
    
    @GetMapping(path ="/redemption/data/get")
    public ApiResponse<RedeemRewardDTO> getUsedPromoCode(@RequestParam(name = "profileId") Integer profileId,
    													   @RequestParam(value =  "promotionIds", required = false) Set<Integer> promotionIds) {

		return new ApiResponse<>(String.format("Saved PromoCode and Reward details with profile id: %d", profileId), promotionCodeEntryService.getRedemptionData(profileId, promotionIds));

    } 
    
    @PostMapping(path = "/validate/promocode") 
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RedeemRewardDTO> validatePromotionCode(@Valid @RequestBody PromoCodeMetaObject promoCodeMetaObject) {
    	    
		return new ApiResponse<>(String.format("Saved Valid Entries for profile id: %d", promoCodeMetaObject.getProfileId()), promotionCodeEntryService.validateBatchCode(promoCodeMetaObject));
    }
  
}
