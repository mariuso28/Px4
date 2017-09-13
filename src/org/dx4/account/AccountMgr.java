package org.dx4.account;

import java.util.Date;

import org.dx4.secure.domain.Dx4SecureUser;

public class AccountMgr {
	
	
	public static Dx4Transaction createPayment(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId)
	{
		Dx4Transaction trans = new Dx4Transaction(user,cp,Dx4TransactionType.Pay,date,amount);
		trans.setRefId(refId);
		return trans;
	}
	
	public static Dx4Transaction createCollect(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId)
	{
		Dx4Transaction trans = new Dx4Transaction(user,cp,Dx4TransactionType.Collect,date,amount);
		trans.setRefId(refId);
		return trans;
	}
	
	public static Dx4Transaction createDeposit(Dx4SecureUser user,double amount,Date date)
	{
		Dx4Transaction trans = new Dx4Transaction(user,user,Dx4TransactionType.Deposit,date,amount);
		return trans;
	}
	
	public static Dx4Transaction createWithrawl(Dx4SecureUser user,double amount,Date date)
	{
		Dx4Transaction trans = new Dx4Transaction(user,user,Dx4TransactionType.Withdrawl,date,amount);
		return trans;
	}
	
}
