package org.dx4.json.message;

public enum Dx4PayOutTypeJson{
	
	First("1st Prize",'F'),Second("2nd Prize",'S'),Third("3rd Prize",'T'),Spec("Special",'P'),Cons("Consolation",'C'),
	
	// IBOX
	FirstIB24("1st 24 Perm",'F'),SecondIB24("2nd 24 Perm",'S'),ThirdIB24("3rd 24 Perm",'T'),SpecIB24("Spec 24 Perm",'P'),ConsIB24("Cons 24 Perm",'C'),
	FirstIB12("1st 12 Perm",'F'),SecondIB12("2nd 12 Perm",'S'),ThirdIB12("3rd 12 Perm",'T'),SpecIB12("Spec 12 Perm",'P'),ConsIB12("Cons 12 Perm",'C'),
	FirstIB6("1st 6 Perm",'F'),SecondIB6("2nd 6 Perm",'S'),ThirdIB6("3rd 6 Perm",'T'),SpecIB6("Spec 6 Perm",'P'),ConsIB6("Cons 6 Perm",'C'),
	FirstIB4("1st 4 Perm",'F'),SecondIB4("2nd 4 Perm",'S'),ThirdIB4("3rd 4 Perm",'T'),SpecIB4("Spec 4 Perm",'P'),ConsIB4("Cons 4 Perm",'C');
	
	private char place;
	private String desc;
	
	private Dx4PayOutTypeJson(String desc,char place)
	{
		this.setDesc(desc);
		this.setPlace(place);
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

	public char getPlace() {
		return place;
	}

	public void setPlace(char place) {
		this.place = place;
	}

	
	
}
