package com.kelloggs.promotions.lib.constants;

/**
 * Prize Selector Type: It holds different
 * type of prize selector method for spin wheel promotion
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 01-03-2022
 */
public enum PrizeSelectorType {

	
	/**
	 * Prize selector types
	 */
    TYPE_DEFAULT("TYPE_DEFAULT"),
    TYPE_NOLOSS("TYPE_NOLOSS");
	
    
    private String name;

    
	/**
     * Instantiate the selector type
     * 
     * @param name	name of the prize selector
     */
    private PrizeSelectorType(String name) {
        this.name = name;
    }
    

    /**
     * @return name of the prize selector
     */
    public String getName() {
        return name;
    }
}
