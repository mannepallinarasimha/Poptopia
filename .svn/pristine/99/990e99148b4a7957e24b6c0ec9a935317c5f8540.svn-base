package com.kelloggs.promotions.promotionservice.service;

import java.util.Map;

import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.model.ApiResponse;

public interface TokenService {

    ApiResponse<Token> generateToken(Integer entryId, Integer profileId);

    /**
     * Validate the generated token
     * 
     * @param token	Generated token to be validate
     * @return	Return token details if validated, otherwise throw exception
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 17th July 2022 (Re-factored)
     */
    public Token validateToken(Token token);
    
    
    /**
     * Validate the generated token by hash code
     * 
     * @param hashCode	Generated hash code to be validate
     * @return	Return promotion entry details if validated, otherwise throw exception
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 19th July 2022
     */
    public Map<String, Object> validateToken(String hashCode);     
	
}
