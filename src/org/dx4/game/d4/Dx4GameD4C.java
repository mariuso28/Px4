package org.dx4.game.d4;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;

public class Dx4GameD4C extends Dx4GameD4Small {

	private static final long serialVersionUID = -9094711325571648417L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD4C.class);
	
	public Dx4GameD4C()
	{
		setGtype(Dx4GameTypeJson.D4C);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {

		return super.calcWin(metaBet, bet, result);
	}
	
}
