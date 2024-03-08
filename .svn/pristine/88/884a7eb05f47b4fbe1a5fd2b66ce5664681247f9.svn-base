package com.kelloggs.promotions.promotionservice.controller;

import com.kelloggs.promotions.lib.entity.Retailer;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.promotionservice.service.RetailerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promotions/{promotionId}/retailers")
public class RetailerController {

    private final RetailerService retailerService;

    public RetailerController(RetailerService retailerService) {
        this.retailerService = retailerService;
    }

    @GetMapping
    public ApiListResponse<Retailer> obtainRetailers(@PathVariable(name = "promotionId") Integer promotionId) {
       return retailerService.getRetailers(promotionId);
    }
}
