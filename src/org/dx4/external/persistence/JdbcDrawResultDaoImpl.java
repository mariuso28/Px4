package org.dx4.external.persistence;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcDrawResultDaoImpl extends NamedParameterJdbcDaoSupport implements DrawResultDao {
	
	private static final Logger log = Logger.getLogger(JdbcDrawResultDaoImpl.class);
	
	private static String selectStr = "SELECT DRAW.ID AS ID,DATE,DRAWNO,PROVIDERID,FIRSTPLACE,SECONDPLACE,THIRDPLACE FROM DRAW,PROVIDER ";
	private static String joinStr = " AND (DRAW.PROVIDERID = PROVIDER.ID) ORDER BY PROVIDER.ID";
	
	@Override
	public void store(Dx4DrawResultJson result) {
		
		long id = getProviderId(result.getProvider().getName());
		if (id<0)
		{
			storeProvider(result.getProvider().getName());
			id = getProviderId(result.getProvider().getName());
			if (id<0)
				throw new PersistenceRuntimeException("JdbcDrawResultDaoImpl : error on provider", "getProviderId");
		}
		
//		removeOldResult(result);
		Timestamp t1 = new Timestamp(result.getDate().getTime());
		
		String sql = "INSERT INTO DRAW ( PROVIDERID, DRAWNO, DATE, FIRSTPLACE, SECONDPLACE, THIRDPLACE ) VALUES ( " 
			+ id 
			+ ",'" + result.getDrawNo() + "'"
			+ ",'" + t1 
			+ "','" + result.getFirstPlace() + "'"
			+ ",'" + result.getSecondPlace() + "'"
			+ ",'" + result.getThirdPlace()
			+ "')";
		log.trace(sql);
		getJdbcTemplate().update(sql);
		long drawId = updatePlacings(result);
		result.setId(drawId);
	}
	
	@SuppressWarnings("unused")
	private void removeOldResult(Dx4DrawResultJson result) {
		Timestamp t1 = new Timestamp(result.getDate().getTime());
		String sql = "SELECT ID FROM DRAW WHERE PROVIDERID=" + "" +
				"(SELECT ID FROM PROVIDER WHERE NAME ='" + result.getProvider() + "')" +
			" AND DRAWNO ='" + result.getDrawNo() + "' AND DATE='" + t1 + "'" ;
		
		long pid;
		try
		{
			pid = getJdbcTemplate().queryForObject(sql,Long.class);
		}
		catch (DataAccessException e)
		{
			return;
		}
		
		sql = "DELETE FROM DRAW WHERE ID=" + pid;
		getJdbcTemplate().update(sql);
		sql = "DELETE FROM DPLACING WHERE DRAWID=" + pid;
		getJdbcTemplate().update(sql);
	}

	@Override
	public void removeResultsForDate(Date date) {
		Timestamp t1 = new Timestamp(date.getTime());
		String sql = "SELECT ID FROM DRAW WHERE DATE='" + t1 + "'";
		
		List<Long> pids;
		try
		{
			pids = getJdbcTemplate().queryForList(sql,Long.class);
		}
		catch (DataAccessException e)
		{
			return;
		}
		
		for (Long pid : pids)
		{
			sql = "DELETE FROM DRAW WHERE ID=" + pid;
			getJdbcTemplate().update(sql);
			sql = "DELETE FROM DPLACING WHERE DRAWID=" + pid;
			getJdbcTemplate().update(sql);
		}
	}
	
	@Override
	public void removeResultsForProviderDate(Date date,char c) {
		Timestamp t1 = new Timestamp(date.getTime());
		String sql = "select d.id from draw as d " +
				" join provider as p on d.providerid = p.id " +
				" where date = '" +t1 +"' and p.code = '" + Character.toString(c) + "'";
		
		List<Long> pids;
		try
		{
			pids = getJdbcTemplate().queryForList(sql,Long.class);
		}
		catch (DataAccessException e)
		{
			return;
		}
		
		for (Long pid : pids)
		{
			sql = "DELETE FROM DRAW WHERE ID=" + pid;
			getJdbcTemplate().update(sql);
			sql = "DELETE FROM DPLACING WHERE DRAWID=" + pid;
			getJdbcTemplate().update(sql);
		}
	}
		
	private long updatePlacings(Dx4DrawResultJson result) {
		String sql = "SELECT MAX(ID) FROM DRAW";
		long pid;
		try
		{
			pid = getJdbcTemplate().queryForObject(sql,Long.class);
		}
		catch (DataAccessException e)
		{
			throw new PersistenceRuntimeException("JdbcDrawResultDaoImpl : error on updatePlacings : sql:", sql);
		}
		
		sql = "INSERT INTO DPLACING (DRAWID,PLACE,NUMBER) VALUES (" + pid + ",'F','" 
					+ result.getFirstPlace()+ "')";
		getJdbcTemplate().update(sql);
		sql = "INSERT INTO DPLACING (DRAWID,PLACE,NUMBER) VALUES (" + pid + ",'S','" 
		+ result.getSecondPlace()+ "')";
		getJdbcTemplate().update(sql);
		sql = "INSERT INTO DPLACING (DRAWID,PLACE,NUMBER) VALUES (" + pid + ",'T','" 
		+ result.getThirdPlace()+ "')";
		getJdbcTemplate().update(sql);		
		for (String special : result.getSpecials())
		{
			sql = "INSERT INTO DPLACING (DRAWID,PLACE,NUMBER) VALUES (" + pid + ",'P','" 
			+ special + "')";
			getJdbcTemplate().update(sql);	
		}
		for (String consulation : result.getConsolations())
		{
			sql = "INSERT INTO DPLACING (DRAWID,PLACE,NUMBER) VALUES (" + pid + ",'C','" 
			+ consulation + "')";
			getJdbcTemplate().update(sql);	
		}
		
		return pid;
	}

	private void storeProvider(String provider) {
		String sql = "INSERT INTO PROVIDER (NAME) VALUES ('" + provider + "')";
		log.trace(sql);
		getJdbcTemplate().update(sql);
	}

	private long getProviderId(String name)
	{
		String sql = "SELECT ID FROM PROVIDER WHERE NAME=" + "'" + name + "'";
		try
		{
			long id = getJdbcTemplate().queryForObject(sql,Long.class);
			return id;
		}
		catch (DataAccessException e)
		{
			return -1L;
		}
	}
	
	@Override
	public List<Dx4DrawResultJson> getResultsForProvider(String provider) {
		String sql = selectStr + " WHERE PROVIDER = '" + provider + "' " + joinStr;
		log.trace("getResultsForProvider sql = "  + sql );
		
		List<Dx4DrawResultJson> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
		return results;
	}
	
	@Override
	public void populateSpecialsAndConsolations(Dx4DrawResultJson result)
	{
		String sql = "SELECT NUMBER FROM DPLACING WHERE DRAWID = " + result.getId() + " AND PLACE='P'";
		try
		{
			log.trace("populateSpecialsAndConsolations : " + sql);
			List<String> numbers = (List<String>) getJdbcTemplate().queryForList(sql,String.class);
			result.setSpecials(numbers);
		}
		catch (DataAccessException e)
		{
			;
		}
		sql = "SELECT NUMBER FROM DPLACING WHERE DRAWID = " + result.getId() + " AND PLACE='C'";
		try
		{
			log.trace("populateSpecialsAndConsolations : " + sql);
			List<String> numbers = (List<String>) getJdbcTemplate().queryForList(sql,String.class);
			result.setConsolations(numbers);
		}
		catch (DataAccessException e)
		{
			;
		}
	}

	@Override
	public List<Dx4DrawResultJson> getResultsForNumber(String number,Date startDate,Date endDate) {
		
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = selectStr + 
				" WHERE DRAW.ID IN (SELECT DRAWID FROM DPLACING WHERE " 
				+ "(PLACE='F' OR PLACE='S' OR PLACE='T') AND NUMBER = '" + number + "')" +
				" AND (DATE<='" + d2.toString() + "' AND DATE>='" + d1.toString() + "')" +
				joinStr;
		log.trace("sql = "  + sql );
		
		List<Dx4DrawResultJson> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
		return results;
	}
	
	@Override
	public List<Dx4DrawResultJson> getResults(Date startDate,Date endDate) {
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = selectStr  
				+ " WHERE DATE<='" + d2.toString() + "' AND DATE>='" + d1.toString() + "'" 
				+ joinStr;
//		log.info("sql = "  + sql );
	
		List<Dx4DrawResultJson> results =  getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
		return results;
	}

	@Override
	public List<Dx4DrawResultJson> getLatestDrawResults() {
		String sql = "SELECT DRAW.ID AS ID,DATE,DRAWNO,PROVIDERID,FIRSTPLACE,SECONDPLACE,THIRDPLACE FROM DRAW WHERE DATE IN " +
		"(SELECT DISTINCT(DATE) FROM DRAW ORDER BY DATE DESC LIMIT 5) ORDER BY PROVIDERID,DATE DESC";
		
		return getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
	}
	
	@Override
	public List<Date> getResultsDateRange() {
		String sql = "SELECT MIN(DATE) FROM DRAW";						// dunno why don't work in one query
		log.trace("sql = "  + sql );
		Date date1 = getJdbcTemplate().queryForObject(sql,Date.class);
		sql = "SELECT MAX(DATE) FROM DRAW";
		log.trace("sql = "  + sql );
		Date date2 = getJdbcTemplate().queryForObject(sql,Date.class);
		
		List<Date> dates = new ArrayList<Date>();
		dates.add(date1);
		dates.add(date2);
		log.trace("Got dates:" + dates);
		return dates;
	}

	@Override
	public List<Dx4DrawResultJson> getResultsForNumberPart(String number,Date startDate,Date endDate) {
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = selectStr + " WHERE DRAW.ID IN " +
				"(SELECT DRAWID FROM DPLACING WHERE (PLACE='F' OR PLACE='S' OR PLACE='T')" +
				" AND NUMBER LIKE '%" + number + "')" +
				" AND (DATE<='" + d2.toString() + "' AND DATE>='" + d1.toString() + "')" +
				joinStr;
				
		log.trace("sql = "  + sql );
	
		List<Dx4DrawResultJson> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
		return results;
	}

	@Override
	public String getNextDrawNoForProvider(String provider)
	{
		String sql = "SELECT DRAWNO FROM DRAW WHERE ID = (SELECT MAX(ID) FROM DRAW WHERE PROVIDERID = "
					+ "(SELECT ID FROM PROVIDER WHERE NAME='"+provider+"'))";
		
		GregorianCalendar gc = new GregorianCalendar();
		String ystr = (new Integer(gc.get(Calendar.YEAR))).toString().substring(2);
		try
		{
			log.trace("getNextDrawNoForProvider - sql :" + sql);
			String result = getJdbcTemplate().queryForObject(sql,String.class);
			log.trace("getNextDrawNoForProvider - got :" + result);
			int slash = result.indexOf("/");
			String yy = result.substring(slash+1);
			String num = result.substring(0,slash);
			if (!yy.equals(ystr))
				return "1/"+ystr;
			int next = new Integer(num)+1;
			return "" + next + "/" + ystr;
		}
		catch (DataAccessException e)
		{
			return "1/"+ystr;
		}
	}

	@Override
	public Dx4DrawResultJson getResult(long id) {
	
		String sql = "SELECT * FROM DRAW WHERE ID = " + id;
		log.trace("sql = "  + sql );
		
		Dx4DrawResultJson result = getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(Dx4DrawResultJson.class));
		return result;
	}

	@Override
	public List<Dx4NumberRevenueJson> getNumberRevenues(Date startDate,Date endDate)
	{
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = "SELECT SUM(PRIZE) AS REVENUE,NUMBER FROM PRIZE,DPLACING WHERE " +
		"(PRIZE.PLACE = DPLACING.PLACE) AND NUMBER IN (SELECT NUMBER FROM DPLACING) AND DRAWID IN " +
		"(SELECT ID FROM DRAW WHERE (DATE<='" + d2.toString() + "' AND DATE>='" + d1.toString() + "')) " +  
		"GROUP BY NUMBER ORDER BY REVENUE DESC LIMIT 200";
				
		log.trace("sql = "  + sql );
	
		List<Dx4NumberRevenueJson> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4NumberRevenueJson.class));
		return results;
	}
	
	@Override
	public double getRevenueForNumber(String number,Date startDate,Date endDate)
	{
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = "SELECT SUM(PRIZE) AS REVENUE FROM PRIZE,DPLACING WHERE " +
		"(PRIZE.PLACE = DPLACING.PLACE) AND NUMBER = '" + number + "' AND DRAWID IN " +
		"(SELECT ID FROM DRAW WHERE (DATE<='" + d2.toString() + "' AND DATE>='" + d1.toString() + "')) ";
				
	//	log.info("sql = "  + sql );
	
		List<Double> results;  
		try
		{
			results = getJdbcTemplate().queryForList(sql,Double.class);
			if (!results.isEmpty())
				return results.get(0);
			return 0.0;
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException(e.getMessage());
		}
	}
	
	public List<Dx4PlacingJson> getPlacingsForNumber(String number,Date startDate,Date endDate)
	{
		Timestamp d1 = new Timestamp(startDate.getTime());
		Timestamp d2 = new Timestamp(endDate.getTime());
		String sql = "SELECT DATE,DRAWNO,NAME AS PROVIDER,PLACE FROM DRAW,PROVIDER,DPLACING " +
					" WHERE (DRAW.PROVIDERID = PROVIDER.ID AND DPLACING.DRAWID=DRAW.ID)" +
					" AND (DRAW.DATE<='" + d2.toString() + "' AND DRAW.DATE>='" + d1.toString() + "')" +
					" AND (DPLACING.NUMBER='" + number + "') ORDER BY DATE DESC";
		log.trace(sql);
		
		List<Dx4PlacingJson> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4PlacingJson.class));
		for (Dx4PlacingJson result : results)
			result.setPayOutType(Dx4PayOutTypeJson.valueOfFromCode(result.getPlace().charAt(0)));
		
		return results;
	}
	/*
	@Override
	public String getDescForNumber(String number)
	{
		String gNum = number.substring(number.length()-3);
		Integer num = Integer.parseInt(gNum);
		String sql = "SELECT DESC FROM NUMBER WHERE NUMBER = " + num;
		log.trace(sql);
		return getJdbcTemplate().queryForObject(sql,String.class);
	}
	
	@Override
	public List<NumberPageElement> getNumberPageElements()
	{
		String sql = "SELECT * FROM NUMBER ORDER BY NUMBER";
		log.trace(sql);
		return getJdbcTemplate().query(sql,new NumberPageElementRowMapper());
	}
	
	@Override
	public List<NumberPageElement> getNumberPageElementsByDesc(String searchTerm)
	{
		String[] toks = searchTerm.split(" ");
		String sql = "SELECT * FROM NUMBER WHERE ";
		for (String tok : toks)
		{
			sql += "DESC LIKE '%" + tok + "%'";
			sql += " OR ";
		}
		sql = sql.substring(0,sql.length()-4);
		log.info(sql);
		return getJdbcTemplate().query(sql,new NumberPageElementRowMapper());
	}
	
	@Override 
	public void getDescsForDrawResult(DrawResult result)
	{
		result.setFirstDesc(getDescForNumber(result.getFirstPlace()));
		result.setSecondDesc(getDescForNumber(result.getSecondPlace()));
		result.setThirdDesc(getDescForNumber(result.getThirdPlace()));
	}
	*/
	
	@Override
	public List<Date> getDrawDates()
	{
		String sql = "SELECT DISTINCT(DATE) FROM DRAW ORDER BY DATE DESC";
		log.trace(sql);
		List<DateWrap> stamps = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(DateWrap.class));
		List<Date> dates = new ArrayList<Date>();
		for (DateWrap stamp : stamps)
			dates.add(stamp.getDate());
		return dates;
	}
	
	public Date getPrevDrawDate(Date date)
	{
		Timestamp d1 = new Timestamp(date.getTime());
		String sql = "SELECT DATE FROM DRAW WHERE DATE<'" + d1.toString() + "' ORDER BY DATE DESC FETCH FIRST 1 ROW ONLY";
		log.trace(sql);
		List<DateWrap> stamps = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(DateWrap.class));
		if (stamps.isEmpty())
			return null;
		return stamps.get(0).getDate();
	}
	
	public Date getNextDrawDate(Date date)
	{
		Timestamp d1 = new Timestamp(date.getTime());
		String sql = "SELECT DATE FROM DRAW WHERE DATE>'" + d1.toString() + "' ORDER BY DATE ASC FETCH FIRST 1 ROW ONLY";
		log.trace(sql);
		List<DateWrap> stamps = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(DateWrap.class));
		if (stamps.isEmpty())
			return null;
		return stamps.get(0).getDate();
	}

	
}
