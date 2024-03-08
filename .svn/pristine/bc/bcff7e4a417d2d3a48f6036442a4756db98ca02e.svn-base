package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.StatusConstants.DUPLICATE;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VALID;
import static com.kelloggs.promotions.lib.constants.StatusConstants.INVALID;
import static com.kelloggs.promotions.lib.constants.StatusConstants.VERIFIED;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Item;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.entity.ReceiptHeader;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.ItemDetails;
import com.kelloggs.promotions.lib.model.OcrInfo;
import com.kelloggs.promotions.lib.model.ProductItem;
import com.kelloggs.promotions.lib.model.ReceiptResult;
import com.kelloggs.promotions.lib.repository.ItemRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.ReceiptHeaderRepo;
import com.kelloggs.promotions.lib.repository.ReceiptRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.promotionservice.service.OcrInfoProcessingService;

@Service
public class OcrInfoProcessingServiceImpl implements OcrInfoProcessingService {

    private final ReceiptRepo receiptRepo;

    private final ItemRepo itemRepo;

    private final ReceiptHeaderRepo receiptHeaderRepo;

    private final StatusService statusService;

    private final PromotionEntryRepo promotionEntryRepo;

    public OcrInfoProcessingServiceImpl(ReceiptRepo receiptRepo, ItemRepo itemRepo, ReceiptHeaderRepo receiptHeaderRepo, StatusService statusService, PromotionEntryRepo promotionEntryRepo) {
        this.receiptRepo = receiptRepo;
        this.itemRepo = itemRepo;
        this.receiptHeaderRepo = receiptHeaderRepo;
        this.statusService = statusService;
        this.promotionEntryRepo = promotionEntryRepo;
    }

    @Override
    public ApiResponse<String> processData(OcrInfo ocrInfo) {

        String snippEventID = ocrInfo.getSnippEventID();
        String clientEventID = ocrInfo.getClientEventID();
        final String VALID_RECEIPT = "Valid";

        Receipt receipt = receiptRepo
                .findBySnippEventIdAndClientEventId(snippEventID, clientEventID)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    400,
                                                    String.format(
                                                            "Invalid SnippEventId: %s and ClientEventId: %s",
                                                            snippEventID, clientEventID)));

        receipt.setImageStatus(ocrInfo.getImgStatus());
        receipt.setProcessingStatus(ocrInfo.getProcStatus());
        receipt.setProcessingTime(ocrInfo.getProcTime());
        receipt.setProcessingTypeId(ocrInfo.getProcessingId());
        receipt.setValidationTypeId(ocrInfo.getValidationId());

        Receipt finalReceipt = receiptRepo.save(receipt);

        PromotionEntry promotionEntry = finalReceipt.getPromotionEntry();

        if (finalReceipt.getImageStatus().equalsIgnoreCase(VALID_RECEIPT)) {

            promotionEntry.setStatus(statusService.getStatus(VERIFIED.getStatus()));

            ItemDetails itemDetails = ocrInfo.getItemDetails();
            List<Item> items = null;
            ReceiptHeader receiptHeader = null;

            if (itemDetails != null) {

                items = itemDetails.getItems();
                receiptHeader = itemDetails.getReceiptHeader();

                if (items != null) {
                    items.forEach(item -> item.setReceipt(finalReceipt));
                    itemRepo.saveAll(items);
                }

                if (receiptHeader != null) {
                    receiptHeader.setReceipt(receipt);
                    receiptHeaderRepo.save(receiptHeader);
                }
            }

        } else {

            if (ocrInfo.getInvalidDetails().getInvalidText().contains("Duplicate"))
                promotionEntry.setStatus(statusService.getStatus(DUPLICATE.getStatus()));
            else
                promotionEntry.setStatus(statusService.getStatus(INVALID.getStatus()));
                promotionEntry.setInvalidShortText(ocrInfo.getInvalidDetails().getInvalidText());
        }

        promotionEntryRepo.save(promotionEntry);

        return new ApiResponse<>("Ocr data received", "Data processed");
    }

	@Override
	public ApiResponse<String> updateReceiptValidationResult(ReceiptResult receiptResult) {
		
        final String VALID_RECEIPT = "VALID_RECEIPT";
        
        // Get the related receipt details from database
        final Receipt receipt = receiptRepo.findBySnippEventIdAndClientEventId(receiptResult.getSnippEventId(), 
        		receiptResult.getClientEventId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), 
        				String.format("Receipt not found with given snippEventId: %s and clientEventId: %s", 
        						receiptResult.getSnippEventId(), receiptResult.getClientEventId())));

        // Update the receipt with validation result if found
        receipt.setImageStatus(receiptResult.getReceiptStatus());
        receipt.setProcessingStatus(VALID_RECEIPT.equals(
        		receiptResult.getReceiptStatus()) ? VALID.getStatus() : INVALID.getStatus());
        receipt.setProcessingTime(receiptResult.getUpdatedAt().toString());
        receipt.setProcessingTypeId(receiptResult.getSubmissionSource());
        receipt.setValidationTypeId(receiptResult.getSubmissionType());
        receiptRepo.save(receipt);
        
        // Get related promotion entry details from the receipt
        PromotionEntry promotionEntry = receipt.getPromotionEntry();

        if (VALID_RECEIPT.equals(receipt.getImageStatus())) {
            
            // Save receipt header and meta data in the database
            ReceiptHeader receiptHeader = new ReceiptHeader();
            receiptHeader.setReceiptTrans(receiptResult.getTransactionId());
            receiptHeader.setTotalPrice(receiptResult.getTotalTransactionAmount());
            receiptHeader.setRetailer(Objects.nonNull(receiptResult.getRetailerName()) 
            		? receiptResult.getRetailerName() : receiptResult.getSellerName());
            receiptHeader.setPhone(receiptResult.getRetailerPhone());
            receiptHeader.setCity(receiptResult.getRetailerCity());
            receiptHeader.setState(receiptResult.getRetailerState());
            receiptHeader.setPostCode(receiptResult.getRetailerZipcode());
            receiptHeader.setReceipt(receipt);
            receiptHeaderRepo.save(receiptHeader);
            
            // Prepare and save product items in the database
            List<Item> productItems = receiptResult.getProductItems()
            		.stream().filter(ProductItem::getIsQualifiedForCampaign).map(productItem -> {
            	Item item = new Item();
            	item.setItemId(productItem.getItemId());
            	item.setName(productItem.getSkuValue() + " | " + productItem.getReferenceValue());
            	item.setPrice(productItem.getUnitPrice());
            	item.setQty(productItem.getQuantity());
            	item.setReceipt(receipt);
            	return item;
            }).collect(Collectors.toList());
            
            if (productItems.size() > 0) {itemRepo.saveAll(productItems);}
            
            // If receipt is valid, then update the related promotion entry status as VERIFIED
            promotionEntry.setStatus(statusService.getStatus(VERIFIED.getStatus()));
        } else {
            
            // If receipt is invalid, then update the related promotion entry status as INVALID
            promotionEntry.setStatus(statusService.getStatus(INVALID.getStatus()));
            promotionEntry.setInvalidShortText(Optional.ofNullable(
            		receiptResult.getReceiptStatus()).map(String::toLowerCase).orElse(null));
        }

        // Update promotion entry with receipt validation status 
        promotionEntryRepo.save(promotionEntry);
        
        return new ApiResponse<>("Receipt validation result updated!", String.format("Receipt Status :: %s", receipt.getImageStatus()));
	}
}
