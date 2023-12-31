package com.kelloggs.promotions.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.util.Objects;

@Entity
@Table(name = "Reward_Master")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reward_code", columnDefinition = "VARCHAR(300)", unique = true, nullable = false)
    private String rewardCode;

    @Column(name = "reward_type", columnDefinition = "VARCHAR(100)", nullable = false)
    private String rewardType;

    @Column(columnDefinition = "VARCHAR(30)")
    private String status;

    @JsonIgnore
    @ManyToOne
    private Promotion promotion;
    
    @Column(name = "reward_step", columnDefinition = "VARCHAR(100)")
    private String rewardStep;

    @Transient
    @JsonProperty(access = READ_ONLY)
    private String country;
    
    @Transient
    @JsonProperty(access = READ_ONLY)
    private Integer profileId;

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

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @PostLoad
    public void updateCountryInReward() {
        this.country = Objects.nonNull(promotion) ? this.promotion.getRegion().getCountry() : null;
    }

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public String getRewardStep() {
		return rewardStep;
	}

	public void setRewardStep(String rewardStep) {
		this.rewardStep = rewardStep;
	}
    
    
}
