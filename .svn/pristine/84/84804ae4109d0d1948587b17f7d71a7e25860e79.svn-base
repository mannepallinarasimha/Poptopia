package com.kelloggs.promotions.promotionservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.DeletePromotionRequest;
import com.kelloggs.promotions.lib.model.DeletePromotionResponse;
import com.kelloggs.promotions.lib.model.PromotionCreateRequest;
import com.kelloggs.promotions.lib.model.PromotionResponse;
import com.kelloggs.promotions.lib.model.PromotionUpdateRequest;
import com.kelloggs.promotions.promotionservice.service.PromotionService;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    
    @GetMapping
    public ApiListResponse<Promotion> obtainPromotions() {
        return promotionService.getPromotions();
    }

    @GetMapping(path ="/{promotionId}")
    public ApiResponse<Promotion> obtainPromotion(@PathVariable(value = "promotionId") Integer promotionId) {
        return promotionService.getPromotion(promotionId);
    }
    
    @GetMapping(path ="/cluster/{clusterId}")
    public ApiListResponse<Promotion> obtainPromotionsByClusterId(@PathVariable(value = "clusterId") Integer clusterId) {
    	List<Promotion> promotions=promotionService.getPromotionsByClusterId(clusterId);
    	return new ApiListResponse<>("All promotions :--- ", promotions, promotions.size());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return promotionService.addPromotion(promotion);
    }
    
    /**
     * Add createPromotionEntry for the PromotionController Layer
     * 
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 5th January 2024
     */
    @PostMapping(path="createPromotion")
    @ResponseStatus(HttpStatus.CREATED)    
    public ApiListResponse<PromotionResponse> createPromotionEntry(@RequestBody @Valid PromotionCreateRequest promotionCreateRequest){
    	return promotionService.createPromotion(promotionCreateRequest);
    }
    
    /**
     * Add updatePromotionEntry for the PromotionController Layer
     * 
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 5th January 2024
     */
    @PutMapping(path="updatePromotion")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiListResponse<PromotionResponse> updatePromotionEntry(@RequestBody @Valid PromotionUpdateRequest promotionUpdateRequest){
    	return promotionService.updatePromotion(promotionUpdateRequest);
    }
    
    /**
     * Add deletePromotion for the PromotionController Layer
     * 
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 5th January 2024
     */
    @DeleteMapping(path="deletePromotion")
    public ApiListResponse<DeletePromotionResponse> deletePromotion(@RequestBody @Valid DeletePromotionRequest deletePromotionRequest){
    	return promotionService.deletePromotion(deletePromotionRequest);
    }

}
