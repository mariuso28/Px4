package org.dx4.bet.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.bet.Dx4NumberWin;
import org.springframework.jdbc.core.RowMapper;

public class Dx4NumberWinRowMapper implements RowMapper<Dx4NumberWin>
{	
	@Override
	public Dx4NumberWin mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dx4NumberWin nw = new Dx4NumberWin();
		
		nw.setNumber(rs.getString("RESULT"));
		nw.setWin(rs.getDouble("TOT"));
		nw.setProviderCode(rs.getString("PROVIDERCODE").charAt(0));
		nw.setPlace(rs.getString("PLACE"));
		return nw;
	}
}
