package org.dx4.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx44DigitGameSmall extends Dx4Game {

	private static final long serialVersionUID = -9094711325571648417L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx44DigitGameSmall.class);
	
	public Dx44DigitGameSmall()
	{
		setGtype(Dx4GameTypeJson.D4Small);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		String use = bet.getChoice();
		if (result.getFirstPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.First).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.First.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getSecondPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Second).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getSecondPlace(),Dx4PayOutTypeJson.Second.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getThirdPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Third).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getThirdPlace(),Dx4PayOutTypeJson.Third.name(),
					 winnings, result.getProvider().getCode());
			wins.add(win);
		}

		return wins;
	}
	
}
