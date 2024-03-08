package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
* Add CreateUGCStatusDTO for the user_generated_content Request
* 
* @author Pranit (10715288)
* 
*/
public class CreateUGCStatusDTO {    

	@JsonProperty(value = "ugcId")
    private Integer ugcId;

	@JsonProperty(value = "userToken")
	@JsonInclude(Include.NON_NULL)
    private String userToken;

	@JsonProperty(value = "status")
    private String status;

	public Integer getUgcId() {
		return ugcId;
	}

	public void setUgcId(Integer ugcId) {
		this.ugcId = ugcId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
