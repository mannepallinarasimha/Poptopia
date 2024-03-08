package com.kelloggs.promotions.codevalidatorservice.controller;

import com.kelloggs.promotions.codevalidatorservice.services.PromotionCodeService;
import com.kelloggs.promotions.lib.entity.PromotionCode;
import com.kelloggs.promotions.lib.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/v1/promocode/{code}")
public class PromotionCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionCodeController.class);

    private final PromotionCodeService promotionCodeService;

    public PromotionCodeController(PromotionCodeService promotionCodeService) {
        this.promotionCodeService = promotionCodeService;
    }

    @GetMapping
    public ApiResponse<PromotionCode> checkCode(@PathVariable(name = "code") String code,
                                                @RequestParam(name = "promotionId") Integer promotionId, Principal user) {

        LOGGER.info("-------Request logging Start for Promotion Code Validation at {} -------", LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
        LOGGER.info("Requesting validation for code: '{}' for Promotion Id: '{}' from user: '{}'", code, promotionId, user.getName());
        return promotionCodeService.validateCode(promotionId, code);
    }
}