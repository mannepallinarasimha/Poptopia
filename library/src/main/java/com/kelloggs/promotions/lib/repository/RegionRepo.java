/**
 * 
 */
package com.kelloggs.promotions.lib.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.Region;

/**
 * Add RegionRepo for the JpaRepository Layer
 * 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
@Repository
public interface RegionRepo extends JpaRepository<Region, Integer> {
	Optional<Region> findByLocale(String locale);
}
