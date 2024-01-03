package com.kelloggs.promotions.lib.model;

public class PromotionIdsRequest {

	private Integer promotionId;

	/**
	 * 
	 */
	public PromotionIdsRequest() {
		super();
	}

	/**
	 * @param promotionId
	 */
	public PromotionIdsRequest(Integer promotionId) {
		super();
		this.promotionId = promotionId;
	}

	/**
	 * @return the promotionId
	 */
	public Integer getPromotionId() {
		return promotionId;
	}

	/**
	 * @param promotionId the promotionId to set
	 */
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	
}
