package org.gz.account.persistence;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gz.account.GzAccount;
import org.gz.account.GzInvoice;
import org.gz.account.GzNumberRetainer;
import org.gz.account.GzPayment;
import org.gz.account.GzRollup;
import org.gz.account.GzTransaction;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.json.GzGameType;
import org.gz.web.summary.GzSummaryEntry;

public interface GzAccountDao {

	public void updateAccount(GzAccount account) throws GzPersistenceException;
	public void updateAccountBalance(GzAccount account,double amount) throws GzPersistenceException;
	public void storeAccount(GzAccount account) throws GzPersistenceException;
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException;
	public void storeTransaction(GzTransaction trans) throws GzPersistenceException;
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException;
	public Long getOutstandingTransactionCount();
	public GzRollup getRollupForUser(String userId,String code,GzRole role);
	public List<GzRollup> getMemberRollups(GzBaseUser currUser);
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException;
	public Long getXactionCount(GzBaseUser currUser);
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException;
	public GzInvoice getOpenInvoice(String payer, String payee) throws GzPersistenceException;
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void updateInvoice(double amount, double commission, double netAmount, long id) throws GzPersistenceException;
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException;
	public void storeInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void storePayment(GzPayment payment) throws GzPersistenceException;
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException;
	public void closeOpenInvoices() throws GzPersistenceException;
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException;
	public GzPayment getPaymentForId(long paymentNum) throws GzPersistenceException;
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public double getHigestDownstreamCommission(char type, String code);
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException;
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException;
	
	public void storeGzNumberRetainer(GzNumberRetainer nr);
	public void updateGzNumberRetainer(GzNumberRetainer nr);
	public List<GzNumberRetainer> getGzDefaultNumberRetainersForUser(GzBaseUser user, int digits);
	public GzNumberRetainer getGzNumberRetainerForUser(GzBaseUser user, GzGameType gameType, String number);
	public List<GzNumberRetainer> getGzIndividualNumberRetainersForUser(GzBaseUser user, int digits);
	
	public List<GzSummaryEntry> getSummaryEntries(GzBaseUser superior);
}
