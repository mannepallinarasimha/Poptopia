package com.kelloggs.promotions.lib.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Promotion_Master")
public class Promotion extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be null or empty")
    @Column(name = "Name", nullable = false, unique = true, columnDefinition = "VARCHAR(100)")
    private String name;

    @NotNull(message = "Epsilon Id cannot be null or empty")
    @Column(name = "Epsilon_sweepstakes_Id", nullable = false, unique = true)
    private Integer epsilonId;

    @NotNull(message = "Start Date cannot be null or empty")
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @NotNull(message = "End Date cannot be null or empty")
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @NotNull(message = "Provide max redemption limit")
    @Column(name = "Max_Redeem_Limit", nullable = false)
    private Integer maxLimit;

    @NotBlank(message = "Module Key cannot be null or empty")
    @Column(name = "Module_Key", nullable = false, unique = true,columnDefinition = "NVARCHAR(100)")
    private String moduleKey;

    @Column(name = "Local_Time_Zone",columnDefinition = "VARCHAR(100)")
    private String localTimeZone;

    @Column(name = "attr1_code" , columnDefinition = "VARCHAR(100)")
    private String attr1_code;
    
    @Column(name = "attr1_value" , columnDefinition = "VARCHAR(100)")
    private String attr1_value;
    
    @OneToOne
    private Region region;
    
    @JsonInclude(Include.NON_NULL)
    @Formula("(SELECT COUNT(w.config_id) FROM Winner_Selection_Config_Reference w WHERE w.promotion_id = id)")
    private Long winnerconfig;
    
    @ManyToOne
    private PromotionCluster promotionCluster;

    public Promotion() {
        //default constructor
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEpsilonId() {
        return epsilonId;
    }

    public void setEpsilonId(Integer epsilonId) {
        this.epsilonId = epsilonId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getLocalTimeZone() {
        return localTimeZone;
    }

    public void setLocalTimeZone(String localTimeZone) {
        this.localTimeZone = localTimeZone;
    }
    
    public String getAttr1_code() {
		return attr1_code;
	}

	public void setAttr1_code(String attr1_code) {
		this.attr1_code = attr1_code;
	}

	public String getAttr1_value() {
		return attr1_value;
	}

	public void setAttr1_value(String attr1_value) {
		this.attr1_value = attr1_value;
	}

	public Long getWinnerconfig() {
		return winnerconfig;
	}

	public void setWinnerconfig(Long winnerconfig) {
		this.winnerconfig = winnerconfig;
	}

	public PromotionCluster getPromotionCluster() {
		return promotionCluster;
	}

	public void setPromotionCluster(PromotionCluster promotionCluster) {
		this.promotionCluster = promotionCluster;
	}

}
