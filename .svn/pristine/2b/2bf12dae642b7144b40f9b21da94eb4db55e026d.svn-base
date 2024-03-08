package com.kelloggs.promotions.lib.repository;


import com.kelloggs.promotions.lib.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepo extends JpaRepository<Status,Integer> {

    Optional<Status> findByType(String type);
}
