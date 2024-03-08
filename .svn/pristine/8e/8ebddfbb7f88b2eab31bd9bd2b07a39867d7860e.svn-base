package com.kelloggs.promotions.lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
* Add UserGeneratedEntry for the user_generated_entry Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 29th January 2024
*/
@Entity
@Table(name = "user_generated_entry")
public class UserGeneratedEntry {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "ugc id cannot be null or empty")
    @Column(name = "ugc_id", nullable = false)
    private Integer ugcId;
    
    @NotNull(message = "user id cannot be null or empty")
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUgcId() {
        return ugcId;
    }

    public void setUgcId(Integer ugcId) {
        this.ugcId = ugcId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    

}
