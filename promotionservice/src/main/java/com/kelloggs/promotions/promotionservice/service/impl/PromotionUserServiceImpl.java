package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_STATUS;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kelloggs.promotions.lib.entity.PromotionUser;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.UsersDTO;
import com.kelloggs.promotions.lib.repository.PromotionUserRepo;
import com.kelloggs.promotions.promotionservice.service.PromotionUserService;

@Service
public class PromotionUserServiceImpl implements PromotionUserService {
	
    private final PromotionUserRepo userRepo;

    public PromotionUserServiceImpl(PromotionUserRepo userRepo) {
    	this.userRepo = userRepo;
    }

	@Override
	public UsersDTO getUser(Integer profileId, Integer defaultScore) {

		PromotionUser user = null;
		UsersDTO validUser = null;
		
		if(Objects.nonNull(defaultScore) ) {
			
			//find the user using profileId
			user = userRepo.findByProfileId(profileId).orElse(null);
			
			if(Objects.isNull(user)) {
				
				//create and return a new User with default attributes if there is no user found
				user = new PromotionUser();
				user.setProfileId(profileId);
				user.setAttribute1(defaultScore);
				user.setAttribute2("0");
				userRepo.save(user);	
			}
			
		}
		
		else {
			 user = userRepo.findByProfileId(profileId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
	                NOT_FOUND.getCode(), String.format("No user found with profileId: %d", profileId)));
		}
		
		//map the User to UserDTO		
		validUser = mapToDTO(user);
					
		return validUser;
	}

	@Override
	public UsersDTO updateUser(UsersDTO userData) {
		 
		UsersDTO validUser = null;
		PromotionUser user = userRepo.findByProfileId(userData.getProfileId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                NOT_FOUND.getCode(), String.format("No user found with profileId: %d", userData.getProfileId())));
				
			//if attribute has a valid value , add it to the current value and return updated user
			if((Optional.ofNullable(userData.getAttr1Value())).isPresent())
			{
				user.setAttribute1(userData.getAttr1Value() + user.getAttribute1());
				userRepo.save(user);
			}	
			
			else {
	    		throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_STATUS.getCode(), "No attributes were found to be updated" );
			}
		//map the User to UserDTO		
		validUser = mapToDTO(user);
		
		return validUser;
	}
	
	public UsersDTO mapToDTO(PromotionUser user)
	{
		final Integer attr1_value = user.getAttribute1()!=null ?user.getAttribute1():0;
		final String attr2_value = user.getAttribute2()!=null?user.getAttribute2():null;
		
		return new UsersDTO(user.getProfileId() , attr1_value, attr2_value);
			
	}

}
