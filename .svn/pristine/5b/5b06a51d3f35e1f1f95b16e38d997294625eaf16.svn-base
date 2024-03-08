package com.kelloggs.promotions.lib.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kelloggs.promotions.lib.entity.Promotion;


public class PromotionClusterDTO {

    private Integer id;

    @JsonProperty(value = "clusterName")
    private String clusterName;
    
    @JsonProperty(value = "promotions")
    @JsonInclude(Include.NON_NULL)
    List<Promotion> promotions; 
    
	public Integer getClusterId() {
		return id;
	}

	public void setClusterId(Integer id) {
		this.id = id;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

}
