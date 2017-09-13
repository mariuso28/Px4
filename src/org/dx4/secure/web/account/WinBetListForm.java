package org.dx4.secure.web.account;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.player.display.DisplayMetaBetWin;

public class WinBetListForm {
	
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(WinBetListForm.class);
	
	private Dx4Transaction trans;
	private DisplayMetaBetWin displayMetaBetWin;
	
	
	public WinBetListForm(Dx4Transaction trans,Dx4Home dx4Home)
	{
		setTrans(trans);
		createDisplayMetaBetWin(dx4Home);
	}
	
	private void createDisplayMetaBetWin(Dx4Home dx4Home)
	{
		Dx4MetaBet metaBet;
		try {
			metaBet = dx4Home.getMetaBetForTransaction(getTrans());
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}

		displayMetaBetWin = new DisplayMetaBetWin(metaBet,dx4Home,"-");
	}

	public Dx4Transaction getTrans() {
		return trans;
	}

	public void setTrans(Dx4Transaction trans) {
		this.trans = trans;
	}

	public DisplayMetaBetWin getDisplayMetaBetWin() {
		return displayMetaBetWin;
	}

	public void setDisplayMetaBetWin(DisplayMetaBetWin displayMetaBetWin) {
		this.displayMetaBetWin = displayMetaBetWin;
	}


}
