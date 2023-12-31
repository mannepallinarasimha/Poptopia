package com.kelloggs.promotions.lib.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.RewardUsed;

public class RedeemRewardDTO {
	
	 private Integer profileId;
	 
	 @JsonProperty(value = "usedBatchCodes")
	 private Set<PromotionCodeEntry> usedBatchCodes;
	 
	 @JsonProperty(value = "redeemedRewards")
	 @JsonInclude(Include.NON_NULL)
	 private Set<RewardUsed> redeemedRewards;

	/**
	 * @return the profileId
	 */
	public Integer getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the usedBatchCodes
	 */
	public Set<PromotionCodeEntry> getUsedBatchCodes() {
		return usedBatchCodes;
	}

	/**
	 * @param usedBatchCodes the usedBatchCodes to set
	 */
	public void setUsedBatchCodes(Set<PromotionCodeEntry> usedBatchCodes) {
		this.usedBatchCodes = usedBatchCodes;
	}

	/**
	 * @return the redeemedRewards
	 */
	public Set<RewardUsed> getRedeemedRewards() {
		return redeemedRewards;
	}

	/**
	 * @param redeemedRewards the redeemedRewards to set
	 */
	public void setRedeemedRewards(Set<RewardUsed> redeemedRewards) {
		this.redeemedRewards = redeemedRewards;
	}


}
