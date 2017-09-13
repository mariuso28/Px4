package org.dx4.secure.web.member;

import java.io.Serializable;

public class UserLogonForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4507290448283438638L;
	
	private UserLogon userLogon;

	public void setUserLogon(UserLogon userLogon) {
		this.userLogon = userLogon;
	}

	public UserLogon getUserLogon() {
		return userLogon;
	}

}
