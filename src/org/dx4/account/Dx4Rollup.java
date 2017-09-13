package org.dx4.account;

import org.dx4.secure.domain.Dx4Role;

public class Dx4Rollup {
	
	private String email;
	private String code;
	private String role;
	private double deposit;
	private double withdrawl;
	private double pay;
	private double collect;
	private double balance;
	
	public Dx4Rollup(String email,String code,Dx4Role role)
	{
		setEmail(email);
		setCode(code);
		setRole(role.getShortCode());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getWithdrawl() {
		return withdrawl;
	}

	public void setWithdrawl(double withdrawl) {
		this.withdrawl = withdrawl;
	}

	public double getPay() {
		return pay;
	}

	public void setPay(double pay) {
		this.pay = pay;
	}

	public double getCollect() {
		return collect;
	}

	public void setCollect(double collect) {
		this.collect = collect;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
}
