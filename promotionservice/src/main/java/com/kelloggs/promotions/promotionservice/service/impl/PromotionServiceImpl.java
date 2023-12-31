package com.kelloggs.promotions.promotionservice.service.impl;


import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Promotion;
import com.kelloggs.promotions.lib.entity.PromotionCluster;
import com.kelloggs.promotions.lib.entity.Region;
import com.kelloggs.promotions.lib.entity.WinnerConfig;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.MechanicRequest;
import com.kelloggs.promotions.lib.model.PromotionCreateRequest;
import com.kelloggs.promotions.lib.model.PromotionRequest;
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
	  private EntityManager em;
    
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
        return promotionRepo.findById(promotionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    NOT_FOUND.getCode(),
                                                    String.format("Promotion with id %d not found", promotionId)));

    }
    
    @Override
    public List<Promotion> getPromotionsByClusterId(Integer clusterId){
    	return promotionRepo.findByPromotionClusterId(clusterId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    400,
                                                    String.format("Promotions with cluster id %d not found", clusterId)));
    	
    }
    
    @Override
    public ApiResponse<Promotion> addPromotion(Promotion promotion) {
        Promotion savedPromotion = promotionRepo.save(promotion);
        return new ApiResponse<>("Promotion created with id : " + savedPromotion.getId(), savedPromotion);
    }

	@Override
	public ApiListResponse<PromotionResponse> createPromotion(PromotionCreateRequest promotionCreateRequest) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		if (promotionCreateRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Emplty ", promotionResponseList));
		}
		List<PromotionRequest> promotions = promotionCreateRequest.getPromotions();
		
		MechanicRequest mechanic = promotionCreateRequest.getMechanic();
		String settingsValue = "";
		List<PromotionSetting> settings = promotionCreateRequest.getSettings();
		for (PromotionSetting promotionSetting : settings) {
			settingsValue = promotionSetting.getValue();
		}

		Date date = new Date();
		Date newStartDate = null;
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		for (PromotionRequest promotionRequest : promotions) {
			Promotion promotion = new Promotion();
			Region region = regionRepo.findById(promotionRequest.getRegionId())
					.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Region with Region id %d not found", promotionRequest.getRegionId())));
			if (promotionRequest.getModuleKey().equals(null) || promotionRequest.getModuleKey().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with ModuleKey Should NOT be null or Empty ", promotionRequest.getModuleKey()));
			} else if (!promotionRequest.getModuleKey().isEmpty() || !promotionRequest.getModuleKey().equals(null)
					|| !promotionRequest.getModuleKey().isBlank()) {
				Optional<List<Promotion>> findByModuleKey = promotionRepo
						.findByModuleKey(promotionRequest.getModuleKey());
				if (findByModuleKey.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with ModuleKey %s is already Exists ", promotionRequest.getModuleKey()));
				} else {
					promotion.setModuleKey(promotionRequest.getModuleKey());
				}
			}
			if (promotionRequest.getPromotionName().equals(null) || promotionRequest.getPromotionName().isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with Name Should NOT be null or Empty ", promotionRequest.getPromotionName()));
			} else if (!promotionRequest.getPromotionName().isEmpty()
					|| !promotionRequest.getPromotionName().equals(null)
					|| !promotionRequest.getPromotionName().isBlank()) {
				Optional<List<Promotion>> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
				if (findByName.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with Name %s is already Exists ", promotionRequest.getPromotionName()));
				} else {
					promotion.setName(promotionRequest.getPromotionName());
				}
			}
			if (promotionRequest.getEpsilonId().equals(null) || promotionRequest.getEpsilonId() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
						"Promotion with EpsilonId Should NOT be null or Empty ", promotionRequest.getEpsilonId()));
			} else if (!promotionRequest.getEpsilonId().equals(null) || promotionRequest.getEpsilonId() != 0) {
				Optional<List<Promotion>> findByEpsilonId = promotionRepo
						.findByEpsilonId(promotionRequest.getEpsilonId());
				if (findByEpsilonId.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
							.format("Promotion with EpsilonId %d is already Exists ", promotionRequest.getEpsilonId()));
				} else {
					promotion.setEpsilonId(promotionRequest.getEpsilonId());
				}
			}
			promotion.setRegion(region);

			promotion.setLocalTimeZone(promotionRequest.getLocalTimeZone());

			if (mechanic.getType().equalsIgnoreCase("wm")) {
				Date startDate = mechanic.getStartDate();
				Date endDate = mechanic.getEndDate();
				if (!(endDate.getTime() > startDate.getTime())) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Mechanic with End Date %s must be Grater Than Start Date ", mechanic.getEndDate()));
				}
				int periodTime = (int) ((endDate.getTime() - startDate.getTime()) / Integer.parseInt(settingsValue));
				int randomNumberUsingInts = getRandomNumberUsingInts(0, periodTime);
				long newStartDateInLong = (startDate.getTime()) + randomNumberUsingInts;
				newStartDate = new Date(newStartDateInLong);
				promotion.setStartDate(newStartDate);
			} else if (mechanic.getType().equalsIgnoreCase("tos") || mechanic.getType().equalsIgnoreCase("pool")) {
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
			if(findById.isPresent()) {
				promotion.setPromotionCluster(findById.get());
			}else {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Cluster with ClusterId %d is NOT exists. ",
								promotionCreateRequest.getClusterId()));
			}
			Promotion savedPromotion = promotionRepo.save(promotion);
			Set<Promotion> setPromotions = new HashSet<>();
			setPromotions.add(savedPromotion);
			if (mechanic.getType().equalsIgnoreCase("wm")) {
				WinnerConfig winnerConfig = new WinnerConfig();
				winnerConfig.setMaxWinner(1);
				winnerConfig.setLimit(0);
				winnerConfig.setWinProbability(0);
				winnerConfig.setWinStep(0);
				winnerConfig.setEndTime(localDateTime);
				winnerConfig.setStartTime(localDateTime);
				winnerConfig.setPromotionDate(newStartDate);
				winnerConfig.setPromotion(setPromotions);

				winnerConfigRepo.save(winnerConfig);
			} else {
				WinnerConfig winnerConfig = new WinnerConfig();
				winnerConfig.setMaxWinner(1);
				winnerConfig.setLimit(0);
				winnerConfig.setWinProbability(0);
				winnerConfig.setWinStep(0);
				winnerConfig.setEndTime(localDateTime);
				winnerConfig.setStartTime(localDateTime);
				winnerConfig.setPromotionDate(mechanic.getStartDate());
				winnerConfig.setPromotion(setPromotions);

				winnerConfigRepo.save(winnerConfig);
			}

			PromotionResponse promotionResponse = new PromotionResponse();
			promotionResponse.setId(savedPromotion.getId());
			promotionResponse.setCreateDate(date);
			promotionResponse.setModifiedDate(date);
			promotionResponse.setName(savedPromotion.getName());
			promotionResponse.setEpsilonId(savedPromotion.getEpsilonId());
			promotionResponse.setStartDate(savedPromotion.getStartDate());
			promotionResponse.setEndDate(savedPromotion.getEndDate());
			promotionResponse.setMaxLimit(10);
			promotionResponse.setModuleKey(savedPromotion.getModuleKey());
			promotionResponse.setLocalTimeZone(savedPromotion.getLocalTimeZone());
			promotionResponse.setAttr1_code(savedPromotion.getAttr1_code());
			promotionResponse.setAttr1_value(savedPromotion.getAttr1_value());
			promotionResponse.setWinnerconfig(11); // TBD doubt
			promotionResponse.setRegion(savedPromotion.getRegion());
			promotionResponseList.add(promotionResponse);

		}
		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
	}

	public int getRandomNumberUsingInts(int min, int max) {
		Random random = new Random();
		return random.ints(min, max).findFirst().getAsInt();
	}

	@Override
	public ApiListResponse<PromotionResponse> updatePromotion(@Valid PromotionUpdateRequest promotionUpdateRequest) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		List<Integer> promotionIdsList = new ArrayList<>();
		List<PromotionSetting> settings = promotionUpdateRequest.getSettings();
		MechanicRequest mechanic = promotionUpdateRequest.getMechanic();
		int sweepStackEpsilinId = 0;
		String promotionName = "";
		String promotionModuleKey = "";
		String settingsValue = "";
		Date newStartDate = null;
		Date date = new Date();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		for (PromotionSetting promotionSetting : settings) {
			settingsValue = promotionSetting.getValue();
		}
		// validate all the promotion Details with DB
		List<PromotionRequest> promotionRequestList = promotionUpdateRequest.getPromotions();
		for (PromotionRequest promotionRequest : promotionRequestList) {
			promotionIdsList.add(promotionRequest.getPromotionId());
			if (promotionRequest.getPromotionId() != null) {
				Optional<Promotion> findById = promotionRepo.findById(promotionRequest.getPromotionId());
				if (findById.isPresent()) {
					System.out.println(findById.get().getEpsilonId());
					System.out.println(promotionRequest.getSweepStake());
					if (!findById.get().getPromotionCluster().getClusterId()
							.equals(promotionUpdateRequest.getClusterId())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with PromotionId  %d is NOT assosciated with clusterId %d is NOT exists. ",
								findById.get().getId(), promotionUpdateRequest.getClusterId()));
					} else if (promotionRequest.getSweepStake().equals(null)
							|| promotionRequest.getSweepStake().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with SweepStack Should NOT be null or Empty ",
										promotionRequest.getSweepStake()));
					} else if (!promotionRequest.getSweepStake().isEmpty()
							|| !promotionRequest.getSweepStake().equals(null)
							|| !promotionRequest.getSweepStake().isBlank()) {
						Optional<List<Promotion>> findByEpsilonId = promotionRepo
								.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
						if (findByEpsilonId.isPresent()) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400,
									String.format("Promotion with SweepStack EpsilonId %d is already Exists ",
											Integer.parseInt(promotionRequest.getSweepStake())));
						}
					}

					if (promotionRequest.getModuleKey().equals(null) || promotionRequest.getModuleKey().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with ModuleKey Should NOT be null or Empty ",
										promotionRequest.getModuleKey()));
					} else if (!promotionRequest.getModuleKey().isEmpty()
							|| !promotionRequest.getModuleKey().equals(null)
							|| !promotionRequest.getModuleKey().isBlank()) {
						Optional<List<Promotion>> findByModuleKey = promotionRepo
								.findByModuleKey(promotionRequest.getModuleKey());
						if (findByModuleKey.isPresent()) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
									"Promotion with ModuleKey %s is already Exists ", promotionRequest.getModuleKey()));
						}
					}

					if (promotionRequest.getPromotionName().equals(null)
							|| promotionRequest.getPromotionName().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Promotion with Name Should NOT be null or Empty ",
										promotionRequest.getPromotionName()));
					} else if (!promotionRequest.getPromotionName().isEmpty()
							|| !promotionRequest.getPromotionName().equals(null)
							|| !promotionRequest.getPromotionName().isBlank()) {
						Optional<List<Promotion>> findByName = promotionRepo
								.findByName(promotionRequest.getPromotionName());
						if (findByName.isPresent()) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
									"Promotion with Name %s is already Exists ", promotionRequest.getPromotionName()));
						}
					}
				}
			}

		}
		List<Promotion> findAllById = new ArrayList<>();
		if (!promotionIdsList.equals(null)) {
			findAllById = promotionRepo.findAllById(promotionIdsList);
			if (!findAllById.isEmpty()) {
				for (Promotion promotion : findAllById) {
					Query query = em.createNativeQuery(
							"select config_id from winner_selection_config_reference where promotion_id="
									+ promotion.getId());
					List<Integer> resultList = (List<Integer>) query.getResultList();
					if (!resultList.isEmpty()) {
						for (Integer configId : resultList) {
							System.out.println("ConfigIds For promotionIds ::: " + configId);
							winnerConfigRepo.deleteById(configId);
						}
					}
				}
			}

		}

		for (PromotionRequest promotionRequest : promotionRequestList) {

			if (promotionRequest.getPromotionId() != null) {
				Optional<Promotion> findById = promotionRepo.findById(promotionRequest.getPromotionId());
				if (findById.isPresent()) {
					Promotion promotionFromDB = findById.get();
					if (mechanic.getType().equalsIgnoreCase("wm")) {
						Date startDate = mechanic.getStartDate();
						Date endDate = mechanic.getEndDate();
						if (!(endDate.getTime() > startDate.getTime())) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400,
									String.format("Mechanic with End Date %s must be Grater Than Start Date ",
											mechanic.getEndDate()));
						}
						int periodTime = (int) ((endDate.getTime() - startDate.getTime())
								/ Integer.parseInt(settingsValue));
						int randomNumberUsingInts = getRandomNumberUsingInts(0, periodTime);
						long newStartDateInLong = (startDate.getTime()) + randomNumberUsingInts;
						newStartDate = new Date(newStartDateInLong);
						promotionFromDB.setStartDate(newStartDate);
					} else if (mechanic.getType().equalsIgnoreCase("tos")
							|| mechanic.getType().equalsIgnoreCase("pool")) {
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
					promotionFromDB.setEndDate(mechanic.getEndDate());
					promotionFromDB.setMaxLimit(1);
					promotionFromDB.setLocalTimeZone(promotionRequest.getLocalTimeZone());
					promotionFromDB.setAttr1_code(mechanic.getAttr1_code());
					promotionFromDB.setAttr1_value(mechanic.getAttr1_value());

					Promotion savedPromotion = promotionRepo.save(promotionFromDB);
					Set<Promotion> setPromotions = new HashSet<>();
					setPromotions.add(savedPromotion);

					if (mechanic.getType().equalsIgnoreCase("wm")) {
						WinnerConfig winnerConfig = new WinnerConfig();
						winnerConfig.setMaxWinner(1);
						winnerConfig.setLimit(0);
						winnerConfig.setWinProbability(0);
						winnerConfig.setWinStep(0);
						winnerConfig.setEndTime(localDateTime);
						winnerConfig.setStartTime(localDateTime);
						winnerConfig.setPromotionDate(newStartDate);
						winnerConfig.setPromotion(setPromotions);

						winnerConfigRepo.save(winnerConfig);
					} else {
						WinnerConfig winnerConfig = new WinnerConfig();
						winnerConfig.setMaxWinner(1);
						winnerConfig.setLimit(0);
						winnerConfig.setWinProbability(0);
						winnerConfig.setWinStep(0);
						winnerConfig.setEndTime(localDateTime);
						winnerConfig.setStartTime(localDateTime);
						winnerConfig.setPromotionDate(mechanic.getStartDate());
						winnerConfig.setPromotion(setPromotions);

						winnerConfigRepo.save(winnerConfig);
					}

					PromotionResponse promotionResponse = new PromotionResponse();
					promotionResponse.setId(savedPromotion.getId());
					promotionResponse.setCreateDate(date);
					promotionResponse.setModifiedDate(date);
					promotionResponse.setName(savedPromotion.getName());
					promotionResponse.setEpsilonId(savedPromotion.getEpsilonId());
					promotionResponse.setStartDate(savedPromotion.getStartDate());
					promotionResponse.setEndDate(savedPromotion.getEndDate());
					promotionResponse.setMaxLimit(10);
					promotionResponse.setModuleKey(savedPromotion.getModuleKey());
					promotionResponse.setLocalTimeZone(savedPromotion.getLocalTimeZone());
					promotionResponse.setAttr1_code(savedPromotion.getAttr1_code());
					promotionResponse.setAttr1_value(savedPromotion.getAttr1_value());
					promotionResponse.setWinnerconfig(11);
					promotionResponse.setRegion(savedPromotion.getRegion());
					promotionResponseList.add(promotionResponse);
				}
			} else {
				Promotion newPromotion = new Promotion();
				if (mechanic.getType().equalsIgnoreCase("wm")) {
					Date startDate = mechanic.getStartDate();
					Date endDate = mechanic.getEndDate();
					if (!(endDate.getTime() > startDate.getTime())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Mechanic with End Date %s must be Grater Than Start Date ", mechanic.getEndDate()));
					}
					int periodTime = (int) ((endDate.getTime() - startDate.getTime())
							/ Integer.parseInt(settingsValue));
					int randomNumberUsingInts = getRandomNumberUsingInts(0, periodTime);
					long newStartDateInLong = (startDate.getTime()) + randomNumberUsingInts;
					newStartDate = new Date(newStartDateInLong);
					newPromotion.setStartDate(newStartDate);
				} else if (mechanic.getType().equalsIgnoreCase("tos") || mechanic.getType().equalsIgnoreCase("pool")) {
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
					Optional<List<Promotion>> findByEpsilonId = promotionRepo
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
					Optional<List<Promotion>> findByModuleKey = promotionRepo
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
					Optional<List<Promotion>> findByName = promotionRepo
							.findByName(promotionRequest.getPromotionName());
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

				Promotion savedPromotion = promotionRepo.save(newPromotion);
				Set<Promotion> setPromotions = new HashSet<>();
				setPromotions.add(savedPromotion);

				if (mechanic.getType().equalsIgnoreCase("wm")) {
					WinnerConfig winnerConfig = new WinnerConfig();
					winnerConfig.setMaxWinner(1);
					winnerConfig.setLimit(0);
					winnerConfig.setWinProbability(0);
					winnerConfig.setWinStep(0);
					winnerConfig.setEndTime(localDateTime);
					winnerConfig.setStartTime(localDateTime);
					winnerConfig.setPromotionDate(newStartDate);
					winnerConfig.setPromotion(setPromotions);

					winnerConfigRepo.save(winnerConfig);
				} else {
					WinnerConfig winnerConfig = new WinnerConfig();
					winnerConfig.setMaxWinner(1);
					winnerConfig.setLimit(0);
					winnerConfig.setWinProbability(0);
					winnerConfig.setWinStep(0);
					winnerConfig.setEndTime(localDateTime);
					winnerConfig.setStartTime(localDateTime);
					winnerConfig.setPromotionDate(mechanic.getStartDate());
					winnerConfig.setPromotion(setPromotions);

					winnerConfigRepo.save(winnerConfig);
				}

				PromotionResponse promotionResponse = new PromotionResponse();
				promotionResponse.setId(savedPromotion.getId());
				promotionResponse.setCreateDate(date);
				promotionResponse.setModifiedDate(date);
				promotionResponse.setName(savedPromotion.getName());
				promotionResponse.setEpsilonId(savedPromotion.getEpsilonId());
				promotionResponse.setStartDate(savedPromotion.getStartDate());
				promotionResponse.setEndDate(savedPromotion.getEndDate());
				promotionResponse.setMaxLimit(10);
				promotionResponse.setModuleKey(savedPromotion.getModuleKey());
				promotionResponse.setLocalTimeZone(savedPromotion.getLocalTimeZone());
				promotionResponse.setAttr1_code(savedPromotion.getAttr1_code());
				promotionResponse.setAttr1_value(savedPromotion.getAttr1_value());
				promotionResponse.setWinnerconfig(11);
				promotionResponse.setRegion(savedPromotion.getRegion());
				promotionResponseList.add(promotionResponse);
			}
		}
		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
	}
}
