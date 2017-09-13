package org.dx4.secure.web.player;

import java.io.Serializable;

public class PlayerDetailsCommand implements Serializable{
	
	private static final long serialVersionUID = -8559560140458303604L;
	private String previousDraw;

	public PlayerDetailsCommand()
	{
		
	}
	
	public void setPreviousDraw(String previousDraw) {
		this.previousDraw = previousDraw;
	}

	public String getPreviousDraw() {
		return previousDraw;
	}

}
