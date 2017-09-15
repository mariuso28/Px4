package org.gz.game.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4GameTypeJson;
import org.gz.game.GzGameTypePayouts;
import org.gz.game.GzPackage;
import org.gz.home.persistence.GzPersistenceRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GzGameDaoImpl extends NamedParameterJdbcDaoSupport implements GzGameDao {

	private static Logger log = Logger.getLogger(GzGameDaoImpl.class);
	
	@Override
	public void storePackage(GzPackage gzPackage) {
		final Timestamp t1 = new Timestamp(gzPackage.getCreated().getTime());
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update("INSERT INTO package (name,agentid,created) "
					+ "VALUES( ?, ?, ?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1,gzPackage.getName());
							ps.setString(2,gzPackage.getMember().getMemberId());
							ps.setTimestamp(3,t1);
						}
					},keyHolder);
			gzPackage.setId(keyHolder.getKey().longValue());
			
			for (GzGameTypePayouts gtp : gzPackage.getGameTypePayouts().values())
				storeGameTypePayouts(gtp,gzPackage.getId());
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updatePackage(GzPackage gzPackage)
	{
		clearPackage(gzPackage.getId());
		for (GzGameTypePayouts gtp : gzPackage.getGameTypePayouts().values())
			storeGameTypePayouts(gtp,gzPackage.getId());
	}

	private void storeGameTypePayouts(GzGameTypePayouts gtp,long packageId)
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update("INSERT INTO gametypepayout (packageid,gametype,commission) "
				+ "VALUES( ?, ?, ?)"
				, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1,packageId);
						ps.setString(2,gtp.getGameType().name());
						ps.setDouble(3,gtp.getCommission());
					}
				},keyHolder);
		gtp.setId(keyHolder.getKey().longValue());
		for (Dx4PayOut po : gtp.getPayOuts())
		{
			storePayout(po,gtp.hashCode());
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
			gtp.setPayOuts(getPayouts(gtp.getId()));
			map.put(gtp.getGameType(), gtp);
		}
		return map;
	}

	private List<Dx4PayOut> getPayouts(long gtpId) {

		List<Dx4PayOut> pos = getJdbcTemplate().query("SELECT * FROM gzpackage WHERE gametypepayoutid = ?", 
						new Object[]{ gtpId }, new GzPayOutRowMapper());
		return pos;
	}
	
	private void clearPackage(long packageId)
	{
		String sql = "DELETE FROM gzpayout WHERE gametypepayoutid = (SELECT id FROM gametypepayout WHERE packageid = ?)";
		getJdbcTemplate().update(sql, new Object[]{packageId} );
		sql = "DELETE FROM gametypepayout WHERE packageid = ?";
		getJdbcTemplate().update(sql, new Object[]{packageId} );
	}
}



