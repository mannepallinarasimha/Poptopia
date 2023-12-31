package com.kelloggs.promotions.lib.rule.impl;

import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.rule.PromoRuleService;
@Service
public class RandomWinnerAndLooser implements PromoRuleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomWinnerAndLooser.class);

	/**
	 * This method is responsible for executing a random logic against the verified user and 
	 * returns a map object which contains a flag value for executing different tasks in the calling method  
	 */
	@Override
	public Map<String, Object> executeRule(Map<String, Object> parameterRuleMap) {

		Map<String, Object> mapToBeReturn = new HashMap<String, Object>();
		String flagToCallProcessEntryForRewardMethod = "no";
		Integer initialCountOfUsedRewards = (Integer) parameterRuleMap.get("Integer");
		PromotionEntry promotionEntry = (PromotionEntry) parameterRuleMap.get("PromotionEntry");
		WinnerConfig winnerConfig = (WinnerConfig)parameterRuleMap.get("WinnerConfig");

		
		SecureRandom randomObj = new SecureRandom();
		int number = 1 + (randomObj.nextInt(winnerConfig.getLimit()));
		LOGGER.info("Random number {}", number);

		if (promotionEntry.getStatus().getType().equals(VERIFIED.getStatus())) {
			if (number <= winnerConfig.getWinProbability()
					&& initialCountOfUsedRewards.intValue() < winnerConfig.getMaxWinner()) {
				flagToCallProcessEntryForRewardMethod = "yes";
				mapToBeReturn.put("FlagToCallProcessEntryForRewardMethod",flagToCallProcessEntryForRewardMethod);
				return mapToBeReturn;
			} else {
				
				flagToCallProcessEntryForRewardMethod = "no";
				mapToBeReturn.put("FlagToCallProcessEntryForRewardMethod",flagToCallProcessEntryForRewardMethod);
				return mapToBeReturn;

			}
		} else {
			flagToCallProcessEntryForRewardMethod = "yesCall";
			mapToBeReturn.put("FlagToCallProcessEntryForRewardMethod",flagToCallProcessEntryForRewardMethod);
			return mapToBeReturn;
		}
	}

}
