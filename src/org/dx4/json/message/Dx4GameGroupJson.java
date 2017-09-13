package org.dx4.json.message;

public enum Dx4GameGroupJson
{
	D4("4 Digits",Dx4GameTypeJson.D4Big,Dx4GameTypeJson.D4Small,4),  
	IBox("IBox",Dx4GameTypeJson.D4IBoxBig,Dx4GameTypeJson.D4IBoxSmall,4),  
	Box("Box",Dx4GameTypeJson.D4BoxBig,Dx4GameTypeJson.D4BoxSmall,4),
	ABC("3 Digits",Dx4GameTypeJson.ABCC,Dx4GameTypeJson.ABCA,3),
	D2("2 Digits",null,Dx4GameTypeJson.D2,2);
	
	private String name;
	private Dx4GameTypeJson big;
	private Dx4GameTypeJson small;
	private int digits;
	
	Dx4GameGroupJson(String name,Dx4GameTypeJson big,Dx4GameTypeJson small,int digits)
	{
		setName(name);
		setSmall(small);
		setBig(big);
		setDigits(digits);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dx4GameTypeJson getBig() {
		return big;
	}

	public void setBig(Dx4GameTypeJson big) {
		this.big = big;
	}

	public Dx4GameTypeJson getSmall() {
		return small;
	}

	public void setSmall(Dx4GameTypeJson small) {
		this.small = small;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

}
