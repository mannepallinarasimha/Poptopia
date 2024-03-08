package com.kelloggs.promotions.lib.rule.impl;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.rule.PromoRuleService;

public class CheckDailyCapWinners implements PromoRuleService {

	
	/**
	 * This method executes the logic of providing an initial won count for random reward logic scheme 
	 * for a particular day and returns it as a map's object
	 */
	@Override
	public Map<String, Object> executeRule(Map<String, Object> parameterRuleMap) {
		
		Map<String, Object> mapToBeReturn = new HashMap<String, Object>();
		
		List<PromotionEntry> promoEntryLists = (List<PromotionEntry>)parameterRuleMap.get("promotionEntriesList");
		PromotionEntry promotionEntry = (PromotionEntry) parameterRuleMap.get("PromotionEntry");
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Integer initialCountOfUsedRewards = 0;
		if(null != promoEntryLists) {
		initialCountOfUsedRewards = promoEntryLists.stream().filter(i-> (i.getCreatedLocalDateTime().format(date).equals(promotionEntry.getCreatedLocalDateTime().format(date)))).collect(Collectors.toList()).size();
		}
		mapToBeReturn.put("Integer",initialCountOfUsedRewards);
		return mapToBeReturn;
	}

}
