package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.MiniGame;

/**
* Add MiniGameRepo for the mini_game Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 07th Feb 2024
*/
@Repository
public interface MiniGameRepo extends JpaRepository<MiniGame, Integer>{
	
	Optional<List<MiniGame>> findByGameId(String gameId);

	Optional<MiniGame> findByGameIdAndProfileId(String gameId,Integer profileId);

}
