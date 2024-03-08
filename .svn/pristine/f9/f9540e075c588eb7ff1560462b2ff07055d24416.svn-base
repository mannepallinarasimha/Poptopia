package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALREADY_VALIDATED;
import static com.kelloggs.promotions.lib.constants.StatusConstants.UNDER_SNIPP_PROCESSING;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.kelloggs.promotions.lib.entity.Answers;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.PromotionUser;
import com.kelloggs.promotions.lib.entity.Receipt;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.SnippInfo;
import com.kelloggs.promotions.lib.repository.AnswerRepo;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.PromotionUserRepo;
import com.kelloggs.promotions.lib.repository.ReceiptRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.promotionservice.service.PromotionEntryService;
import com.kelloggs.promotions.promotionservice.service.ReceiptEntryService;

@Service
public class ReceiptEntryServiceImpl implements ReceiptEntryService {

    private final PromotionEntryService promotionEntryService;
    private final PromotionEntryRepo promotionEntryRepo;
    private final StatusService statusService;
    private final AnswerRepo answerRepo;
    private final PromotionUserRepo userRepo;

    private final ReceiptRepo receiptRepo;

    public ReceiptEntryServiceImpl(PromotionEntryService promotionEntryService,
                                   PromotionEntryRepo promotionEntryRepo, StatusService statusService, ReceiptRepo receiptRepo, AnswerRepo answerRepo, PromotionUserRepo userRepo) {
        this.promotionEntryService = promotionEntryService;
        this.promotionEntryRepo = promotionEntryRepo;
        this.statusService = statusService;
        this.receiptRepo = receiptRepo;
        this.answerRepo = answerRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ApiResponse<Receipt> addReceiptEntry(Integer promotionId, PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry) {

    	String s3ImageUrl = promotionEntry.getS3ImageUrl();

        if (StringUtils.isEmpty(s3ImageUrl) || StringUtils.isEmpty(s3ImageUrl.trim()))
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, "s3ImageUrl not found in request body");

        promotionEntry = promotionEntryService.savePromotionEntryForReceipt(promotionId, promotionEntry, uploadReceiptForExistingEntry);

        Receipt receipt = new Receipt();
        receipt.setPromotionEntry(promotionEntry);
        receipt.setS3ImageUrl(s3ImageUrl);
        receipt = receiptRepo.save(receipt);

        return new ApiResponse<>("Receipt Entry added", receipt);
    }

    @Override
    public ApiResponse<Receipt> updateReceiptEntry(Integer promotionId, Integer entryId, Integer receiptId, SnippInfo snippInfo) {

        Receipt receipt = receiptRepo
                .findByIdAndPromotionEntryId(receiptId, entryId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        404,
                        String.format("No receipts with Id: %d found for promotion entry with Id: %d", receiptId, entryId)));

        if (!receipt.getPromotionEntry().getPromotion().getId().equals(promotionId))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    404,
                    String.format("Invalid promotion id: %d for  promotion entry with Id: %d", promotionId, entryId));

        PromotionEntry promotionEntry = receipt.getPromotionEntry();
        promotionEntry.setStatus(statusService.getStatus(UNDER_SNIPP_PROCESSING.getStatus()));
        promotionEntryRepo.save(promotionEntry);

        receipt.setClientEventId(snippInfo.getClientEventId());
        receipt.setSnippEventId(snippInfo.getSnippEventId());
        receipt = receiptRepo.save(receipt);

        return new ApiResponse<>(String.format("Receipt with Id: %d updated successfully", receiptId), receipt);

    }

	@Override
	public ApiResponse<List<Receipt>> addReceiptEntries(Integer promotionId, 
			@Valid PromotionEntry promotionEntry, Boolean uploadReceiptForExistingEntry, Boolean multiUploadOnSingleEntry, Boolean isMultipleAnswerEnabled, Boolean hasUserScore) {
		
        List<Receipt> receiptList= new ArrayList<>();
		List<String> s3ImageUrls = promotionEntry.getS3ImageUrls();
    	List<String> attributes = promotionEntry.getAttributes();
    	String eventId = promotionEntry.getEventId();

        if (s3ImageUrls.isEmpty())
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, "s3ImageUrl not found in request body");
        
        if (Boolean.TRUE.equals(uploadReceiptForExistingEntry) && Objects.nonNull(promotionEntry.getId()) && receiptRepo.findByPromotionEntryId(promotionEntry.getId()).isPresent()) {
        	throw new ApiException(HttpStatus.ALREADY_REPORTED, ALREADY_VALIDATED.getCode(), String.format("Receipt has been already uploaded for the given entry Id: %d.", promotionEntry.getId()));
		}
        
        promotionEntry = promotionEntryService.savePromotionEntryForReceipt(promotionId, promotionEntry, uploadReceiptForExistingEntry);
       
        for(String imageUrl : s3ImageUrls) {
	        Receipt receipt = new Receipt();
	        receipt.setPromotionEntry(promotionEntry);
	        receipt.setS3ImageUrl(imageUrl);
	        receipt.setClientEventId(eventId);
	        receiptList.add(receipt);
	        
	        if (Boolean.TRUE.equals(multiUploadOnSingleEntry)) {
				receipt.setS3ImageUrl(s3ImageUrls.toString());
				break;
			}	        
        }
        
        if(Boolean.TRUE.equals(isMultipleAnswerEnabled)) {
        	
        	final PromotionEntry promotionEntryforMultiAnswer = promotionEntry;
        	answerRepo.saveAll(attributes.stream().map(attribute->{
            	Answers answer = new Answers();
            	answer.setPromotionEntry(promotionEntryforMultiAnswer);
            	answer.setAnswer(attribute);
            	return answer;
            }).collect(Collectors.toList()));
        	            
        	promotionEntryforMultiAnswer.setAttributes(attributes);
        }
        
        if(Boolean.TRUE.equals(hasUserScore) && promotionEntry.getAttr1Value() != null) {
        	
        	PromotionUser promotionUser  = null;
        	promotionUser = userRepo.findByProfileId(promotionEntry.getProfileId()).orElse(null);
        	
        	if(Objects.nonNull(promotionUser)) {
            	promotionUser.setAttribute1(promotionUser.getAttribute1() + Integer.valueOf(promotionEntry.getAttr1Value()));
        	}
        	else {
        		promotionUser = new PromotionUser();
        		promotionUser.setProfileId(promotionEntry.getProfileId());
        		promotionUser.setAttribute1(Integer.valueOf(promotionEntry.getAttr1Value()));	
        	}
        	userRepo.save(promotionUser);      	
        }
        
        return new ApiResponse<>("Receipt Entry added", receiptRepo.saveAll(receiptList));
    }
}
