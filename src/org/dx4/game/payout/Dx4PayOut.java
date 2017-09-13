package org.dx4.game.payout;

import java.io.Serializable;

import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4PayOut implements Serializable
{
	private static final long serialVersionUID = 2978288001370992497L;
	private Dx4PayOutTypeJson type;
	private double payOut;
	
	public Dx4PayOut()
	{
	}
	
	public Dx4PayOut(Dx4PayOut use)
	{
		setType(use.getType());
		setPayOut(use.getPayOut());
	}
	
	public Dx4PayOut(Dx4PayOutTypeJson type,double payOut)
	{
		setType(type);
		setPayOut(payOut);
	}
	
	public void setType(Dx4PayOutTypeJson type) {
		this.type = type;
	}
	public Dx4PayOutTypeJson getType() {
		return type;
	}
	public void setPayOut(double payOut) {
		this.payOut = payOut;
	}
	public double getPayOut() {
		return payOut;
	}

	@Override
	public String toString() {
		return "Dx4PayOut [type=" + type + ", payOut=" + payOut + "]";
	}
	
	
}
