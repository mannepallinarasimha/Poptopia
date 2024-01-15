package com.kelloggs.promotions.promotionservice.service;

import java.util.List;

import javax.validation.Valid;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.DeletePromotionRequest;
import com.kelloggs.promotions.lib.model.DeletePromotionResponse;
import com.kelloggs.promotions.lib.model.PromotionCreateRequest;
import com.kelloggs.promotions.lib.model.PromotionResponse;
import com.kelloggs.promotions.lib.model.PromotionUpdateRequest;

public interface PromotionService {

     ApiListResponse<Promotion> getPromotions();

     ApiResponse<Promotion> getPromotion(Integer promotionId);
     
     List<Promotion> getPromotionsByClusterId(Integer clusterId);

     Promotion getPromotionById(Integer promotionId);

     ApiResponse<Promotion> addPromotion(Promotion promotion);
     /**
      * Add createPromotion for the PromotionService Layer
      * 
      * @author NARASIMHARAO MANNEPALLI (10700939)
      * @since 5th January 2024
      */
	ApiListResponse<PromotionResponse> createPromotion(@Valid PromotionCreateRequest promotionCreateRequest);
    /**
     * Add updatePromotion for the PromotionService Layer
     * 
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 5th January 2024
     */
	ApiListResponse<PromotionResponse> updatePromotion(@Valid PromotionUpdateRequest promotionUpdateRequest);
    /**
     * Add deletePromotion for the PromotionService Layer
     * 
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 5th January 2024
     */
	ApiListResponse<DeletePromotionResponse> deletePromotion(@Valid DeletePromotionRequest deletePromotionRequest);
}
