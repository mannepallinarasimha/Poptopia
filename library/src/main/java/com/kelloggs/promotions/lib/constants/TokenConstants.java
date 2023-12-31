package com.kelloggs.promotions.lib.constants;

public enum TokenConstants {

    GENERATED("generated"),
    VALIDATED("validated"),
	EXPIRED("expired");

    private String status;

    TokenConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
