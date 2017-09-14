package org.dx4.game.d2;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.game.Dx4Game;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameD2C extends Dx4Game {

	private static final long serialVersionUID = -2072918193382435968L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD2C.class);
	
	public Dx4GameD2C()
	{
		setGtype(Dx4GameTypeJson.D2C);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		String use = bet.getChoice();
		if (result.getFirstPlace().substring(2).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.First).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.First.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getSecondPlace().substring(2).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Second).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getSecondPlace(),Dx4PayOutTypeJson.Second.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getThirdPlace().substring(2).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Third).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getThirdPlace(),Dx4PayOutTypeJson.Third.name(),
					 winnings, result.getProvider().getCode());
			wins.add(win);
		}

		return wins;
	}
	
}
