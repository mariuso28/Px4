package org.dx4.bet.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.bet.Dx4Bet;
import org.springframework.jdbc.core.RowMapper;

public class Dx4BetRowMapper implements RowMapper<Dx4Bet>
{
	@Override
	public Dx4Bet mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dx4Bet bet = new Dx4Bet();
		
		bet.setId(rs.getLong("ID"));
		bet.setGameId(rs.getLong("GAMEID"));
		bet.setStake(rs.getDouble("STAKE"));
		bet.setTotalStake(rs.getDouble("TOTALSTAKE"));
		bet.setChoice(rs.getString("CHOICE"));
		bet.setProviderCodes(rs.getString("PROVIDERCODES"));
		bet.setMetaBetId(rs.getLong("METABETID"));
		bet.setOdds(rs.getDouble("ODDS"));
		return bet;
	}
}
