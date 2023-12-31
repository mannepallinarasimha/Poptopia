package com.kelloggs.promotions.winnerservice.services.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_ENTRY_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_PROFILE_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_STATUS;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NO_ELIGIBLE_ENTRIES;
import static com.kelloggs.promotions.lib.constants.StatusConstants.CLOSED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.DUPLICATE;
import static com.kelloggs.promotions.lib.constants.StatusConstants.INVALID;
import static com.kelloggs.promotions.lib.constants.StatusConstants.LOST;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REUPLOAD;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REUPLOAD_EMAIL_SENT;
import static com.kelloggs.promotions.lib.constants.StatusConstants.UNDER_SNIPP_PROCESSING;
import static com.kelloggs.promotions.lib.constants.StatusConstants.WON;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.constants.StatusConstants;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Winner;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.PromoMetaObject;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.WinnerRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.winnerservice.services.WinnerService;
import com.kelloggs.promotions.winnerservice.utilities.WinnerUtil;

@Service
public class WinnerServiceImpl implements WinnerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WinnerServiceImpl.class);


	private PromotionEntryRepo promotionEntryRepo;
	private PromotionRepo promotionRepo;
	private WinnerRepo winnerRepo;
	private StatusService statusService;

	public WinnerServiceImpl(PromotionEntryRepo promotionEntryRepo, PromotionRepo promotionRepo,WinnerRepo winnerRepo,StatusService statusService) {
		this.promotionEntryRepo = promotionEntryRepo;
		this.promotionRepo = promotionRepo;
		this.winnerRepo = winnerRepo;
		this.statusService = statusService;
	}

	private List<PromotionEntry> removeClosedEntriesForPromotion(List<PromotionEntry> promotionEntries,
			Integer promotionId) {

		List<PromotionEntry> closedEntries = promotionEntryRepo
				.findByPromotionIdAndStatusType(promotionId, CLOSED.getStatus()).orElse(new ArrayList<>());

		if (!closedEntries.isEmpty()) {

			Set<Integer> closedProfileIds = closedEntries // getting profileIds of closed promotion entries
					.stream().map(PromotionEntry::getProfileId).collect(Collectors.toSet());

			return promotionEntries // filter entries based on the closed profileIds
					.stream().filter(promotionEntry -> !closedProfileIds.contains(promotionEntry.getProfileId()))
					.collect(Collectors.toList());
		}

		return promotionEntries;
	}

	@Override
	public List<PromotionEntry> getValidEntriesForPromotion(Integer promotionId, Date startDate, Date endDate) {

		List<String> statusFilters = Arrays.asList(INVALID.getStatus(), UNDER_SNIPP_PROCESSING.getStatus(),
				DUPLICATE.getStatus(), REUPLOAD.getStatus(), REUPLOAD_EMAIL_SENT.getStatus(), LOST.getStatus());

		List<PromotionEntry> promotionEntries = promotionEntryRepo
				.findByPromotionIdAndCreatedDateBetweenAndStatusTypeNotIn(promotionId, startDate, endDate,
						statusFilters)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(), String.format(
						"No eligible entries found for promotion Id: %d for the mentioned slot start: %s and end: %s",
						promotionId, startDate, endDate)));

		return removeClosedEntriesForPromotion(promotionEntries, promotionId);
	}

	@Override
	public ApiListResponse<PromotionEntry> getEntriesForWinnerSelection(Integer promotionId, Date startDate,
			Date endDate) {

		List<PromotionEntry> validEntries = getValidEntriesForPromotion(promotionId, startDate, endDate);

		if (validEntries.isEmpty())
			throw new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(), String.format(
					"No eligible entries found for promotion Id: %d for the mentioned slot start: %s and end: %s",
					promotionId, startDate, endDate));

		return new ApiListResponse<>(String.format("Valid Entries for Promotion with Id :- %d", promotionId),
				validEntries, validEntries.size());

	}

	@Override
	public ApiListResponse<PromotionEntry> getWinnersForSweepstake(Integer promotionId, PromoMetaObject promoMetaObject) {
		Date startDate = new Date();
		Date endDate = new Date();
		if ((null == promoMetaObject.getWinFrequency()) || (null == promoMetaObject.getWinnerCount())) {
			throw new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(),
					String.format("No win frequency or winner count found for promotion ID:: %d ", promotionId));
		} else {
			if (null != promoMetaObject.getWinFrequency()
					&& promoMetaObject.getWinFrequency().toLowerCase().equals("daily")) {
				LocalDate date1 = findCurrentLocalDate(promotionId);
				startDate = java.sql.Date.valueOf(date1);
				endDate = java.sql.Date.valueOf(date1);

			} else if (null != promoMetaObject.getWinFrequency()
					&& promoMetaObject.getWinFrequency().toLowerCase().equals("weekly")) {
				LocalDate date1 = findCurrentLocalDate(promotionId);
				endDate = java.sql.Date.valueOf(date1);
				LocalDate endDateAfterDeduction = date1.minusWeeks(1);
				startDate = java.sql.Date.valueOf(endDateAfterDeduction);

			} else if (null != promoMetaObject.getWinFrequency()
					&& promoMetaObject.getWinFrequency().toLowerCase().equals("monthly")) {
				LocalDate date1 = findCurrentLocalDate(promotionId);
				endDate = java.sql.Date.valueOf(date1);
				LocalDate endDateAfterDeduction = date1.minusMonths(1);
				startDate = java.sql.Date.valueOf(endDateAfterDeduction);
				
			}
			else if (null != promoMetaObject.getWinFrequency()
					&& promoMetaObject.getWinFrequency().toLowerCase().equals("endofpromo")) {
					LocalDate date1 = findCurrentLocalDate(promotionId);
					endDate = java.sql.Date.valueOf(date1);
					Promotion promotion = promotionRepo.findById(promotionId).get();
					startDate = promotion.getStartDate();

					}
			}
		List<PromotionEntry> validEntries = getValidEntriesForPromotion(promotionId, startDate, endDate);
		
		if (validEntries.isEmpty()) {
			throw new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(), String.format(
					"No eligible entries found for promotion Id: %d for the mentioned slot start: %s and end: %s",
					promotionId, startDate, endDate));
		}
		
		Set<Integer> distinctProfileIds = validEntries // getting distinct profileIds from  promotion entries having duplicate profile ids
				.stream().map(PromotionEntry::getProfileId).collect(Collectors.toSet());
		List<PromotionEntry> validPromoEntries = new ArrayList<>();
		for(Integer s: distinctProfileIds) {
		List<PromotionEntry> promotionEntries = getAllPromotionEntries(promotionId, s);
		PromotionEntry promotionEntry = promotionEntries.stream() 
				.findAny().orElseThrow(() -> {
					return new ApiException(HttpStatus.NOT_FOUND, NO_ELIGIBLE_ENTRIES.getCode(), String.format(
							"no Promotion Entry  for user with profile ID:: %d found", s));
				});
		validPromoEntries.add(promotionEntry);
		
		}

		
		 Collections.shuffle(validPromoEntries);
		 validPromoEntries = validPromoEntries.stream().limit(promoMetaObject.getWinnerCount()).collect(Collectors.toList());
		 addWinnersListToDB(validPromoEntries,promoMetaObject.getWinFrequency());
		 return new ApiListResponse<>(String.format("Winners entries for Promotion with Id :- %d got added successfully", promotionId),validPromoEntries,validPromoEntries.size());
	
	}

	private void addWinnersListToDB(List<PromotionEntry> validEntries, String winFrequency) {

		for(PromotionEntry promotionEntry: validEntries) {
		Winner winner = new Winner();
		winner.setPromotionEntry(promotionEntry);
		winner.setWinFrequency(winFrequency.toLowerCase());
		winner = winnerRepo.save(winner);
		promotionEntry.setStatus(statusService.getStatus(CLOSED.getStatus()));
		promotionEntry = promotionEntryRepo.save(promotionEntry);
		}
		
		
	}

	private LocalDate findCurrentLocalDate(Integer promotionId) {
		Promotion promotion = promotionRepo.findById(promotionId).get();
		ZoneId zone = ZoneId.of(promotion.getLocalTimeZone());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		ZonedDateTime date = ZonedDateTime.now(zone);
		LocalDate date1 = null;
		date1 = LocalDate.parse(date.format(formatter), formatter);
		return date1;
	}

	private List<PromotionEntry> getAllPromotionEntries(Integer promotionId, Integer profileId) {
		// getting promotions entries of user
		List<PromotionEntry> promotionEntries = promotionEntryRepo.findByPromotionIdAndProfileId(promotionId, profileId)
				.orElseThrow(() -> {
					LOGGER.info("No promotion entries found for user with profile ID: {} under promotion with ID: {}",
							profileId, promotionId);
					return new ApiException(HttpStatus.NOT_FOUND, INVALID_PROFILE_ID.getCode(), String.format(
							"No promotion entries found for user with profile ID:: %d under promotion with ID:: %d",
							profileId, promotionId));
				});

		return promotionEntries;
	}
	
	public PromotionEntry updateWinnerSelectionResult(PromotionEntry promotionEntry, Map<String, Object> winnerMap) {
    	
    	final String PRIZE  = "prize";
    	final String IS_WINNER  = "isWinner";
    	
    	if(winnerMap.containsKey(IS_WINNER) && Objects.nonNull(winnerMap.get(IS_WINNER)) 
    				&& Boolean.TRUE.equals(WinnerUtil.isValidEntryForWinnerSelection(promotionEntry))) {
    		
		    //Updating "WON/LOST" Status in the entry only when the entry is in 'PARTICIPATED' state.
    		if(StatusConstants.PARTICIPATED.name().equals(promotionEntry.getStatus().getType())) {
	    		promotionEntry.setStatus(statusService.getStatus((Boolean.valueOf(winnerMap.get(IS_WINNER).toString()) ? WON : LOST).getStatus()));
    		}
    		  
    		//Updating winningStatus and Prize for the given entry 
    		promotionEntry.setAttr1Code(IS_WINNER);
    		promotionEntry.setAttr1Value(Boolean.valueOf(winnerMap.get(IS_WINNER).toString()).toString());
    		
    		// Update prize if the attribute is available
    		if (winnerMap.containsKey(PRIZE)) {
        		promotionEntry.setAttr2Code(PRIZE); promotionEntry.setAttr2Value(String.valueOf(winnerMap.get(PRIZE)));
			}
    		
		    //Saving the entry with the updated details
		    promotionEntryRepo.save(promotionEntry);		        
		    
		 } else {
			 
    		//Throw exception if no attributes were provided to update the entry
    		throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_STATUS.getCode(), "No valid attributes were found to be updated" );
    	 }
  
        return  promotionEntry;
    }
}