package org.dx4.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dx4.account.Dx4Transaction;
import org.dx4.account.Dx4TransactionType;
import org.springframework.jdbc.core.RowMapper;

public class TransactionRowMapper implements RowMapper<Dx4Transaction>
{
	@Override
	public Dx4Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dx4Transaction trans = new Dx4Transaction();
		
		TransactionRowMapper.setValues(rs,trans);
		
		return trans;
	}
	
	public static void setValues(ResultSet rs,Dx4Transaction trans) throws SQLException
	{
		trans.setId(rs.getLong("ID"));
		trans.setUserId(rs.getLong("USERID"));
		trans.setCpId(rs.getLong("CPID"));
		trans.setDate(rs.getDate("DATE"));
		char transType = rs.getString("TYPE").charAt(0);
		trans.setType(Dx4TransactionType.getFromInitial(transType));
		trans.setAmount(rs.getDouble("AMOUNT"));
		trans.setRefId(rs.getLong("REFID"));
	}
}
