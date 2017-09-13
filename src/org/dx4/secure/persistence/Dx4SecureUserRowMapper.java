package org.dx4.secure.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.jdbc.core.RowMapper;

public class Dx4SecureUserRowMapper implements RowMapper<Dx4SecureUser>{

	public Dx4SecureUserRowMapper()
	{
	}
	
	public Dx4SecureUser mapRow(ResultSet rs,int rowNum) {
	
		Dx4SecureUser bu = new Dx4SecureUser();
		try {
			setValues(rs,bu);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return bu;
	}

	public static void setValues(ResultSet rs,Dx4SecureUser bu) throws SQLException{
		
		// id,seqid,email,password,nickname,code,enabled,icon,parentcode,contact,phone
		
		bu.setId(UUID.fromString(rs.getString("id")));
		bu.setSeqId(rs.getLong("seqid"));
		bu.setEmail(rs.getString("email"));
		bu.setPassword(rs.getString("password"));
		bu.setNickname(rs.getString("nickname"));
		bu.setEnabled(rs.getBoolean("enabled"));
		bu.setIcon(rs.getString("icon"));
		bu.setCode(rs.getString("code"));
		bu.setParentCode(rs.getString("parentcode"));
		bu.setContact(rs.getString("contact"));
		bu.setPhone(rs.getString("phone"));
		bu.setRole(Dx4Role.valueOf(rs.getString("role")));
	}
	
}
