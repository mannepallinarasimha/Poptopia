package com.kelloggs.promotions.lib.entity;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="Winner_Selection_Config")

@NamedQueries(value = {
		  
		 @NamedQuery(name = "WinnerConfig.findWinnerConfigFromDB", query ="SELECT wc FROM WinnerConfig wc WHERE wc.promotion.id =:promoId")})
public class WinnerConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
	private int id;
	
	@ManyToOne
    @JsonIgnore
    private Promotion promotion;
	
	@Column(name = "promotion_date")
	@Temporal(TemporalType.DATE)
    @CreatedDate
	private Date promotionDate;
	
    @NotEmpty(message = "Max winner can't be null or empty")
	@Column(name = "max_winners", nullable = false)
	private int maxWinners; 
	
    @NotEmpty(message = "Limit can't be null or empty")
	@Column(name = "limit", nullable = false)
	private int limit;
	
    @NotEmpty(message = "Win probability can't be null or empty")
	@Column(name = "win_probability", nullable = false)
	private int winProbability;
	
    @NotEmpty(message = "Win step can't be null or empty")
	@Column(name = "win_step", nullable = false)
	private int winStep;
	
	@Column(name = "start_time")
	private LocalDateTime startTime;
	
	@Column(name = "end_time")
	private LocalDateTime endTime;
	
	@ManyToMany
    @JsonIgnore
    @JoinTable(
    		name = "Winner_Selection_Config_Reference", 
    		joinColumns = @JoinColumn(name = "config_id"), 
    		inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public Date getPromotionDate() {
		return promotionDate;
	}

	public void setPromotionDate(Date promotionDate) {
		this.promotionDate = promotionDate;
	}

	public int getMaxWinner() {
		return maxWinners;
	}

	public void setMaxWinner(int maxWinner) {
		this.maxWinners = maxWinner;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getWinProbability() {
		return winProbability;
	}

	public void setWinProbability(int winProbability) {
		this.winProbability = winProbability;
	}
	
	public int getWinStep() {
		return winStep;
	}
	
	public void setWinStep(int winStep) {
		this.winStep = winStep;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotion(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

}

