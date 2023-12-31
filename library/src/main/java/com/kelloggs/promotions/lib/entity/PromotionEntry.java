package com.kelloggs.promotions.lib.entity;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name = "Promotion_Entry")
@NamedQueries(value = {

		@NamedQuery(name = "PromotionEntry.findContestantsFromDB", query = "SELECT pe FROM PromotionEntry pe WHERE pe.promotion.id =:promoId and"
				+ " pe.attr1Code =:attr1Code and pe.status.id IS NULL and pe.attr2Value = 'entered'"),

		@NamedQuery(name = "PromotionEntry.findContestantsThatHasStatus", query = "select pe from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Code =:attr1Code and pe.attr2Code='dm_status' and pe.attr2Value ='entered' and (pe.status.id=114 OR pe.status.id=115)"),

		@NamedQuery(name = "PromotionEntry.findContestantsThatHasStatusSent", query = "select pe.attr1Value from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Code =:attr1Code and pe.attr2Code='dm_status' and pe.attr2Value ='sent' and (pe.status.id=114 OR pe.status.id=115)"),
		
		@NamedQuery(name = "PromotionEntry.findContestantsThatHasStatusWon", query = "select pe from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Code =:attr1Code and pe.attr2Code='dm_status' and (pe.status.id=114)"),
		
		@NamedQuery(name = "PromotionEntry.findByAttr1ValueAndPromoId", query = "select pe from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Value =:attr1Value"),
		
		@NamedQuery(name = "PromotionEntry.findContestantsThatHasStatusWin", query = "select pe from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Code ='twitterId' and pe.attr2Code='dm_status' and pe.attr2Value ='entered' and pe.status.id=114"),
		
		@NamedQuery(name = "PromotionEntry.findContestantsThatHasStatusLost", query = "select pe from PromotionEntry pe where pe.promotion.id =:promoId and"
				+ " pe.attr1Code ='twitterId' and pe.attr2Code='dm_status' and pe.attr2Value ='entered' and pe.status.id=115"),
		
		@NamedQuery(name = "PromotionEntry.getEntriesHavingWonStatus", query = "select pe from PromotionEntry pe where pe.promotion.id =:promotionId and"
				+ " pe.attr1Code=:attr1Code and pe.attr1Value=:attr1Value"),
		
		@NamedQuery(name = "PromotionEntry.currentWinCountBetweenStartAndEnd", query = "SELECT count(pe) FROM PromotionEntry pe WHERE  pe.promotion.id =:promotionId and "
				+ " pe.attr1Code ='PROMO_CODE_STATUS' and pe.attr1Value='WON' and pe.createdLocalDateTime between :startDate and :endDate ")
		})
@JsonInclude(value = Include.NON_NULL)
public class PromotionEntry extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Profile Id cannot be null or empty")
    @Column(name = "profile_Id", nullable = false)
    private Integer profileId;

    @Column(name = "answer", columnDefinition = "NVARCHAR(max)")
    @Lob
    private String answer;

    @Column(name = "country", nullable = false, columnDefinition = "VARCHAR(100)")
    private String country;

    @JsonProperty(access = WRITE_ONLY)
    @Transient
    private String s3ImageUrl;
    
    @JsonProperty(access = WRITE_ONLY)
    @Transient
    private List<String> s3ImageUrls;
    
    @Transient
    @JsonProperty
    private List<String> attributes;
    
    @JsonProperty(access = WRITE_ONLY, value = "retailer")
    @Transient
    private String retailerName;

    @Column(name = "Local_Created_Date_Time")
    @JsonIgnore
    private LocalDateTime createdLocalDateTime;

    @Column(name = "Local_Time_Zone",columnDefinition = "VARCHAR(100)")
    @JsonIgnore
    private String localTimeZone;

    @ManyToOne
    private Status status;

    @JsonProperty(access = READ_ONLY)
    @OneToOne
    private Retailer retailer;
    
    @Transient
    @JsonProperty(access = READ_ONLY)
    private Token token;

    @ManyToOne
    @JsonIgnore
    private Promotion promotion;
    
    @Column(name = "attr1_code")
	private String attr1Code;

	@Column(name = "attr1_value")
	private String attr1Value;

	@Column(name = "attr2_code")
	private String attr2Code;

	@Column(name = "attr2_value")
	private String attr2Value;
	
	@Column(name = "donate_prize")
	private String donatePrize;
	
	@Column(name = "invalid_short_text")
	private String invalidShortText;
	
	@ManyToOne
	@JoinColumn(name = "prize_status")
    private Status prizeStatus;
	
	@Transient
	@JsonProperty(access = WRITE_ONLY)
    private String eventId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
    
    public Token getToken() {
		return token;
	}
    
    public void setToken(Token token) {
		this.token = token;
	}

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getS3ImageUrl() {
        return s3ImageUrl;
    }

    public void setS3ImageUrl(String s3ImageUrl) {
        this.s3ImageUrl = s3ImageUrl;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public LocalDateTime getCreatedLocalDateTime() {
        return createdLocalDateTime;
    }

    public void setCreatedLocalDateTime(LocalDateTime createdLocalDateTime) {
        this.createdLocalDateTime = createdLocalDateTime;
    }

    public String getLocalTimeZone() {
        return localTimeZone;
    }

    public void setLocalTimeZone(String localTimeZone) {
        this.localTimeZone = localTimeZone;
    }

	public String getAttr1Code() {
		return attr1Code;
	}

	public void setAttr1Code(String attr1Code) {
		this.attr1Code = attr1Code;
	}

	public String getAttr1Value() {
		return attr1Value;
	}

	public void setAttr1Value(String attr1Value) {
		this.attr1Value = attr1Value;
	}

	public String getAttr2Code() {
		return attr2Code;
	}

	public void setAttr2Code(String attr2Code) {
		this.attr2Code = attr2Code;
	}

	public String getAttr2Value() {
		return attr2Value;
	}

	public void setAttr2Value(String attr2Value) {
		this.attr2Value = attr2Value;
	}

	public String getDonatePrize() {
		return donatePrize;
	}

	public void setDonatePrize(String donatePrize) {
		this.donatePrize = donatePrize;
	}

	public List<String> getS3ImageUrls() {
		return s3ImageUrls;
	}

	public void setS3ImageUrls(List<String> s3ImageUrls) {
		this.s3ImageUrls = s3ImageUrls;
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public String getInvalidShortText() {
		return invalidShortText;
	}

	public void setInvalidShortText(String invalidShortText) {
		this.invalidShortText = invalidShortText;
	}
	
	public Status getPrizeStatus() {
		return prizeStatus;
	}
	
	public void setPrizeStatus(Status prizeStatus) {
		this.prizeStatus = prizeStatus;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}	

}
