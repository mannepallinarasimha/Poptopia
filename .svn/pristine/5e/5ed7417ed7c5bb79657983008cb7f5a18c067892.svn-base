package com.kelloggs.promotions.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Receipt_Images")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "S3_Image_URL", columnDefinition = "NVARCHAR(2000)")
    private String s3ImageUrl;

    @Column(name = "ClientEventID", columnDefinition = "VARCHAR(100)")
    private String clientEventId;

    @Column(name = "SnippEventID", columnDefinition = "NVARCHAR(300)")
    private String snippEventId;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(100)")
    private String validationTypeId;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(100)")
    private String processingTypeId;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(100)")
    private String processingStatus;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(100)")
    private String processingTime;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(50)")
    private String imageStatus;

    @OneToOne
    private PromotionEntry promotionEntry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getS3ImageUrl() {
        return s3ImageUrl;
    }

    public void setS3ImageUrl(String s3ImageUrl) {
        this.s3ImageUrl = s3ImageUrl;
    }

    public String getClientEventId() {
        return clientEventId;
    }

    public void setClientEventId(String clientEventId) {
        this.clientEventId = clientEventId;
    }

    public String getSnippEventId() {
        return snippEventId;
    }

    public void setSnippEventId(String snippEventId) {
        this.snippEventId = snippEventId;
    }

    public String getValidationTypeId() {
        return validationTypeId;
    }

    public void setValidationTypeId(String validationTypeId) {
        this.validationTypeId = validationTypeId;
    }

    public String getProcessingTypeId() {
        return processingTypeId;
    }

    public void setProcessingTypeId(String processingTypeId) {
        this.processingTypeId = processingTypeId;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public PromotionEntry getPromotionEntry() {
        return promotionEntry;
    }

    public void setPromotionEntry(PromotionEntry promotionEntry) {
        this.promotionEntry = promotionEntry;
    }
}
