package org.dx4.game.d3;

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

public class Dx4GameABCD extends Dx4Game {

	private static final long serialVersionUID = 185873622933665115L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameABCD.class);
	
	public Dx4GameABCD()
	{
		setGtype(Dx4GameTypeJson.ABCD);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		for (String spec : result.getSpecials())
		{
			if (spec.substring(1).equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Spec).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,spec,Dx4PayOutTypeJson.Spec.name(),winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		return wins;
	}
	
}
