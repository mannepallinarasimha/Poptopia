package com.kelloggs.promotions.lib.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.json.simple.JSONObject;




/**
 * Add MiniGame Entity
 * 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 29th January 2024
 */
@Entity
@Table(name = "mini_game")
public class MiniGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "GameId cannot be null or empty")
    @Column(name = "game_id",unique = true, columnDefinition = "VARCHAR(100)")
    private String gameId;

    @Column(name = "profile_Id", unique = false ,columnDefinition = "VARCHAR(100)")
    private Integer profileId;
    
    @NotNull(message = "Created Date cannot be null or empty")
    @Column(name = "created_date", nullable = false)
    private LocalDateTime created;

    @NotNull(message = "Updated Date cannot be null or empty")
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updated;
    
    @Column(name = "state")
    private JSONObject state;

    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

	public JSONObject getState() {
		return state;
	}

	public void setState(JSONObject state) {
		this.state = state;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
   
}
