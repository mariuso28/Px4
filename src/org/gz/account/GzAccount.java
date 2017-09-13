package org.gz.account;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;

public class GzAccount 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAccount.class);	
	private GzBaseUser baseUser;
	private double balance;
	private double credit;
	private double betCommission;
	private double winCommission;
	
	public GzAccount()
	{
	}
	
	public GzAccount(GzBaseUser baseUser)
	{
		setBaseUser(baseUser);
	}
	
	public void updateBalance(double stake) {
		balance += stake;
	}
	
	public void addBalance(double amount)
	{
		balance += amount;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public GzBaseUser getBaseUser() {
		return baseUser;
	}

	public void setBaseUser(GzBaseUser baseUser) {
		this.baseUser = baseUser;
	}

	public double getBetCommission() {
		return betCommission;
	}

	public void setBetCommission(double betCommission) {
		this.betCommission = betCommission;
	}

	public double getWinCommission() {
		return winCommission;
	}

	public void setWinCommission(double winCommission) {
		this.winCommission = winCommission;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

 }
