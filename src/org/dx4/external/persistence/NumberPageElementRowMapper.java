package org.dx4.external.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.json.message.Dx4NumberPageElementJson;
import org.springframework.jdbc.core.RowMapper;

public class NumberPageElementRowMapper implements RowMapper<Dx4NumberPageElementJson>
{
	@Override
	public Dx4NumberPageElementJson mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4NumberPageElementJson numberPageElement = new Dx4NumberPageElementJson();
		setValues(rs,numberPageElement);
		
		return numberPageElement;
	}
	
	private void setValues(ResultSet rs,Dx4NumberPageElementJson numberPageElement) throws SQLException
	{
		numberPageElement.setNumber(rs.getInt("NUMBERX"));
		numberPageElement.setDescription(rs.getString("DESCX"));
		String token = "" + numberPageElement.getNumber();
		while (token.length()<3)
			token = "0" + token;
		numberPageElement.setToken(token);
	}
}