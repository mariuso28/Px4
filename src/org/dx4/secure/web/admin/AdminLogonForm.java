package org.dx4.secure.web.admin;

import java.io.Serializable;

import org.dx4.secure.web.member.UserLogonForm;

public class AdminLogonForm extends UserLogonForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4984989208249553251L;
	AdminLogon adminLogon;
	
	public AdminLogon getAdminLogon() {
		return (AdminLogon) getUserLogon();
	}
	public void setAdminLogon(AdminLogon adminLogon) {
		setUserLogon(adminLogon);
	}

	
}
