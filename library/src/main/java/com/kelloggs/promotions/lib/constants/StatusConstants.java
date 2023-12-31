package com.kelloggs.promotions.lib.constants;

public enum StatusConstants {

	PARTICIPATED("PARTICIPATED"),
    UNDER_SNIPP_PROCESSING("UNDER_SNIPP_PROCESSING"),
    VERIFIED("VERIFIED"),
    VALID("VALID"),
    INVALID("INVALID"),
    REWARD_CODE_SENT("REWARD_CODE_SENT"),
    IN_WINNER_DRAW("IN_WINNER_DRAW"),
    CLOSED("CLOSED"),
    DUPLICATE("DUPLICATE"),
    MANUAL_VERIFICATION("MANUAL_VERIFICATION"),
    NEED_NEW_IMAGE("NEED_NEW_IMAGE"),
    REUPLOAD("REUPLOAD"),
    REUPLOAD_EMAIL_SENT("REUPLOAD_EMAIL_SENT"),
    REWARD_EMAIL_SENT("REWARD_EMAIL_SENT"),
    WON("WON"),
    LOST("LOST"),
    UPLOADED("UPLOADED"),
    RECEIPT_VERIFICATION_EMAIL_SENT("RECEIPT_VERIFICATION_EMAIL_SENT"),
    SCORE_UPDATED("SCORE_UPDATED");

    private String status;

    StatusConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

