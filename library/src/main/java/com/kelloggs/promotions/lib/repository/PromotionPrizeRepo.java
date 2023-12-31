package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionPrize;

/**
 * SpinWheel Repository: This repository is used to handle all spin wheel database operations.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 24-02-2022
 */
@Repository
public interface PromotionPrizeRepo extends JpaRepository<PromotionPrize, Integer> {
	
	/**
	 * Get all the promotion prize configuration from database
	 * 
	 * @param promotionId id of the current promotion
	 * @return return an optional list of promotion prize configurations
	 */
	Optional<List<PromotionPrize>> findByPromotionId(Integer promotionId);
	
	/**
	 * Count total maximum number of prize win possibilities can be won by a user
	 * 
	 * @param promotionId id of the current promotion
	 * @return return total number of prize win possibilities 
	 */
	@Query("SELECT SUM(P.maxWin) FROM PromotionPrize P WHERE P.promotion.id=?1 AND P.isActive=true")
	Optional<Integer> countMaxWinPossibilities(Integer promotionId);
	
	/**
	 * Get all the promotion prize configuration by promotion id and active status
	 * 
	 * @param promotionId id of the current promotion
	 * @param isActive active status of promotion prize
	 * @return return an optional list of promotion prize configurations based on active status
	 */
	Optional<List<PromotionPrize>> findByPromotionIdAndIsActive(Integer promotionId, Boolean isActive);
	
	/**
	 * Get the specific promotion prize configuration by promotion id , active status and reference
	 * 
	 * @param promotionId id of the current promotion
	 * @param reference contains an id which is related to the promoCodeId
	 * @param isActive active status of promotion prize
	 * @return return the promotion prize configuration based on active status
	 */
	Optional<List<PromotionPrize>> findByPromotionIdAndReferenceAndIsActive(Integer promotionId, String reference, Boolean isActive);
	
	/**
	 * Get the specific promotion prize configuration by promotion id , active status and reference
	 * 
	 * @param promotionId id of the current promotion
	 * @param reference contains an id which is mapped to the promoCodeId
	 * @return return the promotion prize configuration based on active status
	 */
	Optional<List<PromotionPrize>> findByPromotionIdAndReference(Integer promotionId, String reference);

}
