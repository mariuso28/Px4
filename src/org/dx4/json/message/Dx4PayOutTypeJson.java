package org.dx4.json.message;

public enum Dx4PayOutTypeJson{
	
	First("1st Prize"),Second("2nd Prize"),Third("3rd Prize"),Spec("Special"),Cons("Consolation"),
	
	// IBOX
	FirstIB24("1st 24 Perm"),SecondIB24("2nd 24 Perm"),ThirdIB24("3rd 24 Perm"),SpecIB24("Spec 24 Perm"),ConsIB24("Cons 24 Perm"),
	FirstIB12("1st 12 Perm"),SecondIB12("2nd 12 Perm"),ThirdIB12("3rd 12 Perm"),SpecIB12("Spec 12 Perm"),ConsIB12("Cons 12 Perm"),
	FirstIB6("1st 6 Perm"),SecondIB6("2nd 6 Perm"),ThirdIB6("3rd 6 Perm"),SpecIB6("Spec 6 Perm"),ConsIB6("Cons 6 Perm"),
	FirstIB4("1st 4 Perm"),SecondIB4("2nd 4 Perm"),ThirdIB4("3rd 4 Perm"),SpecIB4("Spec 4 Perm"),ConsIB4("Cons 4 Perm");
	
	private String desc;
	
	private Dx4PayOutTypeJson(String desc)
	{
		this.setDesc(desc);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static Dx4PayOutTypeJson valueOfFromCode(char code)
	{
		if (code=='F')
			return Dx4PayOutTypeJson.First;
		if (code=='S')
			return Dx4PayOutTypeJson.Second;
		if (code=='T')
			return Dx4PayOutTypeJson.Third;
		if (code=='P')
			return Dx4PayOutTypeJson.Spec;
		if (code=='C')
			return Dx4PayOutTypeJson.Cons;
		return null;
	}
	
}
