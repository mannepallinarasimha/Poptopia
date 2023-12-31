/**
 * 
 */
package com.kelloggs.promotions.lib.model;

import java.util.Date;

/**
 * 
 */
public class MechanicRequest {

	private String type;
	private Date startDate;
	private Date endDate;
	private String attr1_code;
	private String attr1_value;
	/**
	 * 
	 */
	public MechanicRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @param attr1_code
	 * @param attr1_value
	 */
	public MechanicRequest(String type, Date startDate, Date endDate, String attr1_code, String attr1_value) {
		super();
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attr1_code = attr1_code;
		this.attr1_value = attr1_value;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
	
	

}
