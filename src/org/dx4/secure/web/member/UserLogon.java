package org.dx4.secure.web.member;

import java.io.Serializable;

public class UserLogon implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 959865100674488513L;
	//DONT SIMPLY DELETE AND RECREATE setUsername does a bit of work

	private String username;
	private String message;
	
	
	public UserLogon()
	{
		setMessage("");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
