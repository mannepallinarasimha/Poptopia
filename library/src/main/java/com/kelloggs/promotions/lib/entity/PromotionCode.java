package com.kelloggs.promotions.lib.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Promotion_Code_Master")
public class PromotionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(150)",nullable = false, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private String status;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
    		name = "promotion_code_reference", 
    		joinColumns = @JoinColumn(name = "code_id"), 
    		inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotion(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
