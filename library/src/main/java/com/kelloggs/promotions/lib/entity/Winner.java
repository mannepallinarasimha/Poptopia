package com.kelloggs.promotions.lib.entity;

import javax.persistence.*;

@Entity
@Table(name="Winners")
public class Winner extends Audit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private PromotionEntry promotionEntry;
    
    @Column(name = "win_frequency")
    private String winFrequency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PromotionEntry getPromotionEntry() {
        return promotionEntry;
    }

    public void setPromotionEntry(PromotionEntry promotionEntry) {
        this.promotionEntry = promotionEntry;
    }

	public String getWinFrequency() {
		return winFrequency;
	}

	public void setWinFrequency(String winFrequency) {
		this.winFrequency = winFrequency;
	}
    
    
}
