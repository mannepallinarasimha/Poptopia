package com.kelloggs.promotions.codevalidatorservice.services;

import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.model.ApiResponse;

public interface PromotionCodeService {

    ApiResponse<PromotionCode> validateCode(Integer promotionId, String code);
}
