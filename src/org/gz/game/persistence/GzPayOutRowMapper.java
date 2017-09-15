package org.gz.game.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.springframework.jdbc.core.RowMapper;

public class GzPayOutRowMapper implements RowMapper<Dx4PayOut>
{

	@Override
	public  Dx4PayOut mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4PayOut payOut = new Dx4PayOut();
		payOut.setPayOut(rs.getDouble("PAYOUT"));
		payOut.setType(Dx4PayOutTypeJson.valueOfFromCode(rs.getString("Place").charAt(0)));
		
		return payOut;
	}
}
