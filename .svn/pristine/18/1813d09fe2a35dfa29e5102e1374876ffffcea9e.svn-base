package com.kelloggs.promotions.lib.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.kelloggs.promotions.lib.model.ApiLogger;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Integer errorCode;
    private final String message;
    private Map<String, Object> metaData;
    private ApiLogger logger;

    public ApiException(HttpStatus status, Integer errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }
    
    public ApiException(HttpStatus status, Integer errorCode, String message, ApiLogger logger) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.logger = logger;
    }
    
    public ApiException(HttpStatus status, Integer errorCode, String message, Map<String, Object> metaData, ApiLogger logger) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.metaData = metaData;
        this.logger = logger;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
  
    public ApiLogger getLogger() {
		return logger;
	}
    
    public Map<String, Object> getMetaData() {
		return metaData;
	}

    @Override
    public String getMessage() {
        return message;
    }
}
