package org.dx4.game.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dx4.game.Dx42DigitGame;
import org.dx4.game.Dx44DigitGameBig;
import org.dx4.game.Dx44DigitGameBigBox;
import org.dx4.game.Dx44DigitGameBigIbox;
import org.dx4.game.Dx44DigitGameSmall;
import org.dx4.game.Dx44DigitGameSmallBox;
import org.dx4.game.Dx44DigitGameSmallIbox;
import org.dx4.game.Dx4ABCADigitGame;
import org.dx4.game.Dx4ABCCDigitGame;
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
		
		if (type.equals(Dx4GameTypeJson.D2))
			game = new Dx42DigitGame();
		else
		if (type.equals(Dx4GameTypeJson.D4Big))
			game = new Dx44DigitGameBig();
		else
		if (type.equals(Dx4GameTypeJson.D4Small))
			game = new Dx44DigitGameSmall();
		else	
		if (type.equals(Dx4GameTypeJson.ABCA))
			game = new Dx4ABCADigitGame();
		else
		if (type.equals(Dx4GameTypeJson.ABCC))
			game = new Dx4ABCCDigitGame();
		else	
		if (type.equals(Dx4GameTypeJson.D4IBoxBig))
			game = new Dx44DigitGameBigIbox();
		else
		if (type.equals(Dx4GameTypeJson.D4IBoxSmall))
			game = new Dx44DigitGameSmallIbox();
		else	
		if (type.equals(Dx4GameTypeJson.D4BoxBig))
			game = new Dx44DigitGameBigBox();
		else
		if (type.equals(Dx4GameTypeJson.D4BoxSmall))
			game = new Dx44DigitGameSmallBox();
		else	
		{
			log.error("Dx4GameRowMapper - UNKNOWN game type: " + type);
			throw new SQLException("Dx4GameRowMapper - UNKNOWN game type: " + type);
		}
		
		game.setId(rs.getLong("ID"));
		game.setStake(rs.getDouble("STAKE"));
		game.setMinBet(rs.getDouble("MINBET"));
		game.setMaxBet(rs.getDouble("MAXBET"));
		return game;
	}

}
