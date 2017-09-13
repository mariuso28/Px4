package org.dx4.secure.web.member;

import java.io.Serializable;

public class MemberCommand implements Serializable{
	private static final long serialVersionUID = 1827301005106124109L;
	private String previousDraw;
	private String activeGame;
	private String email;
	private String phone;
	
	public MemberCommand()
	{
	}
		
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPreviousDraw(String previousDraw) {
		this.previousDraw = previousDraw;
	}
	public String getPreviousDraw() {
		return previousDraw;
	}
	public void setActiveGame(String activeGame) {
		this.activeGame = activeGame;
	}
	public String getActiveGame() {
		return activeGame;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
