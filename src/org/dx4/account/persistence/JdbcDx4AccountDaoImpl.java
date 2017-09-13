package org.dx4.account.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.AccountMgr;
import org.dx4.account.Dx4Account;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Rollup;
import org.dx4.account.Dx4Transaction;
import org.dx4.account.Dx4TransactionType;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Config;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcDx4AccountDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4AccountDao {

	private static final Logger log = Logger.getLogger(JdbcDx4AccountDaoImpl.class);
	
	@Override
	public void store(Dx4SecureUser baseUser) 
	{
		Dx4Account account = baseUser.getAccount();
		String sql = "INSERT INTO ACCOUNT ( USERID, BALANCE )" +
				" VALUES ( " + baseUser.getSeqId() + "," + account.getBalance() + ")";
		log.trace(sql);
		
		try
		{
			getJdbcTemplate().update(sql);
			baseUser.setAccount(getForUser(baseUser));							// refresh		
			storeDefaultNumberExpos(baseUser);		
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not store account " + e.getMessage());
		}
	}
	
	@Override
	public void update(Dx4SecureUser user) {
		Dx4Account account = user.getAccount();
		String sql = "UPDATE ACCOUNT SET BALANCE=" + account.getBalance() + " WHERE ID=" + account.getId();
	
		log.trace(sql);
		try
		{
			getJdbcTemplate().update(sql);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not update account " + e.getMessage());
		}
	}
	
	@Override
	public void updateBalance(Dx4Account account) {
		String sql = "UPDATE ACCOUNT SET BALANCE=" + account.getBalance() 
				+ " WHERE USERID=" + account.getUserId();
	
		log.trace(sql);
		try
		{
			getJdbcTemplate().update(sql);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not update balance " + e.getMessage());
		}
	}

	@Override
	public Dx4Account getForUser(Dx4SecureUser user) {
		String sql = "SELECT * FROM ACCOUNT WHERE USERID = " + user.getSeqId();
		log.trace("sql = "  + sql );
		
		try
		{
			return (Dx4Account) getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Dx4Account.class));
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not get account for user " + e.getMessage());
		}
	}

	@Override
	public void performWithdrawl(Dx4SecureUser user,double amount,Date date) {
		Dx4Transaction trans = AccountMgr.createWithrawl(user, amount, date);
		performTransaction(trans);
		Dx4Account account = user.getAccount();
		account.setBalance(account.getBalance()-trans.getAmount());
		updateBalance(account);
	}

	@Override
	public void performDeposit(Dx4SecureUser user,double amount,Date date) {
		Dx4Transaction trans = AccountMgr.createDeposit(user, amount, date);
		performTransaction(trans);
		Dx4Account account = user.getAccount();
		account.setBalance(account.getBalance()+trans.getAmount());
		updateBalance(account);
	}
	
	@Override
	public Dx4Transaction performPayment(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId) {
		Dx4Transaction ptrans = AccountMgr.createPayment(user, cp, amount, date, refId);
		performTransaction(ptrans);
		Dx4Account account = user.getAccount();
		account.setBalance(account.getBalance()-ptrans.getAmount());
		updateBalance(account);
		
		Dx4Transaction ctrans = AccountMgr.createCollect(cp, user, amount, date, refId);
		performTransaction(ctrans);
		account = cp.getAccount();
		account.setBalance(account.getBalance()+ctrans.getAmount());
		updateBalance(account);
		
		return ptrans;
	}

	@Override
	public Dx4Transaction performCollect(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId) {
		Dx4Transaction ctrans = AccountMgr.createCollect(user, cp, amount, date, refId);
		performTransaction(ctrans);
		Dx4Account account = user.getAccount();
		account.setBalance(account.getBalance()+ctrans.getAmount());
		updateBalance(account);
		
		Dx4Transaction ptrans = AccountMgr.createPayment(cp, user, amount, date, refId);
		performTransaction(ptrans);
		account = cp.getAccount();
		account.setBalance(account.getBalance()-ptrans.getAmount());
		updateBalance(account);
		
		return ctrans;
	}
	
	private long performTransaction(final Dx4Transaction trans)
	{
		final Timestamp t1 = new Timestamp(trans.getDate().getTime());	
		try
		{
			getJdbcTemplate().update("INSERT INTO XTRANSACTION (TYPE, AMOUNT, USERID, CPID, DATE, REFID) "
										+ "VALUES (?,?,?,?,?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setString(1, trans.getType().name().substring(0,1));
						ps.setDouble(2, trans.getAmount());
						ps.setLong(3, trans.getUserId());
						ps.setLong(4, trans.getCpId());
						ps.setTimestamp(5,t1);
						ps.setLong(6,trans.getRefId());
			      }
			    });	
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
		
		trans.setId(getLastTransId());
		return trans.getId();
	}

	private long getLastTransId() {
		String sql = "SELECT MAX(ID) FROM XTRANSACTION";
		Long max = getJdbcTemplate().queryForObject(sql,Long.class);
		return max;
	}
	
	public Dx4Transaction getTransactionForId(long id)
	{
		String sql = "SELECT * FROM XTRANSACTION WHERE ID = " + id;
		Dx4Transaction trans;
		try
		{
			trans = (Dx4Transaction) getJdbcTemplate().queryForObject(sql,new TransactionRowMapper());
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		return trans;
	}
	
	@Override
	public Dx4Rollup getRollup(Dx4SecureUser user)
	{
		String sql = "select type,sum(amount) from xtransaction where userid = " + user.getSeqId() + " group by type";
		List<RollupRow> rus = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(RollupRow.class));
		
		Dx4Rollup ru = new Dx4Rollup(user.getEmail(),user.getCode(),user.getRole());
		for (RollupRow r : rus)
		{
			Dx4TransactionType type = Dx4TransactionType.getFromInitial(r.getType().charAt(0));
			switch (type)
			{
				case Deposit : ru.setDeposit(r.getSum()); break;
				case Withdrawl : ru.setWithdrawl(r.getSum()); break;
				case Pay : ru.setPay(r.getSum()); break;
				case Collect : ru.setCollect(r.getSum()); break;
				default:
				break;
			}
		}
		return ru;
	}
	
	@Override
	public Integer getAccountActivityDetailsCount(Long userId)
	{
		String sql = "SELECT COUNT(*) FROM XTRANSACTION WHERE USERID=" + userId; 
		log.trace("getAccountActivityDetailsCount : " + sql);
		Integer count = getJdbcTemplate().queryForObject(sql,Integer.class);
		return count;
	}
	
	@Override
	public void storeDefaultNumberExpos(Dx4SecureUser user)
	{
		if (!user.getRole().isAgentRole())
			return;
		storeDefaultNumberExpos(user,'4');
		storeDefaultNumberExpos(user,'3');
		storeDefaultNumberExpos(user,'2');
	}
	
	private void storeDefaultNumberExpos(Dx4SecureUser user,char digits)
	{
		// dx4.num.exposure.bet.D2.ROLE_AGENT=2000
		// dx4.num.exposure.win.D2.ROLE_AGENT=-1
		
		String winString = "dx4.num.exposure.win.D"+digits+"."+user.getRole().name();
		String betString = "dx4.num.exposure.bet.D"+digits+"."+user.getRole().name();
		log.trace("using winString : " + winString);
		String value = Dx4Config.getProperties().getProperty(winString, "-1");
		double win = -1;
		double bet = -1;

		try
		{
			win = Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			log.error("Could not parse : " + winString + " value : " + value + "setting to -1 (unlimited)");
		}
		log.trace("using betString : " + betString);
		value = Dx4Config.getProperties().getProperty(betString, "-1");
		try
		{
			bet = Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			log.error("Could not parse : " + betString + " value : " + value + "setting to -1 (unlimited)");
		}
		
		Dx4NumberExpo nel = new Dx4NumberExpo(user.getSeqId(),Dx4NumberExpo.getDefaultNumberCode(digits),bet,win);
		log.trace("storing default exposure:" + nel + " digits:" + digits);
		storeDx4NumberExpo(nel,digits);
	}
	
	@Override
	public void deleteDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
	{
		String sql = "DELETE FROM NUMBEREXPO WHERE (USERID = " + numberExpo.getUserId() + " AND DIGITS='" + digits +
				"' AND NUMBER = '" + numberExpo.getNumber() +"')";
		log.trace("storeDx4NumberExpo : " + sql);
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void storeDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
	{
		String sql = "INSERT INTO NUMBEREXPO ( USERID, NUMBER, BETEXPO, WINEXPO, DIGITS, BLOCKED ) VALUES ( " +
		numberExpo.getUserId() + ",'" + numberExpo.getNumber() + "'," + numberExpo.getBetExpo()
			+ "," + numberExpo.getWinExpo() + ",'" + digits + "', 0 )";
		log.trace("storeDx4NumberExpo : " + sql);
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void udateDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
	{
		String sql = "UPDATE NUMBEREXPO SET BETEXPO=" + numberExpo.getBetExpo() +
				",WINEXPO=" + numberExpo.getWinExpo() + ",BLOCKED=" + numberExpo.getBlocked() +
				" WHERE (USERID = " + numberExpo.getUserId() + " AND DIGITS='" + digits +
				"' AND NUMBER = '" + numberExpo.getNumber() +"')";
		log.trace("udateDx4NumberExpo : " + sql);
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public List<Dx4NumberExpo> getDx4NumberExposForUser(Dx4SecureUser user,char digits,boolean winOrder)
	{
		String orderString = "BETEXPO";
		if (winOrder)
			orderString = "WINEXPO";
		String def = Dx4NumberExpo.getDefaultNumberCode(digits);
		String sql = "SELECT USERID, NUMBER, BETEXPO, WINEXPO, BLOCKED FROM NUMBEREXPO WHERE USERID=" + user.getSeqId() 
				+ " AND NUMBER<>'" + def + "' AND DIGITS='" + digits +
				"' ORDER BY " + orderString + " DESC";
		log.info("getDx4NumberExposForUser : " + sql);
		List<Dx4NumberExpo> expoList = (List<Dx4NumberExpo>) getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4NumberExpo.class));
		return expoList;
	}
	
	@Override
	public Dx4NumberExpo getDx4DefaultNumberExpoForUser(Dx4SecureUser user,char digits)
	{
		String def = Dx4NumberExpo.getDefaultNumberCode(digits);
		String sql = "SELECT USERID, NUMBER, BETEXPO, WINEXPO, BLOCKED FROM NUMBEREXPO WHERE USERID=" 
			+ user.getSeqId() + " AND NUMBER='" + def + "'" + " AND DIGITS='" + digits + "'";
		log.trace("getDx4DefaultNumberExpoForUser : " + sql);
		Dx4NumberExpo expo = (Dx4NumberExpo) getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(Dx4NumberExpo.class));
		return expo;
	}
	
	@Override
	public Dx4NumberExpo getDx4NumberExpoForUser(Dx4SecureUser user,String number)
	{
		String sql = "SELECT USERID, NUMBER, BETEXPO, WINEXPO, BLOCKED FROM NUMBEREXPO WHERE USERID=" 
			+ user.getSeqId() + " AND NUMBER='" + number + "'";
		log.trace("getDx4NumberExpoForUser : " + sql);
		Dx4NumberExpo expo;
		try
		{
			expo = (Dx4NumberExpo) getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(Dx4NumberExpo.class));
		}
		catch (DataAccessException e)
		{
			return null;
		}
		return expo;
	}
	
}
