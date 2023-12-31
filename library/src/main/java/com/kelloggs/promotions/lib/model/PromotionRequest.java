/**
 * 
 */
package com.kelloggs.promotions.lib.model;


public class PromotionRequest {

	private Integer regionId;
	private Integer promotionId;
	private String moduleKey;
	private String localTimeZone;
	private String sweepStake;
	private Integer epsilonId;
	private String promotionName;
	/**
	 * 
	 */
	public PromotionRequest() {
		super();
	}
	/**
	 * @param regionId
	 * @param promotionId
	 * @param moduleKey
	 * @param localTimeZone
	 * @param sweepStake
	 * @param epsilonId
	 * @param promotionName
	 */
	public PromotionRequest(Integer regionId, Integer promotionId, String moduleKey, String localTimeZone,
			String sweepStake, Integer epsilonId, String promotionName) {
		super();
		this.regionId = regionId;
		this.promotionId = promotionId;
		this.moduleKey = moduleKey;
		this.localTimeZone = localTimeZone;
		this.sweepStake = sweepStake;
		this.epsilonId = epsilonId;
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
	 * @return the epsilonId
	 */
	public Integer getEpsilonId() {
		return epsilonId;
	}
	/**
	 * @param epsilonId the epsilonId to set
	 */
	public void setEpsilonId(Integer epsilonId) {
		this.epsilonId = epsilonId;
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
