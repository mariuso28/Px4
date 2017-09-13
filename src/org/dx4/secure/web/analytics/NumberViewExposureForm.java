package org.dx4.secure.web.analytics;

import org.dx4.account.Dx4NumberExpo;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;

public class NumberViewExposureForm 
{
	private String number;
	private String threeDigits;
	private String numberDesc;
	private Dx4NumberExpo expo;
	private Dx4NumberExpo defaultExpo;
	private NumberExpo numberExpo;
	private NumberExpoCommand command;
	
	public NumberViewExposureForm()
	{
	}
	
	public NumberViewExposureForm(Dx4Services dx4Services,Dx4SecureUser currUser,String currActiveGame,int gType,String number)
	{
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(currActiveGame);
		Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet();
		
		setNumber(number);
		if (number.length()>=3)
			setThreeDigits(number.substring(number.length()-3));
		if (gType==4 || gType==2)
		{
			setNumberDesc(dx4Services.getDx4Home().getDescForNumber(number));
		}
		char digits =  NumberExpoForm.getDigits(gType);
		defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
		expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, number);
		if (expo==null)
			expo = defaultExpo;
		setNumberExpo(dx4Services.getDx4Home().getSingleNumberExpo(currUser.getCode(), playGame, currUser.getRole(), 
							-1L, false, number));
		
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setThreeDigits(String threeDigits) {
		this.threeDigits = threeDigits;
	}

	public String getThreeDigits() {
		return threeDigits;
	}

	public void setNumberDesc(String numberDesc) {
		this.numberDesc = numberDesc;
	}

	public String getNumberDesc() {
		return numberDesc;
	}

	public void setExpo(Dx4NumberExpo expo) {
		this.expo = expo;
	}

	public Dx4NumberExpo getExpo() {
		return expo;
	}

	public void setCommand(NumberExpoCommand command) {
		this.command = command;
	}

	public NumberExpoCommand getCommand() {
		return command;
	}

	public void setNumberExpo(NumberExpo numberExpo) {
		this.numberExpo = numberExpo;
	}

	public NumberExpo getNumberExpo() {
		return numberExpo;
	}
}
