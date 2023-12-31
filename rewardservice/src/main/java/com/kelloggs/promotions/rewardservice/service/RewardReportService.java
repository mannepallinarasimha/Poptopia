package com.kelloggs.promotions.rewardservice.service;

import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RewardRedeemed;
import com.kelloggs.promotions.lib.model.RewardReport;

import java.util.Date;

public interface RewardReportService {

    ApiResponse<RewardReport> getRewardsReport(Integer promotionId, Integer percentage, Integer maxCodeQty);

    ApiListResponse<RewardUsed> getRewardConsumedByUsersReport(Integer promotionId, Date start, Date end);

    ApiResponse<RewardRedeemed> getRewardsRedeemedReport(Integer promotionId);
}
