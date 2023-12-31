package com.kelloggs.promotions.promotionservice.service.impl;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.PromotionCluster;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.repository.PromotionClusterRepo;
import com.kelloggs.promotions.promotionservice.service.PromotionClusterService;

@Service
public class PromotionClusterServiceImpl implements PromotionClusterService {

    private final PromotionClusterRepo promotionClusterRepo;

    public PromotionClusterServiceImpl(PromotionClusterRepo promotionClusterRepo) {
        this.promotionClusterRepo = promotionClusterRepo;
    }

	@Override
	public PromotionCluster addPromotionCluster(PromotionCluster promotionCluster) {
		PromotionCluster pc=null;
		if (!promotionCluster.getClusterName().isEmpty()) {
			pc= promotionClusterRepo.save(promotionCluster);
		}else {
			 throw new ApiException(HttpStatus.BAD_REQUEST,
						400, String.format("Cluster Name Not Found"));
		}
		return pc;
		}
}
