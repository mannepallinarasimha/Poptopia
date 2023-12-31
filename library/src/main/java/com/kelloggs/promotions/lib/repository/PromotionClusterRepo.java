package com.kelloggs.promotions.lib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionCluster;

@Repository
public interface PromotionClusterRepo extends JpaRepository<PromotionCluster,Integer> {

   
}
