package com.kelloggs.promotions.promotionservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.MiniGame;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.GetMiniGameResponse;
import com.kelloggs.promotions.lib.model.MiniGameDTO;
import com.kelloggs.promotions.lib.repository.MiniGameRepo;
import com.kelloggs.promotions.lib.repository.TokenRepo;
import com.kelloggs.promotions.promotionservice.service.MiniGameService;


@Service
public class MiniGameServiceImpl implements MiniGameService {

    @Autowired
    private MiniGameRepo miniGameRepo;
    
    @Autowired
    private TokenRepo tokenRepo;

    /**
     * setMiniGame to set the state of game
     * 
     * @param gameId,token and state to be pass
     * @return	Return gameId and state if validated, otherwise throw exception
     * 
     * 
     */
	@Override
	public MiniGameDTO setMiniGame(MiniGameDTO miniGameDTO) {
		MiniGame miniGame=null;
		Integer profileId=null;
		validateMinigame(miniGameDTO);
		String gameId=miniGameDTO.getGameId();
		String userToken=miniGameDTO.getToken();
		Optional<MiniGame> optionalMG = miniGameRepo.findByGameId(gameId);
		if (optionalMG.isPresent()) {
			miniGame=updateMiniGame(optionalMG.get(),miniGameDTO);
		} else {
			Optional<Token> optionalToken = tokenRepo.findByHashCode(userToken);
			if (optionalToken.isPresent()) {
				Token dbToken = optionalToken.get();
				profileId = dbToken.getProfileId();
			} else {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Token %s is not present in database", userToken));
			}
			miniGame=getMiniGame(miniGameDTO,profileId);
		}
		MiniGame mg=miniGameRepo.save(miniGame);
		return getMiniGameDTO(mg);
	}
    
	MiniGame getMiniGame(MiniGameDTO miniGameDTO,Integer profileId) {
		MiniGame game=new MiniGame();
		game.setGameId(miniGameDTO.getGameId());
		game.setCreated(LocalDateTime.now());
		game.setUpdated(LocalDateTime.now());
		game.setState(miniGameDTO.getState());
		game.setProfileId(profileId);
		return game;
	}
	
	/**
     * updateMiniGame method
     * This method used to update the date and state of game
     * 
     */
	MiniGame updateMiniGame(MiniGame miniGame,MiniGameDTO miniGameDTO) {
		MiniGame game=new MiniGame();
		game.setId(miniGame.getId());
		game.setGameId(miniGame.getGameId());
		game.setCreated(miniGame.getCreated());
		game.setUpdated(LocalDateTime.now());
		game.setProfileId(miniGame.getProfileId());
		game.setState(miniGameDTO.getState());
		return game;
	}
	
	MiniGameDTO getMiniGameDTO(MiniGame miniGame) {
		MiniGameDTO dto=new MiniGameDTO();
		dto.setGameId(miniGame.getGameId());
		dto.setState(miniGame.getState());
		return dto;
	}
	
	void validateMinigame(MiniGameDTO miniGameDTO) {
		String result="";
		if (miniGameDTO.getGameId() == null || miniGameDTO.getGameId().isEmpty()) {
			result=result+" Game Id,";
		}
		if (miniGameDTO.getToken() == null || miniGameDTO.getToken().isEmpty()) {
			result=result+" User Token,";
		}
		if(miniGameDTO.getState()== null) {
			result=result+" State";
		}
		if(!result.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(result+" Not Found"));
		}
	}

    /**
     * getMiniGame to get the state of game
     * 
     * @param gameId,token to be pass
     * @return	Return gameId and state if validated, otherwise throw exception
     * 
     * Narasimharao Mannepalli(10700939)
     */
	@Override
	public ApiResponse<GetMiniGameResponse> getMiniGame(@Valid String gameId, @Valid String userToken) {
		validateGetMinigame(gameId, userToken);
		GetMiniGameResponse getMiniGameResponse = new GetMiniGameResponse();
		Optional<MiniGame> optionalMiniGame = miniGameRepo.findByGameId(gameId);
		if(!optionalMiniGame.isPresent()) {
			JSONObject jsonEmptyObject = new JSONObject();
			getMiniGameResponse.setGameId(gameId);
			getMiniGameResponse.setState(jsonEmptyObject);
		}else {
			Optional<Token> optionalToken = tokenRepo.findByHashCode(userToken);
			if(optionalToken.isPresent()) {
				if(optionalToken.get().getProfileId().equals(optionalMiniGame.get().getProfileId())) {
					getMiniGameResponse.setGameId(gameId);
					getMiniGameResponse.setState(optionalMiniGame.get().getState());
				}else {
		            throw new ApiException(HttpStatus.BAD_REQUEST, 400,
		                    String.format("Game with gameId \'%s\' is NOT applicable for requested profileId \'%d\'.", gameId, optionalToken.get().getProfileId()));
				}
			}
		}
		return new ApiResponse<GetMiniGameResponse>("MiniGame Details are :--- ", getMiniGameResponse);
	}
	
	/**
	* Add validateGetMinigame for the mini_game Entity
	* 
	* @author NARASIMHARAO MANNEPALLI (10700939)
	* @since 09th Feb 2024
	*/	
	void validateGetMinigame(@Valid String gameId, @Valid String userToken) {
		String result="";
		if (gameId == null || gameId.isEmpty()) {
			result=result+" Game Id,";
		}
		if (userToken == null || userToken.isEmpty()) {
			result=result+" User Token,";
		}
		if(!result.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format("Malfunction Request. "+result+" Not Found"));
		}
	}
}
