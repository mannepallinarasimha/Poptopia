/**
 * 
 */
package com.kelloggs.promotions.lib.model;

import java.util.List;

public class PromotionUpdateRequest {
	
	private Integer clusterId;
	private List<PromotionRequestforUpdate> promotions;
	private MechanicRequest mechanic;
	private List<PromotionSetting> settings;
	/**
	 * 
	 */
	public PromotionUpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param clusterId
	 * @param promotions
	 * @param mechanic
	 * @param settings
	 */
	public PromotionUpdateRequest(Integer clusterId, List<PromotionRequestforUpdate> promotions,
			MechanicRequest mechanic, List<PromotionSetting> settings) {
		super();
		this.clusterId = clusterId;
		this.promotions = promotions;
		this.mechanic = mechanic;
		this.settings = settings;
	}
	/**
	 * @return the clusterId
	 */
	public Integer getClusterId() {
		return clusterId;
	}
	/**
	 * @param clusterId the clusterId to set
	 */
	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}
	/**
	 * @return the promotions
	 */
	public List<PromotionRequestforUpdate> getPromotions() {
		return promotions;
	}
	/**
	 * @param promotions the promotions to set
	 */
	public void setPromotions(List<PromotionRequestforUpdate> promotions) {
		this.promotions = promotions;
	}
	/**
	 * @return the mechanic
	 */
	public MechanicRequest getMechanic() {
		return mechanic;
	}
	/**
	 * @param mechanic the mechanic to set
	 */
	public void setMechanic(MechanicRequest mechanic) {
		this.mechanic = mechanic;
	}
	/**
	 * @return the settings
	 */
	public List<PromotionSetting> getSettings() {
		return settings;
	}
	/**
	 * @param settings the settings to set
	 */
	public void setSettings(List<PromotionSetting> settings) {
		this.settings = settings;
	}

}
