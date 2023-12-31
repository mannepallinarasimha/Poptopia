package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.RewardUsed;

@Repository
public interface RewardUsedRepo extends JpaRepository<RewardUsed, Integer> {

    List<RewardUsed> findByPromotionEntryIdIn(List<Integer> entryIds);
    
    /**
   	 * Find all reward used entries by profileId and promotionIds
   	 * 
   	 * @param promotionIds	Ids of the current promotion
     * @param profileId	Unique Profile id of the user
   	 * @return	Return a list of reward used entry details if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    @Query("SELECT RC FROM RewardUsed RC WHERE RC.profileId=:profileId AND (coalesce(:promotionIds, NULL) IS NULL OR RC.promotion.id IN (:promotionIds))")
	Optional<List<RewardUsed>> findByProfileIdAndPromotionIdIn(Integer profileId, Set<Integer> promotionIds);
    
    /**
   	 * Find all reward used entries by profileId
   	 * 
     * @param profileId	Unique Profile id of the user
   	 * @return	Return a list of reward used entry details if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    Optional<List<RewardUsed>> findByProfileId(Integer profileId);
           
    
}
