package org.dx4.json.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class Dx4MetaBetJson {

	private long id;
	private long metaGameId;
	private List<Dx4BetJson> bets;
	private Date placed;
	private Date played;
	private boolean outstanding;
	private List<Dx4WinJson> wins;
	private double totalStake;
	private double totalWin;
	private UUID metaBetUUID;
	
	public Dx4MetaBetJson()
	{
		bets = new ArrayList<Dx4BetJson>();
		wins = new ArrayList<Dx4WinJson>();
		metaBetUUID = UUID.randomUUID();
	}
	
	public List<Dx4MetaBetJson> createDx4MetaBetJsonForPlaygames(Dx4MetaGameJson metaGameJson)
	{
		List<Dx4MetaBetJson> mbs = new ArrayList<Dx4MetaBetJson>();
		for (Dx4BetJson bet : bets)
		{
			Dx4MetaBetJson mb = getDx4MetaBetJsonByPlayGameId(mbs,bet.getPlayGameId());
			mb.getBets().add(bet);
			totalStake += bet.getBig() + bet.getSmall();
		}
		for (Dx4MetaBetJson mb : mbs)
			totalStake += mb. getTotalStake(metaGameJson);
		
		return mbs;
	}
	
	private Dx4MetaBetJson getDx4MetaBetJsonByPlayGameId(List<Dx4MetaBetJson> mbs,long playGameId) {
		for (Dx4MetaBetJson mb : mbs)
		{
			if (mb.getBets().get(0).getPlayGameId() == playGameId)
				return mb;
		}
		Dx4MetaBetJson mb = new Dx4MetaBetJson();
		mb.setMetaGameId(this.getMetaGameId());
		mb.setPlaced((new GregorianCalendar()).getTime());
		mbs.add(mb);
		return mb;
	}

	public double getTotalStake(Dx4MetaGameJson metaGameJson)
	{
		double total = 0.0;
		for (Dx4BetJson bet : bets)
		{
			List<Dx4GameTypeJson> gameTypes = metaGameJson.getGamesTypesForBet(bet);
			for (Dx4GameTypeJson gameType : gameTypes)
			{
				if (gameType.isBig())
					total += (gameType.calcStake(bet.getChoice(),bet.getBig()) * bet.getProviders().size());
				else
					total += (gameType.calcStake(bet.getChoice(),bet.getSmall()) * bet.getProviders().size());
				
			}
		}
		
		return total;
	}
	
	public static List<String> createRolledNumbers(String choice)
	{
		if (choice.indexOf('R')<0)
		{
			return new ArrayList<String>(Arrays.asList(choice));
		}
		List<String> choices = createRolledNumberList(choice);
		while (choices.get(0).indexOf('R')>=0)
		{
			List<String> newChoices = new ArrayList<String>();
			for (String cc : choices)
				newChoices.addAll(createRolledNumberList(cc));
			choices = newChoices;
		}
		return choices;
	}
	
	private static List<String> createRolledNumberList(String choice)
	{
		int pos = choice.indexOf('R');
		List<String> choices = new ArrayList<String>();
		for (int i=0; i<10; i++)
		{
			String rchoice = choice.substring(0,pos) + Integer.toString(i) + choice.substring(pos+1);
			choices.add(rchoice);
		}
		return choices;
	}
	
	public void consolidateBets()
	{
		setBets(consolidateBets(bets));
	}
	
	public static List<Dx4BetJson> consolidateBets(List<Dx4BetJson> bets)
	{
		List<Dx4BetJson> consolidatedBets = new ArrayList<Dx4BetJson>();
		while (!bets.isEmpty())
		{
			Dx4BetJson bet = bets.remove(0);
			consolidatedBets.add(bet);
			List<Integer> pos = new ArrayList<Integer>();
			int index = 0;
			for (Dx4BetJson cb : bets)
			{
				if (bet.canConsolidate(cb))
					pos.add(index);
				index++;
			}
			for (Integer p : pos)
			{
				bet.consolidate(bets.remove(p.intValue()));
			}
		}
		return consolidatedBets;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public List<Dx4BetJson> getBets() {
		return bets;
	}

	public void setBets(List<Dx4BetJson> bets) {
		this.bets = bets;
	}

	public Date getPlaced() {
		return placed;
	}

	public void setPlaced(Date placed) {
		this.placed = placed;
	}

	public boolean isOutstanding() {
		return outstanding;
	}

	public void setOutstanding(boolean outstanding) {
		this.outstanding = outstanding;
	}

	public List<Dx4WinJson> getWins() {
		return wins;
	}

	public void setWins(List<Dx4WinJson> wins) {
		this.wins = wins;
	}

	public double getTotalStake() {
		return totalStake;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public double getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	public Date getPlayed() {
		return played;
	}

	public void setPlayed(Date played) {
		this.played = played;
	}

	public long getMetaGameId() {
		return metaGameId;
	}

	public void setMetaGameId(long metaGameId) {
		this.metaGameId = metaGameId;
	}

	@Override
	public String toString() {
		return "Dx4MetaBetJson [id=" + id + ", metaGameId=" + metaGameId + ", bets=" + bets + ", placed=" + placed
				+ ", played=" + played + ", outstanding=" + outstanding + ", wins=" + wins + ", totalStake="
				+ totalStake + ", totalWin=" + totalWin + "]";
	}

	public UUID getMetaBetUUID() {
		return metaBetUUID;
	}

	public void setMetaBetUUID(UUID metaBetUUID) {
		this.metaBetUUID = metaBetUUID;
	}

}
