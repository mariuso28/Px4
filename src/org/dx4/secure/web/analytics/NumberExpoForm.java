package org.dx4.secure.web.analytics;

import java.util.List;

import org.dx4.account.Dx4NumberExpo;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.dx4.utils.NumberGrid;

public class NumberExpoForm {
	
	private Dx4NumberExpo defaultExpo;
	private List<Dx4NumberExpo> betExpos;
	private List<Dx4NumberExpo> winExpos;
	private NumberExpoCommand command;
	private String returnAdress;
	
	public NumberExpoForm()
	{
	}
	
	public NumberExpoForm(Dx4Services dx4Services,Dx4SecureUser user,NumberGrid numberGridExpo)
	{
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(user, digits);
		betExpos = dx4Services.getDx4Home().getDx4NumberExposForUser(user, digits, false);
		winExpos = dx4Services.getDx4Home().getDx4NumberExposForUser(user, digits, true);
		setNumberGridExposures(numberGridExpo);
	}

	private void setNumberGridExposures(NumberGrid numberGridExpo) {
		for (Dx4NumberExpo expo : betExpos)
			numberGridExpo.getNumberStore(expo.getNumber()).setBetExposure(expo.getBetExpo());
		for (Dx4NumberExpo expo : winExpos)
			numberGridExpo.getNumberStore(expo.getNumber()).setWinExposure(expo.getWinExpo());
	}

	static char getDigits(int gType)
	{
		char digits = '2';
		if (gType==4)
			digits = '4';
		else
		if (gType==2)
			digits = '3';
		return digits;
	}
	
	public void setDefaultExpo(Dx4NumberExpo defaultExpo) {
		this.defaultExpo = defaultExpo;
	}

	public Dx4NumberExpo getDefaultExpo() {
		return defaultExpo;
	}

	public List<Dx4NumberExpo> getBetExpos() {
		return betExpos;
	}

	public void setBetExpos(List<Dx4NumberExpo> betExpos) {
		this.betExpos = betExpos;
	}

	public List<Dx4NumberExpo> getWinExpos() {
		return winExpos;
	}

	public void setWinExpos(List<Dx4NumberExpo> winExpos) {
		this.winExpos = winExpos;
	}

	public NumberExpoCommand getCommand() {
		return command;
	}

	public void setCommand(NumberExpoCommand command) {
		this.command = command;
	}

	public void setReturnAdress(String returnAdress) {
		this.returnAdress = returnAdress;
	}

	public String getReturnAdress() {
		return returnAdress;
	}

}
