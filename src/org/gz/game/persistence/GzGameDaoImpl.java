package org.gz.game.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4GameTypeJson;
import org.gz.baseuser.GzBaseUser;
import org.gz.game.GzGameTypePayouts;
import org.gz.game.GzGroup;
import org.gz.game.GzPackage;
import org.gz.home.persistence.GzDuplicatePersistenceException;
import org.gz.home.persistence.GzPersistenceRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GzGameDaoImpl extends NamedParameterJdbcDaoSupport implements GzGameDao {

	private static Logger log = Logger.getLogger(GzGameDaoImpl.class);
	
	@Override
	public void storeGroup(GzGroup group) {
		final Timestamp t1 = new Timestamp(group.getCreated().getTime());
		String sql = "INSERT INTO gzgroup (name,description,memberid,created) VALUES( ?, ?, ?, ?)";
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps =
				                connection.prepareStatement(sql, new String[] {"id"});
				            ps.setString(1,group.getName());
				            ps.setString(2,group.getDescription());
							ps.setString(3,group.getMember().getMemberId());
							ps.setTimestamp(4,t1);
				            return ps;
				        }
				    },
				    keyHolder);
			
			group.setId(keyHolder.getKey().longValue());
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceRuntimeException("Could not execute : " + sql + " - " + e.getMessage());
		}	
	}
	
	@Override
	public Map<String,GzGroup> getGroups(GzBaseUser member)
	{
		Map<String,GzGroup> groups = new TreeMap<String,GzGroup>();
		try
		{
			List<GzGroup> grps = getJdbcTemplate().query("SELECT * FROM gzgroup WHERE memberid= ?", new Object[]{ member.getMemberId() }, BeanPropertyRowMapper.newInstance(GzGroup.class));
			for (GzGroup grp : grps)
			{
				grp.setMember(member);
				grp.setPackages(getPackagesByGroupId(grp,member));
				groups.put(grp.getName(), grp);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not get groups - " + e.getMessage());
			throw new GzPersistenceRuntimeException("Could not get groups - " + e.getMessage());
		}	
		return groups;
	}
	
	private Map<String, GzPackage> getPackagesByGroupId(GzGroup grp,GzBaseUser member)
	{
		Map<String, GzPackage> map = new TreeMap<String, GzPackage>();
		List<GzPackage> gzPackages = getJdbcTemplate().query("SELECT * FROM PACKAGE WHERE groupid = ?", 
								new Object[]{ grp.getId() }, BeanPropertyRowMapper.newInstance(GzPackage.class));
		
		for (GzPackage gzPackage : gzPackages)
		{
			gzPackage.setGameTypePayouts(getGameTypePayouts(gzPackage.getId()));
			map.put(gzPackage.getName(), gzPackage);
			gzPackage.setGroup(grp);
			gzPackage.setMember(member);
		}
		return map;
	}


	@Override
	public void storePackage(GzPackage gzPackage) {
		final Timestamp t1 = new Timestamp(gzPackage.getCreated().getTime());
		String sql = "INSERT INTO package (name,memberid,created,groupid) VALUES( ?, ?, ?, ?)";
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps =
				                connection.prepareStatement(sql, new String[] {"id"});
				            ps.setString(1,gzPackage.getName());
							ps.setString(2,gzPackage.getMember().getMemberId());
							ps.setTimestamp(3,t1);
							ps.setLong(4,gzPackage.getGroup().getId());
				            return ps;
				        }
				    },
				    keyHolder);
			
			gzPackage.setId(keyHolder.getKey().longValue());
			
			for (GzGameTypePayouts gtp : gzPackage.getGameTypePayouts().values())
				storeGameTypePayouts(gtp,gzPackage.getId());
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			if (e.getMessage().contains("duplicate key value violates unique constraint"))
				throw new GzDuplicatePersistenceException("Could not execute : " + sql + " - " + e.getMessage());
			
			throw new GzPersistenceRuntimeException("Could not execute : " + sql + " - " + e.getMessage());
		}	
	}
	
	@Override
	public void updatePackage(GzPackage gzPackage)
	{
		try
		{
			clearPackage(gzPackage.getId());
			for (GzGameTypePayouts gtp : gzPackage.getGameTypePayouts().values())
				storeGameTypePayouts(gtp,gzPackage.getId());
		}
		catch (DataAccessException e)
		{
			log.error("Could not update package - " + e.getMessage());
			throw new GzPersistenceRuntimeException("Could not update package - " + e.getMessage());
		}		
	}

	private void storeGameTypePayouts(GzGameTypePayouts gtp,long packageId)
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO gametypepayout (packageid,gametype,commission) VALUES( ?, ?, ?)";
		
		try
		{
			getJdbcTemplate().update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps =
				                connection.prepareStatement(sql, new String[] {"id"});
				            ps.setLong(1,packageId);
							ps.setString(2,gtp.getGameType().name());
							ps.setDouble(3,gtp.getCommission());
				            return ps;
				        }
				    },
				    keyHolder);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceRuntimeException("Could not execute : " + sql + " - " + e.getMessage());
		}	
		
		gtp.setId(keyHolder.getKey().longValue());
		for (Dx4PayOut po : gtp.getPayOuts())
		{
			if (po!=null)
				storePayout(po,gtp.getId());
		}
	}

	private void storePayout(Dx4PayOut po, long gameTypePayoutId) {
		getJdbcTemplate().update("INSERT INTO gzpayout (gametypepayoutid,place,payout) "
				+ "VALUES( ?, ?, ?)"
				, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1,gameTypePayoutId);
						ps.setString(2,Character.toString(po.getType().getPlace()));
						ps.setDouble(3,po.getPayOut());
					}
				});
	}
	
	@Override
	public GzPackage getPackageById(long id)
	{
		GzPackage gzPackage = getJdbcTemplate().queryForObject("SELECT * FROM PACKAGE WHERE ID = ?", 
								new Object[]{ id }, BeanPropertyRowMapper.newInstance(GzPackage.class));
		if (gzPackage == null)
			return null;
		gzPackage.setGameTypePayouts(getGameTypePayouts(gzPackage.getId()));
		
		return gzPackage;
	}

	private Map<Dx4GameTypeJson, GzGameTypePayouts> getGameTypePayouts(long packageId) {
		
		List<GzGameTypePayouts> gtps = getJdbcTemplate().query("SELECT * FROM gametypepayout WHERE packageid = ?", 
						new Object[]{ packageId }, BeanPropertyRowMapper.newInstance(GzGameTypePayouts.class));
		Map<Dx4GameTypeJson, GzGameTypePayouts> map = new TreeMap<Dx4GameTypeJson, GzGameTypePayouts>();
		for (GzGameTypePayouts gtp : gtps)
		{
			gtp.mapPayOuts(getPayouts(gtp.getId()));
			map.put(gtp.getGameType(), gtp);
		}
		return map;
	}

	private List<Dx4PayOut> getPayouts(long gtpId) {

		List<Dx4PayOut> pos = getJdbcTemplate().query("SELECT * FROM gzpayout WHERE gametypepayoutid = ?", 
						new Object[]{ gtpId }, new GzPayOutRowMapper());
		return pos;
	}
	
	private void clearPackage(long packageId)
	{
		String sql = "DELETE FROM gzpayout WHERE gametypepayoutid IN (SELECT id FROM gametypepayout WHERE packageid = ?)";
		getJdbcTemplate().update(sql, new Object[]{packageId} );
		sql = "DELETE FROM gametypepayout WHERE packageid = ?";
		getJdbcTemplate().update(sql, new Object[]{packageId} );
	}
}



