package org.dx4.account;

public class Dx4NumberExpo 
{
	private long userId;
	private String number;
	private double winExpo;
	private double betExpo;
	private int blocked;
	
	public Dx4NumberExpo()
	{
	}
	
	public Dx4NumberExpo(long userId, String number, double betExpo, double winExpo) {
		super();
		this.userId = userId;
		this.number = number;
		this.winExpo = winExpo;
		this.betExpo = betExpo;
		
	}
	
	public Dx4NumberExpo(Dx4NumberExpo other) {
		super();
		this.userId = other.userId;
		this.number = new String(other.getNumber());
		this.winExpo = other.getWinExpo();
		this.betExpo = other.getBetExpo();
		
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getWinExpo() {
		return winExpo;
	}

	public void setWinExpo(double winExpo) {
		this.winExpo = winExpo;
	}

	public double getBetExpo() {
		return betExpo;
	}

	public void setBetExpo(double betExpo) {
		this.betExpo = betExpo;
	}

	public static char getDefaultDigits(String number) {
		
		if (number.length()==3)		
			return '3';
		if (number.length()==4)		
			return '4';
		return 'X';
	}
	
	public static String getDefaultNumberCode(char digits) {
		
		String def = "";
		for (int i=0; i<Integer.parseInt(Character.toString(digits)); i++)
			def += "D";
		return def;				// -4 default for 4D etc.
	}
	
	@Override
	public String toString() {
		return "Dx4NumberExpo [userId=" + userId + ", number=" + number
				+ ", winExpo=" + winExpo + ", betExpo=" + betExpo + "]";
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public int getBlocked() {
		return blocked;
	}
	
	
}
