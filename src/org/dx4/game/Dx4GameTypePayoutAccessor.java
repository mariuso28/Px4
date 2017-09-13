package org.dx4.game;

import java.util.ArrayList;
import java.util.List;

import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameTypePayoutAccessor {

	public static List<Dx4PayOut> getPayOuts(Dx4GameTypeJson gameType)
	{
		List<Dx4PayOut> payOuts = new ArrayList<Dx4PayOut>();
		payOuts.add(new Dx4PayOut(Dx4PayOutTypeJson.First,0.0));
		if (gameType.equals(Dx4GameTypeJson.D4Small) || gameType.equals(Dx4GameTypeJson.D4Big) 
				|| gameType.equals(Dx4GameTypeJson.ABCC)) 
		{
			payOuts.add(new Dx4PayOut(Dx4PayOutTypeJson.Second,0.0));
			payOuts.add(new Dx4PayOut(Dx4PayOutTypeJson.Third,0.0));
		}
		if (gameType.equals(Dx4GameTypeJson.D4Big))
		{
			payOuts.add(new Dx4PayOut(Dx4PayOutTypeJson.Spec,0.0));
			payOuts.add(new Dx4PayOut(Dx4PayOutTypeJson.Cons,0.0));
		}
			
		return payOuts;
	}
}
