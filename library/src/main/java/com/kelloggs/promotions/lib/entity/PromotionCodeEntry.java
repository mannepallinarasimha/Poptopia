package com.kelloggs.promotions.lib.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Promotion_Code_Entry")
@JsonInclude(Include.NON_NULL)
public class PromotionCodeEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	private PromotionCode promotionCode;

	@OneToOne
	private PromotionEntry promotionEntry;

	@Column(columnDefinition = "VARCHAR(150)", nullable = false)
	private String code;

	@Column(name = "profile_Id")
	private Integer profileId;

	@ManyToOne
	@JsonProperty(value = "promotionId")
	private Promotion promotion;

	@ManyToOne
	private RewardUsed rewardUsed;

	@Column(name = "created_date_time")

	private LocalDateTime createdDateTime;

	@Column(name = "modified_date_time")
	private LocalDateTime modifiedDateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PromotionCode getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(PromotionCode promotionCode) {
		this.promotionCode = promotionCode;
	}

	public PromotionEntry getPromotionEntry() {
		return promotionEntry;
	}

	public void setPromotionEntry(PromotionEntry promotionEntry) {
		this.promotionEntry = promotionEntry;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Integer getPromotion() {
		return Objects.nonNull(promotion) ? promotion.getId() : null;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public Object getRewardUsed() {
		return Objects.nonNull(rewardUsed) ? rewardUsed.getId() : null;
	}

	public void setRewardUsed(RewardUsed rewardUsed) {
		this.rewardUsed = rewardUsed;
	}

	@PrePersist
	public void setCreatedDateTime() {
		this.createdDateTime = LocalDateTime.now(ZoneOffset.UTC);
		this.modifiedDateTime = LocalDateTime.now(ZoneOffset.UTC);
	}

	@PreUpdate
	public void setModifiedDateTime() {
		this.modifiedDateTime = LocalDateTime.now(ZoneOffset.UTC);
	}

}
