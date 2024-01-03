/**
 * 
 */
package com.kelloggs.promotions.lib.model;

/**
 * 
 */
public class PromotionRequestforUpdate {

	private Integer regionId;
	private Integer promotionId;
	private String moduleKey;
	private String sweepStake;
	private String localTimeZone;
	private String promotionName;
	/**
	 * 
	 */
	public PromotionRequestforUpdate() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param regionId
	 * @param promotionId
	 * @param moduleKey
	 * @param sweepStake
	 * @param localTimeZone
	 * @param promotionName
	 */
	public PromotionRequestforUpdate(Integer regionId, Integer promotionId, String moduleKey, String sweepStake,
			String localTimeZone, String promotionName) {
		super();
		this.regionId = regionId;
		this.promotionId = promotionId;
		this.moduleKey = moduleKey;
		this.sweepStake = sweepStake;
		this.localTimeZone = localTimeZone;
		this.promotionName = promotionName;
	}
	/**
	 * @return the regionId
	 */
	public Integer getRegionId() {
		return regionId;
	}
	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
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
	/**
	 * @return the moduleKey
	 */
	public String getModuleKey() {
		return moduleKey;
	}
	/**
	 * @param moduleKey the moduleKey to set
	 */
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}
	/**
	 * @return the sweepStake
	 */
	public String getSweepStake() {
		return sweepStake;
	}
	/**
	 * @param sweepStake the sweepStake to set
	 */
	public void setSweepStake(String sweepStake) {
		this.sweepStake = sweepStake;
	}
	/**
	 * @return the localTimeZone
	 */
	public String getLocalTimeZone() {
		return localTimeZone;
	}
	/**
	 * @param localTimeZone the localTimeZone to set
	 */
	public void setLocalTimeZone(String localTimeZone) {
		this.localTimeZone = localTimeZone;
	}
	/**
	 * @return the promotionName
	 */
	public String getPromotionName() {
		return promotionName;
	}
	/**
	 * @param promotionName the promotionName to set
	 */
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	
	
}
