package com.kelloggs.promotions.lib.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Receipt_Header")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty(value = "Retailer")
    private String retailer;

	private String phone;

	private String state;
	
	private String city;
	
    @JsonProperty(value = "Postcode")
    private String postCode;

    @JsonProperty(value = "ReceiptTrans")
    private String receiptTrans;

    @JsonProperty(value = "TotalPrice")
    private String totalPrice;

    @OneToOne
    private Receipt receipt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPhone() {
		return phone;
	}
    
    public void setPhone(String phone) {
		this.phone = phone;
	}
    
    public String getState() {
		return state;
	}
    
    public void setState(String state) {
		this.state = state;
	}
    
    public String getCity() {
		return city;
	}
    
    public void setCity(String city) {
		this.city = city;
	}
    
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getReceiptTrans() {
        return receiptTrans;
    }

    public void setReceiptTrans(String receiptTrans) {
        this.receiptTrans = receiptTrans;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Header{" +
                "retailer='" + retailer + '\'' +
                ", postCode='" + postCode + '\'' +
                ", receiptTrans='" + receiptTrans + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}

