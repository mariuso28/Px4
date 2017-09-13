package org.gz.baseuser.persistence;

import java.sql.ResultSet;

import org.springframework.jdbc.core.RowMapper;

import org.gz.baseuser.GzBaseUser;

public class GzBaseUserRowMapper1 implements RowMapper<GzBaseUser>{

	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	public GzBaseUserRowMapper1(@SuppressWarnings("rawtypes") Class clazz)
	{
		setClazz(clazz);
	}
	
	public GzBaseUser mapRow(ResultSet rs,int row) {
	
		try
		{
			GzBaseUser bu = (GzBaseUser) clazz.newInstance();
			GzBaseUserRowMapper.setValues(rs,bu);
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
