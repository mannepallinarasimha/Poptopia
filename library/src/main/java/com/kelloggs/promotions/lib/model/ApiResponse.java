package com.kelloggs.promotions.lib.model;

public class ApiResponse<T> {

    private final String message;
    private final  T data;

    public ApiResponse(String mesaage, T data) {
        this.message = mesaage;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
