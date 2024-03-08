package com.kelloggs.promotions.promotionservice.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.Leader;
import com.kelloggs.promotions.lib.model.UsersDTO;
import com.kelloggs.promotions.promotionservice.service.PromotionEntryService;

@RestController
@RequestMapping(value = {"/api/v1/promotions", "/api/v1/promotions/{promotionId}"})
public class PromotionEntryController {

    private final PromotionEntryService promotionEntryService;

    public PromotionEntryController(PromotionEntryService promotionEntryService) {
        this.promotionEntryService = promotionEntryService;
    }

    @GetMapping(path ="/entries")
    public ApiListResponse<PromotionEntry> getPromotionEntries(@PathVariable Integer promotionId) {
        return promotionEntryService.getPromotionEntries(promotionId);
    }

    @GetMapping(path ="/entry")
    public ApiListResponse<PromotionEntry> getPromotionEntriesOfUser(@PathVariable Integer promotionId,
                                                                     @RequestParam(value = "profileId") Integer profileId,
                                                                     @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return promotionEntryService.getPromotionEntriesOfUser(promotionId, profileId, date);
    }

    @GetMapping(path ="/entries/{entryId}")
    public ApiResponse<PromotionEntry> getPromotionEntry(@PathVariable(value = "promotionId") Integer promotionId,
                                                         @PathVariable(value = "entryId") Integer entryId) {
        return promotionEntryService.getPromotionEntry(entryId, promotionId);
    }

    @PutMapping(path ="/entries/{entryId}")
    public ApiResponse<PromotionEntry> updateEntry(@PathVariable(value = "promotionId") Integer promotionId,
                                                   @PathVariable(value = "entryId") Integer entryId,
                                                   @RequestParam(value = "status") String status) {
        return promotionEntryService.updatePromotionEntryStatus(entryId, promotionId, status);
    }

    @PostMapping(path ="/entries")
    public ApiResponse<PromotionEntry> addPromotionEntry(@PathVariable(value = "promotionId") Integer promotionId,@Valid @RequestBody PromotionEntry promotionEntry,
			 											 @RequestParam(value = "generateToken", required = false, defaultValue = "false") Boolean generateToken,
			 											 @RequestParam(value = "expireTime", required = false) Integer expireTime,
			 											 @RequestParam(value = "utilizeUnusedToken", required = false, defaultValue = "false") Boolean utilizeUnusedToken,
			 											 @RequestParam(value = "utilizeUnusedEntry", required = false, defaultValue = "false") Boolean utilizeUnusedEntry,
			 											 @RequestParam(value = "isVerified", required = false, defaultValue = "true") Boolean isVerified,
			 											@RequestParam(value = "hasUserScore", required = false, defaultValue = "true") Boolean hasUserScore) {
        return promotionEntryService.addPromotionEntry(promotionId, promotionEntry, generateToken, expireTime, utilizeUnusedToken, utilizeUnusedEntry, isVerified);
    }
    
    @GetMapping(path ="/entryForProfileId")
    public ApiListResponse<PromotionEntry> getPromotionEntriesOfUserForProfileId(@RequestParam(value = "profileId") Integer profileId,
    																 @RequestParam(value =  "promotionIds", required = false) Set<Integer> promotionIds,
                                                                     @RequestParam(value = "startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
                                                                     @RequestParam(value = "endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
                                                                     @RequestParam(value = "hasAttributes", required = false, defaultValue = "false") Boolean hasAttributes,
                                                                     @RequestParam(value = "isTokenValidated", required = false, defaultValue = "false") Boolean isTokenValidated,
                                                                     @RequestParam(value = "isEntryUsed", required = false, defaultValue = "false") Boolean isEntryUsed) {
        return promotionEntryService.getPromotionEntriesOfUserForProfileId(profileId, promotionIds, startDateTime, endDateTime, hasAttributes, isTokenValidated, isEntryUsed);
    }

    @GetMapping("/total/product/quantity")
    public ApiListResponse<?> getTotalProductQuantityForProfileId(@RequestParam("profileId") Integer profileId,
			@RequestParam(value =  "promotionIds", required = false) Set<Integer> promotionIds,
    		@RequestParam(value = "startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(value = "endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime) {
		
    	return new ApiListResponse<>(String.format("Total product quanity accuired by profile id: %d for promotion Id: %s", profileId, promotionIds), 
				null, promotionEntryService.getTotalProductQuantityForProfileId(profileId, promotionIds, startDateTime, endDateTime));
    }  
    
    /**
     * Get users leader board (total receipt product quantity) for the promotion
     * 
     * @param profileId	(Optional) User unique profile Id
     * @param promotionIds	List of promotion Id
     * @param startDateTime	Starting time period to be used for quantity/score calculation
     * @param endDateTime Ending time period to be used for quantity/score calculation
     * @param scorePerUnit Score assigned to user per entry for participation.
     * @param countParticipant If true , then users will get 'scorePerUnit' added to the score for participation.
     * @param sortBy (Optional) Sorting order to be applied on users score or total product quantity. If sortBy = 1, follows ascending order 
     * 		  else if sortBy = -1, follows descending order otherwise follows insertion order and default value is -1
     * @param limit	(Optional) Determines the number of user/leader records want to retrieve 
     * @return	Return the particular user leader board(total product quantity) details if profileId is provided, 
     * 			otherwise return list of specified number of user/leader records
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 19th August 2022
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderBoard(
    		@RequestParam(value = "profileId", required = false) Integer profileId, @RequestParam(value = "promotionIds") Set<Integer> promotionIds,
    		@RequestParam(value = "startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(value = "endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(value = "scorePerUnit", defaultValue = "1", required = false) Integer scorePerUnit,
            @RequestParam(value = "countParticipant", defaultValue = "false", required = false) Boolean countParticipant, 
            @RequestParam(value = "sortBy", defaultValue = "-1") Byte sortBy, @RequestParam(value = "limit", required = false) Integer limit){
    	
    	// Get the user leader board for the promotion
    	List<Leader> leaders = promotionEntryService.getLeaderBoard(profileId, promotionIds, startDateTime, endDateTime, scorePerUnit, countParticipant, sortBy, limit);
    	
    	// Return the leader board details
    	return new ResponseEntity<>((profileId != null)
				?	new ApiResponse<>(String.format("User leaderboard details for the given profile id: %d", profileId), leaders.get(0))
				:	new ApiListResponse<>("Leaderboard details for given promotion!", leaders, leaders.size()), HttpStatus.OK);
    }
    
    /**
     * Update Attribute(score) for the particular entryId
     * 
     * @param entryId User's unique entry Id
     * @param scoreMap	Map containing the attribute name("gameScore") and attribute value(gameScore value)
     * @return Return the updated entry details with the updated attribute("gameScore")
     * @author ANSHAY SEHRAWAT (M1092350)
     * @since 13th January 2023
     */
    @RequestMapping(value = "/entry/{entryId}/update/score", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ApiResponse<PromotionEntry> updateEntryWithScore(
    		@PathVariable(value = "entryId") Integer entryId, @RequestBody Map<String, Object> scoreMap){
        
    	//Returns new entry details after updating the score
    	return promotionEntryService.updateScore(entryId, scoreMap);
    }
    
    @GetMapping("/score/leaderboard")
    public ResponseEntity<?> getLeaderBoardWithScore(
    		@RequestParam(value = "profileId", required = false) Integer profileId, @RequestParam(value = "promotionIds") Set<Integer> promotionIds,
    		@RequestParam(value = "startDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(value = "endDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(value = "sortBy", defaultValue = "-1") Byte sortBy, @RequestParam(value = "limit", required = false) Integer limit){
    	
    	// Get the user leader board for the promotion
    	// Get the user leader board for the promotion
    	List<Leader> leaders = promotionEntryService.getLeaderBoardWithScore(profileId, promotionIds, startDateTime, endDateTime, sortBy, limit);
    	// Return the leader board details
    	return new ResponseEntity<>((profileId != null)
				?	new ApiResponse<>(String.format("User leaderboard details for the given profile id: %d", profileId), leaders.get(0))
				:	new ApiListResponse<>("Leaderboard details for given promotion!", leaders, leaders.size()), HttpStatus.OK);
    }
    
    /**
     * Add Promotion Entry for the particular user with score
     * 
     * @param promotionId Unique promotion Id for the promotion
     * @param UsersDto object - contains profile Id and attribute value(lives) 
     * @return Return the updated promotion entry details
     * @author ANSHAY SEHRAWAT (M1092350)
     * @since 30th August 2023
     */   
    @PostMapping(path ="/user/game/entry")
    public ApiResponse<PromotionEntry> addUserPromotionEntry(@PathVariable(value = "promotionId") Integer promotionId,@RequestBody UsersDTO userData) {
    	
    	PromotionEntry promotionEntry = promotionEntryService.addUserGameEntry(promotionId, userData);
    	
    	return new ApiResponse<>(String.format("User details with profile id: %d",promotionEntry.getProfileId()), promotionEntry);
    }
    
}
