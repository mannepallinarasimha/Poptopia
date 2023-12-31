package com.kelloggs.promotions.lib.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Region_Master")
public class Region implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String locale;
    
    /**
     * Add RegionRequest Entry for the particular Locale with country
     *  
     * @return Return the updated promotion entry details
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 22st December 2023
     */
	public Region() {
		super();
	}


	/**
     * Add RegionRequest Entry for the particular Locale with country
     * 
     * @param country Unique Country Name for the region
     * @param locale object - locale name for perticular region 
     *
     * @author NARASIMHARAO MANNEPALLI (10700939)
     * @since 22st December 2023
     */
	public Region(String country, String locale) {
		super();
		this.country = country;
		this.locale = locale;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
