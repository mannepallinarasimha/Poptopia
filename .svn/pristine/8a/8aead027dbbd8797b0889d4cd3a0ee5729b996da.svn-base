package com.kelloggs.promotions.promotionservice.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.promotionservice.service.TokenService;

@RestController
@RequestMapping("api/v1/token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public ApiResponse<Token> createToken(@RequestParam("profileId") Integer profileId,
                                            @RequestParam("entryId") Integer entryId) {
        return tokenService.generateToken(entryId, profileId);
    }
    
    /**
     * Validate the generated token
     * 
     * @param hashcode	Generated hash code to be validate.
     * @return	Return promotion entry details after the validation
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 17th July 2022
     */
    @GetMapping("/validate")
    public ApiResponse<Map<String, Object>> validateToken(@RequestParam("hashcode") String hashCode) {

    	// Return the token details after validation
    	return new ApiResponse<>(String.format("Promotion entry details "
    			+ "for the given token :: %s", hashCode), tokenService.validateToken(hashCode));
    }

    @PostMapping
    public ApiResponse<Token>checkToken(@Valid @RequestBody Token token) {
        return new ApiResponse<>(String.format("Valid token :: %s", token.getHashCode()), tokenService.validateToken(token));
    }

}
