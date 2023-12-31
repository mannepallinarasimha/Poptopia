package com.kelloggs.promotions.lib.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * PromotionPrize: This model used to handle promotion winner prize configurations
 * 
 * @author UDIT NAYAK (M1064560) 
 * @since 24-02-2022
 */
@Entity
@Table(name="promo_prize_config", uniqueConstraints = 
	@UniqueConstraint(columnNames = {"prize_code", "prize_name", "promotion_id"}))
@DynamicUpdate
public class PromotionPrize implements Serializable {

	/**
	 * Properties of the promotion prize
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "prize_code", nullable = false)
	private String prizeCode;
	
	@Column(name = "prize_name", nullable = false)
	private String prizeName;
	
	@Column(name = "inventory", nullable = false)
	private Integer inventory;
	
	@Column(name = "max_win", nullable = false)
	private Integer maxWin;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive; 
	
	@Column(name = "win_probability", nullable = false, updatable = false)
	private Integer winProbability;
	
	@Column(name = "attribute")
	private String attribute;
	
	@Column(name = "reference")
	private String reference;
	
	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "promotion_id", nullable = false)
	private Promotion promotion;


	/**
	 * Instantiate the prize
	 */
	public PromotionPrize() {
		super();
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the prizeCode
	 */
	public String getPrizeCode() {
		return prizeCode;
	}

	/**
	 * @param prizeCode the prizeCode to set
	 */
	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}

	/**
	 * @return the prizeName
	 */
	public String getPrizeName() {
		return prizeName;
	}

	/**
	 * @param prizeName the prizeName to set
	 */
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	/**
	 * @return the inventory
	 */
	public Integer getInventory() {
		return inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * @return the maxWin
	 */
	public Integer getMaxWin() {
		return maxWin;
	}

	/**
	 * @param maxWin the maxWin to set
	 */
	public void setMaxWin(Integer maxWin) {
		this.maxWin = maxWin;
	}

	/**
	 * @return the winProbability
	 */
	public Integer getWinProbability() {
		return winProbability;
	}

	/**
	 * @param winProbability the winProbability to set
	 */
	public void setWinProbability(Integer winProbability) {
		this.winProbability = winProbability;
	}

	/**
	 * @return the isActive
	 */
	public Boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the promotion
	 */
	public Promotion getPromotion() {
		return promotion;
	}

	/**
	 * @param promotion the promotion to set
	 */
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
