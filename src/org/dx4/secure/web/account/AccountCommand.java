package org.dx4.secure.web.account;

import java.util.ArrayList;
import java.util.List;

import org.dx4.account.Dx4Account;


public class AccountCommand{

	private Dx4Account newAccount;
	private double dwAmount;
	private String dwType;
	private String comment;
	private List<Long> invoiceIds;
	private List<String> payFlags;
	private Boolean newAccountFlag;

	public AccountCommand()
	{
		invoiceIds = new ArrayList<Long>();
		payFlags = new ArrayList<String>();
		setDwAmount(0.0);
		setComment("");
	}
	
	public void setNewAccount(Dx4Account newAccount) {
		this.newAccount = newAccount;
	}

	public Dx4Account getNewAccount() {
		return newAccount;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}
	
	public void setDwType(String dwType) {
		this.dwType = dwType;
	}

	public String getDwType() {
		return dwType;
	}

	public void setDwAmount(double dwAmount) {
		this.dwAmount = dwAmount;
	}

	public double getDwAmount() {
		return dwAmount;
	}

	@Override
	public String toString() {
		return "AccountCommand [newAccount=" + newAccount + ", dwAmount="
				+ dwAmount + ", dwType=" + dwType + ", comment=" + comment
				+ "]";
	}

	public void setPayFlags(List<String> payFlags) {
		this.payFlags = payFlags;
	}

	public List<String> getPayFlags() {
		return payFlags;
	}

	public void setInvoiceIds(List<Long> invoiceIds) {
		this.invoiceIds = invoiceIds;
	}

	public List<Long> getInvoiceIds() {
		return invoiceIds;
	}

	public Boolean getNewAccountFlag() {
		return newAccountFlag;
	}

	public void setNewAccountFlag(Boolean newAccountFlag) {
		this.newAccountFlag = newAccountFlag;
	}

	
	
}
