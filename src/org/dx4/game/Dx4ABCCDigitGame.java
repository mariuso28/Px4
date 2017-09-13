package org.dx4.game;

import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4ABCCDigitGame extends Dx4ABCADigitGame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6723195372592986835L;

	public Dx4ABCCDigitGame()
	{
		setGtype(Dx4GameTypeJson.ABCC);
	}

	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		if (metaBet.isFloatingModel())
			return calcFloatingWin(metaBet,bet,result);
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		wins.addAll(super.calcWin(metaBet,bet,result));
		String use = bet.getChoice();
		super.calcWin(metaBet, bet, result);

		if (result.getSecondPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Second).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getSecondPlace(),Dx4PayOutTypeJson.Second.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getThirdPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Third).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getThirdPlace(),Dx4PayOutTypeJson.Third.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		
		return wins;
	}

	private List<Dx4Win> calcFloatingWin(Dx4MetaBet metaBet, Dx4Bet bet, Dx4DrawResultJson result) {
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		String use = bet.getChoice();
		
		if (result.getFirstPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.First.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getSecondPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getSecondPlace(),Dx4PayOutTypeJson.Second.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getThirdPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getThirdPlace(),Dx4PayOutTypeJson.Third.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		
		return wins;
	}
	
}
