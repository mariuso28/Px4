package org.gz.json;

public enum GzGameType {
	// D2(2,"2 Digit 1st Prize Only",2),
	D4Big(1,"4D 1st/2nd/3rd/Special/Consolation Prizes",4,"Big"),  
	D4Small(2,"4D 1st/2nd/3rd Prizes",4,"Small"), 					
	D4IBoxBig(3,"4D - IBox 1st/2nd/3rd/Special/Consolation Prizes",4,"IBoxBig"),  
	D4IBoxSmall(4,"4D - IBox 1st/2nd/3rd Prizes",4,"IBoxSmall"), 
	D4BoxBig(5,"4D - Box 1st/2nd/3rd/Special/Consolation Prizes",4,"BoxBig"),
	D4BoxSmall(6,"4D - Box 1st/2nd/3rd Prizes",4,"BoxSmall"),
	ABCA(7,"3D 1st Prize Only",3,"ABC/A"),
	ABCC(8,"3D 1st/2nd/3rd Prizes",3,"ABC/C");		
	
	private int index;
	private String shortName;
	private String desc;
	private int digits;
	
	GzGameType(int index,String desc,int digits,String shortName)
	{
		setIndex(index);
		setDesc(desc);
		setDigits(digits);
		setShortName(shortName);
	}
	
	
	
	public String getName()
	{
		return name();
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public int getDigits() {
		return digits;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


}
