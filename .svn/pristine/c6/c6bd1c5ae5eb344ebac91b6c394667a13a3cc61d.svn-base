package com.kelloggs.promotions.lib.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ApiErrorResponse {

    private  Integer errorCode;
    private  String message;
    private  Map<String, Object> metaData;

    public ApiErrorResponse() {
    	super();
    }
    
    public ApiErrorResponse(Integer errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public ApiErrorResponse(Integer errorCode, String message, Map<String, Object> metaData) {
        this.message = message;
        this.errorCode = errorCode;
        this.metaData = metaData;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, Object> getMetaData() {
		return metaData;
	}
    
    public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}
}
