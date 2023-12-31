package com.kelloggs.promotions.lib.rule.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.RewardUsed;
import com.kelloggs.promotions.lib.rule.PromoRuleService;
import com.kelloggs.promotions.lib.rule.RuleExecutorService;

@Service
public class RewardRuleExecutorServiceImpl implements RuleExecutorService {

private final List<PromoRuleService> rules;
	
	public RewardRuleExecutorServiceImpl() {
		rules = Collections.unmodifiableList(Arrays.asList(new CheckDailyCapWinners(),new RandomWinnerAndLooser()));
		
	}

	/**
	 * This method iterates over the list of rules' object defined as in the above lines and calls them sequentially to  
	 * execute list of rules
	 */
	@Override
	public Map<String, Object> execute(Map<String, Object> objRuleMap) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		for ( PromoRuleService rule : rules){
				if(rule.toString().contains("CheckDailyCapWinners")){
					responseMap = rule.executeRule(objRuleMap);    //having integer object for storing initial won count
				}
				else if(rule.toString().contains("RandomWinnerAndLooser")){
					Integer initialCountOfUsedRewards = (Integer) responseMap.get("Integer");
					objRuleMap.put("Integer", initialCountOfUsedRewards); //putting a map object which will act as a parameter for calling another rule
					responseMap = rule.executeRule(objRuleMap);
				}
			
        }
		return responseMap;
	}

}
