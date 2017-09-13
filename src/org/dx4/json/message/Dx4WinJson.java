package org.dx4.json.message;

public class Dx4WinJson {

	private Dx4BetJson bet;
	private String result;
	private String place;
	private String provider;
	private double win;
	
	public Dx4WinJson()
	{
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public double getWin() {
		return win;
	}

	public void setWin(double win) {
		this.win = win;
	}

	public Dx4BetJson getBet() {
		return bet;
	}

	public void setBet(Dx4BetJson bet) {
		this.bet = bet;
	}

	@Override
	public String toString() {
		return "Dx4WinJson [bet=" + bet + ", result=" + result + ", place=" + place + ", provider=" + provider
				+ ", win=" + win + "]";
	}

	
}
