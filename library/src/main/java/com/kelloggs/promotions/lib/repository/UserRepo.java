package com.kelloggs.promotions.lib.repository;

import com.kelloggs.promotions.lib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String userName);
}
