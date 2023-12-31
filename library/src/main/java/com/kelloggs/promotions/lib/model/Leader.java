package com.kelloggs.promotions.lib.model;

/**
 * Leader: This interface is used to handle leader information for leader-board operation
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 19-08-2022
 */
public interface Leader {

	/**
	 * Get the leader rank
	 */
	public Integer getRank();

	/**
	 * Get the leader score
	 */
	public Integer getScore();

	/**
	 * Get the leader profile Id
	 */
	public Integer getProfileId();

}
