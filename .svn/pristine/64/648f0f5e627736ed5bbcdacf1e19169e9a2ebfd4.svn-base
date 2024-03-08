package com.kelloggs.promotions.lib.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.WinnerConfig;

@Repository
public interface WinnerConfigRepo extends JpaRepository<WinnerConfig, Integer> {
	
	Optional<List<WinnerConfig>> findWinnerConfigFromDB(@Param("promoId") Integer promoId);

	Optional<List<WinnerConfig>> findByPromotionId(Integer promotionId);
	
	@Query("SELECT C FROM WinnerConfig C WHERE C.promotion.id=:promotionId AND :time >= C.startTime AND :time <= C.endTime")
	Optional<List<WinnerConfig>> findByPromotionIdAndTime(Integer promotionId, LocalDateTime time, Sort sort);
	
	/**
	 * Get all the winner selection configurations based on promotion Id and request time between start time & end time and max winner greater than the given value
	 * 
	 * @param promotionId	Id of the current promotion
	 * @param time	Current user request time to validate while retrieving the winning moments
	 * @param maxWinners Provided winner count to validate while retrieving the winning moments
	 * @param sort	Sorting condition to sort the winning moments based on the given condition
	 * @return	Return a list of winner configurations if found otherwise optional containing null
	 */
	@Query("SELECT C FROM WinnerConfig C JOIN C.promotions P WHERE P.id=:promotionId AND :time >= C.startTime AND (C.endTime IS NULL OR :time <= C.endTime) AND C.maxWinners > :maxWinners")
	Optional<List<WinnerConfig>> findByPromotionsIdAndTimeAndMaxWinnerGreaterThan(Integer promotionId, LocalDateTime time, Integer maxWinners, Sort sort);

	@Query("SELECT W FROM WinnerConfig W WHERE (:promotionId IS NULL OR W.promotion.id=:promotionId) AND (coalesce(:configIds, NULL) IS NULL OR W.id IN (:configIds))")
	Optional<List<WinnerConfig>> findByPromotionIdAndConfigIdIn(Integer promotionId, List<Integer> configIds);
	
}

