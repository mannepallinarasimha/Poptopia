package com.kelloggs.promotions.promotionservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.Leader;
import com.kelloggs.promotions.lib.model.UsersDTO;

public interface PromotionEntryService {

     ApiListResponse<PromotionEntry> getPromotionEntries(Integer promotionId);

     ApiResponse<PromotionEntry> getPromotionEntry(Integer entryId, Integer promotionId);

     PromotionEntry savePromotionEntryForPromoCode(Integer promotionId, PromotionEntry promotionEntry, Boolean validateForExistingEntry);

     PromotionEntry savePromotionEntryForReceipt(Integer promotionId, PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry);

     ApiResponse<PromotionEntry> updatePromotionEntryStatus(Integer entryId, Integer promotionId, String status);

    ApiResponse<PromotionEntry> addPromotionEntry(Integer promotionId, PromotionEntry promotionEntry, Boolean generateToken, Integer expireTime, Boolean utilizeUnusedToken, Boolean utilizeUnusedEntry, Boolean isVerified);

    ApiListResponse<PromotionEntry> getPromotionEntriesOfUser(Integer promotionId, Integer profileId, LocalDate date);
    
    ApiListResponse<PromotionEntry> getPromotionEntriesOfUserForProfileId(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean hasAttributes, Boolean isTokenValidated, Boolean isEntryUsed);

	Integer getTotalProductQuantityForProfileId(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	/**
     * Get users leader board (total product quantity) for the promotion
     * 
     * @param profileId	User unique profile Id
     * @param promotionIds	List of promotion Id
     * @param startDateTime	Starting time period to be used for quantity/score calculation
     * @param endDateTime Ending time period to be used for quantity/score calculation
     * @param scorePerUnit Score assigned to user per entry for participation.
     * @param countParticipant If true , then users will get 'scorePerUnit' added to the score for participation.
     * @param sortBy Sorting order to be applied on users score or total product quantity,
     * 		  if sortBy = 1, follows ascending order else if sortBy = -1, follows descending order otherwise follows insertion order
     * @param limit Determines the number of user/leader records want to retrieve 
     * @return	Return the leader board details for the given criteria
     * 
     * @author UDIT NAYAK (M1064560)/ ANSHAY SEHRAWAT (M1092350)
     * @since 19th August 2022
     */

	    List<Leader> getLeaderBoard(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer scorePerUnit, Boolean countParticipant, Byte sortBy, Integer limit);
	
	/**
	 * Update Attribute(score) for the particular entryId
	 * 
	 * @param entryId unique entry Id
	 * @param scoreMap Map containing the score which needs to be updated for the particular entry Id
	 * @return Return the updated entry details with the updated attribute("gameScore")
	 * @author ANSHAY SEHRAWAT - M1092350
	 * @since 11th January 2023
	 */
	ApiResponse<PromotionEntry> updateScore(Integer entryId, Map<String, Object> scoreMap);
	
	/**
     * Get users leader board (score) for the promotion
     * 
     * @param profileId	User unique profile Id
     * @param promotionIds	List of promotion Id
     * @param startDateTime	Starting time period to be used for quantity/score calculation
     * @param endDateTime Ending time period to be used for quantity/score calculation
     * @param sortBy Sorting order to be applied on users score or total product quantity,
     * 		  if sortBy = 1, follows ascending order else if sortBy = -1, follows descending order otherwise follows insertion order
     * @param limit Determines the number of user/leader records want to retrieve 
     * @return	Return the leader board details for the given criteria
     * 
     * @author ANSHAY SEHRAWAT (M1092350)
     * @since 28th August 2023
     */
    List<Leader> getLeaderBoardWithScore(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime, Byte sortBy, Integer limit);
    
    /**
     * Add Promotion Entry for the particular user with score
     * 
     * @param promotionId Unique promotion Id for the promotion
     * @param UsersDto object - contains profile Id and attribute value(lives) 
     * @return Return the updated promotion entry details
     * @author ANSHAY SEHRAWAT (M1092350)
     * @since 30th August 2023
     */  
    PromotionEntry addUserGameEntry(Integer promotionId, UsersDTO usersDTO);

}
