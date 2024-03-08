package com.kelloggs.promotions.lib.entity;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Token_Master")
@JsonInclude(value = Include.NON_NULL)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "hashCode", columnDefinition = "VARCHAR(300)", nullable = false)
    private String hashCode;

    @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = false)
    private String status;

    @NotNull
    @Column(name = "User_profile_Id", nullable = false)
    private Integer profileId;

    @NotNull
    @Column(name = "Promotion_entry_Id", nullable = false)
    private Integer entryId;

    @Column(name = "answer")
    @Lob
    private String answer;

    @Column(name = "retailer", columnDefinition = "VARCHAR(50)")
    private String retailer;
    
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }
    
    public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
    
    public LocalDateTime getExpirationTime() {
		return expirationTime;
	}
}
