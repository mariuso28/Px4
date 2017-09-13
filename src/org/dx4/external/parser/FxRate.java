package org.dx4.external.parser;

import java.util.Date;

public class FxRate {

	private String from;
	private String to;
	private Date timestamp;
	private double rate;
	
	public FxRate()
	{
	}
	
	public FxRate(String from,String to,Date timestamp,double rate)
	{
		setFrom(from);
		setTo(to);
		setTimestamp(timestamp);
		setRate(rate);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "FxRate [from=" + from + ", to=" + to + ", timestamp=" + timestamp + ", rate=" + rate + "]";
	}
	
}
