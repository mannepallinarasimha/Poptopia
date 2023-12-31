package com.kelloggs.promotions.lib.exception;

import org.springframework.http.HttpStatus;

public class ApiExceptionExtended extends ApiException{
	
	private final String result;

	public ApiExceptionExtended(HttpStatus status, Integer errorCode, String message, String result) {
		super(status, errorCode, message);
		this.result = result;
		
	}

	public String getResult() {
		return result;
	}

}
