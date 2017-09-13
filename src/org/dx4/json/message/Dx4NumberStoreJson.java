package org.dx4.json.message;

public class Dx4NumberStoreJson {
	private String number;
	private int occurences;
	private Dx4NumberStoreLevelJson level;
	private double betExposure;
	private double winExposure;
	
	public Dx4NumberStoreJson()
	{
	}
	
	public Dx4NumberStoreJson(String number)
	{
		setNumber(number);
		clearExposures();
	}

	public void clearExposures() {
		setBetExposure(-1);
		setWinExposure(-1);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getOccurences() {
		return occurences;
	}

	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}

	public void setLevel(Dx4NumberStoreLevelJson level) {
		this.level = level;
	}

	public Dx4NumberStoreLevelJson getLevel() {
		return level;
	}

	public double getBetExposure() {
		return betExposure;
	}

	public void setBetExposure(double betExposure) {
		this.betExposure = betExposure;
	}

	public double getWinExposure() {
		return winExposure;
	}

	public void setWinExposure(double winExposure) {
		this.winExposure = winExposure;
	}

	@Override
	public String toString() {
		return "NumberStore [number=" + number + ", occurences=" + occurences
				+ ", level=" + level + "]";
	}
	
}
