package com.kelloggs.promotions.lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Reward_Consumed")
public class RewardUsed extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reward_code", columnDefinition = "VARCHAR(300)", unique = true, nullable = false)
    private String rewardCode;

    @ManyToOne
    private PromotionEntry promotionEntry;
    
    @Column(name = "profile_Id")
    private Integer profileId; 
    
    @ManyToOne
    @JsonIgnore
    private Promotion promotion;
    
    @OneToOne
    @JsonInclude(Include.NON_NULL)
    private Reward reward;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }
    @JsonInclude(Include.NON_NULL)
    public PromotionEntry getPromotionEntry() {
        return promotionEntry;
    }

    public void setPromotionEntry(PromotionEntry promotionEntry) {
        this.promotionEntry = promotionEntry;
    }

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}
	
	
    
    
}
