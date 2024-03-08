package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
* Add MiniGameDTO for the mini_game Request
* 
* @author Pranit (10715288)
* 
*/
public class MiniGameDTO {    

	@JsonProperty(value = "gameId")
    private String gameId;

	@JsonProperty(value = "token")
	@JsonInclude(Include.NON_NULL)
    private String token;

	@JsonProperty(value = "state")
    private String state;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

   

}
