package com.kelloggs.promotions.lib.constants;

public enum CodeConstants {
    USED("used"),
    AVAILABLE("available"),
    INVALID("invalid"),
	WINNERS("WON"),
	LOOSERS("LOST"),
	PROMO_CODE_STATUS("PROMO_CODE_STATUS"),
	DUPLICATE("duplicate"),
	/*
	 * Added wm tos and pool for createPromotion API
	 * @Narasimharao Mannepalli(10700939)
	 * 
	 */
	WM("wm"),
	TOS("tos"),
	POOL("pool"),
	/* added by pranit (10715288) */
	TRUE("true"),
	FALSE("false");

    private String status;

    CodeConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
