package com.kelloggs.promotions.promotionservice.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.PromotionCluster;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.promotionservice.service.PromotionClusterService;

@RestController
@RequestMapping("/api/v1/promotioncluster")
public class PromotionClusterController {

    private final PromotionClusterService promotionClusterService;

    public PromotionClusterController(PromotionClusterService promotionClusterService) {
        this.promotionClusterService = promotionClusterService;
    }
    
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionCluster> createPromotionCluster(@Valid @RequestBody PromotionCluster promotionCluster) {
    	PromotionCluster savedPromotionCluster =promotionClusterService.addPromotionCluster(promotionCluster);
        return new ApiResponse<>("PromotionCluster created with id : " + savedPromotionCluster.getClusterId(), savedPromotionCluster);
    }

    
}
