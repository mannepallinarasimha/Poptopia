package com.kelloggs.promotions.winnerservice.controller;

import static org.springframework.boot.logging.LogLevel.INFO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionPrize;
import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.entity.Winner;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.PromoMetaObject;
import com.kelloggs.promotions.winnerservice.services.WinnerSelectionService;
import com.kelloggs.promotions.winnerservice.services.WinnerService;
import com.kelloggs.promotions.winnerservice.utilities.WinnerUtil;

@RestController
@RequestMapping("/api/v1/winners")
public class WinnerController {

	private WinnerSelectionService winnerSelectionService;

	private WinnerService winnerService;
	
	private static final ApiLogger LOGGER = new ApiLogger(WinnerController.class);

	public WinnerController(WinnerSelectionService winnerSelectionService, WinnerService winnerService) {
		this.winnerSelectionService = winnerSelectionService;
		this.winnerService = winnerService;
	}

	@GetMapping
	public ApiResponse<Winner> selectWinner(@RequestParam("promotionId") Integer promotionId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {

		return winnerSelectionService.getWinner(promotionId, start, end);
	}

	@GetMapping("list")
	public ApiListResponse<PromotionEntry> getEntriesForWinner(@RequestParam("promotionId") Integer promotionId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {

		return winnerService.getEntriesForWinnerSelection(promotionId, start, end);
	}
	
	/**
	 * Select the winner/loser for the given promotion entry based on winning moment and winning probability
	 * 
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param hasSpecialEligibility	Determines whether the user has special eligibility criteria for selection or not
	 * @return Return the generated token binded with winner/loser selection details
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 18th July 2022
	 */
	@GetMapping("/select")
	public ApiResponse<Token> selectWinnerOrLoser(@RequestParam("entryId") Integer promotionEntryId, 
			@RequestParam(value = "specialEligibility", required = false) Boolean hasSpecialEligibility) {
		
		// Return the generated token binded with result after winner/loser selection
		return new ApiResponse<>(String.format("Selection result for the given promotion entry id: %d", 
				promotionEntryId), winnerSelectionService.decideWinnerOrLoser(promotionEntryId, hasSpecialEligibility));
	}
	
	/**
	 * Select the winner/loser for the given promotion entry based on the winning moment
	 * 
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param hasPrizeSelection	Select prize for the winner if the flag is set as true, otherwise no prize selection. 
	 * @param isBasedOnRequestTime	Flag to determine whether to consider the current API request time 
	 * 		  for the winning moment selection or entry creation local time, default value is false (i.e. entry creation local time)
	 * @param timeZone	Time zone to be used to get the user current request time if the isBasedOnRequestTime flag is set to true. 
	 * 		  It's an optional attribute, if the value is present then time it retrieves the time based on the given time zone
	 * 		  otherwise if the value is null then it uses the promotion entry time zone to find the request time.
	 * @return Return the updated promotion entry with selected winner/loser status and prize details
	 * 
	 * Note: Replace the value of defaultValue attribute to "false" for hasPrizeSelection & isBasedOnRequestTime parameter 
	 * and remove the defaultValue attribute for timeZone parameter after the BNI promotion completion.
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 24th July 2023
	 */
	@PostMapping("/entry/{promotionEntryId}/select")
	public ApiResponse<PromotionEntry> selectWinnerOrLoser(@PathVariable("promotionEntryId") Optional<PromotionEntry> promotionEntryOptional,
            @RequestParam(value = "hasPrizeSelection", required = false, defaultValue = "true") Boolean hasPrizeSelection,
            @RequestParam(value = "isBasedOnRequestTime", required = false, defaultValue = "true") Boolean isBasedOnRequestTime,
            @RequestParam(value = "timeZone", required = false, defaultValue = "UTC") String timeZone) {
		
		PromotionEntry promotionEntry = promotionEntryOptional.orElse(null);
		
		if (Boolean.TRUE.equals(WinnerUtil.isValidEntryForWinnerSelection(promotionEntry))) {
			
			// Decide winner/loser for the given valid promotion entry based on the eligible winning moment
			promotionEntry = winnerSelectionService.decideWinnerOrLoser(promotionEntry, 
					Boolean.TRUE.equals(isBasedOnRequestTime) ? LocalDateTime.now(ZoneId.of(Objects.isNull(timeZone) 
							? promotionEntry.getLocalTimeZone() : timeZone)) : promotionEntry.getCreatedLocalDateTime(), hasPrizeSelection);
		}
		
		// Return the	 generated token binded with result after winner/loser selection
		return new ApiResponse<>(String.format("Winner selection result for the given promotion entry id: %d", promotionEntry.getId()), promotionEntry);
	}
	
	/**
	 * Select prize card for the given promotion entry
	 * 
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param hasRandomSelection to check if the prize selection is on random basis or not
	 * @return Return the selected prize card details
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 22nd May 2023
	 */
	@GetMapping("/entry/{promotionEntryId}/select/prize/card")
	public ApiResponse<PromotionPrize> selectPrizeCard(@PathVariable("promotionEntryId") Optional<PromotionEntry> promotionEntryOptional, 
			 @RequestParam(value = "hasRandomSelection", required = false, defaultValue = "false") Boolean hasRandomSelection) {
		
		PromotionPrize selectedPrize = null;
		PromotionEntry promotionEntry = promotionEntryOptional.orElse(null);
		
		if (Boolean.TRUE.equals(WinnerUtil.isValidPromotionEntry(promotionEntry))) {
			
			LOGGER.log(INFO, String.format("User Prize Card Selection :: [Promotion Id: %d, Promotion Entry Id: %d, Profile Id: %d] :: ", 
					promotionEntry.getPromotion().getId(), promotionEntry.getId(), promotionEntry.getProfileId()));
			
			// Select the prize card for the given user entry
			selectedPrize = winnerSelectionService.selectPrizeCard(promotionEntry, hasRandomSelection);
			
			LOGGER.log(INFO, String.format("User with profile id: %d has won the prize[%s] for promotion entry id: %d under promotion id: %d", 
					promotionEntry.getProfileId(), promotionEntry.getAttr1Value(), promotionEntry.getId(), promotionEntry.getPromotion().getId()));
		}
		
		// Return the selected prize for the given user entry
		return new ApiResponse<>(String.format("Selected prize for the given promotion entry id: %d", promotionEntry.getId()), selectedPrize);
	}

	@PostMapping
	public ApiResponse<Winner> createWinner(@RequestParam("entryId") Integer entryId) {

		return winnerSelectionService.addWinner(entryId);
	}

	@PostMapping("select")
	public ApiListResponse<?> getWinnerEntry(@RequestParam("promotionId") Integer promotionId,
			@RequestParam("profileId") Integer profileId, @RequestParam("entryId") Integer entryId,
			@RequestBody PromoMetaObject promoMetaObject) {

		Map<Reward, String> responseObj = new HashMap<>();
		List<Reward> responseKeyObj = new ArrayList<>();
		String responseValueObj = new String();
		String rewardStep = new String();

		if (promoMetaObject.getWinSelectionType().toLowerCase().equals("winmoment")
				&& promoMetaObject.getWinMechanism().toLowerCase().equals("instantwin")) {
			responseObj = winnerSelectionService.selectWinOrLostForMoment(promotionId, profileId, entryId,
					promoMetaObject);
			for (Map.Entry<Reward, String> pair : responseObj.entrySet()) {
				responseKeyObj.add(pair.getKey());
				responseValueObj = pair.getValue();
			}
			if (promoMetaObject.getRewardstep().toLowerCase().equals("winoff")) {
				return winnerSelectionService.getResponseForOfflineWinSelection(responseKeyObj, responseValueObj, promotionId, entryId);
			}

			else if (promoMetaObject.getRewardstep().toLowerCase().equals("win")) {
				rewardStep = "win";
				return winnerSelectionService.getRewardForOnlineWinSelection(promotionId, profileId, entryId,
						responseValueObj, rewardStep, responseKeyObj);
			}

			else if (promoMetaObject.getRewardstep().toLowerCase().equals("valid")) {
				rewardStep = "valid";
				return winnerSelectionService.getRewardOnlyForValidUser(promotionId, profileId, entryId,
						responseValueObj, rewardStep);

			} else if (promoMetaObject.getRewardstep().toLowerCase().equals("validwin")) {
				rewardStep = "valid+win";
				return winnerSelectionService.getRewardForValidWinUser(promotionId, profileId, entryId,
						responseValueObj, rewardStep);
			}

			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format("Invalid reward step in request body:"));

		}

		else if (promoMetaObject.getWinSelectionType().toLowerCase().equals("percentage")
				&& promoMetaObject.getWinMechanism().toLowerCase().equals("instantwin")) {
			responseObj = winnerSelectionService.selectWinOrLostForPercentage(promotionId, profileId, entryId);
			for (Map.Entry<Reward, String> pair : responseObj.entrySet()) {
				responseKeyObj.add(pair.getKey());
				responseValueObj = pair.getValue();
			}
			if (promoMetaObject.getRewardstep().toLowerCase().equals("winoff")) {
				return winnerSelectionService.getResponseForOfflineWinSelection(responseKeyObj, responseValueObj, promotionId, entryId);
			}

			else if (promoMetaObject.getRewardstep().toLowerCase().equals("win")) {
				rewardStep = "win";
				return winnerSelectionService.getRewardForOnlineWinSelection(promotionId, profileId, entryId,
						responseValueObj, rewardStep, responseKeyObj);
			}

			else if (promoMetaObject.getRewardstep().toLowerCase().equals("valid")) {
				rewardStep = "valid";
				return winnerSelectionService.getRewardOnlyForValidUser(promotionId, profileId, entryId,
						responseValueObj, rewardStep);

			} else if (promoMetaObject.getRewardstep().toLowerCase().equals("validwin")) {
				rewardStep = "valid+win";
				return winnerSelectionService.getRewardForValidWinUser(promotionId, profileId, entryId,
						responseValueObj, rewardStep);
			}

			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format("Invalid reward step in request body:"));

		} else if (promoMetaObject.getWinSelectionType().toLowerCase().equals("sweepstake")
				&& promoMetaObject.getWinMechanism().toLowerCase().equals("luckywinner")) {
			return winnerService.getWinnersForSweepstake(promotionId, promoMetaObject);
		}
		throw new ApiException(HttpStatus.BAD_REQUEST, 400,
				String.format("Invalid WinSelectionType or WinMechanism in request body:"));

	}
	
	/**
	 * Update the promotion entry with the winner selection status along with the prize.
	 * 
	 * @param promotionEntryId	Id of the current promotion entry
	 * @param winnerMap contains the winner selection status along with the prize
	 * @return Return the updated promotion entry details
	 * 
	 * @author ANSHAY SEHRAWAT (M1092350)
	 * @since 24th July 2023
	 */
	@RequestMapping(value = "/entry/{entryId}/update/selection/result", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ApiResponse<PromotionEntry> updateWinnerSelectionResult(
    		@PathVariable(value = "entryId") Optional<PromotionEntry> promotionEntryOptional, @RequestBody Map<String, Object> winnerMap){
		
    	//Returns new entry details after updating the selection result
		return new ApiResponse<>("Updated selection result for the given promotion entry id.", 
				winnerService.updateWinnerSelectionResult(promotionEntryOptional.orElse(null), winnerMap));
		
    }
	
}
