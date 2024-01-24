/**
 * 
 */
package com.kelloggs.promotions.lib.model;

import java.time.LocalDateTime;

import com.kelloggs.promotions.lib.entity.Region;

/**
 * Added PromotionResponse for createPromotion API
 * @ Narasimharao Mannepalli(10700939)
 */
public class PromotionResponse {

	private LocalDateTime createDate;
	private LocalDateTime modifiedDate;
	private Integer id;
	private String name;
	private Integer epsilonId;
	private String startDate;
	private String endDate;
	private Integer maxLimit;
	private String moduleKey;
	private String localTimeZone;
	private String attr1_code;
	private String attr1_value;
	private Integer winnerconfig;
	private Region region;
	/**
	 * 
	 */
	public PromotionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param createDate
	 * @param modifiedDate
	 * @param id
	 * @param name
	 * @param epsilonId
	 * @param startDate
	 * @param startDate_duplicate
	 * @param endDate
	 * @param maxLimit
	 * @param moduleKey
	 * @param localTimeZone
	 * @param attr1_code
	 * @param attr1_value
	 * @param winnerconfig
	 * @param region
	 */
	public PromotionResponse(LocalDateTime createDate, LocalDateTime modifiedDate, Integer id, String name, Integer epsilonId,
			String startDate, String endDate, Integer maxLimit, String moduleKey,
			String localTimeZone, String attr1_code, String attr1_value, Integer winnerconfig, Region region) {
		super();
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.id = id;
		this.name = name;
		this.epsilonId = epsilonId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.maxLimit = maxLimit;
		this.moduleKey = moduleKey;
		this.localTimeZone = localTimeZone;
		this.attr1_code = attr1_code;
		this.attr1_value = attr1_value;
		this.winnerconfig = winnerconfig;
		this.region = region;
	}
	/**
	 * @return the createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the modifiedDate
	 */
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the maxLimit
	 */
	public Integer getMaxLimit() {
		return maxLimit;
	}
	/**
	 * @param maxLimit the maxLimit to set
	 */
	public void setMaxLimit(Integer maxLimit) {
		this.maxLimit = maxLimit;
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
	 * @return the attr1_code
	 */
	public String getAttr1_code() {
		return attr1_code;
	}
	/**
	 * @param attr1_code the attr1_code to set
	 */
	public void setAttr1_code(String attr1_code) {
		this.attr1_code = attr1_code;
	}
	/**
	 * @return the attr1_value
	 */
	public String getAttr1_value() {
		return attr1_value;
	}
	/**
	 * @param attr1_value the attr1_value to set
	 */
	public void setAttr1_value(String attr1_value) {
		this.attr1_value = attr1_value;
	}
	/**
	 * @return the winnerconfig
	 */
	public Integer getWinnerconfig() {
		return winnerconfig;
	}
	/**
	 * @param winnerconfig the winnerconfig to set
	 */
	public void setWinnerconfig(Integer winnerconfig) {
		this.winnerconfig = winnerconfig;
	}
	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

}
