package org.gz.home.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.gz.account.GzAccount;
import org.gz.account.GzInvoice;
import org.gz.account.GzNumberRetainer;
import org.gz.account.GzPayment;
import org.gz.account.GzRollup;
import org.gz.account.GzTransaction;
import org.gz.account.persistence.GzAccountDao;
import org.gz.account.persistence.GzTransactionRowMapperPaginated;
import org.gz.account.persistence.GzXactionRowMapperPaginated;
import org.gz.admin.GzAdmin;
import org.gz.admin.persistence.GzAdminDao;
import org.gz.agent.GzAgent;
import org.gz.agent.persistence.GzAgentDao;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.baseuser.persistence.GzBaseUserDao;
import org.gz.home.GzHome;
import org.gz.json.GzGameType;
import org.gz.web.summary.GzSummaryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

public class GzHomeDaoImpl implements GzHome {
	protected static Logger log = Logger.getLogger(GzHomeDaoImpl.class);

	@Autowired
	private GzBaseUserDao gzBaseUserDao;
	@Autowired
	private GzAdminDao gzAdminDao;
	@Autowired
	private GzAgentDao gzAgentDao;
	@Autowired
	private GzAccountDao gzAccountDao;
	
	private String closeInvoiceStartAt;
	private String closeInvoiceAfterMins;
	private int closeInvoiceAfter;
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void overrideDataSourceUrl(String url)
	{
		BasicDataSource dataSource = (BasicDataSource) jdbcTemplate.getDataSource();
        dataSource.setUrl(url);
	}
	
	@Override
	public void getMembersForBaseUser(GzBaseUser baseUser) throws GzPersistenceException
	{
		gzBaseUserDao.getDownstreamForParent(baseUser);
	}
	
	@Override
	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException
	{
		gzBaseUserDao.storeBaseUser(baseUser);
	}
	
	@Override
	public GzBaseUser getBaseUserByEmail(String email) throws GzPersistenceException
	{
		return gzBaseUserDao.getBaseUserByEmail(email, GzBaseUser.class);
	}

	
	
	@Override
	public GzAgent getAgentByCode(String code) throws GzPersistenceException {
		return gzAgentDao.getAgentByCode(code);
	}

	@Override
	public void storeTransaction(GzTransaction transaction) throws GzPersistenceException {
		gzAccountDao.storeTransaction(transaction);
	}

	@Override
	public void storeAgent(GzAgent agent) throws GzPersistenceException {
		gzAgentDao.store(agent);
	}

	@Override
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException {
		return gzAgentDao.getAgentByEmail(email);
	}

	@Override
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException {
		gzBaseUserDao.updateBaseUserProfile(baseUser);
	}

	@Override
	public double getDownStreamCredit(GzBaseUser user, GzBaseUser parent) {
		return gzBaseUserDao.getDownStreamCredit(user,parent);
	}

	@Override
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException {
		gzAccountDao.updateTransaction(trans);
	}

	@Override
	public GzTransactionRowMapperPaginated getGzTransactionRowMapperPaginated(int count) {
		
		return new GzTransactionRowMapperPaginated(gzAccountDao,count);
	}

	@Override
	public GzXactionRowMapperPaginated getGzInvoiceRowMapperPaginated(GzBaseUser user,int count) {
		
		return new GzXactionRowMapperPaginated(user,gzAccountDao,count);
	}
	

	@Override
	public GzRollup getRollupForUser(String userId,String code,GzRole role) {
		return gzAccountDao.getRollupForUser(userId,code,role);
	}

	@Override
	public GzBaseUser getParentForUser(GzBaseUser currUser) throws GzPersistenceException{
		currUser.setParent(gzBaseUserDao.getBaseUserByCode(currUser.getParentCode()));
		return currUser.getParent();
	}

	@Override
	public List<GzRollup> getMemberRollups(GzBaseUser currUser) {
		
		return gzAccountDao.getMemberRollups(currUser);
	}

	@Override
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException{
	
		return gzAccountDao.getOutstandingInvoices(payer,payee);
	}

	@Override
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException {
		gzAccountDao.performWithdrawlDeposit(currAccountUser,dwType,dwAmount);
	}

	@Override
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException {
		
		return gzBaseUserDao.getBaseUserByCode(code);
	}

	@Override
	public void updateEnabled(GzBaseUser currAccountUser) throws GzPersistenceException {
		gzBaseUserDao.updateBaseUserProfile(currAccountUser);
	}

	@Override
	public void getDownstreamForParent(GzBaseUser currUser) {
		gzBaseUserDao.getDownstreamForParent(currUser);
	}

	@Override
	public GzInvoice getOpenInvoice(String payer, String payee) throws GzPersistenceException {
		return gzAccountDao.getOpenInvoice(payer,payee);
	}

	@Override
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException {
		gzAccountDao.updateInvoice(invoice);
	}
	
	@Override
	public void updateInvoice(double amount, double commission, double netAmount,  long id)
			throws GzPersistenceException {
		gzAccountDao.updateInvoice(amount, commission, netAmount, id);		
	}

	@Override
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException {
		gzAccountDao.updateSubInvoice(subInvoice,invoice);
	}

	@Override
	public void storeInvoice(GzInvoice invoice) throws GzPersistenceException {
		gzAccountDao.storeInvoice(invoice);
	}

	@Override
	public void storePayment(GzPayment payment) throws GzPersistenceException {
		gzAccountDao.storePayment(payment);
	}

	@Override
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException {
		gzAccountDao.changeInvoiceStatus(invoice,status);
	}

	@Override
	public void closeOpenInvoices() throws GzPersistenceException {
		gzAccountDao.closeOpenInvoices();
	}

	@Override
	public String getCloseInvoiceAfterMins() {
		return closeInvoiceAfterMins;
	}

	@Override
	public void setCloseInvoiceAfterMins(String closeInvoiceAfterMins) {
		this.closeInvoiceAfterMins = closeInvoiceAfterMins;
	}

	@Override
	public int getCloseInvoiceAfter() {
		return closeInvoiceAfter;
	}
	
	@Override
	public void setCloseInvoiceAfter(int closeInvoiceAfter) {
		this.closeInvoiceAfter = closeInvoiceAfter;
	}

	@Override
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException{
		return gzAccountDao.getInvoiceForId(invoiceNum);
	}

	@Override
	public GzPayment getPaymentForId(long paymentNum) throws GzPersistenceException{
		return gzAccountDao.getPaymentForId(paymentNum);
	}
	
	@Override
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException {
		return gzAccountDao.getInvoicesForInvoice(invoice);
	}

	@Override
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException {
		return gzAccountDao.getTransactionsForInvoice(invoice);
	}

		@Override
	public double getHigestDownstreamCommission(char type, String code){
		return gzAccountDao.getHigestDownstreamCommission(type, code);
	}

	@Override
	public String getEmailForId(UUID id) throws GzPersistenceException {
		return gzBaseUserDao.getEmailForId(id);
	}

	public void storeImage(String email, MultipartFile data, String contentType) throws GzPersistenceException
	{
		gzBaseUserDao.storeImage(email,data,contentType);
	}

	@Override
	public byte[] getImage(String email) throws GzPersistenceException {
		
		return gzBaseUserDao.getImage(email);
	}

	@Override
	public void updateAccount(GzAccount account) throws GzPersistenceException {
		gzAccountDao.updateAccount(account);
	}

	@Override
	public void updateAccountBalance(GzAccount account, double amount) throws GzPersistenceException {
		gzAccountDao.updateAccountBalance(account, amount);
	}

	@Override
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException {
		return gzAccountDao.getAccount(baseUser);
	}

	@Override
	public void setDefaultPasswordForAll(String encoded) {
		gzBaseUserDao.setDefaultPasswordForAll(encoded);
	}

	public String getCloseInvoiceStartAt() {
		return closeInvoiceStartAt;
	}

	public void setCloseInvoiceStartAt(String closeInvoiceStartAt) {
		this.closeInvoiceStartAt = closeInvoiceStartAt;
	}

	@Override
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException {
		return gzAccountDao.getOutstandingInvoiceAmounts(user);
	}

	@Override
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException{
		return parent.getAccount().getBalance() - gzAccountDao.getDownStreamAccountBalance(user, parent);
	}

	public void updateLeafInstance(GzBaseUser bu)
	{
		gzBaseUserDao.updateLeafInstance(bu);
	}

	@Override
	public List<String> getColumns(String table) throws GzPersistenceException{

		List<String> cols = new ArrayList<String>();
        try {
            // Gets the metadata of the database
        	Connection conn = getJdbcTemplate().getDataSource().getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getColumns(null, null, table, null);
                while (rs.next()) {
                    String colummName = rs.getString("COLUMN_NAME");
                    cols.add(colummName);
            }
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new GzPersistenceException(e.getMessage());
        }
        return cols;
    }

	@Override
	public GzAdmin getAdminByMemberId(String memberId) throws GzPersistenceException {
		return gzAdminDao.getAdminByMemberId(memberId);
	}

	@Override
	public GzBaseUser getBaseUserByMemberId(String memberId) throws GzPersistenceException {
		return gzBaseUserDao.getBaseUserByMemberId(memberId);
	}

	@Override
	public List<GzBaseUser> search(GzBaseUser user, String term,String type) throws GzPersistenceException {
	
		return gzBaseUserDao.search(user,term,type);
	}
	
	@Override
	public void storeGzNumberRetainer(GzNumberRetainer nr)
	{
		gzAccountDao.storeGzNumberRetainer(nr);
	}
	
	@Override
	public void updateGzNumberRetainer(GzNumberRetainer nr)
	{
		gzAccountDao.updateGzNumberRetainer(nr);
	}
	
	@Override
	public List<GzNumberRetainer> getGzDefaultNumberRetainersForUser(GzBaseUser user, int digits)
	{
		return gzAccountDao.getGzDefaultNumberRetainersForUser(user, digits);
	}
	
	@Override
	public GzNumberRetainer getGzNumberRetainerForUser(GzBaseUser user, GzGameType gameType, String number)
	{
		return gzAccountDao.getGzNumberRetainerForUser(user, gameType, number);
	}
	
	@Override
	public List<GzNumberRetainer> getGzIndividualNumberRetainersForUser(GzBaseUser user, int digits)
	{
		return gzAccountDao.getGzIndividualNumberRetainersForUser(user, digits);
	}

	@Override
	public List<GzSummaryEntry> getSummaryEntries(GzBaseUser superior) {
		return gzAccountDao.getSummaryEntries(superior);
	}
}
