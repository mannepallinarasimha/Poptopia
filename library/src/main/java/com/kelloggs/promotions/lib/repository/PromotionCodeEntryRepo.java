package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;

@Repository
public interface PromotionCodeEntryRepo extends JpaRepository<PromotionCodeEntry,Integer> {
    
	Optional<PromotionCodeEntry> findByPromotionEntryId(Integer entryId);
	
	
	/**
   	 * Find all promotion code entries by promotion entry ID 
   	 * 
   	 * @param promotionEntryId	Id of the existing promotion entry
   	 * @return	Return a list of promotion code entry details if found, otherwise null
   	 * 
   	 * @author UDIT NAYAK (M1064560)
   	 * @since 29th June 2023
   	 */
    Optional<List<PromotionCodeEntry>> findAllByPromotionEntryId(Integer promotionEntryId);
    
    /**
   	 * Find all promotion code entry by code and entry IDs 
   	 * 
   	 * @param code	Status of the token
   	 * @param entryIds	Set of entry IDs
   	 * @return	Return a list of promotion code entry details if found, otherwise null
   	 * 
   	 * @author UDIT NAYAK (M1064560)
   	 * @since 8th May 2023
   	 */
    Optional<List<PromotionCodeEntry>> findAllByCodeAndPromotionEntryIdIn(String code, Set<Integer> entryIds);
    
    /**
   	 * Find all promotion code entry by profileId and promotionIds
   	 * 
   	 * @param promotionIds	Ids of the current promotion
     * @param profileId	Unique Profile id of the user
   	 * @return	Return a list of promotion code entry details if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
	@Query("SELECT P FROM PromotionCodeEntry P WHERE P.profileId=:profileId AND (coalesce(:promotionIds, NULL) IS NULL OR P.promotion.id IN (:promotionIds))")
	Optional<List<PromotionCodeEntry>> findByProfileIdAndPromotionIdIn(Integer profileId, Set<Integer> promotionIds);
	
	/**
   	 * Find all promotion code entry by code
   	 * 
   	 * @param code	the batchcode string
   	 * @return	Return a list of promotion code entry details if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
	Optional<List<PromotionCodeEntry>> findAllByCodeIn(List<String> codes);
	
	/**
   	 * Find all promotion code entry by profileId and rewardUsedId
   	 * 
     * @param profileId	Unique Profile id of the user
   	 * @return	Return a list of promotion code entry details if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
	Optional<List<PromotionCodeEntry>> findByProfileIdAndRewardUsedIdIsNull(Integer profileId);
	

}
