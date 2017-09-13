package org.dx4.external.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.json.message.Dx4NumberPageElementJson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.codec.Base64;

public class NumberPageElementRowMapperX implements RowMapper<Dx4NumberPageElementJson>
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
		numberPageElement.setDescriptionCh(rs.getString("CDESC"));
		numberPageElement.setToken(rs.getString("CODE"));
		numberPageElement.setDictionary(rs.getString("DICTIONARY").charAt(0));
		
		byte[] img = rs.getBytes("IMAGE");
		img = Base64.encode(img);
		try {
			numberPageElement.setImage(new String(img,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}