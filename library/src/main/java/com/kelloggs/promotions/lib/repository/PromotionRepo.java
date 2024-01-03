package com.kelloggs.promotions.lib.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.Promotion;

@Repository
public interface PromotionRepo extends JpaRepository<Promotion,Integer> {
	Optional<Promotion>  findById(Integer promotionId);
	
	Optional<List<Promotion>>  findByPromotionClusterId(Integer clusterId); 
	
	Optional<Promotion>  findByName(String promotionName); 
	
	Optional<Promotion>  findByEpsilonId(Integer epsilonId);
	
	Optional<Promotion>  findByModuleKey(String moduleKey); 
}
