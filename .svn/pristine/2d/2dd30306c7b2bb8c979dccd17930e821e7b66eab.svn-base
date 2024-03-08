/**
 * 
 */
package com.kelloggs.promotions.promotionservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelloggs.promotions.lib.entity.Region;
import com.kelloggs.promotions.lib.model.ApiListResponse;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.model.RegionRequest;
import com.kelloggs.promotions.lib.model.RegionResponse;
import com.kelloggs.promotions.promotionservice.service.RegionService;

/**
 * Add RegionController for the particular Locale with country
 * 
 * @GetLocale for get locale based on locale and country
 * @return Return the Get Locale details by provided regions
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
@RestController
@RequestMapping(path="/api/v1/regions")
public class RegionController {
	
	private final RegionService regionService;

	/**
	 * @param regionService
	 */
	public RegionController(RegionService regionService) {
		this.regionService = regionService;
	}
	
	@GetMapping
	public ApiListResponse<Region> getAllRegions() {
		return regionService.getRegions();
	}
	
	@GetMapping(path="/{id}")
	public ApiResponse<Region> getLocaleByRegionId(@PathVariable("id") Integer id) {
		return regionService.getLocaleByRegionId(id);
	}
	
	@GetMapping(path="/get/{locale}")
	public ApiResponse<Region> getLocaleByRegionLocale(@PathVariable("locale") String locale) {
		return regionService.getLocaleByRegionLocale(locale);
	}
	
	@PostMapping
	public ApiListResponse<RegionResponse> getLocal(@Valid @RequestBody List<RegionRequest> regions){
		return regionService.getLocales(regions);
	}
	 
}
