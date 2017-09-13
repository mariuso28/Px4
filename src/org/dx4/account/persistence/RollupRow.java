package org.dx4.account.persistence;

public class RollupRow 
{
	private String type;
	private double sum;
	
	public RollupRow()
	{
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

}
