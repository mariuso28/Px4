package org.dx4.json.message;

public class Dx4PayOutJson {

	private Dx4PayOutTypeJson type;
	private double payOut;
	
	public Dx4PayOutJson()
	{
	}

	public Dx4PayOutJson(Dx4PayOutTypeJson type,double payOut)
	{
		setType(type);
		setPayOut(payOut);
	}
	
	public Dx4PayOutTypeJson getType() {
		return type;
	}

	public void setType(Dx4PayOutTypeJson type) {
		this.type = type;
	}

	public double getPayOut() {
		return payOut;
	}

	public void setPayOut(double payOut) {
		this.payOut = payOut;
	}
	
}
