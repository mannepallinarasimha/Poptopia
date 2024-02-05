package com.kelloggs.promotions.promotionservice.service;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.CreateUGCRequest;
import com.kelloggs.promotions.lib.model.CreateUGCResponse;
import com.kelloggs.promotions.lib.model.CreateUGCStatusDTO;
import com.kelloggs.promotions.lib.model.GetUGCResponse;
/**
* Add UserGeneratedContentService for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
public interface UserGeneratedContentService {

    ApiResponse<CreateUGCResponse> createUGC(@Valid CreateUGCRequest createUGCRequest);
    
	ApiResponse<GetUGCResponse> getUGC(@Valid Integer ugcId);

    ApiListResponse<GetUGCResponse> getTopUGC(@Valid String date);

	CreateUGCStatusDTO setUGCStatus(CreateUGCStatusDTO createUGCStatusDto);
    
    CreateUGCStatusDTO getUGCStatus(Integer ugcId,String userToken);
    
    public Map<String, Integer>  getProfileId(String userToken); }
