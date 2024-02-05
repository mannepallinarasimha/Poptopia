package com.kelloggs.promotions.promotionservice.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.CreateUGCRequest;
import com.kelloggs.promotions.lib.model.CreateUGCResponse;
import com.kelloggs.promotions.lib.model.CreateUGCStatusDTO;
import com.kelloggs.promotions.lib.model.GetUGCResponse;
import com.kelloggs.promotions.promotionservice.service.UserGeneratedContentService;

/**
* Add UserGeneratedContentController for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
@RestController
@RequestMapping(path="/api/v1/userContent")
public class UserGeneratedContentController {

    @Autowired
    private UserGeneratedContentService userGeneratedContentService;

    @PostMapping(path="createUGC")
    @ResponseStatus(HttpStatus.CREATED)    
    public ApiResponse<CreateUGCResponse> createUGC(@RequestBody @Valid CreateUGCRequest createUGCRequest){
    	return userGeneratedContentService.createUGC(createUGCRequest);
    } 
	@GetMapping(path="getUGC/{ugcId}")
    public ApiResponse<GetUGCResponse> getUGC(@PathVariable("ugcId") @Valid Integer ugcId){
    	return userGeneratedContentService.getUGC(ugcId);
    }

    @GetMapping(path="getTopUGC/{date}")
    public ApiListResponse<GetUGCResponse> getTopUGC(@PathVariable("date") @Valid String date){
    	return userGeneratedContentService.getTopUGC(date);
    }

  @PostMapping(path ="/setUGCStatus")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateUGCStatusDTO> setUGCStatus(@Valid @RequestBody CreateUGCStatusDTO createUGCStatusDto) {
    	CreateUGCStatusDTO ugcStatus =userGeneratedContentService.setUGCStatus(createUGCStatusDto);
        return new ApiResponse<>("UGC Status " , ugcStatus);
    }
    
    @GetMapping(path ="/getUGCStatus")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateUGCStatusDTO> getUGCStatus(@RequestParam("ugcId") Integer ugcId,@RequestParam("userToken") String userToken) {
    	CreateUGCStatusDTO ugcStatus =userGeneratedContentService.getUGCStatus(ugcId,userToken);
        return new ApiResponse<>("UGC Status " , ugcStatus);
    }
    
    @GetMapping("/profile")
    public ApiResponse<Map<String, Integer>> getProfileId(@RequestParam("userToken") String userToken) {
    	return new ApiResponse<>(String.format("Profile id "
    			+ "for the given token :: %s", userToken), userGeneratedContentService.getProfileId(userToken));
    }}
