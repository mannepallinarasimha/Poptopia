/**
 * 
 */
package com.kelloggs.promotions.lib.model;

/**
 * 
 */
public class DeletePromotionResponse {

	private Integer promotionId;

	/**
	 * 
	 */
	public DeletePromotionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param promotionId
	 */
	public DeletePromotionResponse(Integer promotionId) {
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
