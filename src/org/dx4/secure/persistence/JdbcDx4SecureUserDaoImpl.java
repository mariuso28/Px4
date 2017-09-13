package org.dx4.secure.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dx4.account.persistence.Dx4AccountDao;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.Dx4Comp;
import org.dx4.agent.Dx4MA;
import org.dx4.agent.Dx4SMA;
import org.dx4.agent.Dx4ZMA;
import org.dx4.game.persistence.Dx4MetaGameDao;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Config;
import org.dx4.util.GetNextNumberNo4s;
import org.dx4.util.StackDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Transactional
public class JdbcDx4SecureUserDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4SecureUserDao {
	
	private static Logger log = Logger.getLogger(JdbcDx4SecureUserDaoImpl.class);
	@Autowired
	private Dx4MetaGameDao dx4MetaGameDao;
	@Autowired
	private Dx4AccountDao dx4AccountDao;
	
	@Override
	public void storeBaseUser(final Dx4SecureUser baseUser) throws Dx4PersistenceException {
		
		baseUser.setId(UUID.randomUUID());
		
		setNextUserCode(baseUser);
		
		try
		{
			getJdbcTemplate().update("INSERT INTO baseuser (id,email,contact,phone,nickname,code,parentcode,role,icon,enabled,password,leafinstance) "
										+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setObject(1, baseUser.getId());
						ps.setString(2, baseUser.getEmail().toLowerCase());
						ps.setString(3, baseUser.getContact());
						ps.setString(4, baseUser.getPhone());
						ps.setString(5, baseUser.getNickname());
						ps.setString(6, baseUser.getCode());
						ps.setString(7, baseUser.getParentCode());
						ps.setString(8, baseUser.getRole().name());
						ps.setString(9, baseUser.getIcon());
						ps.setBoolean(10, baseUser.isEnabled());
						ps.setString(11, baseUser.getPassword());
						ps.setLong(12, baseUser.getLeafInstance());
			      }
			    });
			
			baseUser.setAuthorities(baseUser.getRole().getAllRoles());
			storeAuthorities(baseUser);							
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public Long getSeqIdForId(UUID id)
	{
		long seqId;	
		try
		{
			String sql = "SELECT seqId FROM baseuser WHERE id=?";
			seqId = getJdbcTemplate().queryForObject(sql,new Object[] { id }, Long.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("","Could not execute : " + e.getMessage());
		}
		return seqId;
	}
	
	@Override
	public void updateBaseUserProfile(final Dx4SecureUser baseUser) throws Dx4PersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE baseuser SET contact=?,phone=?,nickname=?,icon =?,enabled=?,password=? WHERE id=?"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, baseUser.getContact());
						ps.setString(2, baseUser.getPhone());
						ps.setString(3, baseUser.getNickname());
						ps.setString(4, baseUser.getIcon());
						ps.setBoolean(5, baseUser.isEnabled());
						ps.setString(6, baseUser.getPassword());
						ps.setObject(7, baseUser.getId());
			      }
			    });
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private synchronized void setNextUserCode(Dx4SecureUser user) throws Dx4PersistenceException {
		
		if (user.getRole().equals(Dx4Role.ROLE_ADMIN))
		{
			user.setCode(Dx4Admin.getDefaultCode());
			user.setSystemMember(true);
			return;
		}
		
		String parentCode = user.getParentCode();
		Long nextCode = getNextCode(user.getRole(),parentCode);
		if (user.getParent().getRole().equals(Dx4Role.ROLE_ADMIN))
			parentCode = "";
		user.setLeafInstance(nextCode);
		user.setCode( parentCode + user.getRole().getCode() + nextCode.toString() );
	}
	
	@Override
	public String getEmailForId(UUID id) throws Dx4PersistenceException
	{
		String email;	
		try
		{
			String sql = "SELECT email FROM baseuser WHERE id=?";
			email = getJdbcTemplate().queryForObject(sql,new Object[] { id }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
		return email;
	}
	
	private Long getNextCode(Dx4Role role,String parentCode) throws Dx4PersistenceException
	{
		Long leafInstance;	
		try
		{
			String sql = "SELECT MAX(leafinstance) FROM baseuser WHERE role = ? AND PARENTCODE=?";
			log.info(sql);
			leafInstance = getJdbcTemplate().queryForObject(sql,new Object[] { role.name(), parentCode }, Long.class);
			log.info("Got next leaf instance : " + leafInstance);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	

		if (leafInstance==null)
			return 0L;
		
		long lf = GetNextNumberNo4s.next(leafInstance);
		log.info("Using leaf instance : " + lf);
		return lf;
	}
	
	private void storeAuthorities(final Dx4SecureUser baseUser) throws Dx4PersistenceException
	{
		try
		{
			for (final Dx4Role role : baseUser.getAuthorities())
			{
				getJdbcTemplate().update("INSERT INTO authority (baseuserid,role) VALUES (?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setObject(1, baseUser.getId());
							ps.setString(2,role.name());
			      }
			    });
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public Dx4SecureUser getBaseUserByEmail(final String email,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException 
	{	
		try
		{
			final String sql = "SELECT id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone,role FROM baseUser WHERE email=?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, email);
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<Dx4SecureUser> getBaseUsersByRole(final Dx4Role role) throws Dx4PersistenceException 
	{	
		@SuppressWarnings("rawtypes")
		Class clazz = role.getCorrespondingClass();
		try
		{
			final String sql = "SELECT id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone,role FROM baseUser WHERE role=?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, role.name());
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			populateUser(bus.get(0));
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	public List<Dx4SecureUser> getBaseUsersByPhone(final String phone,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException {
		try
		{
			final String sql = "SELECT id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone,role FROM baseUser WHERE phone=?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, phone);
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			for (Dx4SecureUser su : bus)
				populateUser(su);
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	
	@Override
	public Dx4SecureUser getBaseUserByCode(final String code) throws Dx4PersistenceException {
		
		try
		{
			Dx4Role role = Dx4Role.getRoleForCode(code);
			@SuppressWarnings("rawtypes")
			Class clazz = role.getCorrespondingClass();
			
			final String sql = "SELECT id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone,role FROM baseUser WHERE code=?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, code);
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public Dx4SecureUser getBaseUserBySeqId(final long id,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException {
		try
		{
			final String sql = "SELECT id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone,role FROM baseUser WHERE seqId=?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setLong(1, id);
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public String getBaseUserEmailBySeqId(final long id) throws Dx4PersistenceException {
		try
		{
			String email = getJdbcTemplate().queryForObject("SELECT email FROM baseUser WHERE seqId=?", new Object[]{id}, String.class);
			return email;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public Dx4SecureUser getBaseUserBySeqId(long id) throws Dx4PersistenceException {
		try
		{
			String code = getCodeForSeqId(id);			
			@SuppressWarnings("rawtypes")
			Class clazz = Dx4Role.getRoleClassForCode(code);
			return getBaseUserBySeqId(id,clazz);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	/*
	private Dx4Role getRole(Dx4BaseUser user) throws Dx4PersistenceException {
		
		String sql = "SELECT role FROM baseuser WHERE id = ?";
		try
		{
			PreparedStatement ps = getConnection().prepareStatement(sql);	
			ps.setObject(1,user.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			String role = rs.getString(1);
			return Dx4Role.valueOf(role);
		}
		catch (Exception e)
		{
			log.error("Could not execute : PS : " + sql + " - " + e.getMessage());
			throw new Dx4PersistenceException("PS : " + sql + " - " + e.getMessage());
		}	
	}
	*/
	
	@Override
	public List<String> getMemberCodes(final Dx4SecureUser baseUser) throws Dx4PersistenceException
	{
		try
		{
			final String sql = "SELECT code FROM baseUser WHERE parentcode = ?";
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<String> memberCodes = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, baseUser.getParentCode());
				        }
				      }, new RowMapper() {
				          public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				              return resultSet.getString(1);
				            }});
			
			return memberCodes;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
	}

	protected String getCodeForEmail(String email) throws Dx4PersistenceException {
		
		String sql = "SELECT code FROM baseUser WHERE email = ?";
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { email }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	public String getCodeForSeqId(long seqId) throws Dx4PersistenceException {
		
		String sql = "SELECT code FROM baseUser WHERE seqId = ?";
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { seqId }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void getDownstreamForParent(Dx4SecureUser parent) {
		
		try {
			getMembersForUser(parent);
		} catch (Dx4PersistenceException e) {
			log.error(StackDump.toString(e));
		}
	}
	
	protected void getMembersForUser(Dx4SecureUser user) throws Dx4PersistenceException
	{
		user.setMembers(new ArrayList<Dx4SecureUser>());
		
		if (user.getClass().equals(Dx4Admin.class))					// admin only has companies
		{
			Dx4Admin admin = (Dx4Admin) user;
			admin.setComps(getCompsForParent(user));
			return;
		}
		
		if (user.getClass().equals(Dx4Comp.class))
		{
			Dx4Comp comp = (Dx4Comp) user;
			comp.setZmas(getZmasForParent(user));
			comp.setSmas(getSmasForParent(user));
			comp.setMas(getMasForParent(user));
			comp.setAgents(getAgentsForParent(user));
			comp.setPlayers(getPlayersForParent(user));
			return;
		}
		
		if (user.getClass().equals(Dx4ZMA.class))
		{
			Dx4ZMA zma = (Dx4ZMA) user;
			zma.setSmas(getSmasForParent(user));
			zma.setMas(getMasForParent(user));
			zma.setAgents(getAgentsForParent(user));
			zma.setPlayers(getPlayersForParent(user));
			return;
		}
		
		if (user.getClass().equals(Dx4SMA.class))
		{
			Dx4SMA sma = (Dx4SMA) user;
			sma.setMas(getMasForParent(user));
			sma.setAgents(getAgentsForParent(user));
			sma.setPlayers(getPlayersForParent(user));
			return;
		}
		
		if (user.getClass().equals(Dx4MA.class))
		{
			Dx4MA ma = (Dx4MA) user;
			ma.setAgents(getAgentsForParent(user));
			ma.setPlayers(getPlayersForParent(user));
			return;
		}
		
		if (user.getClass().equals(Dx4Agent.class))
		{
			Dx4Agent agent = (Dx4Agent) user;
			agent.setPlayers(getPlayersForParent(user));
			return;
		}
		
		log.error("getMembersForUser : illegal class for user :" + user.getClass());
	}
	
	private List<Dx4Comp> getCompsForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4Comp.class,Dx4Role.ROLE_COMP);
		List<Dx4Comp> comps = new ArrayList<Dx4Comp>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			comps.add((Dx4Comp) user);
		}
		return comps;
	}
	
	private List<Dx4ZMA> getZmasForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4ZMA.class,Dx4Role.ROLE_ZMA); 
		List<Dx4ZMA> zmas = new ArrayList<Dx4ZMA>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			zmas.add((Dx4ZMA) user);
		}
		return zmas;
	}
	
	private List<Dx4SMA> getSmasForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4SMA.class,Dx4Role.ROLE_SMA); 
		List<Dx4SMA> zmas = new ArrayList<Dx4SMA>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			zmas.add((Dx4SMA) user);
		}
		return zmas;
	}
	
	private List<Dx4MA> getMasForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4MA.class,Dx4Role.ROLE_MA); 
		List<Dx4MA> mas = new ArrayList<Dx4MA>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			mas.add((Dx4MA) user);
		}
		return mas;
	}
	
	private List<Dx4Agent> getAgentsForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4Agent.class,Dx4Role.ROLE_AGENT); 
		List<Dx4Agent> agents = new ArrayList<Dx4Agent>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			agents.add((Dx4Agent) user);
		}
		return agents;
	}

	private List<Dx4Player> getPlayersForParent(Dx4SecureUser parent) throws Dx4PersistenceException
	{
		List<Dx4SecureUser> users = getUsersForParent(parent,Dx4Player.class,Dx4Role.ROLE_PLAY); 
		List<Dx4Player> players = new ArrayList<Dx4Player>();
		for (Dx4SecureUser user : users)
		{
			parent.getMembers().add(user);
			players.add((Dx4Player) user);
		}
		return players;
	}
	
	private List<Dx4SecureUser> getUsersForParent(final Dx4SecureUser parent,
			@SuppressWarnings("rawtypes") Class userClass,final Dx4Role role) throws Dx4PersistenceException {

		try
		{
			@SuppressWarnings("rawtypes")
			Class clazz = role.getCorrespondingClass();
			final String sql = "SELECT * FROM baseUser WHERE parentcode = ? AND role = ?";
			List<Dx4SecureUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement ps) throws SQLException {
				        	ps.setString(1,parent.getCode());
							ps.setString(2,role.name());
				        }
				      }, new Dx4SecureUserRowMapper1(clazz));
			for (Dx4SecureUser bu : bus)
				populateUser(bu);
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
		
	}

	private void populateUser(Dx4SecureUser user) throws Dx4PersistenceException {
		user.setAccount(dx4AccountDao.getForUser(user));
		user.setAuthorities(getAuthorities(user));
		user.setGameGroup(dx4MetaGameDao.getGameGroup(user));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Dx4Role> getAuthorities(final Dx4SecureUser user) throws Dx4PersistenceException {
		
		List<String> roleList;
		try
		{
			final String sql = "SELECT role FROM authority WHERE baseuserid= ?";
			roleList = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setObject(1,user.getId());
				        }
				      }, new RowMapper() {
				          public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				              return resultSet.getString(1);
				            }});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
		
		List<Dx4Role> roles = new ArrayList<Dx4Role>();
		for (String r : roleList)
		{
			roles.add(Dx4Role.valueOf(r));
		}
		
		return roles;
	}
	
	@Override
	public void setAsSystemMember(Dx4SecureUser user) throws Dx4PersistenceException  {
		String sql = "UPDATE baseuser SET systemmember = TRUE WHERE id = '" + user.getId() + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new Dx4PersistenceException(sql + " - " + e.getMessage());
		}		
	}

	@Override
	public void storeImage(final String email, MultipartFile data, final String contentType) throws Dx4PersistenceException {
		
		final InputStream is;
		try {
			is = data.getInputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
			log.error("Could not convert : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
		try
		{
			String sql = "DELETE FROM image WHERE email = '" + email + "'";
			getJdbcTemplate().update(sql);
			getJdbcTemplate().update("INSERT INTO image (email,contenttype,data) VALUES (?, ?, ?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, email);
							ps.setString(2, contentType);
							ps.setBinaryStream(3, is);
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
	public byte[] getImage(final String email) throws Dx4PersistenceException {
		
		byte[] imgBytes = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = getConnection().prepareStatement("SELECT data FROM image WHERE email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs != null) {
			    while (rs.next()) {
			        imgBytes = rs.getBytes(1);
			    }
			    rs.close();
			}
			ps.close();
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4PersistenceException("Could not execute : " + e.getMessage());
		}
		finally
		{
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ps!=null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return imgBytes;
	}

	@Override
	public void setDefaultPasswordForAll(String encoded) {
		String sql = "UPDATE baseuser SET password = '" + encoded + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
		}
	}
	
	@Override
	public void updateLeafInstance(Dx4SecureUser bu) {
		String sql = "UPDATE baseuser SET leafinstance = " + bu.getLeafInstance() + " WHERE id = '" + bu.getId() + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
		}
	}

	public Dx4MetaGameDao getDx4MetaGameDao() {
		return dx4MetaGameDao;
	}

	public void setDx4MetaGameDao(Dx4MetaGameDao dx4MetaGameDao) {
		this.dx4MetaGameDao = dx4MetaGameDao;
	}

	public Dx4AccountDao getDx4AccountDao() {
		return dx4AccountDao;
	}

	public void setDx4AccountDao(Dx4AccountDao dx4AccountDao) {
		this.dx4AccountDao = dx4AccountDao;
	}
	
	public static String buildGetPlayerIdsForUserSQL(Dx4SecureUser user)
	{
		String sql = "SELECT seqid FROM baseuser WHERE ROLE='ROLE_PLAY' AND PARENTCODE='" + user.getCode() + "'";
		// 1st tier
		String baseCond = "(SELECT CODE FROM baseuser WHERE PARENTCODE = '" + user.getCode() + "')";
		for (Dx4Role nextRole : user.getRole().getSubMemberRoles())
		{
			if (nextRole.equals(Dx4Role.ROLE_PLAY))
				break;
			sql += " OR PARENTCODE IN " + baseCond;
			baseCond = "(SELECT CODE FROM baseuser WHERE PARENTCODE IN " + baseCond + ")";
		}
		return sql;
	}

	@Override
	public void deleteMember(Dx4SecureUser user) throws Dx4PersistenceException {
		// BRUTE FORCE DELETES ALL MEMBER,CHILD MEMBER TREE, TRANSACTIONS, ETC. ONLY USE FOR DEV TESTING
		
		String value = Dx4Config.getProperties().getProperty("dx4.delete.member", "false");
		if (!value.equalsIgnoreCase("true"))
			throw new PersistenceRuntimeException("Illegal operation : deleteMember", 
					"dx4.delete.member" + " not set in dx4.properties");
		
		log.trace("DELETING MEMBER : " + user);
		
		deleteMember(user.getCode(),user.getSeqId());
		
		log.trace("MEMBER DELETED");
	}
	
	private void deleteMember(String code,long id) throws Dx4PersistenceException {
		// DELETES ALL MEMBER,CHILD MEMBER TREE, TRANSACTIONS, ETC. ONLY USE FOR DEV TESTING
		
		while (true)
		{
			String sql = "SELECT CODE FROM baseuser WHERE PARENTCODE='" + code + "'";
			List<String> userList = (List<String>) getJdbcTemplate().queryForList(sql, String.class );
			if (userList.isEmpty())
				break;
			
			for (String child : userList)
			{
				Dx4SecureUser user = getBaseUserByCode(child);
				deleteMember(child,user.getSeqId());
			}
		}
		
		String sql = "delete from account where userid="+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from authority where baseuserid in (select id from baseuser where seqId="+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from admin where userid="+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from numberexpo where userid="+id; 
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from gameactivator where groupid in (select id from gamegroup where userid="+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);

		sql = "delete from gamegroup where userid="+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from bet where metabetid in (select id from metabet where playerid = "+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from choice where metabetid in (select id from metabet where playerid = "+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from win where metabetid in (select id from metabet where playerid = "+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from metabet where playerid = "+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from comment where objectid in (select id from xtransaction where userid="+id+")";
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from xtransaction where userid="+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		sql = "delete from baseuser where seqid="+id;
		log.trace("deleteMember: " + sql);
		getJdbcTemplate().update(sql);
		
		log.trace("MEMBER DELETED");
	}

}
