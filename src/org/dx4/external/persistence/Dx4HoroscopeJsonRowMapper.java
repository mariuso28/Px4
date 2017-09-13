package org.dx4.external.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.json.message.Dx4HoroscopeJson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.codec.Base64;

public class Dx4HoroscopeJsonRowMapper implements RowMapper<Dx4HoroscopeJson>
{
	@Override
	public Dx4HoroscopeJson mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4HoroscopeJson hj = new Dx4HoroscopeJson();
		setValues(rs,hj);
		
		return hj;
	}
	
	private void setValues(ResultSet rs,Dx4HoroscopeJson hj) throws SQLException
	{
		hj.setSign(rs.getString("SIGN"));
		hj.setMonth(rs.getInt("MONTH"));
		hj.setStartDate(rs.getDate("STARTDATE"));
		hj.setEndDate(rs.getDate("ENDDATE"));
		byte[] img = rs.getBytes("IMAGE");
		img = Base64.encode(img);
		try {
			hj.setImage(new String(img,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}