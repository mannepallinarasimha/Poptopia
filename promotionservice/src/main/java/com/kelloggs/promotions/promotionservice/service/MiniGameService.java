package com.kelloggs.promotions.promotionservice.service;

import javax.validation.Valid;

import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.GetMiniGameResponse;
import com.kelloggs.promotions.lib.model.MiniGameDTO;

public interface MiniGameService {
	
	MiniGameDTO setMiniGame(MiniGameDTO miniGameDTO);

	ApiResponse<GetMiniGameResponse> getMiniGame(@Valid String gameId, @Valid String userToken);

}
