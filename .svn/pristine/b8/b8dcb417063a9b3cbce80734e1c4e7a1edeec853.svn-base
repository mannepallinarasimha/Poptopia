package com.kelloggs.promotions.lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Promotion_User")
public class PromotionUser {
	 
	 @Id
	 @NotNull(message = "Profile Id cannot be null or empty")
	 @Column(name = "profile_Id", nullable = false)
	 private Integer profileId;
	 
	 @Column(name = "attribute1")
	 private Integer attribute1;

	 @Column(name = "attribute2")
	 private String attribute2;

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
	 * @return the attribute1
	 */
	public Integer getAttribute1() {
		return attribute1;
	}

	/**
	 * @param attribute1 the attribute1 to set
	 */
	public void setAttribute1(Integer attribute1) {
		this.attribute1 = attribute1;
	}

	/**
	 * @return the attribute2
	 */
	public String getAttribute2() {
		return attribute2;
	}

	/**
	 * @param attribute2 the attribute2 to set
	 */
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	 
	 
}
