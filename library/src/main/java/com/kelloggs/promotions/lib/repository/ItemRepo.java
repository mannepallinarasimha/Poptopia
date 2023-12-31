package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item,Integer> {

    Optional<List<Item>> findByReceiptId(Integer receiptId);
    
    Optional<List<Item>> findByReceiptIdIn(List<Integer> receiptIds);
}
