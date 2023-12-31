package com.kelloggs.promotions.codevalidatorservice.services.impl;

import com.kelloggs.promotions.codevalidatorservice.services.PromotionCodeService;
import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.repository.PromotionCodeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.DUPLICATE_CODE;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_CODE;
import static com.kelloggs.promotions.lib.constants.CodeConstants.USED;

@Service
public class PromotionCodeServiceImpl implements PromotionCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionCodeServiceImpl.class);

    private final PromotionCodeRepo promotionCodeRepo;

    public PromotionCodeServiceImpl(PromotionCodeRepo promotionCodeRepo) {
        this.promotionCodeRepo = promotionCodeRepo;
    }

    @Override
    public ApiResponse<PromotionCode> validateCode(Integer promotionId, String code) {

        PromotionCode promotionCode = promotionCodeRepo
                .findByCodeAndPromotions_Id(code, promotionId)
                .orElseThrow(() -> {
                    LOGGER.info("Invalid Code: '{}' for promotion Id:- '{}'", code, promotionId);
                    return new ApiException(HttpStatus.NOT_FOUND, INVALID_CODE.getCode(), "Invalid Code");
                });

        if (promotionCode.getStatus().equals(USED.getStatus())) {
            LOGGER.info("Code: '{}' already used for promotion Id:- '{}'", code, promotionId);
            throw new ApiException(HttpStatus.OK, DUPLICATE_CODE.getCode(), "Code Already Used");
        }

        LOGGER.info("Valid Code: '{}' for promotion Id: '{}'", code, promotionId);
        return new ApiResponse<>("Valid Code :-", promotionCode);
    }

}
