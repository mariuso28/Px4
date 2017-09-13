package org.gz.baseuser.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.gz.account.persistence.GzAccountDaoImpl;
import org.gz.admin.GzAdmin;
import org.gz.agent.GzAgent;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.baseuser.GzRoleType;
import org.gz.dustbin.GzDustbin;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.util.GetNextNumberNo4s;
import org.gz.util.StackDump;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Transactional
public class GzBaseUserDaoImpl extends GzAccountDaoImpl implements GzBaseUserDao {
	
	private static Logger log = Logger.getLogger(GzBaseUserDaoImpl.class);
	
	@Override
	public void storeBaseUser(final GzBaseUser baseUser) throws GzPersistenceException {
		
		baseUser.setId(UUID.randomUUID());
		baseUser.setMemberId(getNextMemberId());
		setNextUserCode(baseUser);
		
		try
		{
			getJdbcTemplate().update("INSERT INTO baseuser (id,email,contact,phone,nickname,code,parentcode,role,icon,enabled,password,leafinstance,memberid,roletype,rank) "
										+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
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
						ps.setString(13, baseUser.getMemberId());
						ps.setInt(14, baseUser.getRole().getType().getType());
						ps.setInt(15, baseUser.getRole().getRank());
			      }
			    });
			baseUser.setAuthorities(baseUser.getRole().getAllRoles());
			storeAuthorities(baseUser);
			storeAccount(baseUser.getAccount());
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void updateBaseUserProfile(final GzBaseUser baseUser) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE baseuser SET email=?,contact=?,phone=?,nickname=?,icon =?,enabled=?,password=? WHERE id=?"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, baseUser.getEmail());
						ps.setString(2, baseUser.getContact());
						ps.setString(3, baseUser.getPhone());
						ps.setString(4, baseUser.getNickname());
						ps.setString(5, baseUser.getIcon());
						ps.setBoolean(6, baseUser.isEnabled());
						ps.setString(7, baseUser.getPassword());
						ps.setObject(8, baseUser.getId());
			      }
			    });
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private synchronized void setNextUserCode(GzBaseUser user) throws GzPersistenceException {
		
		if (user.getRole().equals(GzRole.ROLE_GZADMIN))
		{
			user.setCode(GzAdmin.getDefaultCode());
			user.setSystemMember(true);
			return;
		}
		
		String parentCode = user.getParentCode();
		Long nextCode = getNextCode(user.getRole(),parentCode);
		if (user.getParent().getRole().equals(GzRole.ROLE_GZADMIN))
			parentCode = "";
		user.setLeafInstance(nextCode);
		user.setCode( parentCode + user.getRole().getCode() + nextCode.toString() );
	}
	
	@Override
	public String getEmailForId(UUID id) throws GzPersistenceException
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		return email;
	}
	
	private Long getNextCode(GzRole role,String parentCode) throws GzPersistenceException
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	

		if (leafInstance==null)
			return 0L;
		
		long lf = GetNextNumberNo4s.next(leafInstance);
		log.info("Using leaf instance : " + lf);
		return lf;
	}
	
	private void storeAuthorities(final GzBaseUser baseUser) throws GzPersistenceException
	{
		try
		{
			for (final GzRole role : baseUser.getAuthorities())
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public GzBaseUser getBaseUserByEmail(final String email,@SuppressWarnings("rawtypes") Class clazz) throws GzPersistenceException 
	{	
		try
		{
			final String sql = "SELECT * FROM baseUser WHERE email=?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, email);
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public GzBaseUser getBaseUserByMemberId(final String memberId) throws GzPersistenceException 
	{	
		try
		{
			final String sql = "SELECT roletype FROM baseUser WHERE memberId=?";
			int roletype = getJdbcTemplate().queryForObject(sql,new Object[]{ memberId }, Integer.class );
			@SuppressWarnings("rawtypes")
			Class clazz = GzRoleType.getCorrespondingClassForType(roletype);
			final String sql1 = "SELECT * FROM baseUser WHERE memberId=?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql1,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, memberId);
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public GzBaseUser getBaseUserByCode(final String code) throws GzPersistenceException {
		
		try
		{
			GzRole role = GzRole.getRoleForCode(code);
			@SuppressWarnings("rawtypes")
			Class clazz = role.getType().getCorrespondingClass();
			
			final String sql = "SELECT * FROM baseUser WHERE code=?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, code);
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	/*
	private GzRole getRole(GzBaseUser user) throws GzPersistenceException {
		
		String sql = "SELECT role FROM baseuser WHERE id = ?";
		try
		{
			PreparedStatement ps = getConnection().prepareStatement(sql);	
			ps.setObject(1,user.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			String role = rs.getString(1);
			return GzRole.valueOf(role);
		}
		catch (Exception e)
		{
			log.error("Could not execute : PS : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException("PS : " + sql + " - " + e.getMessage());
		}	
	}
	*/
	
	@Override
	public List<String> getMemberCodes(final GzBaseUser baseUser) throws GzPersistenceException
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	protected String getCodeForEmail(String email) throws GzPersistenceException {
		
		String sql = "SELECT code FROM baseUser WHERE email = ?";
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { email.toLowerCase() }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void getDownstreamForParent(GzBaseUser parent) {
		
		try {
			getMembersForUser(parent);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
	}
	
	protected void getMembersForUser(GzBaseUser user) throws GzPersistenceException
	{
		user.setMembers(new ArrayList<GzBaseUser>());
		
		if (user.getClass().equals(GzAdmin.class))					// admin only has dustbins
		{
			user.setMembers(getUsersForParent(user,GzDustbin.class));
			return;
		}
		if (user.getClass().equals(GzDustbin.class))					
		{
			user.setMembers(getUsersForParent(user,GzDustbin.class));
			user.getMembers().addAll(getUsersForParent(user,GzAgent.class));
			return;
		}
		if (user.getClass().equals(GzAgent.class))
		{
			user.setMembers(getUsersForParent(user,GzAgent.class));
			List<GzBaseUser> players = getUsersForParent(user,GzBaseUser.class);
			user.getMembers().addAll(players);				// players
			GzAgent agent = (GzAgent) user;
			for (GzBaseUser player : players)
				agent.getPlayers().put(player.getMemberId(),player);
			return;
		}
		
		
		log.error("getMembersForUser : illegal class for user :" + user.getClass());
	}
	
	private List<GzBaseUser> getUsersForParent(final GzBaseUser parent,@SuppressWarnings("rawtypes") final Class clazz) throws GzPersistenceException {

		try
		{
			List<GzBaseUser> bus = null;
			if (clazz.equals(GzAgent.class))
			{
				final String sql = "SELECT * FROM baseUser WHERE parentcode = ? AND roletype=2 ORDER BY rank,memberid";
				bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
					        public void setValues(PreparedStatement ps) throws SQLException {
					        	ps.setString(1,parent.getCode());
					        }
					      }, new GzBaseUserRowMapper1(clazz));
			}
			else
			if (clazz.equals(GzDustbin.class))
			{
				final String sql = "SELECT * FROM baseUser WHERE parentcode = ? AND roletype=1 ORDER BY rank,memberid";
				bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
					        public void setValues(PreparedStatement ps) throws SQLException {
					        	ps.setString(1,parent.getCode());
					        }
					      }, new GzBaseUserRowMapper1(clazz));
			}
			else				// players
			{
				final String sql = "SELECT * FROM baseUser WHERE parentcode = ? AND roletype=3  ORDER BY memberid";
				bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
						        public void setValues(PreparedStatement ps) throws SQLException {
						        	ps.setString(1,parent.getCode());
						        }
						      }, new GzBaseUserRowMapper1(clazz));
			}
			
			for (GzBaseUser bu : bus)
				populateUser(bu);
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		
	}

	private void populateUser(GzBaseUser user) throws GzPersistenceException {
		user.setAccount(getAccount(user));
		user.setAuthorities(getAuthorities(user));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<GzRole> getAuthorities(final GzBaseUser user) throws GzPersistenceException {
		
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		
		List<GzRole> roles = new ArrayList<GzRole>();
		for (String r : roleList)
		{
			roles.add(GzRole.valueOf(r));
		}
		
		return roles;
	}

	@Override
	public Double getDownStreamCredit(GzBaseUser user,GzBaseUser parent)
	{
		String sql;
		if (user==null)
		{
			sql = "SELECT SUM(credit) FROM account WHERE baseuserid IN " +
				"(SELECT id FROM baseuser WHERE parentcode='" + parent.getCode() +"' AND enabled=TRUE)";
		}
		else
		{
			sql = "SELECT SUM(credit) FROM account WHERE baseuserid IN " +
					"(SELECT id FROM baseuser WHERE parentcode='" + parent.getCode() +"' AND enabled=TRUE) AND " +
					"baseuserid <> '" + user.getId() +"'";
		}
		try
		{
			log.trace("sql = "  + sql );
			Double total = getJdbcTemplate().queryForObject(sql,Double.class);
			if (total == null)
				total = 0.0;
			return total;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("getDownStreamCredit : " + e + " - " + e.getMessage());
			return 0.0;
		}
	}

	@Override
	public void setAsSystemMember(GzBaseUser user) throws GzPersistenceException  {
		String sql = "UPDATE baseuser SET systemmember = TRUE WHERE id = '" + user.getId() + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}		
	}

	@Override
	public void storeImage(final String email, MultipartFile data, final String contentType) throws GzPersistenceException {
		
		final InputStream is;
		try {
			is = data.getInputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
			log.error("Could not convert : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public byte[] getImage(final String email) throws GzPersistenceException {
		
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
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
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
	public void updateLeafInstance(GzBaseUser bu) {
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

	private String getNextMemberId() throws GzPersistenceException
	{
		try
		{
			final String sql = "SELECT id FROM memberid";
			String memberid = getJdbcTemplate().queryForObject(sql,String.class);
			while (true)
			{	
				int id = Integer.parseInt(memberid) + 1;
				memberid = Integer.toString(id);
				if (!memberid.contains("4"))
				{
					while (memberid.length()<4)					// pad to at least 4
						memberid = "0" + memberid;
					String sql1 = "UPDATE memberid SET id = '" + memberid +"'";
					getJdbcTemplate().update(sql1);
					return memberid;
				}
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<GzBaseUser> search(GzBaseUser user,String term,String type) throws GzPersistenceException {
		try
		{
			if (type.equals("email"))
				return searchByEmail(user,term);
			if (type.equals("contact"))
				return searchByContact(user,term);
			if (type.equals("phone"))
				return searchByPhone(user,term);
			return new ArrayList<GzBaseUser>();
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	private List<GzBaseUser> searchByPhone(GzBaseUser user,String term)
	{
		String sql = "select * from baseuser where phone=? and parentcode like ?";
		List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	          preparedStatement.setString(1, term.toLowerCase());
	          preparedStatement.setString(2, user.getCode() + "%");
	        }
	      }, new GzBaseUserRowMapper1(GzBaseUser.class));
		return bus;
	}
	
	private List<GzBaseUser> searchByContact(GzBaseUser user,String term)
	{
		String sql = "select * from baseuser where lower(contact)=? and parentcode like ?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	          preparedStatement.setString(1, term.toLowerCase());
	          preparedStatement.setString(2, user.getCode() + "%");
	        }
	      }, new GzBaseUserRowMapper1(GzBaseUser.class));
		if (!bus.isEmpty())
			return bus;
		sql = "select * from baseuser where lower(contact) like '%"+ term.toLowerCase() + "%' and parentcode like ?";
		bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	         preparedStatement.setString(1, user.getCode() + "%");
	        }
	        }, new GzBaseUserRowMapper1(GzBaseUser.class));
	     return bus;
	}
	
	private List<GzBaseUser> searchByEmail(GzBaseUser user,String term)
	{
		String sql = "select * from baseuser where lower(email)=? and parentcode like ?";
		List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	          preparedStatement.setString(1, term.toLowerCase());
	          preparedStatement.setString(2, user.getCode() + "%");
	        }
	      }, new GzBaseUserRowMapper1(GzBaseUser.class));
		return bus;
	}
}
