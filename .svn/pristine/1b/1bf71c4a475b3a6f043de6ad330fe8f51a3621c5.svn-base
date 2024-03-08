package com.kelloggs.promotions.lib.model;

import java.util.List;

public class BadRequestResponse {

    private Integer errorCode;
    private List<BadRequest> errors;

    public BadRequestResponse(Integer errorCode, List<BadRequest> violations) {
        this.errorCode = errorCode;
        this.errors = violations;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public List<BadRequest> getErrors() {
        return errors;
    }
}
