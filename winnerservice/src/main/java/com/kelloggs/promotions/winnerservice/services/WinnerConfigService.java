package com.kelloggs.promotions.winnerservice.services;

import java.util.List;
import java.util.Map;

import com.kelloggs.promotions.lib.entity.WinnerConfig;

/**
 * This service is used serve all kind of 
 * operation related to winner selection configuration/criteria.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 23rd July 2022
 */
public interface WinnerConfigService {

	/**
	 * Get winner configuration from database
	 * 
	 * @param promotionId	Id of the promotion to be use
	 * @param configIds		List of winner configuration id provided by user		
	 * @return Return the list of winner configurations based on the inputs
	 */
	public List<WinnerConfig> getWinnerConfig(Integer promotionId, List<Integer> configIds);

	/**
	 * Create a new winner configuration in database
	 * 
	 * @param promotionId Id of the current promotion to be use
	 * @param winnerConfig	User input winner configuration details
	 * @return	Return created winner configuration details, otherwise throw exception if any error occur
	 */
	public WinnerConfig createWinnerConfig(Integer promotionId, WinnerConfig winnerConfig);
	
	/**
	 * Update winner configuration in database
	 * 
	 * @param promotionId	Id of the promotion to be use
	 * @param configIds		List of winner configuration id provided by user	
	 * @param newWinnerConfig	(Required) New winner configuration values to be update
	 * @return Return the updated winner configurations based on the inputs
	 */
	public List<WinnerConfig> updateWinnerConfigs(Integer promotionId, List<Integer> configIds, Map<String, Object> newWinnerConfig);

	/**
	 * Delete winner configuration from database
	 * 
	 * @param promotionId	Id of the promotion to be use
	 * @param configIds		List of winner configuration id provided by user		
	 * @return Return the deleted count after winner configuration deletion
	 */
	public Integer deleteWinnerConfig(Integer promotionId, List<Integer> configIds);
	
}
