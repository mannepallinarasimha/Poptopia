package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.constants.CodeConstants;
import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionCluster;
import com.kelloggs.promotions.lib.entity.Region;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.DeletePromotionRequest;
import com.kelloggs.promotions.lib.model.DeletePromotionResponse;
import com.kelloggs.promotions.lib.model.MechanicRequest;
import com.kelloggs.promotions.lib.model.PromotionCreateRequest;
import com.kelloggs.promotions.lib.model.PromotionIdsRequest;
import com.kelloggs.promotions.lib.model.PromotionRequest;
import com.kelloggs.promotions.lib.model.PromotionRequestforUpdate;
import com.kelloggs.promotions.lib.model.PromotionResponse;
import com.kelloggs.promotions.lib.model.PromotionSetting;
import com.kelloggs.promotions.lib.model.PromotionUpdateRequest;
import com.kelloggs.promotions.lib.repository.PromotionClusterRepo;
import com.kelloggs.promotions.lib.repository.PromotionRepo;
import com.kelloggs.promotions.lib.repository.RegionRepo;
import com.kelloggs.promotions.lib.repository.WinnerConfigRepo;
import com.kelloggs.promotions.promotionservice.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	private final PromotionRepo promotionRepo;

	public PromotionServiceImpl(PromotionRepo promotionRepo) {
		this.promotionRepo = promotionRepo;
	}

	@Autowired
	private RegionRepo regionRepo;

	@Autowired
	private WinnerConfigRepo winnerConfigRepo;

	@Autowired
	private PromotionClusterRepo promotionClusterRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ApiListResponse<Promotion> getPromotions() {
		List<Promotion> promotions = promotionRepo.findAll();
		return new ApiListResponse<>("All promotions :--- ", promotions, promotions.size());
	}

	@Override
	public ApiResponse<Promotion> getPromotion(Integer promotionId) {
		Promotion promotion = getPromotionById(promotionId);
		return new ApiResponse<>("Promotion with id :-- " + promotionId, promotion);
	}

	@Override
	public Promotion getPromotionById(Integer promotionId) {
		return promotionRepo.findById(promotionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
				NOT_FOUND.getCode(), String.format("Promotion with id %d not found", promotionId)));

	}

	@Override
	public List<Promotion> getPromotionsByClusterId(Integer clusterId) {
		return promotionRepo.findByPromotionClusterId(clusterId)
				.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Promotions with cluster id %d not found", clusterId)));

	}

	@Override
	public ApiResponse<Promotion> addPromotion(Promotion promotion) {
		Promotion savedPromotion = promotionRepo.save(promotion);
		return new ApiResponse<>("Promotion created with id : " + savedPromotion.getId(), savedPromotion);
	}

	/**
	 * Add createPromotion for the particular PromotionIds
	 * 
	 * @param promotionCreateRequest
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 3rd January 2024
	 */
	@Override
	public ApiListResponse<PromotionResponse> createPromotion(PromotionCreateRequest promotionCreateRequest) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		if (promotionCreateRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Empty ", promotionResponseList));
		}
		List<PromotionRequest> promotions = promotionCreateRequest.getPromotions();
		for (PromotionRequest promotionRequest : promotions) {
			if(promotionRequest.getRegionId() == null 
			   || promotionRequest.getRegionId() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("RegionId \'%d\' Must NOT be Zero OR null OR EMPTY OR Blank", promotionRequest.getRegionId()));
			}else {
				regionRepo.findById(promotionRequest.getRegionId())
				.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Region with Region id \'%d\' not found", promotionRequest.getRegionId())));
			}

			if (promotionRequest.getModuleKey()== null 
				|| promotionRequest.getModuleKey().equals(null) 
				|| promotionRequest.getModuleKey().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with ModuleKey \'%s\' Should NOT be null or Empty ", promotionRequest.getModuleKey()));
			} else if (!promotionRequest.getModuleKey().isEmpty() 
					|| !promotionRequest.getModuleKey().equals(null)
					|| !promotionRequest.getModuleKey().isBlank()) {
				Optional<Promotion> findByModuleKey = promotionRepo.findByModuleKey(promotionRequest.getModuleKey());
				if (findByModuleKey.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with ModuleKey \'%s\' is already Exists with PromotionId \'%d\'", promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
				}
			}
			
			if (promotionRequest.getPromotionName()== null 
					|| promotionRequest.getPromotionName().equals(null) 
					|| promotionRequest.getPromotionName().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with Name \'%s\' Should NOT be null or Empty ", promotionRequest.getPromotionName()));
			} else if (!promotionRequest.getPromotionName().isEmpty()
					|| !promotionRequest.getPromotionName().equals(null)
					|| !promotionRequest.getPromotionName().isBlank()) {
				Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
				if (findByName.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with Name \'%s\' is already Exists with PromotionId \'%d\'", promotionRequest.getPromotionName(), findByName.get().getId()));
				}
			}
			
			if (promotionRequest.getEpsilonId() == null 
					|| promotionRequest.getEpsilonId().equals(null) 
					|| promotionRequest.getEpsilonId() == 0 ) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with EpsilonId \'%d\' Should NOT be Zero or null or Empty ", promotionRequest.getEpsilonId()));
			} else if (!promotionRequest.getEpsilonId().equals(null) 
					|| promotionRequest.getEpsilonId() != 0) {
				Optional<Promotion> findByEpsilonId = promotionRepo.findByEpsilonId(promotionRequest.getEpsilonId());
				if (findByEpsilonId.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with EpsilonId \'%d\' is already Exists with PromotionId \'%d\' ", promotionRequest.getEpsilonId(), findByEpsilonId.get().getId()));
				}
			}
			
		}
		MechanicRequest mechanic = promotionCreateRequest.getMechanic();
		if(mechanic.getType() == null 
				|| mechanic.getType().isBlank() 
				|| mechanic.getType().isBlank()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type MUST NOT be null OR Empty"));	
		} else if(mechanic.getEndDate() == null 
				|| mechanic.getStartDate() == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with StartDate and EndDate MUST NOT be null OR Empty"));
		}else if ((mechanic.getStartDate().before(new Date()) && mechanic.getEndDate().before(new Date()))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with Start Date \'%s\' and  End Date \'%s\' MUST NOT be past dates.", mechanic.getStartDate(), mechanic.getEndDate()));
		}else if (!(mechanic.getEndDate().getTime() > mechanic.getStartDate().getTime())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with End Date \'%s\' must be Grater Than Start Date \'%s\'", mechanic.getEndDate(), mechanic.getStartDate()));
		}else if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())
				|| mechanic.getType().equalsIgnoreCase(CodeConstants.TOS.getStatus())
				|| mechanic.getType().equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
							mechanic.getType()));
		} 
		String settingsValue = "";
		List<PromotionSetting> settings = promotionCreateRequest.getSettings();
		for (PromotionSetting promotionSetting : settings) {
			if(promotionSetting.getName() == null || promotionSetting.getValue() == null
				||	promotionSetting.getName().isEmpty() || promotionSetting.getName().isBlank()
				||	promotionSetting.getValue().isEmpty() || promotionSetting.getValue().isBlank()	
				||	promotionSetting.getValue().equals("0") || promotionSetting.getValue().equals(null)	
					) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Setting value is \'%s\' now. It should NOT be Zero OR NULL OR EMPTY OR BLANK",
								promotionSetting.getValue()));		
			}
			settingsValue = promotionSetting.getValue();
		}
		Set<Promotion> setPromotions = new HashSet<>();
		List<Promotion> promotionList = new ArrayList<>();
		Promotion savedPromotion = new Promotion();
		Date date = new Date();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		for (PromotionRequest promotionRequest : promotions) {
			Promotion promotion = new Promotion();
			Region region = regionRepo.findById(promotionRequest.getRegionId())
					.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Region with Region id %d not found", promotionRequest.getRegionId())));
			if (promotionRequest.getModuleKey() == null 
					|| promotionRequest.getModuleKey().equals(null) 
					|| promotionRequest.getModuleKey().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with ModuleKey \'%s\' Should NOT be null or Empty ", promotionRequest.getModuleKey()));
			} else if (!promotionRequest.getModuleKey().isEmpty() 
					|| !promotionRequest.getModuleKey().equals(null)
					|| !promotionRequest.getModuleKey().isBlank()) {
				Optional<Promotion> findByModuleKey = promotionRepo.findByModuleKey(promotionRequest.getModuleKey());
				if (findByModuleKey.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with ModuleKey \'%s\' is already Exists with promotionId \'%d\'", promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
				} else {
					promotion.setModuleKey(promotionRequest.getModuleKey());
				}
			}
			if (promotionRequest.getPromotionName() == null 
					|| promotionRequest.getPromotionName().equals(null) 
					|| promotionRequest.getPromotionName().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with Name \'%s\' Should NOT be null or Empty ", promotionRequest.getPromotionName()));
			} else if (!promotionRequest.getPromotionName().isEmpty()
					|| !promotionRequest.getPromotionName().equals(null)
					|| !promotionRequest.getPromotionName().isBlank()) {
				Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
				if (findByName.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with Name %s is already Exists with promotionId \'%d\'", promotionRequest.getPromotionName(), findByName.get().getId()));
				} else {
					promotion.setName(promotionRequest.getPromotionName());
				}
			}
			if (promotionRequest.getEpsilonId() == null 
					|| promotionRequest.getEpsilonId().equals(null) 
					|| promotionRequest.getEpsilonId() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with EpsilonId \'%d\' Should NOT be null or Empty ", promotionRequest.getEpsilonId()));
			} else if (!promotionRequest.getEpsilonId().equals(null) 
					|| promotionRequest.getEpsilonId() != 0) {
				Optional<Promotion> findByEpsilonId = promotionRepo.findByEpsilonId(promotionRequest.getEpsilonId());
				if (findByEpsilonId.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with EpsilonId \'%d\' is already Exists with PromotionId \'%d\'", promotionRequest.getEpsilonId(), findByEpsilonId.get().getId()));
				} else {
					promotion.setEpsilonId(promotionRequest.getEpsilonId());
				}
			}
			promotion.setRegion(region);
			promotion.setLocalTimeZone(promotionRequest.getLocalTimeZone());
			
			if(mechanic.getType() == null 
					|| mechanic.getType().isBlank() 
					|| mechanic.getType().isBlank()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Mechanic with Type MUST NOT be null OR Empty"));	
			} else if(mechanic.getEndDate() == null 
					|| mechanic.getStartDate() == null) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Mechanic with StartDate and EndDate MUST NOT be null OR Empty"));
			}else if (!(mechanic.getEndDate().getTime() > mechanic.getStartDate().getTime())) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Mechanic with End Date \'%s\' must be Grater Than Start Date \'%s\'", mechanic.getEndDate(), mechanic.getStartDate()));
			} else if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())
					|| mechanic.getType().equalsIgnoreCase(CodeConstants.TOS.getStatus())
					|| mechanic.getType().equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
				promotion.setStartDate(mechanic.getStartDate());
			} else {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
								mechanic.getType()));
			}
			promotion.setEndDate(mechanic.getEndDate());
			promotion.setAttr1_code(mechanic.getAttr1_code());
			promotion.setAttr1_value(mechanic.getAttr1_value());
			promotion.setCreatedDate(date);
			promotion.setModifiedDate(date);
			promotion.setMaxLimit(1);
			promotion.setCreatedDateTime(localDateTime);
			promotion.setModifiedDateTime(localDateTime);
			Optional<PromotionCluster> findById = promotionClusterRepo.findById(promotionCreateRequest.getClusterId());
			if (findById.isPresent()) {
				promotion.setPromotionCluster(findById.get());
			} else {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Cluster with ClusterId %d is NOT exists. ", promotionCreateRequest.getClusterId()));
			}
			 savedPromotion = promotionRepo.save(promotion);
			
			promotionList.add(savedPromotion);
			setPromotions.add(savedPromotion);
		}

		if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())) {
			wmStartDateCalculation(mechanic.getStartDate(), mechanic.getEndDate(), setPromotions, settingsValue);
		}
		promotionResponseList = promotionResponseSetUp(promotionList);

		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public void wmStartDateCalculation(Date mechanicStartDate, Date mechanicEndDate, Set<Promotion> setPromotions, String settingsValue) {

		long startDateMillis = mechanicStartDate. getTime();
		System.out.println(startDateMillis);
		long endDateMillis = mechanicEndDate. getTime();
		System.out.println(endDateMillis);
		int periodTime = (int) ((endDateMillis - startDateMillis)
				/ Integer.parseInt(settingsValue));
		for (int i=0;i<Integer.parseInt(settingsValue);i++) {
			int randomNumber=getRandomNumber(1,periodTime);
			long prizetime=startDateMillis+randomNumber;
            Date prizeTimeDate = new Date(prizetime);
            long endtime = endDateMillis;
            Date resend = new Date(endtime);
            startDateMillis=startDateMillis+periodTime;
			WinnerConfig winnerCongig = winnerConfigSetUp(setPromotions, prizeTimeDate, mechanicEndDate);
			winnerConfigRepo.save(winnerCongig);
		}

	}

	/**
	 * Add updatePromotion for the particular PromotionIds
	 * 
	 * @param promotionUpdateRequest
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 3rd January 2024
	 */
	@Override
	public ApiListResponse<PromotionResponse> updatePromotion(@Valid PromotionUpdateRequest promotionUpdateRequest) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		List<Integer> promotionIdsList = new ArrayList<>();
		List<PromotionSetting> settings = promotionUpdateRequest.getSettings();
		MechanicRequest mechanic = promotionUpdateRequest.getMechanic();
		String settingsValue = "";
		if (promotionUpdateRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Emplty ", promotionResponseList));
		}
		for (PromotionSetting promotionSetting : settings) {
			settingsValue = promotionSetting.getValue();
		}
		List<PromotionRequestforUpdate> promotionRequestList = promotionUpdateRequest.getPromotions();
		for (PromotionRequestforUpdate promotionRequest : promotionRequestList) {

			promotionIdsList.add(promotionRequest.getPromotionId());
			if (promotionRequest.getPromotionId() != null) {
				if (promotionRequest.getRegionId() == null || promotionRequest.getRegionId() == 0) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("RegionId is \'%d\' now. It Must NOT be Zero OR null OR EMPTY OR Blank",
									promotionRequest.getRegionId()));
				} else {
					regionRepo.findById(promotionRequest.getRegionId()).orElseThrow(() -> new ApiException(
							HttpStatus.BAD_REQUEST, 400,
							String.format("Region with Region id \'%d\' not found", promotionRequest.getRegionId())));
				}
				Optional<Promotion> findById = promotionRepo.findById(promotionRequest.getPromotionId());
				if (findById.isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with PromotionId  \'%d\' is NOT exist .", promotionRequest.getPromotionId()));
				}
				if (findById.isPresent() || !findById.isPresent()) {
					if (!findById.get().getPromotionCluster().getClusterId()
							.equals(promotionUpdateRequest.getClusterId())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with PromotionId  \'%d\' is NOT assosciated with clusterId \'%d\' . ",
										findById.get().getId(), promotionUpdateRequest.getClusterId()));
					} else if (promotionRequest.getSweepStake() == null
							|| promotionRequest.getSweepStake().equals(null)
							|| promotionRequest.getSweepStake().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with SweepStack \'%s\' Should NOT be null or Empty ",
										promotionRequest.getSweepStake()));
					} else if (!promotionRequest.getSweepStake().isEmpty()
							|| !promotionRequest.getSweepStake().equals(null)
							|| !promotionRequest.getSweepStake().isBlank()) {
						Optional<Promotion> findByEpsilonId = promotionRepo
								.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
						if (findByEpsilonId.isPresent()
								&& !(findByEpsilonId.get().getId().equals(promotionRequest.getPromotionId()))) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
									"Promotion with SweepStack EpsilonId \'%d\' is already Exists with promotionId \'%d\' ",
									Integer.parseInt(promotionRequest.getSweepStake()), findByEpsilonId.get().getId()));
						}
					}

					if (promotionRequest.getModuleKey() == null 
						|| promotionRequest.getModuleKey().equals(null) 
						|| promotionRequest.getModuleKey().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with ModuleKey \'%s\' Should NOT be null or Empty ",
										promotionRequest.getModuleKey()));
					} else if (!promotionRequest.getModuleKey().isEmpty()
							|| !promotionRequest.getModuleKey().equals(null)
							|| !promotionRequest.getModuleKey().isBlank()) {
						Optional<Promotion> findByModuleKey = promotionRepo
								.findByModuleKey(promotionRequest.getModuleKey());
						if (findByModuleKey.isPresent()
								&& !(findByModuleKey.get().getId().equals(promotionRequest.getPromotionId()))) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400,
									String.format("Promotion with ModuleKey \'%s\' is already Exists with promotionId \'%d\' ",
											promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
						}
					}

					if (promotionRequest.getPromotionName() == null
						|| promotionRequest.getPromotionName().equals(null)
						|| promotionRequest.getPromotionName().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with Name \'%s\' Should NOT be null or Empty ",
										promotionRequest.getPromotionName()));
					} else if (!promotionRequest.getPromotionName().isEmpty()
							|| !promotionRequest.getPromotionName().equals(null)
							|| !promotionRequest.getPromotionName().isBlank()) {
						Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
						if (findByName.isPresent()
								&& !(findByName.get().getId().equals(promotionRequest.getPromotionId()))) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400,
									String.format("Promotion with Name \'%s\' is already Exists with promotionId \'%d\' ",
											promotionRequest.getPromotionName(), findByName.get().getId()));
						}
					}
				}
			} else {
				if (promotionRequest.getSweepStake()== null 
					|| promotionRequest.getSweepStake().equals(null) 
					|| promotionRequest.getSweepStake().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Promotion with SweepStack \'%s\' Should NOT be null or Empty ",
									promotionRequest.getSweepStake()));
				} else if (!promotionRequest.getSweepStake().isEmpty() || !promotionRequest.getSweepStake().equals(null)
						|| !promotionRequest.getSweepStake().isBlank()) {
					Optional<Promotion> findByEpsilonId = promotionRepo
							.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					if (findByEpsilonId.isPresent()
							&& !(findByEpsilonId.get().getId().equals(promotionRequest.getPromotionId()))) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with SweepStack EpsilonId \'%d\' is already Exists with promotionId \'%d\' ",
								Integer.parseInt(promotionRequest.getSweepStake()), findByEpsilonId.get().getId()));
					}
				}

				if (promotionRequest.getModuleKey()== null 
					|| promotionRequest.getModuleKey().equals(null) 
					|| promotionRequest.getModuleKey().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with ModuleKey \'%s\' Should NOT be null or Empty ", promotionRequest.getModuleKey()));
				} else if (!promotionRequest.getModuleKey().isEmpty() 
						|| !promotionRequest.getModuleKey().equals(null)
						|| !promotionRequest.getModuleKey().isBlank()) {
					Optional<Promotion> findByModuleKey = promotionRepo
							.findByModuleKey(promotionRequest.getModuleKey());
					if (findByModuleKey.isPresent()
						&& !(findByModuleKey.get().getId().equals(promotionRequest.getPromotionId()))) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with ModuleKey \'%s\' is already Exists with promotionId %d ",
										promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
					}
				}

				if (promotionRequest.getPromotionName() == null 
					|| promotionRequest.getPromotionName().equals(null) 
					|| promotionRequest.getPromotionName().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with Name \'%s\' Should NOT be null or Empty ", promotionRequest.getPromotionName()));
				} else if (!promotionRequest.getPromotionName().isEmpty()
						|| !promotionRequest.getPromotionName().equals(null)
						|| !promotionRequest.getPromotionName().isBlank()) {
					Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
					if (findByName.isPresent()
							&& !(findByName.get().getId().equals(promotionRequest.getPromotionId()))) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with Name \'%s\' is already Exists with promotionId \'%d\' ",
										promotionRequest.getPromotionName(), findByName.get().getId()));
					}
				}
			}

		}
		if(mechanic.getType() == null || mechanic.getType().isBlank() || mechanic.getType().isBlank()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type MUST NOT be null OR Empty"));	
		} else if(mechanic.getEndDate() == null || mechanic.getStartDate() == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with StartDate and EndDate MUST NOT be null OR Empty"));
		}else if (!(mechanic.getEndDate().getTime() > mechanic.getStartDate().getTime())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with End Date \'%s\' must be Grater Than Start Date \'%s\'", mechanic.getEndDate(), mechanic.getStartDate()));
		}else if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())
				|| mechanic.getType().equalsIgnoreCase(CodeConstants.TOS.getStatus())
				|| mechanic.getType().equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
							mechanic.getType()));
		}
		for (PromotionSetting promotionSetting : settings) {
			if(promotionSetting.getName() == null || promotionSetting.getValue() == null
				||	promotionSetting.getName().isEmpty() || promotionSetting.getName().isBlank()
				||	promotionSetting.getValue().isEmpty() || promotionSetting.getValue().isBlank()	
				||	promotionSetting.getValue().equals("0") || promotionSetting.getValue().equals(null)	
					) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Setting value is \'%s\' now. It should NOT be Zero OR NULL OR EMPTY OR BLANK",
								settingsValue));		
			}
			settingsValue = promotionSetting.getValue();
		}
		List<Promotion> findAllById = new ArrayList<>();
		if (!promotionIdsList.equals(null)) {
			findAllById = promotionRepo.findAllById(promotionIdsList);
			if (!findAllById.isEmpty()) {
				for (Promotion promotion : findAllById) {
					Query query = entityManager.createNativeQuery(
							"select config_id from winner_selection_config_reference where promotion_id="
									+ promotion.getId());
					List<Integer> resultList = (List<Integer>) query.getResultList();

//					try {
//						Query query1 = entityManager.createNativeQuery(
//								"DELETE FROM winner_selection_config_reference where promotion_id="
//										+ promotion.getId());
//						query1.getResultList();
//					}catch(Exception ex) {
//						ex.printStackTrace();
//					}
					
					if (!resultList.isEmpty()) {
						for (Integer configId : resultList) {
							winnerConfigRepo.deleteById(configId);
						}
					}
				}
			}

		}
		Set<Promotion> setPromotions = new HashSet<>();
		List<Promotion> promotionList = new ArrayList<>();
		Promotion savedPromotion = new Promotion();
		for (PromotionRequestforUpdate promotionRequest : promotionRequestList) {

			if (promotionRequest.getPromotionId() != null) {
				Optional<Promotion> findById = promotionRepo.findById(promotionRequest.getPromotionId());
				if (findById.isPresent()) {
					Promotion promotionFromDB = findById.get();
					if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())
							|| mechanic.getType().equalsIgnoreCase(CodeConstants.TOS.getStatus())
							|| mechanic.getType().equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
						promotionFromDB.setStartDate(mechanic.getStartDate());
					} else {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format(
										"Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
										mechanic.getType()));
					}

					Optional<PromotionCluster> promotionClusterDetails = promotionClusterRepo
							.findById(promotionUpdateRequest.getClusterId());
					promotionFromDB.setPromotionCluster(promotionClusterDetails.get());
					Region region = regionRepo.findById(promotionRequest.getRegionId())
							.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400, String
									.format("Region with Region id %d not found", promotionRequest.getRegionId())));
					promotionFromDB.setRegion(region);
					promotionFromDB.setCreatedDate(new Date());
					promotionFromDB.setModifiedDate(new Date());
					promotionFromDB.setEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					promotionFromDB.setName(promotionRequest.getPromotionName());
					promotionFromDB.setModuleKey(promotionRequest.getModuleKey());
					promotionFromDB.setEndDate(mechanic.getEndDate());
					promotionFromDB.setMaxLimit(1);
					promotionFromDB.setLocalTimeZone(promotionRequest.getLocalTimeZone());
					promotionFromDB.setAttr1_code(mechanic.getAttr1_code());
					promotionFromDB.setAttr1_value(mechanic.getAttr1_value());

					savedPromotion = promotionRepo.save(promotionFromDB);
					setPromotions.add(savedPromotion);
					promotionList.add(savedPromotion);
				}
			} else {
				Promotion newPromotion = new Promotion();
				if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())
						|| mechanic.getType().equalsIgnoreCase(CodeConstants.TOS.getStatus())
						|| mechanic.getType().equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
					newPromotion.setStartDate(mechanic.getStartDate());
				} else {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format(
									"Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
									mechanic.getType()));
				}

				if (promotionRequest.getSweepStake().equals(null) || promotionRequest.getSweepStake().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Promotion with SweepStack Should NOT be null or Empty ",
									promotionRequest.getSweepStake()));
				} else if (!promotionRequest.getSweepStake().isEmpty() || !promotionRequest.getSweepStake().equals(null)
						|| !promotionRequest.getSweepStake().isBlank()) {
					Optional<Promotion> findByEpsilonId = promotionRepo
							.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					if (findByEpsilonId.isPresent()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with SweepStack EpsilonId %d is already Exists ",
										Integer.parseInt(promotionRequest.getSweepStake())));
					} else {
						newPromotion.setEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					}
				}

				if (promotionRequest.getModuleKey().equals(null) || promotionRequest.getModuleKey().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with ModuleKey Should NOT be null or Empty ", promotionRequest.getModuleKey()));
				} else if (!promotionRequest.getModuleKey().isEmpty() || !promotionRequest.getModuleKey().equals(null)
						|| !promotionRequest.getModuleKey().isBlank()) {
					Optional<Promotion> findByModuleKey = promotionRepo
							.findByModuleKey(promotionRequest.getModuleKey());
					if (findByModuleKey.isPresent()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with ModuleKey %s is already Exists ", promotionRequest.getModuleKey()));
					} else {
						newPromotion.setModuleKey(promotionRequest.getModuleKey());
					}
				}

				if (promotionRequest.getPromotionName().equals(null) || promotionRequest.getPromotionName().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with Name Should NOT be null or Empty ", promotionRequest.getPromotionName()));
				} else if (!promotionRequest.getPromotionName().isEmpty()
						|| !promotionRequest.getPromotionName().equals(null)
						|| !promotionRequest.getPromotionName().isBlank()) {
					Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
					if (findByName.isPresent()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with Name %s is already Exists ", promotionRequest.getPromotionName()));
					} else {
						newPromotion.setName(promotionRequest.getPromotionName());
					}
				}

				Optional<PromotionCluster> promotionClusterDetails = promotionClusterRepo
						.findById(promotionUpdateRequest.getClusterId());
				newPromotion.setPromotionCluster(promotionClusterDetails.get());
				Region region = regionRepo.findById(promotionRequest.getRegionId())
						.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Region with Region id %d not found", promotionRequest.getRegionId())));
				newPromotion.setRegion(region);
				newPromotion.setCreatedDate(new Date());
				newPromotion.setModifiedDate(new Date());
				newPromotion.setEndDate(mechanic.getEndDate());
				newPromotion.setMaxLimit(1);
				newPromotion.setLocalTimeZone(promotionRequest.getLocalTimeZone());
				newPromotion.setAttr1_code(mechanic.getAttr1_code());
				newPromotion.setAttr1_value(mechanic.getAttr1_value());
				savedPromotion = promotionRepo.save(newPromotion);
				
				setPromotions.add(savedPromotion);
				promotionList.add(savedPromotion);
				}
		}
		if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())) {
			wmStartDateCalculation(mechanic.getStartDate(), mechanic.getEndDate(), setPromotions, settingsValue);
	}
		promotionResponseList = promotionResponseSetUp(promotionList);
		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
	}

	/**
	 * Add promotionResponseSetUp for the PromotionServiceInpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 5th January 2024
	 */	
	public List<PromotionResponse> promotionResponseSetUp(List<Promotion> savedPromotions) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		
		for (Promotion savedPromotion : savedPromotions) {
			PromotionResponse promotionResponse = new PromotionResponse();
			Date date = new Date();
			promotionResponse.setId(savedPromotion.getId());
			promotionResponse.setCreateDate(date);
			promotionResponse.setModifiedDate(date);
			promotionResponse.setName(savedPromotion.getName());
			promotionResponse.setEpsilonId(savedPromotion.getEpsilonId());
			promotionResponse.setStartDate(savedPromotion.getStartDate());
			promotionResponse.setEndDate(savedPromotion.getEndDate());
			promotionResponse.setMaxLimit(1);
			promotionResponse.setModuleKey(savedPromotion.getModuleKey());
			promotionResponse.setLocalTimeZone(savedPromotion.getLocalTimeZone());
			promotionResponse.setAttr1_code(savedPromotion.getAttr1_code());
			promotionResponse.setAttr1_value(savedPromotion.getAttr1_value());
			Query query = entityManager.createNativeQuery(
					" SELECT COUNT(w.config_id) FROM Winner_Selection_Config_Reference w WHERE w.promotion_id ="
							+ savedPromotion.getId());
			List<Integer> resultList = (List<Integer>) query.getResultList();
			if (!resultList.isEmpty()) {
				promotionResponse.setWinnerconfig(resultList.get(0));
			}
			promotionResponse.setRegion(savedPromotion.getRegion());
			promotionResponseList.add(promotionResponse);
		}
		
		return promotionResponseList;
	}
	

	/**
	 * Add winnerCongigSetUp for the PromotionServiceInpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 5th January 2024
	 */
	public WinnerConfig winnerConfigSetUp(Set<Promotion> setPromotions, Date dateOfPromotion, Date endDate) {
//		Date date = new Date();
		LocalDateTime winnerConfigStartDateTime = dateOfPromotion.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime winnerConfigEndDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		WinnerConfig winnerConfig = new WinnerConfig();
		winnerConfig.setMaxWinner(0);
		winnerConfig.setLimit(1);
		winnerConfig.setWinProbability(0);
		winnerConfig.setWinStep(0);
		winnerConfig.setEndTime(winnerConfigEndDateTime);
		winnerConfig.setStartTime(winnerConfigStartDateTime);
		winnerConfig.setPromotion(setPromotions);
		return winnerConfig;
	}

	/**
	 * Add deletePromotion for the particular PromotionIds
	 * 
	 * @param deletePromotionRequest
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 3rd January 2024
	 */
	@Override
	public ApiListResponse<DeletePromotionResponse> deletePromotion(
			@Valid DeletePromotionRequest deletePromotionRequest) {
		List<DeletePromotionResponse> deletePromotionResponseList = new ArrayList<>();
		List<PromotionIdsRequest> promotions = deletePromotionRequest.getPromotions();

		if (deletePromotionRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Emplty ", deletePromotionResponseList));
		} else {
			for (PromotionIdsRequest promotionIdsRequest : promotions) {
				// check All the promotions are there in Table or NOT
				Optional<Promotion> findById = promotionRepo.findById(promotionIdsRequest.getPromotionId());
				if (!findById.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with PromotionId  %d is NOT Exist. ", promotionIdsRequest.getPromotionId()));
				}
			}
		}

		for (PromotionIdsRequest promotionIdsRequest : promotions) {
			DeletePromotionResponse deletePromotionResponse = new DeletePromotionResponse();
			if (promotionIdsRequest.getPromotionId() != null) {
				Optional<Promotion> findById = promotionRepo.findById(promotionIdsRequest.getPromotionId());
				if (findById.isPresent()) {
					if (!findById.get().getPromotionCluster().getClusterId()
							.equals(deletePromotionRequest.getClusterId())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with PromotionId  %d is NOT assosciated with clusterId %d . ",
										findById.get().getId(), deletePromotionRequest.getClusterId()));
					} else {
						// Logic to get ConfigIds from winner_selection_config_reference table
						Query query = entityManager.createNativeQuery(
								"select config_id from winner_selection_config_reference where promotion_id="
										+ promotionIdsRequest.getPromotionId());
						List<Integer> resultList = (List<Integer>) query.getResultList();
						if (!resultList.isEmpty()) {
							for (Integer configId : resultList) {
								deletePromotionResponse.setPromotionId(promotionIdsRequest.getPromotionId());
								winnerConfigRepo.deleteById(configId);
								promotionRepo.deleteById(promotionIdsRequest.getPromotionId());
							}
						} else {
							deletePromotionResponse.setPromotionId(promotionIdsRequest.getPromotionId());
							promotionRepo.deleteById(promotionIdsRequest.getPromotionId());
						}
					}
				}
			}
			deletePromotionResponseList.add(deletePromotionResponse);
		}
		return new ApiListResponse<>("All promotions :--- ", deletePromotionResponseList,
				deletePromotionResponseList.size());
	}
}
