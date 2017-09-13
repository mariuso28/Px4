package org.dx4.secure.web.player;

public class BetMappingBigSmallOddEven extends BetMappingBigSmall {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9042522468231835446L;

	public BetMappingBigSmallOddEven()
	{
		this.getChoices().set(0,"Small");
		this.getChoices().set(1,"Even");
	}

}
