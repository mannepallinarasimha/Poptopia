package com.kelloggs.promotions.lib.model;

public class ApiErrorResponseExtended extends ApiErrorResponse{
	
	private String result;

	public ApiErrorResponseExtended(Integer errorCode,String message,String result) {
		super(errorCode, message);
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	

}
