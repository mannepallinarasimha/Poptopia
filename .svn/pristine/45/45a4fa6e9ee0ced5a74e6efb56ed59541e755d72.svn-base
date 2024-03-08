package com.kelloggs.promotions.promotionservice.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromotionClusterDTO;
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
    public ApiResponse<PromotionClusterDTO> createPromotionCluster(@Valid @RequestBody PromotionClusterDTO promotionCluster) {
    	PromotionClusterDTO savedPromotionCluster =promotionClusterService.addPromotionCluster(promotionCluster);
        return new ApiResponse<>("PromotionCluster created with id : " + savedPromotionCluster.getClusterId(), savedPromotionCluster);
    }
    
    @DeleteMapping(path ="/delete/{clusterId}")
    public ApiResponse<PromotionClusterDTO> removePromotionCluster(@PathVariable(value = "clusterId") Integer clusterId) {
    	PromotionClusterDTO promotionCluster=promotionClusterService.deletePromotionCluster(clusterId);
    	return new ApiResponse<>("Deleted Promotion Cluster Id : "+ promotionCluster.getClusterId(), promotionCluster);
    }

    
}
