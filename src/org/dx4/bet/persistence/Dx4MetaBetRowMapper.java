package org.dx4.bet.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.MetaBetType;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.springframework.jdbc.core.RowMapper;

public class Dx4MetaBetRowMapper implements RowMapper<Dx4MetaBet>
{
	@Override
	public Dx4MetaBet mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String type = rs.getString("TYPE");
		MetaBetType metaBetType;
		Dx4MetaBet metaBet;
		if (type.startsWith("A"))
		{
			metaBet = new Dx4MetaBet();
			metaBetType = MetaBetType.AGENT;
		}
		/*
		else
		if (type.startsWith("H"))
		{
			metaBet = new Dx4Hedge();
			metaBetType = MetaBetType.HEDGE;
		}
		*/
		else
			throw new Dx4ExceptionFatal("INVALID METABET TYPE : " + type);
		
		Dx4MetaBetRowMapper.populateMetaBet(metaBet,metaBetType,rs);
		
		return metaBet;
	}
	
	public static void populateMetaBet(Dx4MetaBet metaBet,MetaBetType metaBetType,ResultSet rs)  throws SQLException
	{
		metaBet.setId(rs.getLong("ID"));
		metaBet.setPlayerId(rs.getLong("PLAYERID"));
		metaBet.setCpId(rs.getLong("CPID"));
		metaBet.setMetaGameId(rs.getLong("METAGAMEID"));
		metaBet.setPlaced(rs.getTimestamp("PLACED"));
		metaBet.setPlayed(rs.getTimestamp("PLAYED"));
		int outstanding = rs.getInt("OUTSTANDING");
		metaBet.setOutstanding(outstanding == 1);
		metaBet.setPlayGameId(rs.getLong("PLAYGAMEID")); 
		metaBet.setTotalStake(rs.getDouble("TOTALSTAKE"));
		metaBet.setTotalWin(rs.getDouble("TOTALWIN"));
		metaBet.setType(metaBetType);
		metaBet.setPlayDate(rs.getDate("PLAYDATE"));
		metaBet.setFloatingModel(rs.getBoolean("FLOATINGMODEL"));
	}
}
