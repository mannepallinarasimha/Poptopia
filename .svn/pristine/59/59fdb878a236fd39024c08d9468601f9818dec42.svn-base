package com.kelloggs.promotions.lib.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ReceiptResult :: This class is used to handle receipt validation result
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 11th April 2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptResult implements Serializable {

	 /**
	 * Properties of the receipt result
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "client_transaction_id")
	 private String clientEventId;

	 @JsonProperty(value = "snipp_event_id")
	 private String snippEventId;
	 
	 @JsonProperty(value = "client_user_id")
	 private String profileId;
	 
	 @JsonProperty(value = "campaign_id")
	 private String campaignId;
	 
	 @JsonProperty(value = "submission_source")
	 private String submissionSource;
	 
	 @JsonProperty(value = "submission_type")
	 private String submissionType;
	 
	 @JsonProperty(value = "transaction_amount")
	 private String totalTransactionAmount;
	 
	 @JsonProperty(value = "transaction_id")
	 private String transactionId;
	 
	 @JsonProperty(value = "store_name")
	 private String retailerName;
	 
	 @JsonProperty(value = "seller_name")
	 private String sellerName;
	 
	 @JsonProperty(value = "phone")
	 private String retailerPhone;
	 
	 @JsonProperty(value = "city")
	 private String retailerCity;
	 
	 @JsonProperty(value = "state")
	 private String retailerState;
	 
	 @JsonProperty(value = "zipcode")
	 private String retailerZipcode;

	 @JsonProperty(value = "receipt_status_code")
	 private String receiptStatus;
	 
	 @JsonProperty(value = "receipt_status_message")
	 private String receiptStatusMessage;
	 
	 @JsonProperty(value = "ipe_status")
	 private String receiptProcessingStatus;

	 @JsonProperty(value = "items")
	 private List<ProductItem> productItems;

	 @JsonProperty(value = "created_at")
	 private LocalDateTime createdAt;
	 
	 @JsonProperty(value = "updated_at")
	 private LocalDateTime updatedAt;
	 
	 /**
	  * Instantiate the receipt result
	  */
	 public ReceiptResult() {
		 super();
	}

	/**
	 * @return the clientEventId
	 */
	public String getClientEventId() {
		return clientEventId;
	}

	/**
	 * @param clientEventId the clientEventId to set
	 */
	public void setClientEventId(String clientEventId) {
		this.clientEventId = clientEventId;
	}

	/**
	 * @return the snippEventId
	 */
	public String getSnippEventId() {
		return snippEventId;
	}

	/**
	 * @param snippEventId the snippEventId to set
	 */
	public void setSnippEventId(String snippEventId) {
		this.snippEventId = snippEventId;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the campaignId
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @return the submissionSource
	 */
	public String getSubmissionSource() {
		return submissionSource;
	}

	/**
	 * @param submissionSource the submissionSource to set
	 */
	public void setSubmissionSource(String submissionSource) {
		this.submissionSource = submissionSource;
	}

	/**
	 * @return the submissionType
	 */
	public String getSubmissionType() {
		return submissionType;
	}

	/**
	 * @param submissionType the submissionType to set
	 */
	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}

	/**
	 * @return the totalTransactionAmount
	 */
	public String getTotalTransactionAmount() {
		return totalTransactionAmount;
	}

	/**
	 * @param totalTransactionAmount the totalTransactionAmount to set
	 */
	public void setTotalTransactionAmount(String totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the retailerName
	 */
	public String getRetailerName() {
		return retailerName;
	}

	/**
	 * @param retailerName the retailerName to set
	 */
	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	/**
	 * @return the sellerName
	 */
	public String getSellerName() {
		return sellerName;
	}

	/**
	 * @param sellerName the sellerName to set
	 */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	/**
	 * @return the retailerPhone
	 */
	public String getRetailerPhone() {
		return retailerPhone;
	}

	/**
	 * @param retailerPhone the retailerPhone to set
	 */
	public void setRetailerPhone(String retailerPhone) {
		this.retailerPhone = retailerPhone;
	}

	/**
	 * @return the retailerCity
	 */
	public String getRetailerCity() {
		return retailerCity;
	}

	/**
	 * @param retailerCity the retailerCity to set
	 */
	public void setRetailerCity(String retailerCity) {
		this.retailerCity = retailerCity;
	}

	/**
	 * @return the retailerState
	 */
	public String getRetailerState() {
		return retailerState;
	}

	/**
	 * @param retailerState the retailerState to set
	 */
	public void setRetailerState(String retailerState) {
		this.retailerState = retailerState;
	}

	/**
	 * @return the retailerZipcode
	 */
	public String getRetailerZipcode() {
		return retailerZipcode;
	}

	/**
	 * @param retailerZipcode the retailerZipcode to set
	 */
	public void setRetailerZipcode(String retailerZipcode) {
		this.retailerZipcode = retailerZipcode;
	}

	/**
	 * @return the receiptStatus
	 */
	public String getReceiptStatus() {
		return receiptStatus;
	}

	/**
	 * @param receiptStatus the receiptStatus to set
	 */
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

	/**
	 * @return the receiptStatusMessage
	 */
	public String getReceiptStatusMessage() {
		return receiptStatusMessage;
	}

	/**
	 * @param receiptStatusMessage the receiptStatusMessage to set
	 */
	public void setReceiptStatusMessage(String receiptStatusMessage) {
		this.receiptStatusMessage = receiptStatusMessage;
	}

	/**
	 * @return the receiptProcessingStatus
	 */
	public String getReceiptProcessingStatus() {
		return receiptProcessingStatus;
	}

	/**
	 * @param receiptProcessingStatus the receiptProcessingStatus to set
	 */
	public void setReceiptProcessingStatus(String receiptProcessingStatus) {
		this.receiptProcessingStatus = receiptProcessingStatus;
	}

	/**
	 * @return the productItems
	 */
	public List<ProductItem> getProductItems() {
		return productItems;
	}

	/**
	 * @param productItems the productItems to set
	 */
	public void setProductItems(List<ProductItem> productItems) {
		this.productItems = productItems;
	}

	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
