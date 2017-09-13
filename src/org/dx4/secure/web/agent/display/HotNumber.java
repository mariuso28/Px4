package org.dx4.secure.web.agent.display;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dx4.bet.persistence.NumberExpo;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.agent.AgentController;

public class HotNumber 
{
	private String number;
	private String color;
	private Double bet;
	private String tickerString;
	private static int MAXDISPLAY = 20;
	
	
	private void createTickerString()
	{
		DecimalFormat df = new DecimalFormat("#0.00"); 
		tickerString = number + "&nbsp&nbsp&nbsp$" + df.format(bet);
	}

	
	private HotNumber(NumberExpo numberExpo) {
		super();
		this.number = numberExpo.getNumber();
		this.color = "blue";
		this.bet = numberExpo.getTbet();
		createTickerString();
	}
	
	public static List<HotNumber> refreshHotNumbers(Dx4SecureUser user,Dx4PlayGame playGame,Dx4Home dx4Home)
	{
		List<NumberExpo> currNumberExposBet = dx4Home.getNumberExpo( user.getCode(), playGame, user.getRole(), AgentController.getDefaultBetExpo(), false);
		return createHotNumbers(user,playGame,dx4Home,currNumberExposBet);
	}
	
	public static List<HotNumber> createHotNumbers(Dx4SecureUser user,Dx4PlayGame playGame, Dx4Home dx4Home,List<NumberExpo> currNumberExposBet)
	{
		Map<String,HotNumber> hotNumbers = new HashMap<String,HotNumber>();
		int lim=0;
		
		for (NumberExpo ne : currNumberExposBet)
		{
			if (lim++>MAXDISPLAY)
				break;
			if (hotNumbers.get(ne.getNumber())==null)
				hotNumbers.put(ne.getNumber(),new HotNumber(ne));
		}
		
		List<HotNumber> values = new ArrayList<HotNumber>();
		for (HotNumber hn : hotNumbers.values())
			values.add(hn);
		Collections.sort(values,new HotNumberComparator() );
		return values;
	}
	
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public static int getMAXDISPLAY() {
		return MAXDISPLAY;
	}

	public void setTickerString(String tickerString) {
		this.tickerString = tickerString;
	}

	public String getTickerString() {
		return tickerString;
	}
	
}
