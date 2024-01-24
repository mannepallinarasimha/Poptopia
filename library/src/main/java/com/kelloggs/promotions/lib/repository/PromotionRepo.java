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
	
	/**
	 * Added findByName for createPromotion API
	 * @ Narasimharao Mannepalli(10700939)
	 */
	Optional<Promotion>  findByName(String promotionName); 
	
	/**
	 * Added findByEpsilonId for createPromotion API
	 * @ Narasimharao Mannepalli(10700939)
	 */
	Optional<Promotion>  findByEpsilonId(Integer epsilonId);
	/**
	 * Added findByModuleKey for createPromotion API
	 * @ Narasimharao Mannepalli(10700939)
	 */
	Optional<Promotion>  findByModuleKey(String moduleKey); 
}
