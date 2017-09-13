package org.dx4.bet;

import java.io.Serializable;
import java.util.Date;

public class Dx4DateWin implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1045057255057523933L;
	private double win;
	private Date date;
	
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Dx4DateWin [win=" + win + ", date=" + date + "]";
	}
	
	
}
