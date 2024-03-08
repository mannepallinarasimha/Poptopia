package com.kelloggs.promotions.lib.model;

/**
* Add GetMiniGameResponse for the mini_game Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 07th Feb 2024
*/
public class GetMiniGameResponse {
    private String gameId;
    private String state;

    public String getGameId() {
        return gameId;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    

}
