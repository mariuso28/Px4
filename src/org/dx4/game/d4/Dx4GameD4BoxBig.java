package org.dx4.game.d4;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;

public class Dx4GameD4BoxBig extends Dx4GameD4IBoxBig {

	private static final long serialVersionUID = -3110651259620290862L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD4BoxBig.class);
	
	public Dx4GameD4BoxBig()
	{
		setGtype(Dx4GameTypeJson.D4BoxBig);
	}

	@Override
	public synchronized List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		List<Dx4Win> wins = super.calcWin(metaBet, bet, result);
		return wins;
	}
	
}
