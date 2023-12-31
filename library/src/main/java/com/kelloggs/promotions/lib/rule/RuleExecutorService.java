package com.kelloggs.promotions.lib.rule;

import java.util.Map;

import com.kelloggs.promotions.lib.entity.Reward;
import com.kelloggs.promotions.lib.model.ApiListResponse;

public interface RuleExecutorService {

	public Map<String, Object> execute(Map<String,Object> objRuleMap);
	
}
