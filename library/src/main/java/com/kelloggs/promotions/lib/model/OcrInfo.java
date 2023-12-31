package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrInfo {

    @JsonProperty(value = "ClientEventID")
    private String clientEventID;

    @JsonProperty(value = "SnippEventID")
    private String snippEventID;

    @JsonProperty(value = "ImageUrl")
    private String imageUrl;

    @JsonProperty(value = "ValidationTypeID")
    private String validationId;

    @JsonProperty(value = "ProcessingTypeID")
    private String processingId;

    @JsonProperty(value = "ProcessingStatus")
    private String procStatus;

    @JsonProperty(value = "ProcTime")
    private String procTime;

    @JsonProperty(value = "ImageStatus")
    private String imgStatus;

    @JsonProperty(value = "ItemDetails")
    private ItemDetails itemDetails;

    @JsonProperty(value = "ItemDetailsInvalid")
    private InvalidDetails invalidDetails;

    public String getClientEventID() {
        return clientEventID;
    }

    public void setClientEventID(String clientEventID) {
        this.clientEventID = clientEventID;
    }

    public String getSnippEventID() {
        return snippEventID;
    }

    public void setSnippEventID(String snippEventID) {
        this.snippEventID = snippEventID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    public String getProcessingId() {
        return processingId;
    }

    public void setProcessingId(String processingId) {
        this.processingId = processingId;
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public String getProcTime() {
        return procTime;
    }

    public void setProcTime(String procTime) {
        this.procTime = procTime;
    }

    public String getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(String imgStatus) {
        this.imgStatus = imgStatus;
    }

    public ItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }

    public InvalidDetails getInvalidDetails() {
        return invalidDetails;
    }

    public void setInvalidDetails(InvalidDetails invalidDetails) {
        this.invalidDetails = invalidDetails;
    }

    @Override
    public String toString() {
        return "OcrInfo{" +
                "clientEventID='" + clientEventID + '\'' +
                ", snippEventID='" + snippEventID + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", validationId='" + validationId + '\'' +
                ", processingId='" + processingId + '\'' +
                ", procStatus='" + procStatus + '\'' +
                ", procTime='" + procTime + '\'' +
                ", imgStatus='" + imgStatus + '\'' +
                ", itemDetails=" + itemDetails +
                ", invalidDetails=" + invalidDetails +
                '}';
    }
}
