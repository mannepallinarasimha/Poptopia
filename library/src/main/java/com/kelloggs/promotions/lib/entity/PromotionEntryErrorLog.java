package com.kelloggs.promotions.lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "promotion_entry_errorlog")
public class PromotionEntryErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JsonIgnore
	private Promotion promotion;
	
	@ManyToOne
	private PromotionEntry promotionEntry;
	
	@Column(name = "created_date_time", columnDefinition = "datetime2(7) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private String created_date_time;
	
	@Column(name = "error_code")
	private int errorCode;
	
	@Column(name = "error_msg")
	private String errorMessage;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public PromotionEntry getPromotionEntry() {
		return promotionEntry;
	}

	public void setPromotionEntry(PromotionEntry promotionEntry) {
		this.promotionEntry = promotionEntry;
	}

	public String getCreated_date_time() {
		return created_date_time;
	}

	public void setCreated_date_time(String created_date_time) {
		this.created_date_time = created_date_time;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String string) {
		this.errorMessage = string;
	}

}
