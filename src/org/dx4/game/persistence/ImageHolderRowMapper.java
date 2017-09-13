package org.dx4.game.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.codec.Base64;

public class ImageHolderRowMapper implements RowMapper<ImageHolder>
{
	@Override
	public ImageHolder mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ImageHolder ih = new ImageHolder();
		setValues(rs,ih);
		
		return ih;
	}
	
	private void setValues(ResultSet rs,ImageHolder ih) throws SQLException
	{
		ih.setName(rs.getString("NAME"));
		byte[] img = rs.getBytes("IMAGE");
		if (img!=null)
		{
			img = Base64.encode(img);
			try {
				ih.setImage(new String(img,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}