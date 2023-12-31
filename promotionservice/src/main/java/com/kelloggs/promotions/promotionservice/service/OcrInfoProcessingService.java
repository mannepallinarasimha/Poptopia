package com.kelloggs.promotions.promotionservice.service;

import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.OcrInfo;
import com.kelloggs.promotions.lib.model.ReceiptResult;

public interface OcrInfoProcessingService {

    ApiResponse<String> processData(OcrInfo ocrInfo);

    /**
     * Update the receipt validation result in the database
     * 
     * @param receiptResult	This is the receipt validation result which contains the receipt validation status, product items, other details, etc.
     * @return	Return the success response if the validation result is updated successfully otherwise throws exceptions. 
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 12th April 2023
     */
	ApiResponse<String> updateReceiptValidationResult(ReceiptResult receiptResult);
}
