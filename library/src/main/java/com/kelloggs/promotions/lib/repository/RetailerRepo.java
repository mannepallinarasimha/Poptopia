package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetailerRepo extends JpaRepository<Retailer,Integer> {

    Optional<Retailer> findByName(String retailerName);

    Optional<List<Retailer>> findByPromotionsId(Integer promotionId);
}
