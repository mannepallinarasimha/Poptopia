package com.kelloggs.promotions.lib.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProductItem :: This class is used to handle product details in receipt validation result
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 11th April 2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem implements Serializable {

	 /**
	 * Properties of the product item
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "line_number")
	private Integer itemId;
	
	@JsonProperty(value = "qualified")
	private Boolean isQualifiedForCampaign;

	@JsonProperty(value = "sku_value")
	private String skuValue;
	
	@JsonProperty(value = "sku_description")
	private String skuDescription;
	
	@JsonProperty(value = "upc_value")
	private String upcValue;
	
	@JsonProperty(value = "upc_description")
	private String upcDescription;
	
	@JsonProperty(value = "reference_value")
	private String referenceValue;
	
	@JsonProperty(value = "product_family")
	private String productFamily;
	
	@JsonProperty(value = "quantity")
	private Integer quantity;
	
	@JsonProperty(value = "net_unit_price")
	private Double unitPrice;
	
	@JsonProperty(value = "net_total_price")
	private Double totalPrice;
	
	@JsonProperty(value = "brand_name")
	private String brandName;
	
	@JsonProperty(value = "manufacturer_name")
	private String manufacturerName;
	
	@JsonProperty(value = "taxonomy_path")
	private String taxonomyPath;
	
	@JsonProperty(value = "category")
	private String category;
	
	/**
	 * Instantiate the product item
	 */
	public ProductItem() {
		super();
	}

	/**
	 * @return the itemId
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the isQualifiedForCampaign
	 */
	public Boolean getIsQualifiedForCampaign() {
		return isQualifiedForCampaign;
	}

	/**
	 * @param isQualifiedForCampaign the isQualifiedForCampaign to set
	 */
	public void setIsQualifiedForCampaign(Boolean isQualifiedForCampaign) {
		this.isQualifiedForCampaign = isQualifiedForCampaign;
	}

	/**
	 * @return the skuValue
	 */
	public String getSkuValue() {
		return skuValue;
	}

	/**
	 * @param skuValue the skuValue to set
	 */
	public void setSkuValue(String skuValue) {
		this.skuValue = skuValue;
	}

	/**
	 * @return the skuDescription
	 */
	public String getSkuDescription() {
		return skuDescription;
	}

	/**
	 * @param skuDescription the skuDescription to set
	 */
	public void setSkuDescription(String skuDescription) {
		this.skuDescription = skuDescription;
	}

	/**
	 * @return the upcValue
	 */
	public String getUpcValue() {
		return upcValue;
	}

	/**
	 * @param upcValue the upcValue to set
	 */
	public void setUpcValue(String upcValue) {
		this.upcValue = upcValue;
	}

	/**
	 * @return the upcDescription
	 */
	public String getUpcDescription() {
		return upcDescription;
	}

	/**
	 * @param upcDescription the upcDescription to set
	 */
	public void setUpcDescription(String upcDescription) {
		this.upcDescription = upcDescription;
	}

	/**
	 * @return the referenceValue
	 */
	public String getReferenceValue() {
		return referenceValue;
	}

	/**
	 * @param referenceValue the referenceValue to set
	 */
	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	/**
	 * @return the productFamily
	 */
	public String getProductFamily() {
		return productFamily;
	}

	/**
	 * @param productFamily the productFamily to set
	 */
	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the totalPrice
	 */
	public Double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the manufacturerName
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}

	/**
	 * @param manufacturerName the manufacturerName to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	/**
	 * @return the taxonomyPath
	 */
	public String getTaxonomyPath() {
		return taxonomyPath;
	}

	/**
	 * @param taxonomyPath the taxonomyPath to set
	 */
	public void setTaxonomyPath(String taxonomyPath) {
		this.taxonomyPath = taxonomyPath;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
