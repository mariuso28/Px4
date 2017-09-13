package org.gz.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.gz.account.GzAccount;

public class GzAccountRowMapper implements RowMapper<GzAccount>{
	
	public GzAccountRowMapper()
	{
	}
	
	public GzAccount mapRow(ResultSet rs,int rowNum) {
	
		try
		{
			GzAccount account = new GzAccount();
			setValues(rs,account);
			return account;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void setValues(ResultSet rs,GzAccount account) throws SQLException {
		
		account.setBalance(rs.getDouble("balance"));
		account.setCredit(rs.getDouble("credit"));
		account.setBetCommission(rs.getDouble("betcommission"));
		account.setWinCommission(rs.getDouble("wincommission"));
	}

}
