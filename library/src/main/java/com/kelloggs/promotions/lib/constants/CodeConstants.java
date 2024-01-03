package com.kelloggs.promotions.lib.constants;

public enum CodeConstants {
    USED("used"),
    AVAILABLE("available"),
    INVALID("invalid"),
	WINNERS("WON"),
	LOOSERS("LOST"),
	PROMO_CODE_STATUS("PROMO_CODE_STATUS"),
	DUPLICATE("duplicate"),
	WM("wm"),
	TOS("tos"),
	POOL("pool");

    private String status;

    CodeConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
