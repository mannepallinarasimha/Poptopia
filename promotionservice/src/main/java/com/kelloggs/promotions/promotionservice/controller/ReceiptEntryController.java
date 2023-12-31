package com.kelloggs.promotions.promotionservice.controller;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.SnippInfo;
import com.kelloggs.promotions.promotionservice.service.ReceiptEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/promotions/{promotionId}")
public class ReceiptEntryController {

    private final ReceiptEntryService receiptEntryService;

    public ReceiptEntryController(ReceiptEntryService receiptEntryService) {
        this.receiptEntryService = receiptEntryService;
    }

    @PostMapping(value = "receiptEntry")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Receipt> createReceiptEntry(@PathVariable(value = "promotionId") Integer promotionId, @Valid @RequestBody PromotionEntry promotionEntry,
    		@RequestParam(name = "uploadReceiptForExistingEntry", required = false, defaultValue = "false") Boolean uploadReceiptForExistingEntry) {

        return receiptEntryService.addReceiptEntry(promotionId, promotionEntry, uploadReceiptForExistingEntry);
    }
    
    @PostMapping(value = "receiptEntries")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<Receipt>> createReceiptEntries(@PathVariable(value = "promotionId") Integer promotionId, @Valid @RequestBody PromotionEntry promotionEntry,
    		@RequestParam(name = "uploadReceiptForExistingEntry", required = false, defaultValue = "false") Boolean uploadReceiptForExistingEntry,
    		@RequestParam(name = "multiUploadOnSingleEntry", required = false, defaultValue = "false") Boolean multiUploadOnSingleEntry,
    		@RequestParam(name = "isMultipleAnswerEnabled", required = false, defaultValue = "false") Boolean isMultipleAnswerEnabled,
    		@RequestParam(name = "hasUserScore", required = false, defaultValue = "false") Boolean hasUserScore) {

        return receiptEntryService.addReceiptEntries(promotionId, promotionEntry, uploadReceiptForExistingEntry, multiUploadOnSingleEntry, isMultipleAnswerEnabled, hasUserScore);
    }

    @PutMapping(value = "entries/{entryId}/receipts/{receiptId}")
    public ApiResponse<Receipt> changeReceiptEntry(@PathVariable(value = "promotionId") Integer promotionId,
                                                   @PathVariable(value = "entryId") Integer entryId,
                                                   @PathVariable(value = "receiptId") Integer receiptId,
                                                   @Valid @RequestBody SnippInfo snippInfo) {
        return receiptEntryService.updateReceiptEntry(promotionId, entryId, receiptId, snippInfo);
    }
}
