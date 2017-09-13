package org.dx4.bet.persistence;

import java.util.Date;

public class SumDate {

	private double sum;
	private Date date;
	
	public SumDate()
	{
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SumDate [sum=" + sum + ", date=" + date + "]";
	}
	
}
