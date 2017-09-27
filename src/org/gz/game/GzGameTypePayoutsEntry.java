package org.gz.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GzGameTypePayoutsEntry implements Serializable{

	private static final long serialVersionUID = 626201738087655661L;
	private String commission;
	private List<String> payOuts = new ArrayList<String>();
	
	public GzGameTypePayoutsEntry()
	{
	}
	
	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public List<String> getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(List<String> payOuts) {
		this.payOuts = payOuts;
	}

	@Override
	public String toString() {
		return "GzGameTypePayoutsEntry [commission=" + commission + ", payOuts=" + payOuts + "]";
	}
	
	
}
