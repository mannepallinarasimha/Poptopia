package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.ALREADY_VALIDATED;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.EXPIRED_TOKEN;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_ENTRY_ID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_TOKEN;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_DUPLICATE_OR_INVALID;
import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;
import static com.kelloggs.promotions.lib.constants.StatusConstants.DUPLICATE;
import static com.kelloggs.promotions.lib.constants.StatusConstants.INVALID;
import static com.kelloggs.promotions.lib.constants.StatusConstants.REUPLOAD;
import static com.kelloggs.promotions.lib.constants.TokenConstants.EXPIRED;
import static com.kelloggs.promotions.lib.constants.TokenConstants.GENERATED;
import static com.kelloggs.promotions.lib.constants.TokenConstants.VALIDATED;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.INFO;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Retailer;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.repository.PromotionEntryRepo;
import com.kelloggs.promotions.lib.repository.TokenRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import com.kelloggs.promotions.promotionservice.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepo tokenRepo;

    private final PromotionEntryRepo promotionEntryRepo;

    private final StatusService statusService;
    
    private static final ApiLogger LOGGER = 
    		new ApiLogger(TokenServiceImpl.class);

    public TokenServiceImpl(TokenRepo tokenRepo, PromotionEntryRepo promotionEntryRepo, StatusService statusService) {
        this.tokenRepo = tokenRepo;
        this.promotionEntryRepo = promotionEntryRepo;
        this.statusService = statusService;
    }

    @Override
    public ApiResponse<Token> generateToken(Integer entryId, Integer profileId) {

        String hashCode = UUID.randomUUID().toString().replace("-", "");

        PromotionEntry promotionEntry = promotionEntryRepo
                .findById(entryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    NOT_FOUND.getCode(),
                                                    String.format("Invalid Entry Id :: %d", entryId)));

        String status = promotionEntry.getStatus().getType();

        if (status.equals(DUPLICATE.getStatus()) || status.equals(INVALID.getStatus())) {

            promotionEntry.setStatus(statusService.getStatus(REUPLOAD.getStatus()));
            promotionEntry = promotionEntryRepo.save(promotionEntry);

            Token token = new Token();
            token.setHashCode(hashCode);
            token.setProfileId(profileId);
            token.setEntryId(promotionEntry.getId());
            token.setAnswer(promotionEntry.getAnswer());
            token.setStatus(GENERATED.getStatus());

            Retailer retailer = promotionEntry.getRetailer();
            if (retailer == null)
                token.setRetailer(null);
            else
                token.setRetailer(retailer.getName());

            token = tokenRepo.save(token);

            return new ApiResponse<>("Token Generated", token);
        }

        throw new ApiException(HttpStatus.BAD_REQUEST,
                               NOT_DUPLICATE_OR_INVALID.getCode(),
                               String.format("Not a duplicate or invalid Entry :: %d", entryId));
    }

    @Override
    public Token validateToken(Token token) {

    	// Get the token details from database
        Token savedToken = (token.getProfileId() != null && token.getEntryId() != null 
        		? tokenRepo.findByEntryIdAndProfileIdAndHashCode(token.getEntryId(), token.getProfileId(), token.getHashCode())
        				: tokenRepo.findByHashCode(token.getHashCode())).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 
        						INVALID_TOKEN.getCode(), String.format("Invalid token :: %s", token.getHashCode()), LOGGER.setLogLevel(ERROR)));

        if (!savedToken.getStatus().equals(VALIDATED.getStatus())) {
        	
        	if (savedToken.getStatus().equals(EXPIRED.getStatus()) 
        			|| (savedToken.getExpirationTime() != null && savedToken.getExpirationTime().isBefore(LocalDateTime.now()))) {
        		
        		// Given token has been expired
        		savedToken.setStatus(EXPIRED.getStatus());
                tokenRepo.save(savedToken);
                throw new ApiException(HttpStatus.BAD_REQUEST, EXPIRED_TOKEN.getCode(), 
                				String.format("Token already expired :: %s", token.getHashCode()), LOGGER.setLogLevel(ERROR));
        	} else {
        		
        		// Update the status, if token is valid
                savedToken.setStatus(VALIDATED.getStatus());
                savedToken = tokenRepo.save(savedToken);
                LOGGER.log(INFO, String.format("Token validated successfully :: %s", token.getHashCode()));
        	}
        } else {
        	
        	// Throw exception, if token is already validated
            throw new ApiException(HttpStatus.BAD_REQUEST, ALREADY_VALIDATED.getCode(), 
            				String.format("Token already validated :: %s", token.getHashCode()), LOGGER.setLogLevel(ERROR));
        }

        return savedToken;
    }
    
    @Override
    public Map<String, Object> validateToken(String hashCode) {
    	
    	// Validate the input token   	
    	Token validatedToken = validateToken(new Token() {{setHashCode(hashCode);}});
    	
    	// Retrieve the promotion entry details from database
    	PromotionEntry promotionEntry = promotionEntryRepo.findById(validatedToken.getEntryId())
    			.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, INVALID_ENTRY_ID.getCode(), String.format(
    					"Invalid promotion entry id :: %d", validatedToken.getEntryId()), LOGGER.setLogLevel(ERROR)));
    	
    	// Bind the required details and return back 
    	Map<String, Object> entryDetails = new ObjectMapper()
    			.convertValue(promotionEntry, new TypeReference<Map<String, Object>>() {});

        entryDetails.remove("country");
        entryDetails.put("region", promotionEntry.getPromotion().getRegion());
        entryDetails.put("promotionId", promotionEntry.getPromotion().getId());
        entryDetails.replace("createdDate", promotionEntry.getCreatedDateTime());
        entryDetails.replace("modifiedDate", promotionEntry.getCreatedDateTime());
		
    	return entryDetails;
    }
}
