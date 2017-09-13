package org.dx4.admin.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.dx4.admin.Dx4Admin;
import org.springframework.jdbc.core.RowMapper;

public class Dx4AdminRowMapper implements RowMapper<Dx4Admin>{
	
	public Dx4AdminRowMapper()
	{
	}
	
	public Dx4Admin mapRow(ResultSet rs,int rowNum) {
	
		try
		{
			Dx4Admin admin = new Dx4Admin();
			setValues(rs,admin);
			return admin;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void setValues(ResultSet rs,Dx4Admin admin) throws SQLException {
		
		admin.setScheduledDowntimeSet(rs.getBoolean("scheduleddowntimeset"));
		Timestamp ts = rs.getTimestamp("scheduleddowntime");
		if (ts!=null)
			admin.setScheduledDowntime(new Date(ts.getTime()));	
	}

}
