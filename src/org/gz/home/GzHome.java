package org.gz.home;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gz.account.GzAccount;
import org.gz.account.GzInvoice;
import org.gz.account.GzNumberRetainer;
import org.gz.account.GzPayment;
import org.gz.account.GzRollup;
import org.gz.account.GzTransaction;
import org.gz.account.persistence.GzTransactionRowMapperPaginated;
import org.gz.account.persistence.GzXactionRowMapperPaginated;
import org.gz.admin.GzAdmin;
import org.gz.agent.GzAgent;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.json.GzGameType;
import org.gz.web.summary.GzSummaryEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

public interface GzHome 
{	
	public JdbcTemplate getJdbcTemplate();
	
	// general + account stuff
	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzBaseUser getBaseUserByEmail(String email) throws GzPersistenceException;
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException;
	public void getMembersForBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzAgent getAgentByCode(String code) throws GzPersistenceException;
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException;
	public GzAdmin getAdminByMemberId(String memberId) throws GzPersistenceException;
	public void storeTransaction(GzTransaction transaction) throws GzPersistenceException;
	public void updateAccount(GzAccount account) throws GzPersistenceException;
	public void storeAgent(GzAgent agent) throws GzPersistenceException;
	public double getDownStreamCredit(GzBaseUser user, GzBaseUser parent);
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException;
	public GzTransactionRowMapperPaginated getGzTransactionRowMapperPaginated(int count) throws GzPersistenceException;
	public GzRollup getRollupForUser(String userId,String code,GzRole role);
	public GzBaseUser getParentForUser(GzBaseUser currUser) throws GzPersistenceException;
	public List<GzRollup> getMemberRollups(GzBaseUser currUser);
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException;
	public GzXactionRowMapperPaginated getGzInvoiceRowMapperPaginated(GzBaseUser user, int count);
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException;
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException;
	public void updateEnabled(GzBaseUser currAccountUser) throws GzPersistenceException;
	public void getDownstreamForParent(GzBaseUser currUser);
	public GzInvoice getOpenInvoice(String payer, String payee) throws GzPersistenceException;
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void updateInvoice(double amount, double commission, double netAmount, long id) throws GzPersistenceException;
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException;
	public void storeInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void storePayment(GzPayment payment) throws GzPersistenceException;
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException;
	public void closeOpenInvoices() throws GzPersistenceException;
	public String getCloseInvoiceAfterMins();
	public void setCloseInvoiceAfterMins(String closeInvoiceAfterMins);
	public int getCloseInvoiceAfter();
	public void setCloseInvoiceAfter(int closeInvoiceAfter);
	public String getCloseInvoiceStartAt();
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException;
	public GzPayment getPaymentForId(long paymentNum) throws GzPersistenceException;
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public double getHigestDownstreamCommission(char type, String code);
	public String getEmailForId(UUID id) throws GzPersistenceException;
	public void storeImage(String email, MultipartFile data, String contentType) throws GzPersistenceException;
	public byte[] getImage(final String email) throws GzPersistenceException;
	public void updateAccountBalance(GzAccount account,double amount) throws GzPersistenceException;
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException;
	public void setDefaultPasswordForAll(String encoded);
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException;
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException;
	public void updateLeafInstance(GzBaseUser bu);
	
	public void overrideDataSourceUrl(String url);

	public List<String> getColumns(String table) throws GzPersistenceException;
	public GzBaseUser getBaseUserByMemberId(String memberId) throws GzPersistenceException;

	public List<GzBaseUser> search(GzBaseUser user, String term, String type) throws GzPersistenceException;


	public void storeGzNumberRetainer(GzNumberRetainer nr);
	public void updateGzNumberRetainer(GzNumberRetainer nr);
	public List<GzNumberRetainer> getGzDefaultNumberRetainersForUser(GzBaseUser user, int digits);
	public GzNumberRetainer getGzNumberRetainerForUser(GzBaseUser user, GzGameType gameType, String number);
	public List<GzNumberRetainer> getGzIndividualNumberRetainersForUser(GzBaseUser user, int digits);
	
	public List<GzSummaryEntry> getSummaryEntries(GzBaseUser superior);
	
	
}
