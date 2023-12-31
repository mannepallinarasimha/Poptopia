package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.PromotionCode;

@Repository
public interface PromotionCodeRepo extends JpaRepository<PromotionCode,Integer> {

    Optional<PromotionCode> findByCodeAndPromotions_Id(String code, Integer promotionIds);

    Optional<PromotionCode> findByIdAndPromotions_Id(Integer promocodeId, Integer promotionId);

    /**
     * Find a list promotion code details based on promotion Id, codes and status
     * 
     * @param promoCodes	List of promotion codes
     * @param statuses	List of status to be filter out
     * @param promotionId	Id of the current promotion
     * @return	Return a list of promotion code details based on the provided condition
     * 
     * @author UDIT NAYAK (M1064560)
     * @since 15th July 2022
     */
	Optional<List<PromotionCode>> findByCodeInAndStatusInAndPromotions_Id(List<String> promoCodes, List<String> statuses, Integer promotionId);

}
