package com.kelloggs.promotions.lib.service.impl;


import com.kelloggs.promotions.lib.entity.Status;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.repository.StatusRepo;
import com.kelloggs.promotions.lib.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.INVALID_STATUS;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepo statusRepo;

    public StatusServiceImpl(StatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    @Override
    public Status getStatus(String type){
        return statusRepo.findByType(type)
                         .orElseThrow(()-> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, INVALID_STATUS.getCode(),"Invalid status set: "+ type));
    }
}
