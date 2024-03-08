package com.kelloggs.promotions.promotionservice.service;

import java.util.List;

import javax.validation.Valid;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.SnippInfo;

public interface ReceiptEntryService {
    ApiResponse<Receipt> addReceiptEntry(Integer promotionId, PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry);

    ApiResponse<Receipt> updateReceiptEntry(Integer promotionId, Integer entryId, Integer receiptId, SnippInfo snippInfo);

	ApiResponse<List<Receipt>> addReceiptEntries(Integer promotionId, @Valid PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry, Boolean multiUploadOnSingleEntry, Boolean isMultipleAnswerEnabled, Boolean hasUserScore);
}
