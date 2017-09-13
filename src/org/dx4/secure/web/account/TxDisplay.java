package org.dx4.secure.web.account;

import org.dx4.account.Dx4Transaction;

public class TxDisplay 
{
	private Dx4Transaction transX;
	private String userEmail;
	private String cpEmail;
	
	public TxDisplay(Dx4Transaction tx,String userEmail,String cpEmail)
	{
		setTransX(tx);
		setUserEmail(userEmail);
		setCpEmail(cpEmail);
	}
	
	
	public Dx4Transaction getTransX() {
		return transX;
	}


	public void setTransX(Dx4Transaction transX) {
		this.transX = transX;
	}


	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCpEmail() {
		return cpEmail;
	}
	public void setCpEmail(String cpEmail) {
		this.cpEmail = cpEmail;
	}
	
	
}
