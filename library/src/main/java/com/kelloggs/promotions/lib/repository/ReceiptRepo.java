package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface ReceiptRepo extends JpaRepository<Receipt, Integer> {

    Optional<Receipt> findByIdAndPromotionEntryId(Integer receiptId, Integer entryId);

    Optional<List<Receipt>> findByPromotionEntryId(Integer entryId);
    
    Optional<List<Receipt>> findByPromotionEntryIdIn(List<Integer> entryIds);

    Optional<Receipt> findBySnippEventIdAndClientEventId(String snippEventId, String clientEventId);
}
