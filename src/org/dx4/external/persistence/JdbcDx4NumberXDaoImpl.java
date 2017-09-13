package org.dx4.external.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4ZodiacJson;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcDx4NumberXDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4NumberXDao{

	private static final Logger log = Logger.getLogger(JdbcDx4NumberXDaoImpl.class);
	
	@Override
	public Dx4NumberPageElementJson getNumberPageElement(String number,Character dictionary)
	{
		String sql;
		int num;
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYMODERN4)
		{
			num = Integer.parseInt(number);
			sql = "SELECT * FROM NUMBER4M WHERE NUMBERX = ?";
		}
		else
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYTRADITIONAL3)	
		{
			num = Integer.parseInt(number.substring(number.length()-3));
			sql = "SELECT * FROM NUMBER3T WHERE NUMBERX = ?";
		}
		else
		{
			num = Integer.parseInt(number.substring(number.length()-3));
			sql = "SELECT * FROM NUMBER3S WHERE NUMBERX = ?";
		}
		
		log.trace(sql);
		return getJdbcTemplate().queryForObject(sql,new Object[] { num },new NumberPageElementRowMapperX());
	}
	
	@Override
	public List<Dx4NumberPageElementJson> getNumberPageElements(String number)
	{
		Integer num = Integer.parseInt(number);
		String sql = "SELECT * FROM NUMBERX WHERE NUMBERX = " + num;
		log.trace(sql);
		return getJdbcTemplate().query(sql,new NumberPageElementRowMapperX());
	}
	
	@Override
	public List<Dx4NumberPageElementJson> getNumberPageElementsRange(int num1, int num2, Character dictionary) {
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYALL)
		{
			String sql = "SELECT * FROM NUMBERX WHERE NUMBERX >=? AND NUMBERX <=? ORDER BY NUMBERX";
			return getJdbcTemplate().query(sql,new Object[] { num1, num2 },new NumberPageElementRowMapperX());
		}
		String sql;
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYMODERN4)
		{
			sql = "SELECT * FROM NUMBER4M WHERE NUMBERX >=? AND NUMBERX <=? ORDER BY NUMBERX";
			return getJdbcTemplate().query(sql,new Object[] { num1, num2 },new NumberPageElementRowMapperX());
		}
		else
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYTRADITIONAL3)	
		{
			sql = "SELECT * FROM NUMBER3T WHERE NUMBERX >=? AND NUMBERX <=? ORDER BY NUMBERX";
			return getJdbcTemplate().query(sql,new Object[] { num1, num2 },new NumberPageElementRowMapperX());
		}
		sql = "SELECT * FROM NUMBER3S WHERE NUMBERX >=? AND NUMBERX <=? ORDER BY NUMBERX";
		return getJdbcTemplate().query(sql,new Object[] { num1, num2 },new NumberPageElementRowMapperX());
	}
	
	@Override
	public void storeNumberSearchTerm(final NumberSearchTerm nst)
	{
		try
		{	
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String INSERT_SQL = "INSERT INTO NUMBERSEARCH (TERM) VALUES (?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
			            ps.setString(1, nst.getTerm().toLowerCase());
			            return ps;
			        }
			    },
			    keyHolder);
			
			log.info("key holder: " + keyHolder.getKey().longValue());
			final long link = keyHolder.getKey().longValue();
			for (final NumberSearchEntry nse : nst.getNumbers())
			{
				getJdbcTemplate().update("INSERT INTO NUMBERSEARCHLINK (NUMBER, LINK, DICTIONARY) VALUES (?,?,?)"
						, new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setString(1,nse.getNumber());
								ps.setLong(2,link);
								ps.setString(3,Character.toString(nse.getDictionary()));
							}
						});
			}
		}
		catch (DataAccessException e)
		{
			if (e.getMessage().contains("duplicate key value"))
			{
				log.error("Term already saved");
				return;
			}
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public List<NumberSearchEntry> getNumbersFormTerm(String term) {
		String sql = "select number,dictionary from numbersearchlink as nsl join numbersearch as ns on ns.id=nsl.link where ns.term=?";
		return getJdbcTemplate().query(sql,new Object[] { term.toLowerCase() },BeanPropertyRowMapper.newInstance(NumberSearchEntry.class));
	}
	
	@Override
	public String getDescForNumber(String number)
	{
		String gNum = number.substring(number.length()-3);
		Integer num = Integer.parseInt(gNum);
		String sql = "SELECT DESCX FROM NUMBERX WHERE NUMBERX = " + num + " AND DICTIONARY='S'";
		log.trace(sql);
		return getJdbcTemplate().queryForObject(sql,String.class);
	}
	
	@Override
	public byte[] getRawImageForNumber(String number)
	{
		String gNum = number.substring(number.length()-3);
		Integer num = Integer.parseInt(gNum);
		String sql = "SELECT IMAGE FROM NUMBERX WHERE NUMBERX = " + num + " AND DICTIONARY='S'";;
		log.trace(sql);
		return getJdbcTemplate().queryForObject(sql,new ImageMapper());
	}
	
	@Override 
	public void getDescsForDrawResult(Dx4DrawResultJson result)
	{
		Dx4NumberPageElementJson npe = getNumberPageElement(result.getFirstPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		result.setFirstDesc(npe.getDescription());
		result.setFirstDescCh(npe.getDescriptionCh());
		
		npe = getNumberPageElement(result.getSecondPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		result.setSecondDesc(npe.getDescription());
		result.setSecondDescCh(npe.getDescriptionCh());
		
		npe = getNumberPageElement(result.getThirdPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		result.setThirdDesc(npe.getDescription());
		result.setThirdDescCh(npe.getDescriptionCh());
	}
	
	@Override
	public List<Dx4NumberPageElementJson> getNumberPageElements()
	{
		String sql = "SELECT * FROM NUMBER3S ORDER BY NUMBERX";
		log.trace(sql);
		return getJdbcTemplate().query(sql,new NumberPageElementRowMapperX());
	}
	
	@Override
	public List<Dx4NumberPageElementJson> getNumberPageElementsByDesc(String searchTerm)
	{
		String[] toks = searchTerm.split(" ");
		while (toks.length>0)
		{
			List<Dx4NumberPageElementJson>  elems = getNumberPageElementsByDesc(toks);
			if (!elems.isEmpty())
				return elems;
			if (toks.length==1)
				break;
			String[] toks1 = new String[toks.length-1];
			for (int i = 0; i<toks1.length; i++)
				toks1[i] = toks[i];
			toks = toks1;
		}
		return new ArrayList<Dx4NumberPageElementJson>();
	}
	
	private List<Dx4NumberPageElementJson> getNumberPageElementsByDesc(String[] toks)
	{
		String sql = "SELECT * FROM NUMBERX WHERE ";
		for (String tok : toks)
		{
			sql += "descx @@ to_tsquery('"+tok+"') AND ";
			/*sql += "DESCX LIKE '%" + tok + "%'";
			sql += " OR ";select * from numberx where code in 
(select number from znumber where year in
(select year from zodiac where animal = 'Tiger'));

			*/
		}
		sql = sql.substring(0,sql.length()-4);
		return getJdbcTemplate().query(sql,new NumberPageElementRowMapperX());
	}
	
	@Override
	public void updateImage(final String code,final byte[] image)
	{
		try
		{
			getJdbcTemplate().update("UPDATE NUMBERX SET image = ? WHERE code=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBytes(1, image);
							ps.setString(2,code);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void storeDx4NumberPageElementJson(final Dx4NumberPageElementJson npe,final byte[] image)
	{
		try
		{
			getJdbcTemplate().update("DELETE FROM NUMBERX WHERE NUMBERX = " + npe.getNumber() + " AND DICTIONARY = '" + npe.getDictionary() + "'");
			
			getJdbcTemplate().update("INSERT INTO NUMBERX (NUMBERX,CODE,DESCX,CDESC,IMAGE,DICTIONARY) VALUES (?,?,?,?,?,?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setInt(1, npe.getNumber());
							ps.setString(2,npe.getToken());
							ps.setString(3,npe.getDescription());
							ps.setString(4,npe.getDescriptionCh());
							ps.setBytes(5,image);
							ps.setString(6,Character.toString(npe.getDictionary()));
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void addNumber4D(final String number) {
		
		try
		{
			getJdbcTemplate().update("INSERT INTO NUMBER4D (NUMBER) VALUES (?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, number);
						}
					});
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public String getRandom(long playgameId,int digits)
	{
		try
		{
			String sql = "select count(*) from number4d where number not in (select number from betrollup where playgameid="+playgameId+")"
					+ " and length(number)=" + digits;
			Long count = getJdbcTemplate().queryForObject(sql,Long.class);
			if (count < 10)
			{
				sql = "select number from number4d where number not in (select number from betrollup where playgameid="
							+playgameId+") and length(number)=" + digits + " order by random() limit 1";
		//		log.info(sql);
				return getJdbcTemplate().queryForObject(sql,String.class).trim();
			}
			sql = "select number from betrollup where stake = (select min(stake) from betrollup where playgameid="
					+playgameId + ") and length(number) = " + digits + " order by random() limit 1";
			return getJdbcTemplate().queryForObject(sql,String.class).trim();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void storeZodiacImage(final String animal, final int set, final int year, final byte[] image) {
		try
		{
			getJdbcTemplate().update("DELETE FROM ZODIAC WHERE YEAR = ? AND SET = ?",
			 new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, year);
					ps.setInt(2, set);
				}
			});
			
			getJdbcTemplate().update("INSERT INTO ZODIAC (animal,set,year,image) VALUES (?,?,?,?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, animal);
							ps.setInt(2, set);
							ps.setInt(3, year);
							ps.setBytes(4, image);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public List<Dx4ZodiacJson> getZodiacs(int set)
	{
		String sql = "SELECT * FROM ZODIAC WHERE SET = " + set + " ORDER BY YEAR";
		log.trace(sql);
		try
		{
			List<Dx4ZodiacJson> zs = getJdbcTemplate().query(sql,new Dx4ZodiacJsonRowMapper());
			for (Dx4ZodiacJson z : zs)
				z.setNumbers(getZodiacNumbers(z.getYear()));
			return zs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	private List<String> getZodiacNumbers(int year) {
		String sql = "select number from znumber where year = " + year;
		return getJdbcTemplate().queryForList(sql,String.class);
	}

	@Override
	public void storeHoroscope(final Dx4HoroscopeJson hj,final byte[] image) {
		try
		{
			getJdbcTemplate().update("DELETE FROM HOROSCOPENUMBER WHERE MONTH = ?",
					 new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setInt(1, hj.getMonth());
						}
					});
			
			getJdbcTemplate().update("DELETE FROM HOROSCOPE WHERE MONTH = ?",
			 new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, hj.getMonth());
				}
			});
			
			getJdbcTemplate().update("INSERT INTO HOROSCOPE (sign,month,startdate,enddate,image) VALUES (?,?,?,?,?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, hj.getSign());
							ps.setInt(2,hj.getMonth());
							ps.setTimestamp(3,new Timestamp(hj.getStartDate().getTime()));
							ps.setTimestamp(4,new Timestamp(hj.getEndDate().getTime()));
							ps.setBytes(5,image);
						}
					});
			
			for (final String number : hj.getNumbers())
			{
				getJdbcTemplate().update("INSERT INTO HOROSCOPENUMBER (month,number) VALUES (?,?)"
						, new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1,hj.getMonth());
								ps.setString(2, number);
							}
						});
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public List<Dx4HoroscopeJson> getHoroscopes()
	{
		String sql = "SELECT * FROM HOROSCOPE ORDER BY ID";
		log.trace(sql);
		try
		{
			List<Dx4HoroscopeJson> hs = getJdbcTemplate().query(sql,new Dx4HoroscopeJsonRowMapper());
			for (Dx4HoroscopeJson h : hs)
				h.setNumbers(getHoroscopeNumbers(h.getMonth()));
			return hs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	private List<String> getHoroscopeNumbers(int month) {
		String sql = "select number from horoscopenumber where month = " + month;
		return getJdbcTemplate().queryForList(sql,String.class);
	}
	
	//==================================================================================================================================
	
	@Override
	public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(String number)
	{
		
		String sql = "select npo.number as number,npo.lasttraded as lasttraded,npo.odds as odds,npo.volume as volume,npo.band as band,"
				+ "npo.lastchanged as lastchanged,npo.lastchange as lastchange,npo.count as count from numberfloatpayout as npo where npo.number=?";
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { number },BeanPropertyRowMapper.newInstance(Dx4NumberFloatPayoutJson.class));
			
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
		
	}
	
	@Override
	public List<Dx4NumberFloatPayoutJson> getDx4NumberFloatPayoutTrending(int limit)
	{
		String sql = "select npo.number as number,npo.lasttraded as lasttraded,npo.odds as odds,npo.volume as volume,npo.band as band,"
				+ "npo.lastchanged as lastchanged,npo.lastchange as lastchange,npo.count as count from numberfloatpayout as npo " +
						"order by lastchanged desc,volume desc,count desc limit ?";
		try
		{
			return getJdbcTemplate().query(sql,new Object[] { limit },BeanPropertyRowMapper.newInstance(Dx4NumberFloatPayoutJson.class));
			
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
		
	}

	@Override
	public Dx4NumberFloatPayoutJson getWorstDx4NumberFloatPayoutForBand(int band,Dx4NumberFloatPayoutJson nfp)
	{
		if (nfp.getNumber().length()==3)
			return getWorstDx4NumberFloatPayoutForBand3(band,nfp);
		
		String sql = "select npo.number as number,npo.lasttraded as lasttraded,npo.odds as odds,npo.volume as volume,npo.band as band,"
				+ "npo.lastchanged as lastchanged,npo.lastchange as lastchange,npo.count as count from numberfloatpayout as npo " +
						"where length(npo.number) = 4 and npo.band=? and (volume<? or (volume=? and lasttraded<?) " +
						"or ((volume=? and lasttraded=?) and count>?)) limit 1";
		Timestamp t1 = new Timestamp(nfp.getLastTraded().getTime());
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { band, nfp.getVolume(), nfp.getVolume(), t1, nfp.getVolume(), t1, nfp.getCount() },
						BeanPropertyRowMapper.newInstance(Dx4NumberFloatPayoutJson.class));
			
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	private Dx4NumberFloatPayoutJson getWorstDx4NumberFloatPayoutForBand3(int band,Dx4NumberFloatPayoutJson nfp)
	{
		String sql = "select npo.number as number,npo.lasttraded as lasttraded,npo.odds as odds,npo.volume as volume,npo.band as band,"
				+ "npo.lastchanged as lastchanged,npo.lastchange as lastchange,npo.count as count from numberfloatpayout as npo " +
						"where length(npo.number) = 3 and npo.band=? and (volume<? or (volume=? and lasttraded<?) " +
						"or ((volume=? and lasttraded=?) and count>?)) limit 1";
		Timestamp t1 = new Timestamp(nfp.getLastTraded().getTime());
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { band, nfp.getVolume(), nfp.getVolume(), t1, nfp.getVolume(), t1, nfp.getCount() },
						BeanPropertyRowMapper.newInstance(Dx4NumberFloatPayoutJson.class));
			
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void initializeNumberFloatPayouts(Date lastDrawDate)
	{
		final Timestamp ts = new Timestamp(lastDrawDate.getTime());
		GregorianCalendar gc = new GregorianCalendar();
		final Timestamp ts1 = new Timestamp(gc.getTime().getTime());
		try
		{	
			final String sql = "UPDATE numberfloatpayout SET odds=0.0,lastchange=0,lasttraded=?,lastchanged=?";
			getJdbcTemplate().update(sql
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							 	ps.setTimestamp(1,ts);
							 	ps.setTimestamp(2,ts1);
					    }
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updateNumberFloatPayout(final Dx4NumberFloatPayoutJson nfp)
	{
		GregorianCalendar gc = new GregorianCalendar();
		nfp.setLastChanged(gc.getTime());
		final Timestamp ts = new Timestamp(nfp.getLastChanged().getTime());
		try
		{	
			nfp.setCount(getTotalPlacingsForNumber(nfp.getNumber()));
			final String sql = "UPDATE numberfloatpayout SET lasttraded=?,odds=?,band=?,volume=?,lastchanged=?,lastchange=?,count=? WHERE number=?";
			getJdbcTemplate().update(sql
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							 	ps.setTimestamp(1,new Timestamp(nfp.getLastTraded().getTime()));
					            ps.setDouble(2,nfp.getOdds());
					            ps.setInt(3,nfp.getBand());
					            ps.setDouble(4,nfp.getVolume());
					            ps.setTimestamp(5,ts);
					            ps.setDouble(6,nfp.getLastChange());
					            ps.setInt(7,nfp.getCount());
					            ps.setString(8,nfp.getNumber());
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	private List<Integer> getNumberFloatCountTotals3()
	{
		String sql = "select distinct(count) as dc from " 
						+ "(select count(*) as count,numb3 from "
						+ "(select substring(number,2,3) as numb3 from dplacing) as a "
						+ "group by numb3) as foo order by dc";

		return getJdbcTemplate().queryForList(sql,Integer.class);
	}
	
	
	private NumberFloatStat getNumberFloatStat()
	{
		String sql = "select avg(count),max(count),min(count),stddev(count) from " +
						"(select count(*) from dplacing group by number) as foo";
		
		return getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(NumberFloatStat.class));
	}
	
	@Override
	public List<PayoutBand> createPayoutBands()
	{
		List<PayoutBand> bands = new ArrayList<PayoutBand>();
		NumberFloatStat stat = getNumberFloatStat();
		double basePo = getFloatBasePayout();
		
		int lower = stat.getMin();
		while (lower<stat.getMax())
		{
			PayoutBand pb = new PayoutBand();
			pb.setLower(lower);
			int upper = lower+(int) Math.round(stat.getStddev());
			pb.setUpper(upper);
			lower=upper;
			bands.add(pb);
		}
		
		int middle = bands.size()/2;
		bands.get(middle).setOdds(Math.round(basePo));
		double tenPc = basePo/10.0;
		double adjustOdds = tenPc/(middle-1); 
		double downWard = basePo;
		for (int i=middle-1; i>=0; i--)
		{
			downWard += adjustOdds;
			bands.get(i).setOdds(Math.round(downWard));
		}
		
		double upWard = basePo;
		for (int i=middle+1; i<bands.size(); i++)
		{
			upWard -= adjustOdds;
			bands.get(i).setOdds(Math.round(upWard));
		}
		
		log.info("Created bands : ");
		for (PayoutBand pb : bands)
			log.info("PB4 : " + pb);
		return bands;
	}
	
	@Override
	public List<PayoutBand> createPayoutBands3()
	{
		List<PayoutBand> bands = new ArrayList<PayoutBand>();
		List<Integer> totals = getNumberFloatCountTotals3();
		double basePo = getFloatBasePayout3();
		
		int lower = 0;
		while (lower<totals.size()-1)
		{
			PayoutBand pb = new PayoutBand();
			pb.setLower(totals.get(lower++));
			pb.setUpper(totals.get(lower));
			bands.add(pb);
		}
		
		int middle = bands.size()/2;
		bands.get(middle).setOdds(Math.round(basePo));
		double tenPc = basePo/10.0;
		double adjustOdds = tenPc/(middle-1); 
		double downWard = basePo;
		for (int i=middle-1; i>=0; i--)
		{
			downWard += adjustOdds;
			bands.get(i).setOdds(Math.round(downWard));
		}
		
		double upWard = basePo;
		for (int i=middle+1; i<bands.size(); i++)
		{
			upWard -= adjustOdds;
			bands.get(i).setOdds(Math.round(upWard));
		}
		
		Map<Double,PayoutBand> bandMap = new TreeMap<Double,PayoutBand>();
		for (PayoutBand pb : bands)
		{
			PayoutBand me = bandMap.get(pb.getOdds());
			if (me==null)
				bandMap.put(pb.getOdds(),pb);
			else
			{
				if (me.getUpper()<pb.getUpper())
					me.setUpper(pb.getUpper());
				if (me.getLower()>pb.getLower())
					me.setLower(pb.getLower());
			}		
		}
		
		bands = new ArrayList<PayoutBand>();
			
		for (PayoutBand pb : bandMap.values())
			bands.add(pb);
		
		log.info("Created bands 3 : ");
		for (PayoutBand pb : bands)
			log.info("PB3 : " + pb);
		return bands;
	}
	
	private double getFloatBasePayout3()
	{
		String sql = "select sum(po.payout) from payout as po "
						+ "join game as g on g.id = po.gameid where g.gtype='ABCC'";
		
		Double sum = getJdbcTemplate().queryForObject(sql,Double.class);
		return sum / 30;				// (3 * 10 cos only 3 digits)
	}

	
	private double getFloatBasePayout()
	{
		String sql = "select * from prize";
		List<Prize> prizes = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Prize.class));
		double total = 0.0;
		for (Prize p : prizes)
		{
			if (p.getPlace()=='P' || p.getPlace()=='C')
				total += p.getPrize() * 10;
			else
				total += p.getPrize();
		}
		
		return total / 23.0;
	}

	@Override
	public void insertNumberFloatPayout(final Dx4NumberFloatPayoutJson nfp) {
		GregorianCalendar gc = new GregorianCalendar();
		final Timestamp ts = new Timestamp(gc.getTime().getTime());
		try
		{	final int count = getTotalPlacingsForNumber(nfp.getNumber());
			final String sql = "INSERT INTO numberfloatpayout (number,lastchanged, count) VALUES (?,?,?)";
			getJdbcTemplate().update(sql
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
					            ps.setString(1,nfp.getNumber());
					            ps.setTimestamp(2,ts);
					            ps.setInt(3, count);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	private int getTotalPlacingsForNumber(String number)
	{
		String sql = "select count(*) as count from dplacing where number = ?";
		if (number.length()==3)
			sql = "select count(*) as count from dplacing where substring(number,2,3) = ?";

		return getJdbcTemplate().queryForObject(sql,new Object[] { number }, Integer.class);
	}
	
}
