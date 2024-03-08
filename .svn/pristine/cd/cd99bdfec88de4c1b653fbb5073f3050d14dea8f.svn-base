package com.kelloggs.promotions.promotionservice.service.impl;

import com.kelloggs.promotions.lib.entity.Retailer;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.repository.RetailerRepo;
import com.kelloggs.promotions.promotionservice.service.RetailerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

@Service
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepo retailerRepo;

    public RetailerServiceImpl(RetailerRepo retailerRepo) {
        this.retailerRepo = retailerRepo;
    }

    @Override
    public ApiListResponse<Retailer> getRetailers(Integer promotionId) {

        List<Retailer> retailers = retailerRepo
                .findByPromotionsId(promotionId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No retailers found for promotion with Id:: %d", promotionId)));

        return new ApiListResponse<>(String.format("Retailer list for promotion with Id:: %d", promotionId),
                                     retailers,
                                     retailers.size());
    }
}
