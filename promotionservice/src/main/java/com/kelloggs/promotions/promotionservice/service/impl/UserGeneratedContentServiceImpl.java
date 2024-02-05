package com.kelloggs.promotions.promotionservice.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.constants.CodeConstants;
import com.kelloggs.promotions.lib.entity.Token;
import com.kelloggs.promotions.lib.entity.UserGeneratedContent;
import com.kelloggs.promotions.lib.entity.UserGeneratedEntry;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.CreateUGCRequest;
import com.kelloggs.promotions.lib.model.CreateUGCResponse;
import com.kelloggs.promotions.lib.model.GetUGCResponse;
import com.kelloggs.promotions.lib.model.CreateUGCStatusDTO;
import com.kelloggs.promotions.lib.repository.TokenRepo;import com.kelloggs.promotions.lib.repository.UserGeneratedContentRepo;
import com.kelloggs.promotions.lib.repository.UserGeneratedEntryRepo;
import com.kelloggs.promotions.promotionservice.service.UserGeneratedContentService;

/**
* Add UserGeneratedContentServiceImpl for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/

@Service
public class UserGeneratedContentServiceImpl implements UserGeneratedContentService {

    @Autowired
    private UserGeneratedContentRepo userGeneratedContentRepo;
    
    @Autowired
    private TokenRepo tokenRepo;
    
    @Autowired
    private UserGeneratedEntryRepo userGeneratedEntryRepo;

    @Override
    public ApiResponse<CreateUGCResponse> createUGC(@Valid CreateUGCRequest createUGCRequest) {
        CreateUGCResponse createUGCResponse = new CreateUGCResponse();
        UserGeneratedContent userGeneratedContent = new UserGeneratedContent();
        contentNameValidation(createUGCRequest.getName());
        if (createUGCRequest.getStart() == null
            || createUGCRequest.getEnd() == null
            || createUGCRequest.getStart().equals(null)
            || createUGCRequest.getEnd().equals(null)
            || createUGCRequest.getStart().isEmpty()
            || createUGCRequest.getEnd().isEmpty()
            || createUGCRequest.getStart().isBlank()
            || createUGCRequest.getEnd().isBlank() ) {
            throw new ApiException(HttpStatus.BAD_REQUEST, 400,
                    String.format("Content with StartDate and EndDate MUST NOT be null OR Empty"));
        }
        String requiredStartDateFromString = getRequiredDateFromString(createUGCRequest.getStart());
        String requiredEndDateFromString = getRequiredDateFromString(createUGCRequest.getEnd());
        final String df = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat(df);
        Date contentStartDateFormat = null;
		Date contentEndDateFormat = null;
		try {
			contentStartDateFormat = simpleDateFormate.parse(requiredStartDateFromString);
			contentEndDateFormat = simpleDateFormate.parse(requiredEndDateFromString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

         if (!(isDatePastTodayFuture(requiredStartDateFromString, df))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Content with Start Date \'%s\'  MUST NOT be past dates.", createUGCRequest.getStart()));
		} else if (!(isDatePastTodayFuture(requiredEndDateFromString, df))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Content with End Date \'%s\'  MUST NOT be past dates.", createUGCRequest.getEnd()));
		} else if (!(contentEndDateFormat.getTime() > contentStartDateFormat.getTime())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Content with End Date \'%s\' must be Grater Than Start Date \'%s\'", createUGCRequest.getEnd(),
                    createUGCRequest.getStart()));
		}

        LocalDateTime contentStartDateTime = contentStartDateFormat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime contentEndDateTime = contentEndDateFormat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(contentStartDateTime);
        System.out.println(contentEndDateTime);
        userGeneratedContent.setName(createUGCRequest.getName());
        userGeneratedContent.setStart(contentStartDateTime);
        userGeneratedContent.setEnd(contentEndDateTime);
        userGeneratedContent.setCreated(LocalDateTime.now());
        UserGeneratedContent savedContent = userGeneratedContentRepo.save(userGeneratedContent);
        createUGCResponse.setUgcId(savedContent.getId());
        createUGCResponse.setName(savedContent.getName());
        createUGCResponse.setStart(createUGCRequest.getStart());
        createUGCResponse.setEnd(createUGCRequest.getEnd());
        return new ApiResponse<CreateUGCResponse>("Created UGC is :--- ", createUGCResponse);
    }

    @Override
    public ApiResponse<GetUGCResponse> getUGC(@Valid Integer ugcId) {
        GetUGCResponse getUGCResponse = new GetUGCResponse();
        if(ugcId != 0){
            Optional<UserGeneratedContent> byUGCId = userGeneratedContentRepo.findById(ugcId);
            if(!byUGCId.isPresent()){
                throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                    "Content with UGCID \'%d\' is NOT exist.", ugcId));
            }else {
                getUGCResponse.setUgcId(byUGCId.get().getId());
                getUGCResponse.setName(byUGCId.get().getName());
                getUGCResponse.setStart(byUGCId.get().getStart().toString());
                getUGCResponse.setEnd(byUGCId.get().getEnd().toString());
                Optional<List<UserGeneratedEntry>> byUgcIdFromEntry = userGeneratedEntryRepo.findByUgcId(ugcId);
                if(byUgcIdFromEntry.isPresent()){
                    getUGCResponse.setCount(byUgcIdFromEntry.get().size());
                }else{
                    getUGCResponse.setCount(0);
                }
            }
        }else if(ugcId == 0 || ugcId.equals(null)){
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                "Content with UGCID is \'%d\' now. It must NOT be Zero OR null OR Empty.", ugcId));
        }
        return new ApiResponse<GetUGCResponse>("Get UGC is :--- ", getUGCResponse);
    }

    @Override
    public ApiListResponse<GetUGCResponse> getTopUGC(@Valid String date) {
        List<GetUGCResponse> getTopUGCResponseList = new ArrayList<>();
        if(date == null || date.equals(null) || date.isBlank() || date.isBlank()){
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                "Content with date is \'%s\' now. It must NOT be Zero OR null OR Empty.", date));
        }else if(!date.contains("T") || !date.contains("Z")){
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                "Content with date is \'%s\' now. It must be JSON date format like (yyyy-MM-ddTHH:mm:ss.SSSZ).", date));
        } else {
            LocalDateTime contentStartDateTime = LocalDateTime.ofInstant(Instant.parse(date), ZoneId.of(ZoneOffset.UTC.getId()));
            List<UserGeneratedContent> ListOFUserGeneratedContent = userGeneratedContentRepo.findAll();
            if(ListOFUserGeneratedContent.isEmpty()){
                throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                    "Content with date \'%s\' NO content Exist.", date));
            }else{
                for (UserGeneratedContent userGeneratedContent : ListOFUserGeneratedContent) {
                    long reqStartDateEpochMilli = contentStartDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    long startDateEpochMilli = userGeneratedContent.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    long endDateEpochMilli = userGeneratedContent.getEnd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                    LocalDateTime reqCvStartDate = Instant.ofEpochMilli(reqStartDateEpochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime cvStartDate = Instant.ofEpochMilli(startDateEpochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime cvEndDate = Instant.ofEpochMilli(endDateEpochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Range<Long> open = Range.open(startDateEpochMilli, endDateEpochMilli);
                    if(open.contains(reqStartDateEpochMilli) || reqStartDateEpochMilli == startDateEpochMilli || reqStartDateEpochMilli == endDateEpochMilli){
                        GetUGCResponse getUGCResponse = new GetUGCResponse();
                        getUGCResponse.setUgcId(userGeneratedContent.getId());
                        getUGCResponse.setName(userGeneratedContent.getName());
                        getUGCResponse.setStart(userGeneratedContent.getStart().toString());
                        getUGCResponse.setEnd(userGeneratedContent.getEnd().toString());
                        Optional<List<UserGeneratedEntry>> byUgcIdFromEntry = userGeneratedEntryRepo
                                .findByUgcId(userGeneratedContent.getId());
                        if (byUgcIdFromEntry.isPresent()) {
                            getUGCResponse.setCount(byUgcIdFromEntry.get().size());
                        } else {
                            getUGCResponse.setCount(0);
                        }
                        getTopUGCResponseList.add(getUGCResponse);
                    }
                }

            }

            }
        System.out.println(getTopUGCResponseList);
        if(getTopUGCResponseList.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
                "Content with date \'%s\' NO content Exist.", date));
        }
        return new ApiListResponse<GetUGCResponse>("Get Top UGC's are :--- ", getTopUGCResponseList, getTopUGCResponseList.size());
    }

    public static String getRequiredDateFromString(String str) {
		String[] ts = str.split("T");
		return ts[0] + " " + ts[1].substring(0, 8);
	}

    /**
	 * Add isDatePastTodayFuture for the UserGeneratedContentServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 30th January 2024
	 */
	public static boolean isDatePastTodayFuture(final String date, final String dateFormat) {
		boolean flag = false;
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate inputDate = LocalDate.parse(date, formatter);

		if (inputDate.isBefore(localDate)) {
			flag = false;
		} else if (inputDate.isEqual(localDate) || inputDate.isAfter(localDate)) {
			flag = true;
		}
		return flag;
	}

    /**
	 * Add contentNameValidation for the UserGeneratedContentServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 30th January 2024
	 */
	public void contentNameValidation(String contentName) {
		if (contentName == null
				|| contentName.equals(null)
				|| contentName.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"Content with Name is \'%s\' now. It Should NOT be null or Empty ", contentName));
		} else if (!contentName.isEmpty()
				|| !contentName.equals(null)
				|| !contentName.isBlank()) {
			Optional<UserGeneratedContent> findByName = userGeneratedContentRepo.findByName(contentName);
			if (findByName.isPresent()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Content with Name \'%s\' is already Exists with ContentId \'%d\'", contentName,
								findByName.get().getId()));
			}
		}
	}
	
	/**
	 * setUGCStatus method will validate the record first else throw the error
	 * for status true it will add new entry in ugc entry table else throw error for duplicate
	 * for status false it will delete the entry from ugc entry table else throw error if entry missing
	 * 
	 */
	@Override
	public CreateUGCStatusDTO setUGCStatus(CreateUGCStatusDTO createUGCStatusDto) {
		String userToken=createUGCStatusDto.getUserToken();
		Integer ugcId=createUGCStatusDto.getUgcId();
		String status=createUGCStatusDto.getStatus();
		Optional<Token> optionalToken=tokenRepo.findByHashCode(userToken);
		Integer profileId=null;
		if(optionalToken.isPresent()) {
			Token dbToken=optionalToken.get();
			profileId=dbToken.getProfileId();
		}else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"User Token %s is not present in database",userToken));
		}
		Optional<UserGeneratedContent> optionalUGC=userGeneratedContentRepo.findById(ugcId);
		if(optionalUGC.isPresent()) {
			Optional<UserGeneratedEntry> optionalUGE=userGeneratedEntryRepo.findByUgcIdAndUserId(ugcId,profileId);
			if(status.equalsIgnoreCase(CodeConstants.TRUE.getStatus())) {
				if(optionalUGE.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"UGC Id %d and User Id  is already present in database",ugcId));
				}else {
					userGeneratedEntryRepo.save(getUserGeneratedEntry(ugcId,profileId));
				}
			}else if(status.equalsIgnoreCase(CodeConstants.FALSE.getStatus())) {
				if(optionalUGE.isPresent()) {
					UserGeneratedEntry uge=optionalUGE.get();
					userGeneratedEntryRepo.deleteById(uge.getId());
				}else {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"UGC Id : %d  and User Id  does not exists in database",ugcId));
				}
			}
			
		}else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"UGC Id : %d is not present in database", ugcId));
		}
		return getCreateUGCStatusDTO(ugcId,status);
	}
	
	UserGeneratedEntry getUserGeneratedEntry(Integer ugcId,Integer userId) {
		UserGeneratedEntry uge=new UserGeneratedEntry();
		uge.setUgcId(ugcId);
		uge.setUserId(userId);
		return uge;
	}
	
	CreateUGCStatusDTO getCreateUGCStatusDTO(Integer ugcId,String status) {
		CreateUGCStatusDTO dto=new CreateUGCStatusDTO();
		dto.setUgcId(ugcId);
		dto.setStatus(status);
		return dto;
	}

	/**
	 * getUGCStatus method will validate the record first else throw the error
	 * If record is present in ugc entry table then it will update the status as true,
	 * else it will update the status as false
	 * 
	 */
	@Override
	public CreateUGCStatusDTO getUGCStatus(Integer ugcId,String userToken) {
		String status="";
		Optional<Token> optionalToken=tokenRepo.findByHashCode(userToken);
		Integer profileId=null;
		if(optionalToken.isPresent()) {
			Token dbToken=optionalToken.get();
			profileId=dbToken.getProfileId();
		}else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"User Token : %s is not present in database",userToken));
		}
		Optional<UserGeneratedContent> optionalUGC=userGeneratedContentRepo.findById(ugcId);
		if (optionalUGC.isPresent()) {
			Optional<UserGeneratedEntry> optionalUGE = userGeneratedEntryRepo.findByUgcIdAndUserId(ugcId, profileId);
			if (optionalUGE.isPresent()) {
				status = CodeConstants.TRUE.getStatus();
			} else {
				status = CodeConstants.FALSE.getStatus();
			}

		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("UGC Id : %d is not present in database", ugcId));
		}
		return getCreateUGCStatusDTO(ugcId,status);
	}
	
	
	/**
     * Get the profile Id using the token
     * 
     * @param userToken	Generated hash code to be pass
     * @return	Return profile Id details if validated, otherwise throw exception
     * 
     * 
     */
	@Override
	public Map<String, Integer> getProfileId(String userToken) {
		Map<String, Integer> entryDetails =null;
		Optional<Token> optionalToken=tokenRepo.findByHashCode(userToken);
		if(optionalToken.isPresent()) {
			Token dbToken=optionalToken.get();
			entryDetails=new HashMap<String,Integer>();
			entryDetails.put("profileId", dbToken.getProfileId());
		}else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"User Token :: %s is not present in database",userToken));
		}
		
		return entryDetails;
	}
}
