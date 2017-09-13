package org.dx4.player.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4FavouriteJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.persistence.JdbcDx4SecureUserDaoImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
	

public class JdbcDx4PlayerDaoImpl extends JdbcDx4SecureUserDaoImpl implements Dx4PlayerDao {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(JdbcDx4PlayerDaoImpl.class);
	
	@Override
	public void store(Dx4Player player){
		try
		{
			super.storeBaseUser(player);
		}
		catch (Dx4PersistenceException e)
		{
			throw new PersistenceRuntimeException("storing player : ",e.getMessage());
		}
	}
	
	@Override
	public Dx4Player getByUsername(String username) {
		Dx4Player player;
		try {
			player = (Dx4Player) super.getBaseUserByEmail(username,Dx4Player.class);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		if (player == null)
			return null;
		if (!player.getRole().equals(Dx4Role.ROLE_PLAY))
			return null;
		return player;
	}

	@Override
	public Dx4Player getPlayerById(long id) {
		Dx4Player player;
		try {
			player = (Dx4Player) super.getBaseUserBySeqId(id,Dx4Player.class);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		if (player == null)
			return null;
		if (!player.getRole().equals(Dx4Role.ROLE_PLAY))
			return null;
		return player;
	}

	@Override
	public List<Dx4Player> getPlayersByPhone(String phone) {
		List<Dx4Player> players = new ArrayList<Dx4Player>();
		try {
			List<Dx4SecureUser> users = (List<Dx4SecureUser>) super.getBaseUsersByPhone(phone,Dx4Player.class);
			for (Dx4SecureUser user : users)
			{
				if (!user.getRole().equals(Dx4Role.ROLE_PLAY))
					continue;
				
				players.add((Dx4Player) user);
			}
			
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			return players;
		}
		
		return players;
	}

	@Override
	public void storeFavourite(final Dx4FavouriteJson favourite,final long betId) {
		
		try
		{
			String sql = "DELETE FROM FAVOURITE WHERE BETID=? AND PLAYERID=?";			
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
					public void setValues(PreparedStatement preparedStatement) throws SQLException {
						preparedStatement.setLong(1,betId);
						preparedStatement.setObject(2,favourite.getPlayerId());
					}
				});
			
			sql = "INSERT INTO FAVOURITE (BETID,DESCRIPTION,PLAYERID) VALUES (?,?,?)";
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,betId);
					preparedStatement.setString(2,favourite.getDescription());
					preparedStatement.setObject(3,favourite.getPlayerId());
				}
			});
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not store favourite : " + favourite + " - " + e.getMessage());
		}
	}

	@Override
	public void deleteFavourite(final long favouriteId) {
		String sql = "DELETE FROM FAVOURITE WHERE ID=?";
		try
		{
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,favouriteId);
				}
			});
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("Could not delete favourite - " + e.getMessage());
		}
	}

	@Override
	public List<Dx4FavouriteJson> getFavourites(final UUID playerId) {

		String sql = "select f.* from favourite as f join bet as b on f.betid=b.id  where f.playerid=?  order by b.choice";
		List<Dx4FavouriteJson> favourites = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setObject(1,playerId);
			}
		}, BeanPropertyRowMapper.newInstance(Dx4FavouriteJson.class));
		return favourites;
	}
	
}
