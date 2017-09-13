package org.dx4.secure.web.account;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.player.display.DisplayMetaBet;

public class BetListForm {
	
	// @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(BetListForm.class);
	
	private Dx4Transaction trans;
	private DisplayMetaBet displayMetaBet;
	private String code;
	
	
	public BetListForm(Dx4SecureUser currAccountUser,long transNumber,Dx4Home dx4Home,HashSet<Long> currExpandedBets)
	{
		setCode(currAccountUser.getCode());
		setTrans(dx4Home.getTransactionForId(transNumber));
		createDisplayMetaBetList(dx4Home,currExpandedBets);
	}
	
	
	private void createDisplayMetaBetList(Dx4Home dx4Home,HashSet<Long> currExpandedBets)
	{
		Dx4MetaBet metaBet;
		try {
			metaBet = dx4Home.getMetaBetForTransaction(trans);
		} catch (Dx4PersistenceException e) {
			
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		log.info("createDisplayMetaBetList : " + metaBet);
		String expanded = "+";
		if (currExpandedBets!=null)
			if (currExpandedBets.contains(metaBet.getId()))
				expanded = "-";
		
		displayMetaBet = new DisplayMetaBet(metaBet,dx4Home,expanded);
	}

	public Dx4Transaction getTrans() {
		return trans;
	}


	public void setTrans(Dx4Transaction trans) {
		this.trans = trans;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


	public DisplayMetaBet getDisplayMetaBet() {
		return displayMetaBet;
	}


	public void setDisplayMetaBet(DisplayMetaBet displayMetaBet) {
		this.displayMetaBet = displayMetaBet;
	}



}
