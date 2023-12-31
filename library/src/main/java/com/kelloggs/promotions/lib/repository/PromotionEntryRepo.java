package com.kelloggs.promotions.lib.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionEntry;
import com.kelloggs.promotions.lib.model.Leader;

@Repository
public interface PromotionEntryRepo extends JpaRepository<PromotionEntry, Integer> {

	Optional<List<PromotionEntry>> findByPromotionId(Integer promotionId);

	Optional<PromotionEntry> findByIdAndPromotionId(Integer entryId, Integer promotionId);

	Optional<List<PromotionEntry>> findByPromotionIdAndProfileId(Integer promotionId, Integer profileId);
	
	Optional<List<PromotionEntry>> findByPromotionIdAndProfileIdAndPrizeStatusType(Integer promotionId, Integer profileId, String status);

	Optional<List<PromotionEntry>> findByPromotionIdAndProfileIdAndCreatedLocalDateTimeBetweenAndPrizeStatusTypeIn(
			Integer promotionId, Integer profileId, LocalDateTime startTime, LocalDateTime endTime, List<String> status);
	
	@Query("SELECT P FROM PromotionEntry P WHERE P.profileId=:profileId AND (coalesce(:promotionIds, NULL) IS NULL OR P.promotion.id IN (:promotionIds)) AND (P.createdLocalDateTime BETWEEN :startDateTime AND :endDateTime)")
	Optional<List<PromotionEntry>> findByProfileIdAndPromotionIdInAndCreatedLocalDateTimeBetween(Integer profileId, Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	Optional<List<PromotionEntry>> findByPromotionIdAndCreatedDateBetweenAndStatusTypeNotIn(Integer promotionId,
			Date startDate, Date endDate, List<String> status);

	Optional<List<PromotionEntry>> findByPromotionIdAndStatusType(Integer promotionId, String status);
	
	Optional<List<PromotionEntry>> findByProfileIdAndStatusTypeIn(Integer profileId, List<String> statuses);
	
	Optional<List<PromotionEntry>> findByProfileIdAndAttr1CodeAndAttr1Value(Integer profileId, String attr1Code, String attr1Value);

	@Query(value = "select * from promotion_entry where profile_id=?1 and local_created_date_time like ?2", nativeQuery = true)
	Optional<List<PromotionEntry>> findByProfileIdAndCreatedLocalDateTime(Integer profileId, String date);

	Optional<List<PromotionEntry>> findByPromotionIdAndProfileIdAndCreatedDate(Integer promotionId, Integer profileId,
			Date date);

	Optional<List<PromotionEntry>> findContestantsFromDB(@Param("promoId") Integer promoId,
			@Param("attr1Code") String attr1Code);

	Optional<List<PromotionEntry>> findContestantsThatHasStatus(@Param("promoId") Integer promoId,
			@Param("attr1Code") String attr1Code);

	Optional<List<String>> findContestantsThatHasStatusSent(@Param("promoId") Integer promoId,
			@Param("attr1Code") String attr1Code);

	Optional<List<PromotionEntry>> findContestantsThatHasStatusWon(@Param("promoId") Integer promoId,
			@Param("attr1Code") String attr1Code);

	Optional<PromotionEntry> findByAttr1ValueAndPromoId(@Param("promoId") Integer promoId,
			@Param("attr1Value") String attr1_value);

	Optional<List<PromotionEntry>> findContestantsThatHasStatusWin(@Param("promoId") Integer promoId);

	Optional<List<PromotionEntry>> findContestantsThatHasStatusLost(@Param("promoId") Integer promoId);

	Optional<List<PromotionEntry>> getEntriesHavingWonStatus(@Param("promotionId") Integer promotionId, @Param("attr1Code") String attr1_code,@Param("attr1Value") String attr1_value);

	Long countByPromotionIdAndCreatedLocalDateTimeBetweenAndStatusTypeIn(Integer promotionId, LocalDateTime startTime, LocalDateTime endTime, List<String> status);

	Long countByPromotionIdAndProfileIdAndStatusType(Integer promotionId, Integer profileId, String status);
	
	Long countByProfileIdAndAttr1Value(Integer profileId, String attr1Value);

	Optional<Integer> currentWinCountBetweenStartAndEnd(@Param("promotionId") Integer promotionId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	
	@Query(value = "select * from promotion_entry where profile_id=?1", nativeQuery = true)
	Optional<List<PromotionEntry>> findByProfileId(Integer profileId);

	@Query(value ="SELECT ROW_NUMBER() OVER(ORDER BY CASE WHEN :order = 'ASC' THEN SUM(PE.qty) END ASC, " 
	+ "CASE WHEN :order = 'DESC' THEN SUM(PE.qty) END DESC) AS rank, SUM(PE.qty) AS score, PE.profile_id AS profileId FROM " 
	+ "(SELECT (I.qty * :scorePerUnit) AS qty, P.profile_id, P.promotion_id, P.local_created_date_time FROM receipt_product_details I INNER JOIN receipt_images R " 
	+ "ON I.receipt_id = R.id INNER JOIN promotion_entry P on R.promotion_entry_id = P.id UNION ALL SELECT :scorePerUnit AS qty, E.profile_id, " 
	+ "E.promotion_id, E.local_created_date_time FROM promotion_entry E WHERE E.status_id = :participationStatus AND :countParticipant = 'TRUE') AS PE " 
	+ "WHERE PE.promotion_id IN (:promotionIds) AND (PE.local_created_date_time BETWEEN :startDateTime AND :endDateTime) GROUP BY PE.profile_id" , nativeQuery = true)
	List<Leader> findLeaderBoardRankAndScore(Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer scorePerUnit, Boolean countParticipant, Integer participationStatus, String order);
	
	@Query(value ="SELECT ROW_NUMBER() OVER(ORDER BY CASE WHEN :order = 'ASC' THEN MAX(CAST(US.attr1_value as float)) END ASC, " 
	+ "CASE WHEN :order = 'DESC' THEN MAX(CAST(US.attr1_value as float)) END DESC) AS rank, MAX(CAST(US.attr1_value as float)) AS score, US.profile_id AS profileId FROM " 
	+ "(SELECT attr1_value , profile_id, promotion_id, local_created_date_time FROM promotion_entry) AS US " 
	+ "WHERE US.promotion_id IN (:promotionIds) AND (US.local_created_date_time BETWEEN :startDateTime AND :endDateTime) AND US.attr1_value!='null' group by profile_id" , nativeQuery = true)
	List<Leader> findLeaderBoardWithScore(Set<Integer> promotionIds, LocalDateTime startDateTime, LocalDateTime endDateTime, String order);
}
