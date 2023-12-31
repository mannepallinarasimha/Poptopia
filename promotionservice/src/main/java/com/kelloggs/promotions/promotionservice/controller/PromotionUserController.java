package com.kelloggs.promotions.promotionservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.UsersDTO;
import com.kelloggs.promotions.promotionservice.service.PromotionUserService;

@RestController
@RequestMapping("/api/v1/promotions/user")
public class PromotionUserController {

	private final PromotionUserService userService;
	
	public PromotionUserController(PromotionUserService userService) {
	        this.userService = userService;
	    }
	 
	/**
	 * Get the details of the user
	 * @param profileId the unique profileId of the user
	 * @param defaultScore to check if the user has a defaultScore to add in the attributes
	 * 
	 * @return updated details of the User
	 * @author M1092350
	 *
	 */
	@GetMapping(value="/get")
    public ApiResponse<UsersDTO> getUser(@RequestParam("profileId") Integer profileId , 
    		 @RequestParam(value = "defaultScore", required = false) Integer defaultScore) {
				
		return new ApiResponse<>(String.format("User details with profile id: %d", profileId), userService.getUser(profileId, defaultScore));
    }
	
	/**
	 * UPDATE the details of the user
	 * @param UsersDTO Object - contains profileId, user Lives, highScore of the user
	 * 
	 * @return updated details of the User
	 * @author M1092350
	 * @throws JsonMappingException 
	 *
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ApiResponse<UsersDTO> updateUser( @RequestBody UsersDTO userData) {
    	
    	//Returns new user details after updating the score 	
    	return new ApiResponse<>(String.format("User details with profile id: %d", userData.getProfileId()), userService.updateUser(userData));
    }	 
}
