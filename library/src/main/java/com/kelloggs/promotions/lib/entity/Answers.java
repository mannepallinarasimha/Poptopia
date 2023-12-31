package com.kelloggs.promotions.lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Answers: This model used to enter the answer details entered by the user against their entryiD
 * 
 * @author ANSHAY SEHRAWAT (M1064560) 
 * @since 03-05-2023
 */
@Entity
@Table(name = "answers")
public class Answers {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	    
    @ManyToOne
    @JoinColumn(name="promotion_entry_id")
    private PromotionEntry promotionEntry;
	
	@Column(name = "answer" , nullable = false,columnDefinition = "NVARCHAR(2000)")
	private String answer;

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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	 
}
