package com.kelloggs.promotions.rewardservice.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.lib.model.RedeemRewardMetaData;
import com.kelloggs.promotions.lib.model.RewardRedeemed;
import com.kelloggs.promotions.lib.model.RewardReport;
import com.kelloggs.promotions.rewardservice.service.RewardReportService;
import com.kelloggs.promotions.rewardservice.service.RewardService;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardController.class);

    private final RewardService rewardService;

    private final RewardReportService rewardReportService;

    public RewardController(RewardService rewardService, RewardReportService rewardReportService) {
        this.rewardService = rewardService;
        this.rewardReportService = rewardReportService;
    }

    @GetMapping
    public ApiListResponse<Reward> getRewards(@RequestParam("promotionId") Integer promotionId,
                                              @RequestParam("entryId") Integer entryId,
                                              @RequestParam("profileId") Integer profileId,
                                              @RequestParam("type") String promotionType,
                                              @RequestParam(name = "maxRewardPerEntry", defaultValue = "-1", required = false) Integer maxRewardPerEntry,
                                              Principal user) {
        LOGGER.info("-------Request logging Start for Reward Service at {} -------", LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
        LOGGER.info("Requesting reward code for Promotion Id: '{}' , Promotion EntryId: '{}' , Profile Id: '{}' from user: '{}'", promotionId, entryId, profileId, user.getName());
        return rewardService.getRewards(promotionId, entryId, profileId, promotionType, maxRewardPerEntry);
    }

    @GetMapping("/reports")
    public ApiResponse<RewardReport> getRewardReport(@RequestParam("promotionId") Integer promotionId,
                                                     @RequestParam("percentage") Integer percentage,
                                                     @RequestParam("maximum") Integer maxCodeQty,
                                                     Principal user) {

        return rewardReportService.getRewardsReport(promotionId, percentage, maxCodeQty);
    }

    @GetMapping("/userReport")
    public ApiListResponse<RewardUsed> getRewardConsumedByUsers(@RequestParam("promotionId") Integer promotionId,
                                                                @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
                                                                @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {

        return rewardReportService.getRewardConsumedByUsersReport(promotionId, start, end);
    }

    @GetMapping("/check")
    public ApiResponse<RewardRedeemed> getRewardRedeemed(@RequestParam("promotionId") Integer promotionId) {

        return rewardReportService.getRewardsRedeemedReport(promotionId);
    }

    @GetMapping("/random")
    public ApiListResponse<Reward> getRewardRedeemed(@RequestParam("promotionId") Integer promotionId,
                                                     @RequestParam("entryId") Integer entryId,
                                                     @RequestParam("profileId") Integer profileId,
                                                     @RequestParam("type") String promotionType,
                                                     @RequestParam(name = "maxRewardPerEntry", defaultValue = "-1", required = false) Integer maxRewardPerEntry) {

        return rewardService.getRewardsRandomly(promotionId,entryId,profileId,promotionType, maxRewardPerEntry);
    }
    
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
    @PostMapping("/redeem")
    public ApiResponse<RedeemRewardDTO> redeemReward(@RequestParam("profileId") Integer profileId,
    											@RequestParam(name ="promotionId", required = false) Integer promotionId,
    											@RequestBody(required=false) RedeemRewardMetaData redeemRewardMetaData) {
       
		return new ApiResponse<>(String.format("Redeemed Reward details for user with profile id: %d", profileId), rewardService.redeemReward(profileId, promotionId, redeemRewardMetaData));
    }
    
}
