package org.dx4.bet;

public enum Dx4MetaBetExpoOrder 
{
	tbet("Total Stake"),winexpo("Total Win Expo"),choice("Number"),code("Player Code");

	private String desc;
	
	Dx4MetaBetExpoOrder(String desc)
	{
		setDesc(desc);
	}
	
	private void setDesc(String desc2) {
		desc=desc2;
	}

	public String getDesc() {
		return desc;
	}
}
