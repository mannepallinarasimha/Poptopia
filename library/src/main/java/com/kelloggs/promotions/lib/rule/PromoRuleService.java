package com.kelloggs.promotions.lib.rule;

import java.util.Map;

import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.model.ApiListResponse;

public interface PromoRuleService {

	/**
	 * @param parameterRuleMap
	 * @return
	 */
	public Map<String, Object> executeRule(Map<String,Object> parameterRuleMap);
	
}
