package com.kelloggs.promotions.promotionservice.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.kelloggs.promotions.lib.model.UsersDTO;

public interface PromotionUserService {

	/**
	 * Get the details of the user
	 * @param profileId the unique profileId of the user
	 * @param defaultScore to check if the user has a defaultScore to add in the attributes
	 * 
	 * @return updated details of the User
	 * @author M1092350
	 *
	 */
	public UsersDTO getUser(Integer profileId, Integer defaultScore);
	
	/**
	 * UPDATE the details of the user
	 * @param UsersDTO Object - contains profileId, user Lives, highScore of the user
	 * 
	 * @return updated details of the User
	 * @author M1092350
	 * @throws JsonMappingException 
	 *
	 */
	public UsersDTO updateUser(UsersDTO userData);

}
