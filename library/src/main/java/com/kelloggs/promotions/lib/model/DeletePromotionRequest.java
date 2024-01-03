/**
 * 
 */
package com.kelloggs.promotions.lib.model;

import java.util.List;

/**
 * 
 */
public class DeletePromotionRequest {

	private Integer clusterId;
	private List<PromotionIdsRequest> promotions;
	/**
	 * 
	 */
	public DeletePromotionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param clusterId
	 * @param promotion
	 */
	public DeletePromotionRequest(Integer clusterId, List<PromotionIdsRequest> promotions) {
		super();
		this.clusterId = clusterId;
		this.promotions = promotions;
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
	 * @return the promotion
	 */
	public List<PromotionIdsRequest> getPromotions() {
		return promotions;
	}
	/**
	 * @param promotion the promotion to set
	 */
	public void setPromotion(List<PromotionIdsRequest> promotions) {
		this.promotions = promotions;
	}
	
}
