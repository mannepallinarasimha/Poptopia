package com.kelloggs.promotions.lib.rule;

/**
 * This class is used as root selector for multiple selection process like winner selection, 
 * prize selection etc. and provides a select method to be implement for different selection mechanism.
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 20th July 2022
 */
@FunctionalInterface
public interface Selector<T, U, R> {
	
	/**
	 * Perform the selection process
	 * 
	 * @param option   Option to be used for selection process
	 * @param criteria   Criteria to be used for selection process
	 * @return Return the selection result
	 */
	public R select(T option, U criteria);

}
