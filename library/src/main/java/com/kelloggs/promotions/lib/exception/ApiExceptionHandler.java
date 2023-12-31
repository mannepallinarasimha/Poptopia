package com.kelloggs.promotions.lib.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kelloggs.promotions.lib.model.ApiErrorResponse;
import com.kelloggs.promotions.lib.model.ApiErrorResponseExtended;
import com.kelloggs.promotions.lib.model.ApiLogger;
import com.kelloggs.promotions.lib.model.BadRequest;
import com.kelloggs.promotions.lib.model.BadRequestResponse;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<Object> handleApiExceptions(ApiException apiException) {
    	
    	ApiLogger logger = apiException.getLogger();
    	
    	if (logger != null) {
    		logger.log(String.format("%d :: %s", apiException.getErrorCode(), apiException.getMessage()));
    	}
    	
        return new ResponseEntity<>(new ApiErrorResponse(apiException.getErrorCode(), apiException.getMessage(), apiException.getMetaData()), apiException.getStatus());
    }


    @ExceptionHandler(value = {ApiExceptionExtended.class})
    public ResponseEntity<Object> handleApiExceptions(ApiExceptionExtended apiException) {
        return new ResponseEntity<>(new ApiErrorResponseExtended(apiException.getErrorCode(), apiException.getMessage(), apiException.getResult()),apiException.getStatus());
    }
 
    
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBadRequest(MethodArgumentNotValidException exception) {

        List<BadRequest> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new BadRequest(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new BadRequestResponse(400, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleBadRequest(MissingServletRequestParameterException exception) {
        final String parameterName = exception.getParameterName();
        return new ResponseEntity<>(new ApiErrorResponse(400,String.format("Required paramter '%s' missing",parameterName)), HttpStatus.BAD_REQUEST);
    }
}
