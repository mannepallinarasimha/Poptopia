package com.kelloggs.promotions.promotionservice.service;

import com.kelloggs.promotions.lib.model.PromotionClusterDTO;

public interface PromotionClusterService {

     PromotionClusterDTO addPromotionCluster(PromotionClusterDTO promotionCluster);
     
     PromotionClusterDTO deletePromotionCluster(Integer clusterId);
}
