package org.dx4.game.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.game.Dx4Game;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameD2E extends Dx4Game implements Serializable{

	private static final long serialVersionUID = 8848295505485311928L;

	public Dx4GameD2E()
	{
		setGtype(Dx4GameTypeJson.D2E);
	}
	
	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		for (String cons : result.getConsolations())
		{
			if (cons.substring(2).equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Cons).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,cons,Dx4PayOutTypeJson.Cons.name(),winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		return wins;
	}
}
