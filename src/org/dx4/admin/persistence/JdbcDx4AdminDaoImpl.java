package org.dx4.admin.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.admin.Dx4Version;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.persistence.JdbcDx4SecureUserDaoImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class JdbcDx4AdminDaoImpl extends JdbcDx4SecureUserDaoImpl implements Dx4AdminDao {

	private static final Logger log = Logger.getLogger(JdbcDx4AdminDaoImpl.class);

	@Override
	public void store(Dx4Admin admin)
	{
		try
		{
			super.storeBaseUser(admin);
			initializeAdminProperties(admin);
		}
		catch (Dx4PersistenceException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("storing admin : ",e.getMessage());
		}
	}

	@Override
	public void initializeAdminProperties(final Dx4Admin admin) {
		try
		{
			getJdbcTemplate().update("INSERT INTO admin (userid) VALUES( ? )"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setLong(1,admin.getSeqId());
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
	public Dx4Admin getAdminByEmail(String email) throws Dx4PersistenceException {
		Dx4Admin admin = (Dx4Admin) super.getBaseUserByEmail(email,Dx4Admin.class);
		if (admin == null)
			return null;
		super.getMembersForUser(admin);
		return admin;
	}
	
	@Override
	public Dx4Admin getAdminProperties() throws Dx4PersistenceException {
		try
		{
			final String sql = "SELECT * FROM admin LIMIT 1";
			Dx4Admin ad = getJdbcTemplate().queryForObject(sql,new Dx4AdminRowMapper());
			return ad;
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			 return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void update(Dx4Admin admin) throws Dx4PersistenceException {
		super.updateBaseUserProfile(admin);
	}

	@Override
	public boolean getScheduledDownTimeSet() throws Dx4PersistenceException {
		try
		{
			final String sql = "SELECT scheduleddowntimeset FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,Boolean.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void setScheduledDownTime(final boolean set) throws Dx4PersistenceException {
		try
		{
			getJdbcTemplate().update("UPDATE admin set scheduleddowntimeset=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBoolean(1,set);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void storeScheduledDownTime(final Date date) throws Dx4PersistenceException {
		final Timestamp t1 = new Timestamp(date.getTime());
		try
		{
			getJdbcTemplate().update("UPDATE admin set scheduleddowntime = ?,scheduleddowntimeset=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setTimestamp(1,t1);
							ps.setBoolean(2,true);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public Date getScheduledDownTime() throws Dx4PersistenceException {
		try
		{
			final String sql = "SELECT scheduleddowntime FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,Date.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void updateVersion(final Dx4Version version) {
		try
		{
			getJdbcTemplate().update("UPDATE admin SET versioncode = ?, apk = ?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1,version.getCode());
							ps.setBytes(2,version.getApk());
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
	public Dx4Version getVersion()  {
		try
		{
			final String sql = "SELECT versioncode as code,apk FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(Dx4Version.class));
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public String getVersionCode()  {
		try
		{
			final String sql = "SELECT versioncode FROM admin LIMIT 1";
			return getJdbcTemplate().queryForObject(sql,String.class);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
}
