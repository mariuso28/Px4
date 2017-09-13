package org.dx4.game.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.json.message.Dx4ProviderJson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.codec.Base64;

public class ProviderRowMapper implements RowMapper<Dx4ProviderJson>
{
	@Override
	public Dx4ProviderJson mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Dx4ProviderJson provider = new Dx4ProviderJson();
		setValues(rs,provider);
		
		return provider;
	}
	
	private void setValues(ResultSet rs,Dx4ProviderJson provider) throws SQLException
	{
		provider.setId(rs.getLong("ID"));
		provider.setName(rs.getString("NAME"));
		provider.setCode(rs.getString("CODE").charAt(0));
		provider.setUrl(rs.getString("URL"));
		byte[] img = rs.getBytes("IMAGE");
		provider.setRawImage(img);
		if (img!=null)
		{
			img = Base64.encode(img);
			try {
				provider.setImage(new String(img,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}