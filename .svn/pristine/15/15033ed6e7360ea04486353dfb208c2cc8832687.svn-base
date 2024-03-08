package com.kelloggs.promotions.promotionservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.OcrInfo;
import com.kelloggs.promotions.lib.model.ReceiptResult;
import com.kelloggs.promotions.promotionservice.service.OcrInfoProcessingService;

@RestController
public class SnippController {

    private final OcrInfoProcessingService ocrInfoProcessingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippController.class);

    public SnippController(OcrInfoProcessingService ocrInfoProcessingService) {
        this.ocrInfoProcessingService = ocrInfoProcessingService;
    }

    @PostMapping("/api/v1/snipp/ocrInfo")
    public ApiResponse<String> addOcrInfo(@RequestBody OcrInfo ocrInfo) {
        LOGGER.info("Logging Snipp OCR Data for SnippEventId: {} , Data:: {}", ocrInfo.getSnippEventID(), ocrInfo);
        return ocrInfoProcessingService.processData(ocrInfo);
    }
    
    /**
     * This version of end point is used to update the receipt validation result processed by SNIPP V2 API 
     * 
     * @param receiptResult	This is the receipt validation result which contains the receipt validation status, product items, other details, etc.
     * @return	Return the success response if the validation result is updated successfully otherwise throws exceptions. 
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 12th April 2023
     */
    @RequestMapping(value = "/api/v2/snipp/ocrInfo", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ApiResponse<String> updateReceiptValidationResult(@RequestBody ReceiptResult receiptResult) {
    	
        LOGGER.info("Snipp V2 OCR Info :: SnippEventId: {}, Validation Result: {}", 
        		receiptResult.getSnippEventId(), receiptResult.getReceiptStatus());
        
        // Update the receipt validation result in database
        return ocrInfoProcessingService.updateReceiptValidationResult(receiptResult);
    }
}
