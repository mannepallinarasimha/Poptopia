package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.Reward;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepo extends JpaRepository<Reward, Integer> {

    Optional<Reward> findFirstByPromotionIdAndStatus(Integer promotionId, String status);

    List<Reward> findByPromotionIdAndStatus(Integer promotionId, String status);

    Optional<List<Reward>> findByPromotionIdAndStatus(Integer promotionId, String status, Pageable pageable);

    default Optional<List<Reward>> getRewardsByQuantity(Integer promotionId, String status, Integer limit){
         return findByPromotionIdAndStatus(promotionId, status, PageRequest.of(0,limit));
    }
    
    List<Reward> findByPromotionIdAndStatusAndRewardStep(Integer promotionId, String status, String rewardStep);
    
    Optional<List<Reward>> findByPromotionIdAndStatusAndRewardStep(Integer promotionId, String status,
			String rewardStep, Pageable pageable);
    
    default Optional<List<Reward>> getRewardsByQuantities(Integer promotionId, String status, String rewardStep, Integer limit){
        return findByPromotionIdAndStatusAndRewardStep(promotionId, status,rewardStep, PageRequest.of(0,limit));
   }
    /**
   	 * Find all rewards by promotionId(if not null) , status and reward step
   	 * 
   	 * @param profileId - unique profileId of the user
     * @param promotionId - Id of the current promotion
     * @param rewardStep the reward step associated with the reward
     * 
   	 * @return	Return a list of rewards if found, otherwise null
   	 * 
   	 * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    @Query("SELECT R FROM Reward R WHERE (coalesce(:promotionId, NULL) IS NULL OR R.promotion.id = :promotionId) AND R.status=:status AND R.rewardStep = :rewardStep")
    Optional<List<Reward>> findAllByPromotionIdAndStatusAndRewardStep(Integer promotionId, String status, String rewardStep, Pageable pageable);

	Reward findByRewardCode(String code);

    Optional<List<Reward>> findByPromotionId(Integer promotionId);
}
