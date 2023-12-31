/**
 * 
 */
package com.kelloggs.promotions.lib.model;

/**
 * Add RegionRequest Entry for the particular Locale with country
 * 
 * @param country Unique Country Name for the region
 * @param locale object - locale name for particular region 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
public class RegionRequest {
    private String country;

    private String locale;

	public RegionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param country
	 * @param locale
	 */
	public RegionRequest(String country, String locale) {
		super();
		this.country = country;
		this.locale = locale;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
    
    
}
