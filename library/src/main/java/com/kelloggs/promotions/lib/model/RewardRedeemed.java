package com.kelloggs.promotions.lib.model;

public class RewardRedeemed {

    private Integer promotionId;
    private Integer totalCodes;
    private Integer codeUsed;
    private Boolean fullyRedeemed;

    public RewardRedeemed(Integer promotionId, Integer totalCodes, Integer codeUsed, Boolean fullyRedeemed) {
        this.promotionId = promotionId;
        this.totalCodes = totalCodes;
        this.codeUsed = codeUsed;
        this.fullyRedeemed = fullyRedeemed;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getTotalCodes() {
        return totalCodes;
    }

    public void setTotalCodes(Integer totalCodes) {
        this.totalCodes = totalCodes;
    }

    public Integer getCodeUsed() {
        return codeUsed;
    }

    public void setCodeUsed(Integer codeUsed) {
        this.codeUsed = codeUsed;
    }

    public Boolean getFullyRedeemed() {
        return fullyRedeemed;
    }

    public void setFullyRedeemed(Boolean fullyRedeemed) {
        this.fullyRedeemed = fullyRedeemed;
    }
}
