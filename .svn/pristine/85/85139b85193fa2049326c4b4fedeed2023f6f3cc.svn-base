package com.kelloggs.promotions.lib.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionUser;

@Repository
public interface PromotionUserRepo extends JpaRepository<PromotionUser,Integer> {
	
	/**
	 * @param profileId the profileId of the user
	 * @return the details of the user fetched via unique profileId
	 * @author M1092350
	 *
	 */
	Optional<PromotionUser> findByProfileId(Integer profileId);
}