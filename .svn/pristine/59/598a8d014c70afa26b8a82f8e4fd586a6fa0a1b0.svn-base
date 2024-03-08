package com.kelloggs.promotions.winnerservice.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.winnerservice.services.WinnerConfigService;

/**
 * This controller is used handle all kind of 
 * request/response related to winner selection configuration/criteria.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 23rd July 2022
 */
@RestController
@RequestMapping("/api/v1/winner/config")
public class WinnerConfigController {

	@Autowired
	private WinnerConfigService winnerConfigService;
	
	/**
	 * Instantiate the WinnerConfig
	 */
	public WinnerConfigController() {
		super();
	}
	
	 /**
	 * Get the winner selection configuration/criteria based on provided conditions.
	 * 
	 * <ul>
	 * 	<li>If request contains only [id], then searching occurs based on only given id.</li>
	 * 	<li>If request contains only [promotionId], then searching occurs based on only given promotionId.</li>
	 * 	<li>If request contains both [id] and [promotionId], then searching occurs based on both condition.</li>
	 *	<li>If request doesn't contain any of the above condition/parameter, then it searches all the record irrespective with any condition.</li>
	 * </ul>
	 * 
	 * @param promotionId	(Optional) Unique id of the promotion
	 * @param configIds		(Optional) List of winner configuration id
	 * @return Return single response if an unique id is provided and found, otherwise list of response of winner configurations.
	 */
	@GetMapping
	public ResponseEntity<?> getWinnerConfig(@RequestParam(value = "promotionId", required = false) 
				Integer promotionId, @RequestParam(value = "id", required = false) List<Integer> configIds) {
		
		ResponseEntity<?> winnerConfigReponseEntity = null;
		
		// Get the required winner selection configuration
		List<WinnerConfig> winnerConfigs = winnerConfigService.getWinnerConfig(promotionId, configIds);
		
		if (configIds != null && configIds.size() == 1) {
			
			// Return single winner configuration for the given configuration id
			winnerConfigReponseEntity = new ResponseEntity<>(new ApiResponse<>(String.format(
					"Winner config for the given id: %d", configIds.get(0)), winnerConfigs.get(0)), OK);
		} else {
			
			// Return all the winner configurations based on the request
			winnerConfigReponseEntity = new ResponseEntity<>(new ApiListResponse<>(
					"All the winner configs for the given request!", winnerConfigs, winnerConfigs.size()), OK);
		}
		return winnerConfigReponseEntity;
	}
	
	
	/**
	 * Add new winner configuration/criteria in the system
	 * 
	 * @param promotionId	Id of the current promotion
	 * @param winnerConfig	User input winner configuration details
	 * @return	Return newly created winner configuration details, otherwise throw exception if any error occur
	 */
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<WinnerConfig> addWinnerConfig(@RequestParam("promotionId") Integer promotionId, @RequestBody @Valid WinnerConfig winnerConfig) {
		
		// Create and return new winner configuration details
		return new ApiResponse<>("Winner config created successfully!", winnerConfigService.createWinnerConfig(promotionId, winnerConfig));
	} 
	
	
	/**
	 * Update winner selection configuration/criteria based on provided conditions.
	 * 
	 * <ul>
	 * 	<li>If request contains only [id], then updating occurs based on only given id.</li>
	 * 	<li>If request contains only [promotionId], then updating occurs based on only given promotionId.</li>
	 * 	<li>If request contains both [id] and [promotionId], then updating occurs based on both condition.</li>
	 *	<li>If request doesn't contain any of the above condition/parameter, then it throws bad request exception.</li>
	 * </ul>
	 * 
	 * @param promotionId	(Optional) Unique id of the promotion
	 * @param configIds		(Optional) List of winner configuration id
	 * @param newWinnerConfig	(Required) New winner configuration values to be update
	 * @return Return updated single response if an unique id is provided, otherwise list of updated winner configurations.
	 */
	@PatchMapping("/update")
	public ResponseEntity<?> updateWinnerConfig(@RequestParam(value = "promotionId", required = false) Integer promotionId, 
			@RequestParam(value = "id", required = false) List<Integer> configIds, @RequestBody Map<String, Object> newWinnerConfig) {
				
		if (promotionId == null && configIds == null) {
			
			// Throw bad request exception as any of the condition required to update winner configuration
			throw new ApiException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), 
					"Coudn't perform update operation, either promotionId or configId is required.");
		}
		
		// Update the old winner configuration details with new values
		List<WinnerConfig> updatedWinnerConfigs = winnerConfigService.updateWinnerConfigs(promotionId, configIds, newWinnerConfig);
		
		// Return the updated winner configuration details
		return new ResponseEntity<>((configIds != null && configIds.size() == 1) 
				?	new ApiResponse<>(String.format("Updated winner config for the given id: %d", configIds.get(0)), updatedWinnerConfigs.get(0))
				:	new ApiListResponse<>("Winner configs updated successfully!", updatedWinnerConfigs, updatedWinnerConfigs.size()), OK);
	} 
	
	/**
	 * Delete the winner selection configuration/criteria based on provided conditions.
	 * 
	 * <ul>
	 * 	<li>If request contains only [id], then deleting occurs based on only given id.</li>
	 * 	<li>If request contains only [promotionId], then deleting occurs based on only given promotionId.</li>
	 * 	<li>If request contains both [id] and [promotionId], then deleting occurs based on both condition.</li>
	 *	<li>If request doesn't contain any of the above condition/parameter, then it throws bad request exception.</li>
	 * </ul>
	 * 
	 * @param promotionId	(Optional) Unique id of the promotion
	 * @param configIds		(Optional) List of winner configuration id
	 * @return Return success response with deleted count
	 */
	@DeleteMapping("/delete")
	public  Map<String, Object> removeWinnerConfig(@RequestParam(value = "promotionId", required = false) 
				Integer promotionId, @RequestParam(value = "id", required = false) List<Integer> configIds) {
	
		if (promotionId == null && configIds == null) {
			
			// Throw bad request exception as any of the condition required to delete winner configuration
			throw new ApiException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), 
					"Coudn't perform delete operation, either promotionId or configId is required.");
		}
		
		// Delete the winner configuration
		Integer deletedCount = winnerConfigService.deleteWinnerConfig(promotionId, configIds);
		
		// Delete the winner configuration and generate the delete response
		Map<String, Object> deletedWinnerConfigReponse = new HashMap<>();
		deletedWinnerConfigReponse.put("message", "Winner config deleted successfully!");
		deletedWinnerConfigReponse.put("deletedCount", deletedCount);
		
		return deletedWinnerConfigReponse;
	}
	
}
