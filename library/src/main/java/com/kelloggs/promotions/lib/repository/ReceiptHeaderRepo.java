package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.ReceiptHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptHeaderRepo extends JpaRepository<ReceiptHeader, Integer> {
}
