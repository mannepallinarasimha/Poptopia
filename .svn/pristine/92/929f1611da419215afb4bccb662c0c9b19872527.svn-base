package com.kelloggs.promotions.lib.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.UserGeneratedContent;
/**
* Add UserGeneratedContentRepo for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
@Repository
public interface UserGeneratedContentRepo extends JpaRepository<UserGeneratedContent, Integer>{

    Optional<UserGeneratedContent> findByName(String contentName);
    
    Optional<UserGeneratedContent> findById(Integer id);

    Optional<List<UserGeneratedContent>> findByStart(LocalDateTime date);

}
