package org.dx4.secure.web.player;

import java.io.Serializable;

import org.dx4.secure.web.member.UserLogonForm;

public class PlayerLogonForm extends UserLogonForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1402547467298194350L;

	public void setPlayerLogon(PlayerLogon playerLogon) {
		setUserLogon(playerLogon);
	}
	 
    public PlayerLogon getPlayerLogon()
    {
    	return (PlayerLogon) getUserLogon();
    }
  
}

