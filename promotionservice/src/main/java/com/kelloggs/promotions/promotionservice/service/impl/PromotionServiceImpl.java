package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		clusterIdValidation(promotionCreateRequest.getClusterId());
		List<PromotionResponse> promotionResponseList = new ArrayList<>();
		if (promotionCreateRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Empty ", promotionResponseList));
		}
		List<PromotionRequest> promotions = promotionCreateRequest.getPromotions();
		for (PromotionRequest promotionRequest : promotions) {

			promotionRegionIdValidation(promotionRequest.getRegionId());
			promotionModuleKeyValidation(promotionRequest.getModuleKey());
			promotionNameValidation(promotionRequest.getPromotionName());
			promotionEpsilonIdValidation(promotionRequest.getEpsilonId());

		}
		MechanicRequest mechanic = promotionCreateRequest.getMechanic();
		final String df = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat(df);
		Date mechanicStartDate = null;
		Date mechanicEndDate = null;
		try {
			mechanicStartDate = simpleDateFormate.parse(getRequiredStringDate(mechanic.getStartDate()));
			mechanicEndDate = simpleDateFormate.parse(getRequiredStringDate(mechanic.getEndDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mechanicValidattion(mechanic.getType(), getRequiredStringDate(mechanic.getStartDate()),
				getRequiredStringDate(mechanic.getEndDate()));
		String settingsValue = "";
		List<PromotionSetting> settings = promotionCreateRequest.getSettings();
		for (PromotionSetting promotionSetting : settings) {
			promotionSettingValidation(promotionSetting.getName(), promotionSetting.getValue());
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
							String.format("Region with Region id \'%d\' not found", promotionRequest.getRegionId())));

			promotion.setModuleKey(promotionRequest.getModuleKey());
			promotion.setName(promotionRequest.getPromotionName());
			promotion.setEpsilonId(promotionRequest.getEpsilonId());
			promotion.setRegion(region);
			promotion.setLocalTimeZone(promotionRequest.getLocalTimeZone());
			promotion.setStartDate(mechanicStartDate);
			promotion.setEndDate(mechanicEndDate);
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
						.format("Cluster with ClusterId \'%d\'' is NOT exists. ",
								promotionCreateRequest.getClusterId()));
			}
			savedPromotion = promotionRepo.save(promotion);

			promotionList.add(savedPromotion);
			setPromotions.add(savedPromotion);
		}

		if (mechanic.getType().equalsIgnoreCase(CodeConstants.WM.getStatus())) {
			wmStartDateCalculation(mechanicStartDate, mechanicEndDate, setPromotions, settingsValue);
		}
		promotionResponseList = promotionResponseSetUp(promotionList, mechanic.getStartDate(), mechanic.getEndDate());

		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
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
		clusterIdValidation(promotionUpdateRequest.getClusterId());
		if (promotionUpdateRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Emplty ", promotionResponseList));
		}
		Optional<List<Promotion>> findByPromotionClusterId = promotionRepo
				.findByPromotionClusterId(promotionUpdateRequest.getClusterId());
		if (findByPromotionClusterId.isPresent()) {

			List<Promotion> promotionListFromClusterId = findByPromotionClusterId.get();
			List<Integer> requestedPromotionIdsList = new ArrayList<>();
			List<Integer> clusterPromotionIds = new ArrayList<>();

			List<PromotionRequestforUpdate> promotions = promotionUpdateRequest.getPromotions();
			for (PromotionRequestforUpdate promotionRequestforUpdate : promotions) {
				requestedPromotionIdsList.add(promotionRequestforUpdate.getPromotionId());
			}
			for (Promotion clusterPromotion : promotionListFromClusterId) {
				clusterPromotionIds.add(clusterPromotion.getId());
			}

			List<Integer> base = new ArrayList<Integer>(clusterPromotionIds);
			base.removeAll(requestedPromotionIdsList);
			if (!base.isEmpty()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400,
						String.format("Please add these promotions for update \'%s\'", base.toString()));
			}
		}
		for (PromotionSetting promotionSetting : settings) {
			promotionSettingValidation(promotionSetting.getName(), promotionSetting.getValue());
			settingsValue = promotionSetting.getValue();
		}
		List<PromotionRequestforUpdate> promotionRequestList = promotionUpdateRequest.getPromotions();
		for (PromotionRequestforUpdate promotionRequest : promotionRequestList) {

			promotionIdsList.add(promotionRequest.getPromotionId());

			if (promotionRequest.getPromotionId() != null) {
				promotionRegionIdValidation(promotionRequest.getRegionId());
				Optional<Promotion> findById = promotionRepo.findById(promotionRequest.getPromotionId());
				if (findById.isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with PromotionId  \'%d\' is NOT exist .", promotionRequest.getPromotionId()));
				}
				if (findById.isPresent() || !findById.isPresent()) {

					if (!findById.get().getPromotionCluster().getClusterId()
							.equals(promotionUpdateRequest.getClusterId())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format(
										"Promotion with PromotionId  \'%d\' is NOT assosciated with clusterId \'%d\' . ",
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
						System.out.println(
								"eplsilonID FROM Sweepstack " + Integer.parseInt(promotionRequest.getSweepStake()));
						// Optional<Promotion> findByEpsilonId =
						// promotionRepo.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
						Optional<Promotion> findByEpsilonId = promotionRepo
								.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
						System.out.println("findByEpsilonId :::  " + findByEpsilonId);
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
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with ModuleKey is \'%s\' now. It Should NOT be null or Empty ",
								promotionRequest.getModuleKey()));
					} else if (!promotionRequest.getModuleKey().isEmpty()
							|| !promotionRequest.getModuleKey().equals(null)
							|| !promotionRequest.getModuleKey().isBlank()) {
						Optional<Promotion> findByModuleKey = promotionRepo
								.findByModuleKey(promotionRequest.getModuleKey());
						if (findByModuleKey.isPresent()
								&& !(findByModuleKey.get().getId().equals(promotionRequest.getPromotionId()))) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
									.format("Promotion with ModuleKey \'%s\' is already Exists with PromotionId \'%d\'",
											promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
						}
					}
					if (promotionRequest.getPromotionName() == null
							|| promotionRequest.getPromotionName().equals(null)
							|| promotionRequest.getPromotionName().isEmpty()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with Name is \'%s\' now. It Should NOT be null or Empty ",
								promotionRequest.getPromotionName()));
					} else if (!promotionRequest.getPromotionName().isEmpty()
							|| !promotionRequest.getPromotionName().equals(null)
							|| !promotionRequest.getPromotionName().isBlank()) {
						Optional<Promotion> findByName = promotionRepo.findByName(promotionRequest.getPromotionName());
						if (findByName.isPresent()
								&& !(findByName.get().getId().equals(promotionRequest.getPromotionId()))) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
									.format("Promotion with Name \'%s\' is already Exists with PromotionId \'%d\'",
											promotionRequest.getPromotionName(), findByName.get().getId()));
						}
					}
				}
			} else {
				if (promotionRequest.getSweepStake() == null
						|| promotionRequest.getSweepStake().equals(null)
						|| promotionRequest.getSweepStake().isEmpty()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Promotion with SweepStack is \'%s\' now. It Should NOT be null or Empty ",
									promotionRequest.getSweepStake()));
				} else if (!promotionRequest.getSweepStake().isEmpty()
						|| !promotionRequest.getSweepStake().equals(null)
						|| !promotionRequest.getSweepStake().isBlank()) {
					Optional<Promotion> findByEpsilonId = promotionRepo
							.findByEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					if (findByEpsilonId.isPresent()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
								"Promotion with SweepStack EpsilonId \'%d\' is already Exists with promotionId \'%d\' ",
								Integer.parseInt(promotionRequest.getSweepStake()), findByEpsilonId.get().getId()));
					}
				}
				promotionModuleKeyValidation(promotionRequest.getModuleKey());
				promotionNameValidation(promotionRequest.getPromotionName());
			}

		}
		final String df = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat(df);
		Date mechanicStartDate = null;
		Date mechanicEndDate = null;
		try {
			mechanicStartDate = simpleDateFormate.parse(getRequiredStringDate(mechanic.getStartDate()));
			mechanicEndDate = simpleDateFormate.parse(getRequiredStringDate(mechanic.getEndDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mechanicValidattion(mechanic.getType(), getRequiredStringDate(mechanic.getStartDate()),
				getRequiredStringDate(mechanic.getEndDate()));

		for (PromotionSetting promotionSetting : settings) {
			promotionSettingValidation(promotionSetting.getName(), promotionSetting.getValue());
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
						promotionFromDB.setStartDate(mechanicStartDate);
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
									.format("Region with Region id \'%d\' not found", promotionRequest.getRegionId())));
					promotionFromDB.setRegion(region);
					promotionFromDB.setCreatedDate(new Date());
					promotionFromDB.setModifiedDate(new Date());
					promotionFromDB.setEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
					promotionFromDB.setName(promotionRequest.getPromotionName());
					promotionFromDB.setModuleKey(promotionRequest.getModuleKey());
					promotionFromDB.setEndDate(mechanicEndDate);
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
					newPromotion.setStartDate(mechanicStartDate);
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
				} else {
					newPromotion.setEpsilonId(Integer.parseInt(promotionRequest.getSweepStake()));
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
								"Promotion with ModuleKey \'%s\' is already Exists with promotionId \'%d\'",
								promotionRequest.getModuleKey(), findByModuleKey.get().getId()));
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
								"Promotion with Name \'%s\' is already Exists with promotionId \'%d\'",
								promotionRequest.getPromotionName(), findByName.get().getId()));
					} else {
						newPromotion.setName(promotionRequest.getPromotionName());
					}
				}

				Optional<PromotionCluster> promotionClusterDetails = promotionClusterRepo
						.findById(promotionUpdateRequest.getClusterId());
				newPromotion.setPromotionCluster(promotionClusterDetails.get());
				Region region = regionRepo.findById(promotionRequest.getRegionId())
						.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
								String.format("Region with Region id \'%d\' not found",
										promotionRequest.getRegionId())));
				newPromotion.setRegion(region);
				newPromotion.setCreatedDate(new Date());
				newPromotion.setModifiedDate(new Date());
				newPromotion.setEndDate(mechanicEndDate);
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
			wmStartDateCalculation(mechanicStartDate, mechanicEndDate, setPromotions, settingsValue);
		}
		promotionResponseList = promotionResponseSetUp(promotionList, mechanic.getStartDate(), mechanic.getEndDate());
		return new ApiListResponse<>("All promotions :--- ", promotionResponseList, promotionResponseList.size());
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
		clusterIdValidation(deletePromotionRequest.getClusterId());
		Optional<List<Promotion>> findByPromotionClusterId = promotionRepo
				.findByPromotionClusterId(deletePromotionRequest.getClusterId());
		List<PromotionIdsRequest> promotionIdsList = deletePromotionRequest.getPromotions().stream()
				.collect(Collectors.toList());
		List<Integer> promotionIds = new ArrayList<>();
		for (PromotionIdsRequest promotionIdsRequest : promotionIdsList) {
			promotionIds.add(promotionIdsRequest.getPromotionId());
		}
		int sizeFindByPromotionClusterId = 0;
		int sizeDeletePromotionRequest = 0;
		if (!findByPromotionClusterId.isPresent() && !(promotionIdsList.isEmpty())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"No Promotions Present with ClusterId  \'%d\' But \'%s\' promotionIds are provided in the request for deletion. ",
					deletePromotionRequest.getClusterId(), promotionIds.toString()));
		}

		List<DeletePromotionResponse> deletePromotionResponseList = new ArrayList<>();
		List<PromotionIdsRequest> promotions = deletePromotionRequest.getPromotions();
		if (deletePromotionRequest.getPromotions().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Required Promotions are Emplty ", deletePromotionResponseList));
		} else if (sizeFindByPromotionClusterId != 0 && sizeDeletePromotionRequest != 0) {
			sizeFindByPromotionClusterId = findByPromotionClusterId.get().size();
			sizeDeletePromotionRequest = deletePromotionRequest.getPromotions().size();
		} else {
			for (PromotionIdsRequest promotionIdsRequest : promotions) {
				Optional<Promotion> findById = promotionRepo.findById(promotionIdsRequest.getPromotionId());
				if (!findById.isPresent()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
							"Promotion with PromotionId  \'%d\' is NOT Exist. ", promotionIdsRequest.getPromotionId()));
				} else if (!(findById.get().getPromotionCluster().getClusterId()
						.equals(deletePromotionRequest.getClusterId()))) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format(
									"Promotion with PromotionId  \'%d\' is NOT assosciated with clusterId \'%d\' . ",
									findById.get().getId(), deletePromotionRequest.getClusterId()));
				}
			}
		}

		for (PromotionIdsRequest promotionIdsRequest : promotions) {
			DeletePromotionResponse deletePromotionResponse = new DeletePromotionResponse();
			if (promotionIdsRequest.getPromotionId() != null) {
				Optional<Promotion> findById = promotionRepo.findById(promotionIdsRequest.getPromotionId());

				Query query = entityManager.createNativeQuery(
						"select config_id from winner_selection_config_reference where promotion_id="
								+ findById.get().getId());
				List<Integer> resultList = (List<Integer>) query.getResultList();
				if (findByPromotionClusterId.get().size() == deletePromotionRequest.getPromotions().size()) {
					if (!resultList.isEmpty()) {
						for (Integer configId : resultList) {
							winnerConfigRepo.deleteById(configId);
						}
					}
				} else {
					try {
						Query query1 = entityManager.createNativeQuery(
								"DELETE FROM winner_selection_config_reference where promotion_id="
										+ findById.get().getId());
						query1.getResultList();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				deletePromotionResponse.setPromotionId(promotionIdsRequest.getPromotionId());
				promotionRepo.deleteById(findById.get().getId());
			}
			deletePromotionResponseList.add(deletePromotionResponse);
		}
		return new ApiListResponse<>("All promotions :--- ", deletePromotionResponseList,
				deletePromotionResponseList.size());
	}

	/**
	 * Add promotionResponseSetUp for the PromotionServiceInpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 5th January 2024
	 */
	public List<PromotionResponse> promotionResponseSetUp(List<Promotion> savedPromotions, String mechanicStartDate,
			String mechanicEndDate) {
		List<PromotionResponse> promotionResponseList = new ArrayList<>();

		for (Promotion savedPromotion : savedPromotions) {
			PromotionResponse promotionResponse = new PromotionResponse();
			Date date = new Date();
			promotionResponse.setId(savedPromotion.getId());
			promotionResponse.setCreateDate(LocalDateTime.now());
			promotionResponse.setModifiedDate(LocalDateTime.now());
			promotionResponse.setName(savedPromotion.getName());
			promotionResponse.setEpsilonId(savedPromotion.getEpsilonId());
			promotionResponse.setStartDate(mechanicStartDate);
			promotionResponse.setEndDate(mechanicEndDate);
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
	 * Add clusterIdValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void clusterIdValidation(Integer promotionClusterId) {
		if (promotionClusterId == null
				|| promotionClusterId == 0) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("ClusterId is \'%d\' now. It Must NOT be Zero OR null OR EMPTY OR Blank",
							promotionClusterId));
		} else {
			promotionClusterRepo.findById(promotionClusterId)
					.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Cluster with ClusterId \'%d\' not found", promotionClusterId)));
		}
	}

	/**
	 * Add promotionRegionIdValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void promotionRegionIdValidation(Integer promotionRegionId) {
		if (promotionRegionId == null
				|| promotionRegionId == 0) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("RegionId is \'%d\' now. It Must NOT be Zero OR null OR EMPTY OR Blank",
							promotionRegionId));
		} else {
			regionRepo.findById(promotionRegionId)
					.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Region with Region id \'%d\' not found", promotionRegionId)));
		}
	}

	/**
	 * Add promotionSettingValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void promotionSettingValidation(String settingName, String settingValue) {
		if (settingName == null || settingValue == null
				|| settingName.isEmpty() || settingName.isBlank()
				|| settingValue.isEmpty() || settingValue.isBlank()
				|| settingValue.equals("0") || settingValue.equals(null)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Setting value is \'%s\' now. It should NOT be Zero OR NULL OR EMPTY OR BLANK",
							settingValue));
		}
	}

	/**
	 * Add promotionEpsilonIdValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void promotionEpsilonIdValidation(Integer promotionEpsilonId) {
		if (promotionEpsilonId == null
				|| promotionEpsilonId.equals(null)
				|| promotionEpsilonId == 0) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"Promotion with EpsilonId is \'%d\' now. It Should NOT be Zero or null or Empty ",
					promotionEpsilonId));
		} else if (!promotionEpsilonId.equals(null)
				|| promotionEpsilonId != 0) {
			Optional<Promotion> findByEpsilonId = promotionRepo.findByEpsilonId(promotionEpsilonId);
			if (findByEpsilonId.isPresent()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Promotion with EpsilonId \'%d\' is already Exists with PromotionId \'%d\' ",
								promotionEpsilonId, findByEpsilonId.get().getId()));
			}
		}
	}

	/**
	 * Add promotionModuleKeyValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void promotionModuleKeyValidation(String promotionModuleKey) {
		if (promotionModuleKey == null
				|| promotionModuleKey.equals(null)
				|| promotionModuleKey.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"Promotion with ModuleKey is \'%s\' now. It Should NOT be null or Empty ", promotionModuleKey));
		} else if (!promotionModuleKey.isEmpty()
				|| !promotionModuleKey.equals(null)
				|| !promotionModuleKey.isBlank()) {
			Optional<Promotion> findByModuleKey = promotionRepo.findByModuleKey(promotionModuleKey);
			if (findByModuleKey.isPresent()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Promotion with ModuleKey \'%s\' is already Exists with PromotionId \'%d\'",
								promotionModuleKey, findByModuleKey.get().getId()));
			}
		}
	}

	/**
	 * Add mechanicValidattion for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void mechanicValidattion(String mechanicType, String mechanicStartDate, String mechanicEndDate) {
		// Date mechanicStartDateFormat = new Date(mechanicStartDate);
		// Date mechanicEndDateFormat = new Date(mechanicEndDate);
		final String df = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat(df);
		Date mechanicStartDateFormat = null;
		Date mechanicEndDateFormat = null;
		try {
			mechanicStartDateFormat = simpleDateFormate.parse(mechanicStartDate);
			mechanicEndDateFormat = simpleDateFormate.parse(mechanicEndDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (mechanicType == null
				|| mechanicType.isBlank()
				|| mechanicType.isBlank()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type MUST NOT be null OR Empty"));
		} else if (mechanicEndDate == null
				|| mechanicStartDate == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with StartDate and EndDate MUST NOT be null OR Empty"));
		} else if (!(isDatePastTodayFuture(mechanicStartDate, df))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with Start Date \'%s\'  MUST NOT be past dates.", mechanicStartDate));
		} else if (!(isDatePastTodayFuture(mechanicEndDate, df))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with End Date \'%s\'  MUST NOT be past dates.", mechanicEndDate));
		} else if (!(mechanicEndDateFormat.getTime() > mechanicStartDateFormat.getTime())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
					.format("Mechanic with End Date \'%s\' must be Grater Than Start Date \'%s\'", mechanicEndDate,
							mechanicStartDate));
		} else if (mechanicType.equalsIgnoreCase(CodeConstants.WM.getStatus())
				|| mechanicType.equalsIgnoreCase(CodeConstants.TOS.getStatus())
				|| mechanicType.equalsIgnoreCase(CodeConstants.POOL.getStatus())) {
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400,
					String.format("Mechanic with Type \'%s\' is NOT matching with \'wm\' or \'tos\' or \'pool\' ",
							mechanicType));
		}
	}

	/**
	 * Add promotionNameValidation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void promotionNameValidation(String promotionName) {
		if (promotionName == null
				|| promotionName.equals(null)
				|| promotionName.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format(
					"Promotion with Name is \'%s\' now. It Should NOT be null or Empty ", promotionName));
		} else if (!promotionName.isEmpty()
				|| !promotionName.equals(null)
				|| !promotionName.isBlank()) {
			Optional<Promotion> findByName = promotionRepo.findByName(promotionName);
			if (findByName.isPresent()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
						.format("Promotion with Name \'%s\' is already Exists with PromotionId \'%d\'", promotionName,
								findByName.get().getId()));
			}
		}
	}

	/**
	 * Add getRandomNumber for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	/**
	 * Add getRequiredStringDate for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 18th January 2024
	 */
	public static String getRequiredStringDate(String str) {
		String[] ts = str.split("T");
		return ts[0] + " " + ts[1].substring(0, 8);
	}

	/**
	 * Add isDatePastTodayFuture for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
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
	 * Add wmStartDateCalculation for the PromotionServiceImpl Layer
	 * 
	 * @author NARASIMHARAO MANNEPALLI (10700939)
	 * @since 17th January 2024
	 */
	public void wmStartDateCalculation(Date mechanicStartDate, Date mechanicEndDate, Set<Promotion> setPromotions,
			String settingsValue) {

		long startDateMillis = mechanicStartDate.getTime();
		long endDateMillis = mechanicEndDate.getTime();
		int periodTime = (int) ((endDateMillis - startDateMillis)
				/ Integer.parseInt(settingsValue));
		for (int i = 0; i < Integer.parseInt(settingsValue); i++) {
			int randomNumber = getRandomNumber(1, periodTime);
			long prizetime = startDateMillis + randomNumber;
			Date prizeTimeDate = new Date(prizetime);
			long endtime = endDateMillis;
			Date resend = new Date(endtime);
			startDateMillis = startDateMillis + periodTime;
			WinnerConfig winnerConfig = winnerConfigSetUp(setPromotions, prizeTimeDate, mechanicEndDate);
			winnerConfigRepo.save(winnerConfig);
		}

	}
}
