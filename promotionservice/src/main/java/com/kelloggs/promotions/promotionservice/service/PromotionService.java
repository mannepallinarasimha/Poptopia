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

	ApiListResponse<PromotionResponse> createPromotion(@Valid PromotionCreateRequest promotionCreateRequest);

	ApiListResponse<PromotionResponse> updatePromotion(@Valid PromotionUpdateRequest promotionUpdateRequest);

	ApiListResponse<DeletePromotionResponse> deletePromotion(@Valid DeletePromotionRequest deletePromotionRequest);
}
