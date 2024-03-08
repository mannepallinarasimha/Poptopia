/**
 * 
 */
package com.kelloggs.promotions.promotionservice.service;

import java.util.List;

import com.kelloggs.promotions.lib.entity.Region;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RegionRequest;
import com.kelloggs.promotions.lib.model.RegionResponse;

/**
 * Add RegionService for the Region API
 * 
 * @Param RegionService
 * 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
public interface RegionService {

	ApiListResponse<Region> getRegions();

	ApiListResponse<RegionResponse> getLocales(List<RegionRequest> regions);

	ApiResponse<Region> getLocaleByRegionId(Integer id);

	ApiResponse<Region> getLocaleByRegionLocale(String locale);


}
