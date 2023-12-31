package com.kelloggs.promotions.lib.utilities;

import static com.kelloggs.promotions.lib.constants.TokenConstants.GENERATED;

import java.util.UUID;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.entity.Token;

/**
 * Service Util: This class is used to provide 
 * utility functions to perform various operations
 * used by all the services
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 10-01-2023
 */
public class ServiceUtil {
	
	
	/**
	 * Instantiate the utility
	 */
	private ServiceUtil() {
		super();
	}
	
	/**
	 * Generate token from promotion entry details
	 * 
	 * @param promotionEntry	Entry details for the current promotion
	 * @return Return generated token details binded with given entry details
	 */
	public static Token generateToken(PromotionEntry promotionEntry) {
		
		// Generate the token and bind the selection result
		String hashCode = promotionEntry.getProfileId() 
				+ UUID.randomUUID().toString().replace("-", "") + promotionEntry.getId();
		
		Token token = new Token();
		token.setHashCode(hashCode);
		token.setEntryId(promotionEntry.getId());
		token.setProfileId(promotionEntry.getProfileId());
		token.setAnswer(promotionEntry.getAnswer());
		token.setRetailer(promotionEntry.getRetailerName());
		token.setStatus(GENERATED.getStatus());
		
		return token;
	}
}
