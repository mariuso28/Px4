package org.dx4.account;

import java.io.Serializable;

public class Dx4Account implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6017806869660539670L;
	private long id;
	private long userId;
	private double balance;
	
	public Dx4Account()
	{
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

}
