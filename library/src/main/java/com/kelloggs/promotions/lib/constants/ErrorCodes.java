package com.kelloggs.promotions.lib.constants;

public enum ErrorCodes {

    DUPLICATE_CODE(1001),
    INVALID_CODE(1002),
    NOT_FOUND(404),
    INVALID_STATUS(2001),
    INVALID_PROFILE_ID(3001),
    INVALID_ENTRY_ID(3002),
    ALL_REWARDS_USED(4001),
    REDEMPTION_LIMIT_REACHED(4002),
    NOT_ELIGIBLE_FOR_REWARD(4003),
    NO_PRIZE(4004),
    NO_ELIGIBLE_ENTRIES(5001),
    NOT_DUPLICATE_OR_INVALID(6001),
    INVALID_TOKEN(6002),
    ALREADY_VALIDATED(6003),
    EXPIRED_TOKEN(6004);

    private Integer code;

    ErrorCodes(Integer code) {
        this.code =  code;
    }

    public Integer getCode() {
        return code;
    }
}
