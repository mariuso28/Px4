package org.dx4.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx42DigitGame extends Dx4Game implements Serializable{

	private static final long serialVersionUID = -2960239079755865368L;

	public Dx42DigitGame()
	{
		setGtype(Dx4GameTypeJson.D2);
	}
	
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		if (result.getFirstPlace().substring(2).equals(bet.getChoice()))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.First).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.First.name(), winnings, result.getProvider().getCode());
			wins.add(win);
		}
		return wins;
	}
}
