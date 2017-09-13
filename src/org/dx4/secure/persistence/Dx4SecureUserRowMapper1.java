package org.dx4.secure.persistence;

import java.sql.ResultSet;

import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.jdbc.core.RowMapper;

public class Dx4SecureUserRowMapper1 implements RowMapper<Dx4SecureUser>{

	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	public Dx4SecureUserRowMapper1(@SuppressWarnings("rawtypes") Class clazz)
	{
		setClazz(clazz);
	}
	
	public Dx4SecureUser mapRow(ResultSet rs,int row) {
	
		try
		{
			Dx4SecureUser bu = (Dx4SecureUser) clazz.newInstance();
			Dx4SecureUserRowMapper.setValues(rs,bu);
			return bu;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void setClazz(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;
	}
	
	
}
