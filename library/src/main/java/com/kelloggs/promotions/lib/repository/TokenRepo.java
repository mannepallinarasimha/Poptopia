package com.kelloggs.promotions.lib.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelloggs.promotions.lib.entity.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Integer> {

	/**
	 * Find token details by hash code
	 * 
	 * @param hashCode	Generated input token 
	 * @return	Return token details if found, otherwise null
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 17th July 2022
	 */
    Optional<Token> findByHashCode(String hashCode);
    
    /**
  	 * Find all token by entry IDs
  	 * 
  	 * @param entryIds	List of entry IDs
  	 * @return	Return a list of token details if found, otherwise null
  	 * 
  	 * @author UDIT NAYAK (M1064560)
  	 * @since 31st July 2023
  	 */
    Optional<List<Token>> findAllByEntryIdIn(List<Integer> entryIds);
    
    /**
	 * Find all token by entry IDs and status
	 * 
	 * @param entryIds	Set of entry IDs
	 * @param status	Status of the token
	 * @return	Return a list of token details if found, otherwise null
	 * 
	 * @author UDIT NAYAK (M1064560)
	 * @since 22nd February 2023
	 */
    Optional<List<Token>> findAllByEntryIdInAndStatus(Set<Integer> entryIds, String status);
    
    /**
  	 * Find all token by profileId and statuses
  	 * 
  	 * @param profileId	Unique user profile ID
  	 * @param statuses	Set of token statuses
  	 * @return	Return a list of token details if found, otherwise null
  	 * 
  	 * @author UDIT NAYAK (M1064560)
  	 * @since 17th April 2023
  	 */
      Optional<List<Token>> findAllByProfileIdAndStatusIn(Integer profileId, List<String> statuses);

    Optional<Token> findByEntryIdAndProfileIdAndHashCode(Integer entryId, Integer profileId, String hashCode);
}
