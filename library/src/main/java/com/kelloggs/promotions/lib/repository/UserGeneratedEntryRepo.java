package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.UserGeneratedEntry;

/**
* Add UserGeneratedEntryRepo for the user_generated_entry Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
@Repository
public interface UserGeneratedEntryRepo extends JpaRepository<UserGeneratedEntry, Integer>{

    Optional<UserGeneratedEntry> findByUgcIdAndUserId(Integer ugcId,Integer userId);
    
    Optional<List<UserGeneratedEntry>> findByUgcId(@Valid Integer ugcId);

}
