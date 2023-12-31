package com.kelloggs.promotions.rewardservice.service.impl;

import com.kelloggs.promotions.lib.entity.*;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RewardRedeemed;
import com.kelloggs.promotions.lib.model.RewardReport;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.RewardRepo;
import com.kelloggs.promotions.lib.repository.RewardUsedRepo;
import com.kelloggs.promotions.rewardservice.service.RewardReportService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.kelloggs.promotions.lib.constants.CodeConstants.USED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static com.kelloggs.promotions.lib.constants.StatusConstants.*;

@Service
public class RewardReportServiceImpl implements RewardReportService {

    private final PromotionRepo promotionRepo;

    private final RewardRepo rewardRepo;

    private final RewardUsedRepo rewardUsedRepo;

    private final PromotionEntryRepo promotionEntryRepo;

    public RewardReportServiceImpl(PromotionRepo promotionRepo, RewardRepo rewardRepo, RewardUsedRepo rewardUsedRepo, PromotionEntryRepo promotionEntryRepo) {
        this.promotionRepo = promotionRepo;
        this.rewardRepo = rewardRepo;
        this.rewardUsedRepo = rewardUsedRepo;
        this.promotionEntryRepo = promotionEntryRepo;
    }

    @Override
    public ApiResponse<RewardReport> getRewardsReport(Integer promotionId, Integer percentage, Integer maxCodeQty) {

        Promotion promotion = promotionRepo.findById(promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No promotion found with Id: %d", promotionId)));

        List<Reward> usedRewards = rewardRepo.findByPromotionIdAndStatus(promotionId, USED.getStatus());

        Integer codeUsed = usedRewards.size();
        Double percentageUsed = (codeUsed * 100) / Double.valueOf(maxCodeQty);

        Boolean alert = percentageUsed >= percentage;

        Region region = promotion.getRegion();

        RewardReport rewardReport = new RewardReport(promotion.getName(),
                region.getCountry(),
                region.getLocale(),
                codeUsed,
                maxCodeQty,
                percentageUsed,
                alert);

        return new ApiResponse<>(String.format("Rewards Report for promotion with Id ::- %d", promotionId), rewardReport);

    }

    @Override
    public ApiListResponse<RewardUsed> getRewardConsumedByUsersReport(Integer promotionId, Date startDate, Date endDate) {

        List<String> statusFilters = Arrays.asList(INVALID.getStatus(),
                UNDER_SNIPP_PROCESSING.getStatus(),
                DUPLICATE.getStatus(),
                VERIFIED.getStatus(),
                IN_WINNER_DRAW.getStatus(),
                REUPLOAD.getStatus(),
                REUPLOAD_EMAIL_SENT.getStatus());

        List<PromotionEntry> rewardedEntries = promotionEntryRepo
                .findByPromotionIdAndCreatedDateBetweenAndStatusTypeNotIn(promotionId, startDate, endDate, statusFilters)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No entries found that consumed reward for the slot from %s to %s for promotion with Id : %d",
                                startDate, endDate, promotionId)));

        List<Integer> rewardedEntryIds = rewardedEntries
                .stream()
                .map(PromotionEntry::getId)
                .collect(Collectors.toList());

        Integer totalRewardedEntries = rewardedEntryIds.size();
        Integer start = 0;
        Integer limit = totalRewardedEntries > 2000 ? 2000 : totalRewardedEntries;
        Integer end = limit;
        List<RewardUsed> rewardUsedList = new ArrayList<>();

        while (totalRewardedEntries > 0) {
            List<Integer> entries = rewardedEntryIds.subList(start, end);
            start = end;
            totalRewardedEntries -= limit;
            end = limit + totalRewardedEntries;
            List<RewardUsed> rewardUsed = rewardUsedRepo.findByPromotionEntryIdIn(entries);
            rewardUsedList.addAll(rewardUsed);
        }

        return new ApiListResponse<>(String.format("Reward Consumed Report for promotion with Id :- %d for the slot from %s to %s", promotionId, startDate, endDate),
                rewardUsedList,
                rewardUsedList.size());

    }

    @Override
    public ApiResponse<RewardRedeemed> getRewardsRedeemedReport(Integer promotionId) {

        List<Reward> rewards = rewardRepo
                .findByPromotionId(promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No promotion found with Id: %d", promotionId)));
        Integer totalRewards = rewards.size();

        List<Reward> used = rewards
                .stream()
                .filter(reward -> reward.getStatus().equals(USED.getStatus()))
                .collect(Collectors.toList());

        Integer rewardUsed = used.size();

        Boolean fullyRedeemed = false;

        if (totalRewards.equals(rewardUsed))
            fullyRedeemed = true;

        RewardRedeemed rewardRedeemed = new RewardRedeemed(promotionId,
                totalRewards,
                rewardUsed,
                fullyRedeemed);

        return new ApiResponse<>(String.format("Reward Redeemed Report for promotion Id:- %d", promotionId), rewardRedeemed);
    }
}
