package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.CodeConstants.AVAILABLE;
import static com.kelloggs.promotions.lib.constants.CodeConstants.DUPLICATE;
import static com.kelloggs.promotions.lib.constants.CodeConstants.INVALID;
import static com.kelloggs.promotions.lib.constants.CodeConstants.USED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALREADY_VALIDATED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.DUPLICATE_CODE;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_CODE;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.entity.PromotionCodeEntry;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromoCodeMetaObject;
import com.kelloggs.promotions.lib.model.RedeemRewardDTO;
import com.kelloggs.promotions.lib.repository.PromotionCodeEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionCodeRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.RewardUsedRepo;
import com.kelloggs.promotions.promotionservice.service.PromotionCodeEntryService;
import com.kelloggs.promotions.promotionservice.service.PromotionEntryService;

@Service
public class PromotionCodeEntryServiceImpl implements PromotionCodeEntryService {

    private final PromotionCodeEntryRepo promotionCodeEntryRepo;

    private final PromotionEntryService promotionEntryService;

    private final PromotionCodeRepo promotionCodeRepo;
    
    private final PromotionEntryRepo promotionEntryRepo;
    
    private final RewardUsedRepo rewardUsedRepo;
    
    private final PromotionRepo promotionRepo;
    
    private static final ApiLogger LOGGER = 
    		new ApiLogger(PromotionCodeEntryServiceImpl.class);

    public PromotionCodeEntryServiceImpl(PromotionCodeEntryRepo promotionCodeEntryRepo,
                                         PromotionEntryService promotionEntryService,
                                         PromotionCodeRepo promotionCodeRepo, 
                                         PromotionEntryRepo promotionEntryRepo,
                                         RewardUsedRepo rewardUsedRepo,
                                         PromotionRepo promotionRepo) {
        this.promotionCodeEntryRepo = promotionCodeEntryRepo;
        this.promotionEntryService = promotionEntryService;
        this.promotionCodeRepo = promotionCodeRepo;
        this.promotionEntryRepo = promotionEntryRepo;
        this.rewardUsedRepo = rewardUsedRepo;
        this.promotionRepo = promotionRepo;
    }

    @Override
    public ApiResponse<PromotionCodeEntry> addPromotionCodeEntry(Integer promotionId, Integer promocodeId, 
    		PromotionEntry promotionEntry, Boolean validateForExistingEntry, Boolean isCommonPromoCode, Boolean isMultiUsePromoCode, Boolean hasAttributes) {

    	PromotionCode promotionCode = promotionCodeRepo.findByIdAndPromotions_Id(promocodeId, promotionId)
        		.orElseThrow(() -> new ApiException(NOT_FOUND, NOT_FOUND.value(), String.format(
        				"Promotion Code with id : %d not found under promotion with id : %d ", promocodeId, promotionId)));

        if (Boolean.FALSE.equals(isMultiUsePromoCode) &&((Boolean.FALSE.equals(isCommonPromoCode) && promotionCode.getStatus().equals(USED.getStatus())) 
        		|| (Boolean.TRUE.equals(isCommonPromoCode) && promotionCodeEntryRepo.findAllByCodeAndPromotionEntryIdIn(promotionCode.getCode(), 
        				promotionEntryRepo.findByPromotionIdAndProfileId(promotionId, promotionEntry.getProfileId()).orElse(Collections.emptyList())
        					.stream().filter(entry -> Boolean.FALSE.equals(hasAttributes) || (Boolean.TRUE.equals(hasAttributes) 
        							&& ((Objects.nonNull(entry.getAttr1Code()) && Objects.nonNull(entry.getAttr1Value())) 
        									|| (Objects.nonNull(entry.getAttr2Code()) && Objects.nonNull(entry.getAttr2Value())))))
        										.map(PromotionEntry::getId).collect(Collectors.toSet())).orElse(Collections.emptyList()).size() > 0))) {
    
            throw new ApiException(OK, DUPLICATE_CODE.getCode(), "Code already used");
        }
        
        if (Boolean.TRUE.equals(validateForExistingEntry) && Objects.nonNull(promotionEntry.getId()) && promotionCodeEntryRepo.findAllByPromotionEntryId(promotionEntry.getId()).isPresent()) {
        	throw new ApiException(HttpStatus.ALREADY_REPORTED, ALREADY_VALIDATED.getCode(), 
					String.format("Promocode has been already validated for the given entry Id: %d.", promotionEntry.getId()));
		}

        promotionEntry = promotionEntryService.savePromotionEntryForPromoCode(promotionId, promotionEntry, validateForExistingEntry);
        
        promotionCode.setStatus(Boolean.TRUE.equals(isCommonPromoCode) || Boolean.TRUE.equals(isMultiUsePromoCode) ? AVAILABLE.getStatus() : USED.getStatus());        
        promotionCode = promotionCodeRepo.save(promotionCode);

        PromotionCodeEntry promotionCodeEntry = new PromotionCodeEntry();
        promotionCodeEntry.setPromotionEntry(promotionEntry);
        promotionCodeEntry.setPromotionCode(promotionCode);
        promotionCodeEntry.setCode(promotionCode.getCode());
        promotionCodeEntry = promotionCodeEntryRepo.save(promotionCodeEntry);

        return new ApiResponse<>("Promotion Code Entry added", promotionCodeEntry);
    }
    
    @Override
    public List<PromotionCode> validatePromoCode(Integer promotionId, List<String> promoCodes, Boolean isCodeValidationRequired) {
		
		List<PromotionCode> promotionCodeDetails = null;
		
		 if (promoCodes == null || promoCodes.isEmpty()) {
			 	 
			 // Throw bad request, if promotion codes are not provided
			 throw new ApiException(BAD_REQUEST, BAD_REQUEST.value(), "Promocodes not found in request body!", LOGGER.setLogLevel(ERROR));
		 }
		
		if (Boolean.TRUE.equals(isCodeValidationRequired)) {
			
    		// Validate the promotion codes, if the value is set to true
			promotionCodeDetails = promotionCodeRepo.findByCodeInAndStatusInAndPromotions_Id(promoCodes, 
					Arrays.asList(AVAILABLE.getStatus(), USED.getStatus()), promotionId).orElse(Collections.emptyList());
			
			if (promoCodes.size() != promotionCodeDetails.stream().filter(promotionCodeDetail -> 
										AVAILABLE.getStatus().equals(promotionCodeDetail.getStatus())).count()) {
				
				// Generate code statuses for input validation, if any of the user code is invalid
				Map<String, Object> promoCodeStatuses = promotionCodeDetails.stream()
						.collect(Collectors.toMap(PromotionCode::getCode, PromotionCode::getStatus));
				
				promoCodes.forEach(promoCode -> promoCodeStatuses.putIfAbsent(promoCode, INVALID.getStatus()));
				
				// Throw code validation failed exception with there respective statuses
				throw new ApiException(NOT_FOUND, INVALID_CODE.getCode(), "Couldn't fulfill the "
						+ "request as all the promocodes are not valid!", promoCodeStatuses, LOGGER.setLogLevel(ERROR));
			}
		} else {	
			
			// If promotion code validation is not required
			promotionCodeDetails = promotionCodeRepo.findByCodeInAndStatusInAndPromotions_Id(promoCodes, 
					Arrays.asList(AVAILABLE.getStatus()), promotionId).orElseThrow(() -> new ApiException(NOT_FOUND, 
							INVALID_CODE.getCode(), "Need at least one valid promocode for entry!", LOGGER.setLogLevel(ERROR)));
		}
		
		return promotionCodeDetails;
	}
    
    @Override
    public PromotionEntry addMultiPromoCodeEntry(Integer promotionId, 
    		PromotionEntry promotionEntry, Boolean isCodeValidationRequired, Boolean validateForExistingEntry) {
    	
    	LOGGER.log(LogLevel.INFO, String.format("Multi promotion code entry operation:: "
    			+ "{PromotionId: %d, ProfileId: %d, PromoCodes: %s, Validation: %s}", promotionId, 
    			promotionEntry.getProfileId(), promotionEntry.getAttributes(), isCodeValidationRequired));

    	// Validate user input promotion codes
    	List<PromotionCode> promotionCodes = validatePromoCode(
    			promotionId, promotionEntry.getAttributes(), isCodeValidationRequired);
    	
    	// Save promotion entry details in database
    	promotionEntry = promotionEntryService.savePromotionEntryForPromoCode(promotionId, promotionEntry, validateForExistingEntry);

    	for (PromotionCode promotionCode : promotionCodes) {
    		
    		// Store the used promotion code again the promotion entry
            PromotionCodeEntry promotionCodeEntry = new PromotionCodeEntry();
            promotionCodeEntry.setPromotionEntry(promotionEntry);
            promotionCodeEntry.setPromotionCode(promotionCode);
            promotionCodeEntry.setCode(promotionCode.getCode());
            promotionCodeEntryRepo.save(promotionCodeEntry);
            
            // Update the promotion code status in database
            promotionCode.setStatus(USED.getStatus());
            promotionCodeRepo.save(promotionCode);
		}
    	
    	LOGGER.log(LogLevel.INFO, String.format("Promotion entry created "
    			+ "with id[%d] for multi promotion code entry operation.", promotionEntry.getId()));
    	
    	return promotionEntry;
    }

	@Override
    public ApiResponse<PromotionCode> getPromotionCodeByEntryId(Integer promotionId, Integer entryId) {
        ApiResponse<PromotionEntry> result = promotionEntryService.getPromotionEntry(entryId, promotionId);

        PromotionCodeEntry promotionCodeEntry = promotionCodeEntryRepo
                .findByPromotionEntryId(result.getData().getId())
                .orElseThrow(() -> new ApiException(NOT_FOUND,
                        NOT_FOUND.value(),
                        String.format("No promocode found for entry Id: %d", entryId)));

        return new ApiResponse<>(String.format("Promotion code for entry Id: %d", entryId), promotionCodeEntry.getPromotionCode());
    }
	
	@Override
	public RedeemRewardDTO getRedemptionData(Integer profileId, Set<Integer> promotionIds){
		
		RedeemRewardDTO savedCodes = new RedeemRewardDTO();
		Set<PromotionCodeEntry> usedBatchCodes;
		Set<RewardUsed> redeemedRewards;
		
		//Fetch all the used Batch codes of the user using their profileId
		usedBatchCodes = Optional.ofNullable(promotionCodeEntryRepo.findByProfileIdAndPromotionIdIn(profileId, promotionIds).map(List::stream).
				orElse(Stream.empty()).collect(Collectors.toSet())).orElse(Collections.emptySet());
		
		//Fetch all the redeemed rewards of the user using their profileId
		redeemedRewards = Optional.of(rewardUsedRepo.findByProfileIdAndPromotionIdIn(profileId, promotionIds).map(List::stream).
				orElse(Stream.empty()).collect(Collectors.toSet())).orElse(Collections.emptySet());
		
		redeemedRewards.parallelStream().forEach(rewards -> rewards.setPromotionEntry(null));
		
		savedCodes.setProfileId(profileId);
		savedCodes.setUsedBatchCodes(usedBatchCodes);
		savedCodes.setRedeemedRewards(redeemedRewards);	

		return savedCodes;

 }
	@Override
	@Transactional
	public RedeemRewardDTO validateBatchCode(PromoCodeMetaObject promoCodeMetaObject) {
				
		List<String> batchCodes = promoCodeMetaObject.getBatchCodes();				
		Map<String, Object> entryStatusMap = new HashMap<>(validatePromoCodeWithRegex(batchCodes, promoCodeMetaObject.getRegex()));
		RedeemRewardDTO savedEntries= new RedeemRewardDTO();
		
		//Check whether all the given codes are in 'available' status
		if(entryStatusMap.values().stream().filter(value -> value.equals(AVAILABLE.getStatus().toString())).count() == entryStatusMap.size()) {		
			
			Promotion promotion = promotionRepo.findById(promoCodeMetaObject.getPromotionId()).orElse(null);
			Set<PromotionCodeEntry> entries = new HashSet<>();	
			promotionCodeEntryRepo.saveAll(batchCodes.stream().map(batchCode->{
				
				//Save the promotion code entry
            	PromotionCodeEntry promotionCodeEntry = new PromotionCodeEntry();
            	promotionCodeEntry.setProfileId(promoCodeMetaObject.getProfileId());
            	promotionCodeEntry.setCode(batchCode);
            	promotionCodeEntry.setPromotion(promotion);
            	entries.add(promotionCodeEntry);
            	return promotionCodeEntry;
            	
            }).collect(Collectors.toList()));
		
			savedEntries.setUsedBatchCodes(entries);
			savedEntries.setProfileId(promoCodeMetaObject.getProfileId());
			
		}
				
		else {
			throw new ApiException(NOT_FOUND, INVALID_CODE.getCode(), "Couldn't fulfill the request as all the promocodes are not valid!", 
					entryStatusMap, LOGGER.setLogLevel(ERROR));		
		}
				
		return savedEntries;
	}
	
	public Map<String, String> validatePromoCodeWithRegex(List<String> promoCodes, String regex) {
		
		Map<String, String> entryStatus = new HashMap<>();

		if (promoCodes == null || promoCodes.isEmpty()) {
		 	 
			 // Throw bad request, if promotion codes are not provided
			 throw new ApiException(BAD_REQUEST, BAD_REQUEST.value(), "Promocodes not found in request body!", LOGGER.setLogLevel(ERROR));
		 }
		
		else {
				
		List<PromotionCodeEntry> promoCodeEntries = promotionCodeEntryRepo.findAllByCodeIn(promoCodes).orElse(null);
		
		//Check if the given batch codes are either invalid/duplicate/used/available and return the map of codes with their respective status
	    promoCodes.stream().map(
				 batchCode->{
					 if(promoCodes.stream().filter(code -> code.equals(batchCode)).count()>1) {
						 entryStatus.put(batchCode, DUPLICATE.getStatus().toString());						 
					 }				 
					 else if(!batchCode.matches(regex)) {
						 entryStatus.put(batchCode, INVALID.getStatus().toString());						 
					 }					 
					 else if(Objects.nonNull(promoCodeEntries) ? promoCodeEntries.stream().anyMatch(entry -> entry.getCode().equals(batchCode)) : Boolean.FALSE) {					 
						 entryStatus.put(batchCode, USED.getStatus().toString());
					 }					 
					 else {
						 entryStatus.put(batchCode, AVAILABLE.getStatus().toString());
					 }
				
			return entryStatus;
				       
		 }).collect(Collectors.toList());	    
		}
		
	    return entryStatus;	  
   }
}
