package com.kelloggs.promotions.lib.model;
/**
* Add GetMiniGameRequest for the mini_game Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
public class GetMiniGameRequest {

    private String gameId;
    private String token;
    
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
    
}
