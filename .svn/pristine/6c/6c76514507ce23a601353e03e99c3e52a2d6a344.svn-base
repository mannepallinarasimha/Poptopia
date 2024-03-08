package com.kelloggs.promotions.promotionservice.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.GetMiniGameResponse;
import com.kelloggs.promotions.lib.model.MiniGameDTO;
import com.kelloggs.promotions.promotionservice.service.MiniGameService;

@RestController
@RequestMapping(path="/api/v1/minigame")
public class MiniGameController {

    @Autowired
    private MiniGameService miniGameService;
    
    /**
     * Post setMiniGame to save the current state of game
     * 
     * @author Pranit (10715288)
     * 
     */
    @PostMapping(path ="/setGame")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MiniGameDTO> setMiniGame(@Valid @RequestBody MiniGameDTO miniGameDTO) {
    	MiniGameDTO miniGame =miniGameService.setMiniGame(miniGameDTO);
        return new ApiResponse<>("Mini Game Status " , miniGame);
    }
    
    /**
     * Get gMiniGame to fetch the current state of game
     * 
     * @author Narasimharao Mannepalli (10700939)
     * 
     */
    @GetMapping(path ="/getGame")
    public ApiResponse<GetMiniGameResponse> getMiniGame(@Valid @PathParam("gameId") String gameId, @Valid @PathParam("userToken") String userToken) {
    	return miniGameService.getMiniGame(gameId, userToken);
    }

}
