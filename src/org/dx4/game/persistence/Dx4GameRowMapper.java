package org.dx4.game.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.json.message.Dx4GameTypeJson;
import org.springframework.jdbc.core.RowMapper;

public class Dx4GameRowMapper implements RowMapper<Dx4Game>
{
	private static final Logger log = Logger.getLogger(Dx4GameRowMapper.class);
	
	@Override
	public Dx4Game mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4Game game = null;
		Dx4GameTypeJson type = Dx4GameTypeJson.valueOf(rs.getString("GTYPE"));
		
		@SuppressWarnings("rawtypes")
		Class clazz = type.getCorrespondingClass();
		try {
			game = (Dx4Game) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			
			e.printStackTrace();
			log.error("Couldn't instantiate game from : " + clazz.getName());
		}
		
		game.setId(rs.getLong("ID"));
		game.setStake(rs.getDouble("STAKE"));
		game.setMinBet(rs.getDouble("MINBET"));
		game.setMaxBet(rs.getDouble("MAXBET"));
		return game;
	}

}
