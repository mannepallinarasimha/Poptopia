package com.kelloggs.promotions.rewardservice.service;

import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.lib.model.RedeemRewardMetaData;

public interface RewardService {

    ApiListResponse<Reward> getRewards(Integer promotionId, Integer entryId, Integer profileId, String promotionType, Integer maxRewardPerEntry);
    ApiListResponse<Reward> getRewardsRandomly(Integer promotionId, Integer entryId, Integer profileId, String promotionType, Integer maxRewardPerEntry);
    
    /**
     * POST Redeem the reward for the user
     *  
     * @param profileId - unique profileId of the user
     * @param promotionId - Id of the current promotion
     * @param RequestBody - RedeemRewardMetaData which contains below data
     * maxRewardPerUser - the max number of reward available per user for redemption
     * exchangeCodePerReward - the number of codes for which the specific reward has to be returned.
     * @return Return the redeemed reward for the particular user.
     * 
     * @author Anshay Sehrawat (M1092350)  
     * @since 09th Nov 2023
     */
    public RedeemRewardDTO redeemReward(Integer profileId, Integer promotionId, RedeemRewardMetaData redeemRewardMetaData);
}
