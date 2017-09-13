package org.dx4.secure.web.admin;

import java.io.Serializable;
import java.util.Date;

public class AdminCommand implements Serializable{

	private static final long serialVersionUID = -2669929662291156446L;
	private String username;
	private Date playDate;
	
	public AdminCommand()
	{
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}


	public String getUsername() {
		return username;
	}


	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}


	public Date getPlayDate() {
		return playDate;
	}

	
	
}
