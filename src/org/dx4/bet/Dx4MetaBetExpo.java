package org.dx4.bet;

public class Dx4MetaBetExpo{

	private double tbet;
	private double winexpo;
	private String choice;
	private String code;
	private String contact;
	private String username;
	
	public Dx4MetaBetExpo()
	{
	}
	
	public double getTbet() {
		return tbet;
	}
	public void setTbet(double tbet) {
		this.tbet = tbet;
	}
	public double getWinexpo() {
		return winexpo;
	}
	public void setWinexpo(double winexpo) {
		this.winexpo = winexpo;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return contact;
	}

	@Override
	public String toString() {
		return "\nDx4MetaBetExpo [tbet=" + tbet + ", winexpo=" + winexpo
				+ ", choice=" + choice + ", code=" + code + "]";
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	
}
