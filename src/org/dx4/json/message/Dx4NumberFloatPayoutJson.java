package org.dx4.json.message;

import java.util.Date;

public class Dx4NumberFloatPayoutJson {

	private String number;
	private int count;
	private Date lastTraded;
	private Date lastChanged;
	private double volume;
	private double odds;
	private int band;
	private double lastChange;
	
	public Dx4NumberFloatPayoutJson()
	{
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getLastTraded() {
		return lastTraded;
	}

	public void setLastTraded(Date lastTraded) {
		this.lastTraded = lastTraded;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public int getBand() {
		return band;
	}

	public void setBand(int band) {
		this.band = band;
	}

	@Override
	public String toString() {
		return "Dx4NumberFloatPayoutJson [number=" + number + ", count=" + count + ", lastTraded=" + lastTraded
				+ ", volume=" + volume + ", odds=" + odds + "]";
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public double getLastChange() {
		return lastChange;
	}

	public void setLastChange(double lastChange) {
		this.lastChange = lastChange;
	}

}
