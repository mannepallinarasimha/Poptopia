/**
 * 
 */
package com.kelloggs.promotions.promotionservice.service.impl;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kelloggs.promotions.lib.entity.Region;
import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RegionRequest;
import com.kelloggs.promotions.lib.model.RegionResponse;
import com.kelloggs.promotions.lib.repository.RegionRepo;
import com.kelloggs.promotions.promotionservice.service.RegionService;

/**
 * Add RegionServiceImpl for the particular Locale with country
 *
 * @Param RegionServiceImpl for RegionController business logic
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
@Service
public class RegionServiceImpl implements RegionService {

	private final RegionRepo regionRepo;

	public RegionServiceImpl(RegionRepo regionRepo) {
		this.regionRepo = regionRepo;
	}

	@Override
	public ApiListResponse<Region> getRegions() {
		List<Region> regions = regionRepo.findAll();
		return new ApiListResponse<>("All Regions :--- ", regions, regions.size());
	}

	@Override
	public ApiListResponse<RegionResponse> getLocales(List<RegionRequest> regions) {
		List<RegionResponse> regionResponseList = new ArrayList<>();
		if (!regions.isEmpty()) {
			for (RegionRequest region : regions) {
				if (region.getCountry() == null || region.getLocale() == null || region.getLocale().isEmpty()
						|| region.getLocale().isBlank()) {
					throw new ApiException(HttpStatus.BAD_REQUEST, 400,
							String.format("Locale OR Country Must NOT be null OR Empty"));
				} else {
					Optional<Region> findRegionByLocale = regionRepo.findByLocale(region.getLocale());
					if (findRegionByLocale.isPresent()) {
						RegionResponse regionResponse = new RegionResponse();
						regionResponse.setId(findRegionByLocale.get().getId());
						regionResponse.setLocale(findRegionByLocale.get().getLocale());
						regionResponseList.add(regionResponse);
					} else {
						Region regionToSave = new Region();
						RegionResponse regionResponse = new RegionResponse();
						if (region.getLocale().matches(".*\\d.*")) {
							throw new ApiException(HttpStatus.BAD_REQUEST, 400, String
									.format("Locale %s Should NOT contain Any Numaric Values", region.getLocale()));
						}
						regionToSave.setLocale(region.getLocale());
						regionToSave.setCountry(region.getCountry());
						Region regionFromDB = regionRepo.save(regionToSave);
						regionResponse.setId(regionFromDB.getId());
						regionResponse.setLocale(regionFromDB.getLocale());
						regionResponseList.add(regionResponse);
					}
				}
			}

		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 400, String.format("No Region Found "));
		}
		return new ApiListResponse<>("All Regions :--- ", regionResponseList, regionResponseList.size());
	}


	@Override
	public ApiResponse<Region> getLocaleByRegionId(Integer id) {
		Region region = regionRepo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
				NOT_FOUND.getCode(), String.format("Region with id %d not found", id)));
		return new ApiResponse<>("All Regions :--- ", region);
	}

	@Override
	public ApiResponse<Region> getLocaleByRegionLocale(String locale) {
		Region region = regionRepo.findByLocale(locale).get();
		return new ApiResponse<>("All Regions :--- ", region);
	}

}
