package org.dx4.json.message;

public enum Dx4NumberStoreLevelJson {
	NONE("black"),THIRD("BA006D"),SECOND("FFA600"),FIRST("00B00F");

	private String color;
	
	Dx4NumberStoreLevelJson()
	{
	}
	
	Dx4NumberStoreLevelJson(String color)
	{
		setColor(color);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	

}
