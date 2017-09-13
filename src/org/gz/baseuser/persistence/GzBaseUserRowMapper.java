package org.gz.baseuser.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;

public class GzBaseUserRowMapper implements RowMapper<GzBaseUser>{

	public GzBaseUserRowMapper()
	{
	}
	
	public GzBaseUser mapRow(ResultSet rs,int rowNum) {
	
		GzBaseUser bu = new GzBaseUser();
		try {
			setValues(rs,bu);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return bu;
	}

	public static void setValues(ResultSet rs,GzBaseUser bu) throws SQLException{
		
		// id,email,password,nickname,code,enabled,icon,parentcode,contact,phone
		
		bu.setId(UUID.fromString(rs.getString("id")));
		bu.setEmail(rs.getString("email"));
		bu.setPassword(rs.getString("password"));
		bu.setNickname(rs.getString("nickname"));
		bu.setEnabled(rs.getBoolean("enabled"));
		bu.setIcon(rs.getString("icon"));
		bu.setCode(rs.getString("code"));
		bu.setParentCode(rs.getString("parentcode"));
		bu.setContact(rs.getString("contact"));
		bu.setPhone(rs.getString("phone"));
		bu.setMemberId(rs.getString("memberid"));
		bu.setRole(GzRole.valueOf(rs.getString("role")));
	}
	
}
