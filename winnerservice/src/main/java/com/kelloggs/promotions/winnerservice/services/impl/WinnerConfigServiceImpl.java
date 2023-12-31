package com.kelloggs.promotions.winnerservice.services.impl;

import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.winnerservice.services.WinnerConfigService;

/**
 * This service is used server all kind of 
 * operation related to winner selection configuration/criteria.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 23rd July 2022
 */
@Service
public class WinnerConfigServiceImpl implements WinnerConfigService {
	
	@Autowired
	private WinnerConfigRepo winnerConfigRepo;
	
	@Autowired
	private PromotionRepo promotionRepo;
	
	private static final ApiLogger LOGGER =
			new ApiLogger(WinnerConfigServiceImpl.class);
	
	/**
	 * Instantiate the service
	 */
	public WinnerConfigServiceImpl() {
		super();
	}

	@Override
	public List<WinnerConfig> getWinnerConfig(Integer promotionId, List<Integer> configIds) {
		
		// Return all the winner selection configuration from database
		return winnerConfigRepo.findByPromotionIdAndConfigIdIn(promotionId, configIds)
				.orElseThrow(() -> new ApiException(NOT_FOUND, NOT_FOUND.value(), 
						"No winner config found for the given request!", LOGGER.setLogLevel(ERROR)));
	}
	
	@Override
	public WinnerConfig createWinnerConfig(Integer promotionId, WinnerConfig winnerConfig) {
		
		// Set promotion details in winner configuration 
		winnerConfig.setPromotion(promotionRepo.findById(promotionId).orElseThrow(() -> new ApiException(NOT_FOUND, 
				NOT_FOUND.value(), String.format("Invalid promotion Id:: %d", promotionId), LOGGER.setLogLevel(ERROR))));
		
		// Save the winner configuration in database
		return winnerConfigRepo.save(winnerConfig);
	}
	
	@Override
	public List<WinnerConfig> updateWinnerConfigs(Integer promotionId, List<Integer> configIds, Map<String, Object> newWinnerConfig) {
		
		// Get the winner configuration from database
		List<WinnerConfig> updatedWinnerConfigs = getWinnerConfig(promotionId, configIds).stream().map(oldWinnerConfig -> {
			try {
				
				Integer newPromotionId = (Integer) newWinnerConfig.get("promotion");
				
				if (newPromotionId != null) {
					
					// Update the new promotion details in winner configuration
					oldWinnerConfig.setPromotion(promotionRepo.findById(newPromotionId).orElseThrow(() -> new ApiException(NOT_FOUND, 
							NOT_FOUND.value(), String.format("Invalid new promotion Id:: %d", newPromotionId), LOGGER.setLogLevel(ERROR))));
				}
				// Update the new winner configuration values
				return new ObjectMapper().findAndRegisterModules().updateValue(oldWinnerConfig, newWinnerConfig);
				
			} catch (JsonMappingException exception) {
				
				// Throw request parsing exception, if failed to parse
				throw new ApiException(NOT_ACCEPTABLE, NOT_ACCEPTABLE.value(), 
						String.format("Request Parsing Error:: %s", exception.getMessage()), LOGGER.setLogLevel(ERROR));
			}
		}).collect(Collectors.toList());
		
		// Return the updated winner configurations
		return winnerConfigRepo.saveAll(updatedWinnerConfigs);
	}
	
	@Override
	public Integer deleteWinnerConfig(Integer promotionId, List<Integer> configIds) {
		
		// Get the winner configuration from database
		List<WinnerConfig> winnerConfigs = getWinnerConfig(promotionId, configIds);
		
		// Delete the winner configuration
		winnerConfigRepo.deleteAll(winnerConfigs);
		
		return winnerConfigs.size();
	}

}
