package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALREADY_VALIDATED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_STATUS;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_ELIGIBLE_FOR_REWARD;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static com.kelloggs.promotions.lib.constants.StatusConstants.PARTICIPATED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.RECEIPT_VERIFICATION_EMAIL_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REUPLOAD;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REUPLOAD_EMAIL_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REWARD_CODE_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REWARD_EMAIL_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.UPLOADED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.SCORE_UPDATED;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

import com.kelloggs.promotions.lib.constants.StatusConstants;
import com.kelloggs.promotions.lib.constants.TokenConstants;
import com.kelloggs.promotions.lib.entity.Item;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.entity.Retailer;
import com.kelloggs.promotions.lib.entity.Status;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.entity.PromotionUser;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.Leader;
import com.kelloggs.promotions.lib.model.UsersDTO;
import com.kelloggs.promotions.lib.repository.ItemRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.ReceiptRepo;
import com.kelloggs.promotions.lib.repository.RetailerRepo;
import com.kelloggs.promotions.lib.repository.TokenRepo;
import com.kelloggs.promotions.lib.repository.PromotionUserRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.lib.utilities.ServiceUtil;
import com.kelloggs.promotions.promotionservice.service.PromotionEntryService;
import com.kelloggs.promotions.promotionservice.service.PromotionService;


@Service
public class PromotionEntryServiceImpl implements PromotionEntryService {

    private final PromotionEntryRepo promotionEntryRepo;

    private final PromotionService promotionService;

    private final StatusService statusService;

    private final RetailerRepo retailerRepo;
    
    private final ReceiptRepo receiptRepo;
    
    private final ItemRepo itemRepo;
    
    private final TokenRepo tokenRepo;
    
    private final PromotionUserRepo promotionUserRepo;
    
	@Autowired
	private EntityManager entityManager;


    public PromotionEntryServiceImpl(PromotionEntryRepo promotionEntryRepo,
                                     PromotionService promotionService,
                                     StatusService statusService, RetailerRepo retailerRepo,
                                     ReceiptRepo receiptRepo,
                                     ItemRepo itemRepo,
                                     TokenRepo tokenRepo,
                                     PromotionUserRepo promotionUserRepo) {
        this.promotionEntryRepo = promotionEntryRepo;
        this.promotionService = promotionService;
        this.statusService = statusService;
        this.retailerRepo = retailerRepo;
        this.receiptRepo = receiptRepo;
        this.itemRepo = itemRepo;
        this.tokenRepo = tokenRepo;
        this.promotionUserRepo = promotionUserRepo;
    }

    @Override
    public ApiListResponse<PromotionEntry> getPromotionEntries(Integer promotionId) {
        List<PromotionEntry> promotionEntries = promotionEntryRepo
                .findByPromotionId(promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        "No promotion entry found for promotion with Id: " + promotionId));

        return new ApiListResponse<>(String.format("Promotion entries for promotion: %d", promotionId),
                promotionEntries,
                promotionEntries.size());
    }

    @Override
    public ApiResponse<PromotionEntry> getPromotionEntry(Integer entryId, Integer promotionId) {
        PromotionEntry promotionEntry = promotionEntryRepo
                .findByIdAndPromotionId(entryId, promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No promotion entry with Id: %d found for promotion with Id: %d", entryId, promotionId)));
        return new ApiResponse<>(String.format("Promotion Entry with Id : %d", entryId), promotionEntry);
    }

    @Override
    public PromotionEntry savePromotionEntryForPromoCode(Integer promotionId, PromotionEntry promotionEntry, Boolean validateForExistingEntry) {
        
        PromotionEntry updatedPromotionEntry = getUpdatedPromotionEntry(promotionId, promotionEntry, validateForExistingEntry);
     
        updatedPromotionEntry.setStatus(promotionEntry.getStatus() != null 
        		? promotionEntry.getStatus() : statusService.getStatus(VERIFIED.getStatus()));
        
        if(updatedPromotionEntry.getAttr2Code()==null && updatedPromotionEntry.getAttr2Value()==null) {
	        updatedPromotionEntry.setAttr2Code(promotionEntry.getAttr2Code());
	        updatedPromotionEntry.setAttr2Value(promotionEntry.getAttr2Value());
        }
        updatedPromotionEntry.setDonatePrize(promotionEntry.getDonatePrize());
        return promotionEntryRepo.save(updatedPromotionEntry);
    }

    @Override
    public PromotionEntry savePromotionEntryForReceipt(Integer promotionId, PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry) {
    	
        PromotionEntry updatedPromotionEntry = getUpdatedPromotionEntry(promotionId, promotionEntry, uploadReceiptForExistingEntry);
        
        updatedPromotionEntry.setStatus(statusService.getStatus(UPLOADED.getStatus()));
        if(updatedPromotionEntry.getAttr1Code()==null && updatedPromotionEntry.getAttr1Value()==null) {
        	updatedPromotionEntry.setAttr1Code(promotionEntry.getAttr1Code());
            updatedPromotionEntry.setAttr1Value(promotionEntry.getAttr1Value());
        }
        updatedPromotionEntry.setDonatePrize(promotionEntry.getDonatePrize());
        updatedPromotionEntry.setAnswer(promotionEntry.getAnswer());
        return promotionEntryRepo.save(updatedPromotionEntry);
    }

	private PromotionEntry getUpdatedPromotionEntry(Integer promotionId, PromotionEntry promotionEntry, Boolean isActionForExistingEntry) {
		
		PromotionEntry updatedPromotionEntry = null;
		
		if (Boolean.FALSE.equals(isActionForExistingEntry) && Objects.isNull(promotionEntry.getId())) {
			updatedPromotionEntry = updatePromotionEntry(promotionId, promotionEntry);
		} else if (Boolean.TRUE.equals(isActionForExistingEntry) && Objects.nonNull(promotionEntry.getId())) {
        	
            updatedPromotionEntry =  promotionEntryRepo.findByIdAndPromotionId(promotionEntry.getId(), promotionId)
            		.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(),
                            String.format("No promotion entry with Id: %d found for promotion with Id: %d", promotionEntry.getId(), promotionId)));
		} else {
			throw new ApiException(HttpStatus.EXPECTATION_FAILED, NOT_ELIGIBLE_FOR_REWARD.getCode(), String.format("Unsatisfied entry "
					+ "condition:: [isActionForExistingEntry:%b, entryId: %d]", isActionForExistingEntry, promotionEntry.getId()));
		}
		return updatedPromotionEntry;
	}

    @Override
    public ApiResponse<PromotionEntry> updatePromotionEntryStatus(Integer entryId, Integer promotionId, String status) {
        PromotionEntry promotionEntry = promotionEntryRepo
                .findByIdAndPromotionId(entryId, promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        NOT_FOUND.getCode(),
                        String.format("No promotion entry with Id: %d found for promotion with Id: %d", entryId, promotionId)));

        boolean valid;

        if (REUPLOAD_EMAIL_SENT.getStatus().equals(status)) {
            valid = promotionEntry.getStatus().getType().equals(REUPLOAD.getStatus());
        } else if (REWARD_EMAIL_SENT.getStatus().equals(status)) {
            valid = promotionEntry.getStatus().getType().equals(REWARD_CODE_SENT.getStatus());
        } else if (RECEIPT_VERIFICATION_EMAIL_SENT.getStatus().equals(status)) {
            valid = promotionEntry.getStatus().getType().equals(VERIFIED.getStatus());
        } else {
            throw new ApiException(HttpStatus.NOT_FOUND,
                    NOT_FOUND.getCode(),
                    String.format("Invalid Status Sent : %s ", status));
        }

        if (valid) {
            Status entryStatus = statusService.getStatus(status);
            promotionEntry.setStatus(entryStatus);
            PromotionEntry updatedEntry = promotionEntryRepo.save(promotionEntry);
            return new ApiResponse<>(String.format("Entry with Id : %d updated", entryId), updatedEntry);
        }

        throw new ApiException(HttpStatus.BAD_REQUEST,
                400,
                String.format("Not an eligible entry : %d for status change", entryId));
    }

    @Override
    public ApiResponse<PromotionEntry> addPromotionEntry(Integer promotionId, PromotionEntry promotionEntry, 
    		Boolean generateToken, Integer expireTime, Boolean utilizeUnusedToken, Boolean utilizeUnusedEntry, Boolean isVerified) {
            	
    	if (generateToken && utilizeUnusedToken) {
    		
    		// Get the unused(Generated/Expired) tokens created by particular user and reuse them
    		tokenRepo.findAllByProfileIdAndStatusIn(promotionEntry.getProfileId(), Arrays.asList(
					TokenConstants.GENERATED.getStatus(), TokenConstants.EXPIRED.getStatus())).ifPresent(unUsedTokens -> {
						updateUnusedEntry(unUsedTokens.get(0).getEntryId(), unUsedTokens.get(0), promotionEntry);
					});
		} else if (Boolean.TRUE.equals(utilizeUnusedEntry)) {
			
			// Get the unused entries(Participated) created by particular user and reuse them
			promotionEntryRepo.findByProfileIdAndAttr1CodeAndAttr1Value(
					promotionEntry.getProfileId(), null, null).ifPresent(unUsedEntries -> {
						updateUnusedEntry(unUsedEntries.get(0).getId(), Boolean.TRUE.equals(generateToken) 
								? tokenRepo.findAllByEntryIdIn(Arrays.asList(unUsedEntries.get(0).getId()))
								.orElse(Collections.emptyList()).stream().findFirst().orElse(null) : null, promotionEntry);
						receiptRepo.findByPromotionEntryId(unUsedEntries.get(0).getId()).ifPresent(receiptRepo::deleteInBatch);
					});
		}
    	
        promotionEntry.setStatus(statusService.getStatus(Boolean.TRUE.equals(isVerified)? VERIFIED.getStatus() : PARTICIPATED.getStatus()));
        PromotionEntry savedEntry = promotionEntryRepo.save(updatePromotionEntry(promotionId, promotionEntry));
                  
    	if (Boolean.TRUE.equals(generateToken)) {
    		
    		// Generate the token and bind it the promotion entry		
    		Token generatedToken = ServiceUtil.generateToken(savedEntry);
    		generatedToken.setId(Objects.nonNull(promotionEntry.getToken()) ? promotionEntry.getToken().getId() : null);
    		generatedToken.setExpirationTime(expireTime != null && expireTime > 0 ? LocalDateTime.now().plusMinutes(expireTime) : null);
    		savedEntry.setToken(tokenRepo.save(generatedToken));
		}  
       
        return new ApiResponse<>("Promotion Entry added", savedEntry);
    }

	private void updateUnusedEntry(Integer unUsedEntryId, Token unUsedToken, PromotionEntry promotionEntry) {
		promotionEntry.setId(unUsedEntryId);
		promotionEntry.setCreatedDate(new Date()); 
		promotionEntry.setModifiedDate(new Date());
		promotionEntry.setCreatedDateTime(LocalDateTime.now(ZoneId.of("GMT")));
		promotionEntry.setToken(unUsedToken);
	}

    @Override
    public ApiListResponse<PromotionEntry> getPromotionEntriesOfUser(Integer promotionId, Integer profileId, LocalDate date) {

        List<PromotionEntry> promotionEntries = null;

        if (date == null) {
            promotionEntries = promotionEntryRepo
                    .findByPromotionIdAndProfileId(promotionId, profileId)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                            NOT_FOUND.getCode(),
                            String.format("No promotion entries found for the user: %d under the promotion with id: %d", profileId, promotionId)));
        } else {
            promotionEntries = promotionEntryRepo
                    .findByProfileIdAndCreatedLocalDateTime(profileId, date.toString().concat("%"))
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                            NOT_FOUND.getCode(),
                            String.format("No promotion entries found for the user: %d under the promotion with id: %d for date: %s", profileId, promotionId, date)));

            promotionEntries = promotionEntries
                    .stream()
                    .filter(promotionEntry -> promotionEntry.getPromotion().getId().intValue() == promotionId)
                   .collect(Collectors.toList());
        }

        return new ApiListResponse<>(String.format("Promotion entries for user %d", profileId),
                promotionEntries,
                promotionEntries.size());
    }

    private PromotionEntry updatePromotionEntry(Integer promotionId, PromotionEntry promotionEntry) {

        Promotion promotion = promotionService.getPromotionById(promotionId);
        
        promotionEntry.setCountry((promotionEntry.getCountry() == null || promotionEntry.getCountry().isEmpty()) ? 
        		promotion.getRegion().getCountry() : promotionEntry.getCountry());
		
        promotionEntry.setPromotion(promotion);
        Retailer retailer = retailerRepo
                .findByName(promotionEntry.getRetailerName())
                .orElse(null);
        promotionEntry.setRetailer(retailer);
        

        // Set local date time based on the zone
        String timeZone = promotion.getLocalTimeZone();
        if (timeZone != null) {
            promotionEntry.setLocalTimeZone(timeZone);
            promotionEntry.setCreatedLocalDateTime(LocalDateTime.now(ZoneId.of(timeZone)));
        }
        return promotionEntry;
    }
    
    @Override
    public ApiListResponse<PromotionEntry> getPromotionEntriesOfUserForProfileId(Integer profileId, Set<Integer> promotionIds, 
    		LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean hasAttributes, Boolean isTokenValidated, Boolean isEntryUsed) {
    	
        Optional<List<PromotionEntry>> promotionEntryListOptional = 
        		promotionEntryRepo.findByProfileIdAndPromotionIdInAndCreatedLocalDateTimeBetween(profileId, promotionIds, startDateTime, endDateTime);
        
        if (Boolean.TRUE.equals(hasAttributes)) {
			
        	promotionEntryListOptional = promotionEntryListOptional.map(promotionEntryList -> promotionEntryList
        			.stream().filter(entry -> entry.getAttr1Code() != null && entry.getAttr1Value() != null 
        			&& entry.getAttr2Code() != null && entry.getAttr2Value() != null).collect(Collectors.toList()));
        }

        if (Boolean.TRUE.equals(isTokenValidated)) {
        	
        	List<Integer> validatedEntryIds = tokenRepo.findAllByEntryIdInAndStatus(promotionEntryListOptional.orElse(Collections.emptyList())
        			.stream().map(PromotionEntry::getId).collect(Collectors.toSet()), TokenConstants.VALIDATED.getStatus())
        			.orElse(Collections.emptyList()).stream().map(Token::getEntryId).collect(Collectors.toList());
        	
        	promotionEntryListOptional = promotionEntryListOptional.map(promotionEntryList -> promotionEntryList
        			.stream().filter(entry -> validatedEntryIds.contains(entry.getId())).collect(Collectors.toList()));
		}
        
        if (Boolean.TRUE.equals(isEntryUsed)) {
			
        	promotionEntryListOptional = promotionEntryListOptional.map(promotionEntryList -> promotionEntryList.stream()
        			.filter(entry -> Objects.nonNull(entry.getAttr1Code()) && Objects.nonNull(entry.getAttr1Value())).collect(Collectors.toList()));
        }
        
        List<PromotionEntry> promotionEntries =  promotionEntryListOptional.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(), 
				String.format("No promotion entries found for the user: %d under the datetime between %s and %s", profileId, startDateTime, endDateTime)));
   
        return new ApiListResponse<>(String.format("Promotion entries for user %d", profileId), promotionEntries, promotionEntries.size());
    }
    
    @Override
    public Integer getTotalProductQuantityForProfileId(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    	    	
    	List<Integer> entryIds = promotionEntryRepo.findByProfileIdAndPromotionIdInAndCreatedLocalDateTimeBetween(profileId, promotionIds, startDateTime, endDateTime)
    			.orElse(Collections.emptyList()).stream().map(PromotionEntry::getId).collect(Collectors.toList());
    	
    	List<Integer> receiptIds = receiptRepo.findByPromotionEntryIdIn(entryIds)
    			.orElse(Collections.emptyList()).stream().map(Receipt::getId).collect(Collectors.toList());
    		
    	return itemRepo.findByReceiptIdIn(receiptIds).orElse(Collections.emptyList())
    			.stream().collect(Collectors.summingInt(Item::getQty));
    }
    
    @Override
	public List<Leader> getLeaderBoard(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, 
			LocalDateTime endDateTime, Integer scorePerUnit, Boolean countParticipant, Byte sortBy, Integer limit) {
    	
    	List<Leader> leaders = null;
    	final Integer PARTICIPATION_STATUS = 100;
    	
    	// Get the leader board score details from database  	
		leaders = promotionEntryRepo.findLeaderBoardRankAndScore(promotionIds, startDateTime, endDateTime, scorePerUnit, 
				countParticipant, PARTICIPATION_STATUS, sortBy == 1 ? Direction.ASC.name() : (sortBy == -1) ? Direction.DESC.name() : new String());        	
		  	 	
    	if (profileId != null) {
    		
    		// Retrieve leader details for the given profile Id
			leaders = leaders.stream().filter(leader -> profileId.equals(leader.getProfileId())).collect(Collectors.toList());
			
			if (leaders.size() == 0) {
				
				// Prepare default/initial rank and score, if the leader record not found
				leaders.add(new Leader() {
					@Override
					public Integer getRank() {return 0;}
					@Override
					public Integer getScore() {return 0;}
					@Override
					public Integer getProfileId() {return profileId;}
				});
			}
 		} 
    	
    	if (leaders.size() > 0) {
    		
			// Retrieve the specified number of leader details
    		if (profileId == null && limit != null && limit > 0) {
    			leaders = leaders.stream().limit(limit).collect(Collectors.toList());
    		} 
		} else {
			
			// Throw the exception if no leaders found
			throw new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(), 
					String.format("Leaderboard details not found for the given promotionIds %s and time period between %s and %s !",
							promotionIds, startDateTime, endDateTime), new ApiLogger(this.getClass(), LogLevel.ERROR));
		}
    	
		return 	leaders;
	}
    
    public ApiResponse<PromotionEntry> updateScore(Integer entryId, Map<String, Object> scoreMap) {
    	
    	PromotionEntry promotionEntry = null;
    	final String GAME_SCORE  = "gameScore";
    	final String GAME_STATUS  = "isGameCompleted";
    	
    	if(scoreMap.containsKey(GAME_STATUS) && scoreMap.containsKey(GAME_SCORE)) {
    		
    		//Get entry details from the given entryId
    		promotionEntry = promotionEntryRepo.findById(entryId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, 
    				NOT_FOUND.getCode(), String.format("Promotion entry with Id: %d was not found.", entryId)));
    		
    		// Throw exception if the game score has been already updated
    		if (Objects.nonNull(promotionEntry.getAttr1Code()) && Objects.nonNull(promotionEntry.getAttr1Value())
    				&& Objects.nonNull(promotionEntry.getAttr2Code()) && Objects.nonNull(promotionEntry.getAttr2Value())) {
    			
    			throw new ApiException(HttpStatus.ALREADY_REPORTED, ALREADY_VALIDATED.getCode(), 
    					String.format("Game score has been already updated for the given entry Id: %d.", entryId));
    		}
    		
    		//Updating score in the given entry along with the game completion status
    		promotionEntry.setAttr1Code(GAME_STATUS);
    		promotionEntry.setAttr1Value(Boolean.valueOf(scoreMap.get(GAME_STATUS).toString()).toString());
    		promotionEntry.setAttr2Code(GAME_SCORE);
    		promotionEntry.setAttr2Value(NumberUtils.parseNumber(scoreMap.get(GAME_SCORE).toString(), Number.class).toString());
    		
		    if(promotionEntry.getStatus().getType().equals(StatusConstants.PARTICIPATED.name())) {
		    	
		       //Updating Status in the entry only when the entry is in 'PARTICIPATED' state.
		       promotionEntry.setStatus(statusService.getStatus(StatusConstants.SCORE_UPDATED.getStatus()));
		    }
		    
		    //Saving the entry with the updated details
		    promotionEntry = promotionEntryRepo.save(promotionEntry);		        
		 } else {
    		//Throw exception if no attributes were provided to update the entry
    		throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_STATUS.getCode(), "No attributes were found to be updated" );
    	 }
  
        return new ApiResponse<>(String.format("Updated Promotion Entry with Id : %d", entryId), promotionEntry);
    }
    
    @Override
	public List<Leader> getLeaderBoardWithScore(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, 
			LocalDateTime endDateTime, Byte sortBy, Integer limit) {
    	
    	List<Leader> leaders = null;
    	
    	//Get the leader board score details from database  	
	    leaders = promotionEntryRepo.findLeaderBoardWithScore(promotionIds, startDateTime, endDateTime, 
				  sortBy == 1 ? Direction.ASC.name() : (sortBy == -1) ? Direction.DESC.name() : new String());        	
		  	 	
    	if (profileId != null) {
    		
    		// Retrieve leader details for the given profile Id
			leaders = leaders.stream().filter(leader -> profileId.equals(leader.getProfileId())).collect(Collectors.toList());
			
			if (leaders.size() == 0) {
				
				// Prepare default/initial rank and score, if the leader record not found
				leaders.add(new Leader() {
					@Override
					public Integer getRank() {return 0;}
					@Override
					public Integer getScore() {return 0;}
					@Override
					public Integer getProfileId() {return profileId;}
				});
			}
 		} 
    	
    	if (leaders.size() > 0) {
    		
			// Retrieve the specified number of leader details
    		if (profileId == null && limit != null && limit > 0) {
    			leaders = leaders.stream().limit(limit).collect(Collectors.toList());
    		} 
		} else {
			
			// Throw the exception if no leaders found
			throw new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND.getCode(), 
					String.format("Leaderboard details not found for the given promotionIds %s and time period between %s and %s !",
							promotionIds, startDateTime, endDateTime), new ApiLogger(this.getClass(), LogLevel.ERROR));
		}
    	
		return 	leaders;
	}
    
    @Override @Transactional
	public PromotionEntry addUserGameEntry(Integer promotionId, UsersDTO userData) {
		
    	final String GAME_SCORE  = "gameScore";
    	final String REMAINING_LIVES  = "remainingLives";
    	PromotionEntry promotionEntry = null;
   	
    	if(Objects.nonNull(userData.getAttr2Value()) && Objects.nonNull(userData.getProfileId())) {
    		
    		PromotionUser promotionUser = promotionUserRepo.findByProfileId(userData.getProfileId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                NOT_FOUND.getCode(), String.format("No user found with profileId: %d", userData.getProfileId())));
		
    		if(Objects.isNull(promotionUser.getAttribute1()) || promotionUser.getAttribute1() <= 0) {
    			throw new ApiException(HttpStatus.PRECONDITION_FAILED, INVALID_STATUS.getCode(), "No lives left for the user" );
    		}
    	    //save gamescore in promotionEntry , save highest score in users and reduce lives   	
			promotionEntry = new PromotionEntry();
    		promotionEntry.setProfileId(userData.getProfileId());
        	promotionEntry.setAttr1Code(GAME_SCORE);
    		promotionEntry.setAttr1Value(userData.getAttr2Value().toString());
            promotionEntry.setStatus(statusService.getStatus(SCORE_UPDATED.getStatus())); 
            promotionEntryRepo.save(updatePromotionEntry(promotionId, promotionEntry));

            //set the highscore(attribute2) value if the current score(attr1) is greater           
            promotionUser.setAttribute1(promotionUser.getAttribute1()-1);    
            promotionUser.setAttribute2(Objects.nonNull((promotionUser.getAttribute2())) ? (Integer.valueOf(userData.getAttr2Value()) 
            		> Integer.valueOf(promotionUser.getAttribute2()) ? userData.getAttr2Value() : promotionUser.getAttribute2())  
            		: userData.getAttr2Value());
            
            promotionUserRepo.save(promotionUser);
            entityManager.detach(promotionEntry);
            promotionEntry.setAttr2Code(REMAINING_LIVES);
            promotionEntry.setAttr2Value(promotionUser.getAttribute1().toString());
        
    	} else {
    		throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_STATUS.getCode(), "No attributes were found to be updated" );
    	}  
    	
		return promotionEntry;
	}
}
