package com.kelloggs.promotions.lib.model;

import java.util.List;

public class ApiListResponse<T> {

    private final String message;
    private final List<T> data;
    private final Integer length;

    public ApiListResponse(String message, List<T> data, Integer length) {
        this.data = data;
        this.message = message;
        this.length = length;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    public Integer getLength() {
        return length;
    }
}
