package org.dx4.external.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.json.message.Dx4ZodiacJson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.codec.Base64;

public class Dx4ZodiacJsonRowMapper implements RowMapper<Dx4ZodiacJson>
{
	@Override
	public Dx4ZodiacJson mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4ZodiacJson zj = new Dx4ZodiacJson();
		setValues(rs,zj);
		
		return zj;
	}
	
	private void setValues(ResultSet rs,Dx4ZodiacJson zj) throws SQLException
	{
		zj.setSet(rs.getInt("SET"));
		zj.setYear(rs.getInt("YEAR"));
		zj.setAnimal(rs.getString("ANIMAL"));
		zj.setcAnimal(rs.getString("CANIMAL"));
		byte[] img = rs.getBytes("IMAGE");
		img = Base64.encode(img);
		try {
			zj.setImage(new String(img,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}