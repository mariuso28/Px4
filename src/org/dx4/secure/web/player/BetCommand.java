package org.dx4.secure.web.player;

import java.io.Serializable;

public class BetCommand implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6940888711204909323L;
	private String username;
	
	public BetCommand() {
	}

	public BetCommand(String username2) {
		setUsername(username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public String toString() {
		return "BetCommand [username=" + username + "]";
	}
	
	

}
