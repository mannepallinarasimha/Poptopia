/**
 * 
 */
package com.kelloggs.promotions.lib.model;

/**
 * Add RegionResponse Entry for the particular Locale with country
 * 
 * @param id Unique Region Id the region
 * @param locale object - locale name for particular region 
 * 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */

public class RegionResponse {
    private Integer id;

    private String locale;

	/**
	 * 
	 */
	public RegionResponse() {
		super();
	}

	/**
	 * @param id
	 * @param locale
	 */
	public RegionResponse(Integer id, String locale) {
		super();
		this.id = id;
		this.locale = locale;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	 
}
