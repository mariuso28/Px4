package org.dx4.account.persistence;

import java.util.Date;
import java.util.List;

import org.dx4.account.Dx4Account;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Rollup;
import org.dx4.account.Dx4Transaction;
import org.dx4.secure.domain.Dx4SecureUser;

public interface Dx4AccountDao {
	public Dx4Account getForUser(Dx4SecureUser user);
	public void update(Dx4SecureUser user);
	public void store(Dx4SecureUser user);
	public void updateBalance(Dx4Account account);
	
	public void performWithdrawl(Dx4SecureUser user,double amount,Date date);
	public void performDeposit(Dx4SecureUser user,double amount,Date date);
	public Dx4Transaction performPayment(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId);
	public Dx4Transaction performCollect(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId);	
	public Dx4Rollup getRollup(Dx4SecureUser user);
	public Integer getAccountActivityDetailsCount(Long userId);
	
	
	public void storeDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public void deleteDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public void udateDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public List<Dx4NumberExpo> getDx4NumberExposForUser(Dx4SecureUser user,char digits,boolean winOrder);
	public Dx4NumberExpo getDx4DefaultNumberExpoForUser(Dx4SecureUser user,char digits);
	public Dx4NumberExpo getDx4NumberExpoForUser(Dx4SecureUser user,String number);
	public void storeDefaultNumberExpos(Dx4SecureUser user);
	public Dx4Transaction getTransactionForId(long id);
	
}
