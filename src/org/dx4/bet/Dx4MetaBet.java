package org.dx4.bet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4MetaBet implements Serializable{
	
	private static final long serialVersionUID = 3774581601200648795L;

	private static final Logger log = Logger.getLogger(Dx4MetaBet.class);
	
	// !! DON'T SIMPLE DELETE AND RECREATE GETTER & SETTERS, SOME SET METHODS CONTAIN DEPENDENT METHODS
	private long id;
	private Dx4SecureUser player;
	private long playerId;
	private Dx4SecureUser cp;
	private long cpId;
	private long metaGameId;
	private Dx4MetaGame metaGame;
	private List<Dx4Bet> bets;
	private Date placed;
	private Date played;
	private long playGameId;
	private Dx4PlayGame playGame;
	private Date playDate;
	private boolean outstanding;
	private List<Dx4Win> wins;
	private double totalStake;
	private double totalWin;
	private MetaBetType type;
	private boolean floatingModel;
	
	public Dx4MetaBet()
	{
		id = -1L;
		setType(MetaBetType.AGENT);
		bets = new ArrayList<Dx4Bet>();
		wins = new ArrayList<Dx4Win>(); 
		totalStake = -1.0;					// stored once never updated
		totalWin = -1.0;
		type = MetaBetType.AGENT;
	}
	
	public List<Dx4Bet> getBetsForGame(Dx4Game game)
	{
		List<Dx4Bet> gbets = new ArrayList<Dx4Bet>();
		for (Dx4Bet bet : bets)
		{
			if (bet.getGameId()==game.getId())
				gbets.add(bet);
		}
		return gbets;
	}
	
	public List<Dx4Bet> getBetsForChoice(String choice)
	{
		List<Dx4Bet> gbets = new ArrayList<Dx4Bet>();
		for (Dx4Bet bet : bets)
		{
			if (bet.getChoice().equals(choice))
				gbets.add(bet);
		}
		return gbets;
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
	
	public Dx4SecureUser getPlayer() {
		return player;
	}
	public void setPlayer(Dx4SecureUser player) {
		this.player = player;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public Dx4SecureUser getCp() {
		return cp;
	}

	public void setCp(Dx4SecureUser cp) {
		this.cp = cp;
	}

	public long getCpId() {
		return cpId;
	}

	public void setCpId(long cpId) {
		this.cpId = cpId;
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}
	
	public void setMetaGame(Dx4MetaGame metaGame) {
		setMetaGameId(metaGame.getId());
		this.metaGame = metaGame;
	}

	public List<Dx4Bet> getBets() {
		return bets;
	}
	public void setBets(List<Dx4Bet> bets) {
		this.bets = bets;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setMetaGameId(long metaGameId) {
		this.metaGameId = metaGameId;
	}

	public long getMetaGameId() {
		return metaGameId;
	}

	public long getPlayGameId() {
		return playGameId;
	}

	public void setPlayGameId(long playGameId) {
		this.playGameId = playGameId;
	}

	public void setPlayGame(Dx4PlayGame playGame) {
		setPlayGameId(playGame.getId());
		this.playGame = playGame;
	}

	public Dx4PlayGame getPlayGame() {
		return playGame;
	}
	
	public boolean contains3DGame()
	{
		for (Dx4Bet bet : bets)
		{
			if (bet.getGame().getGtype().getDigits()==3)
				return true;
		}
		return false;
	}
	
	public boolean contains4DGame()
	{
		for (Dx4Bet bet : bets)
		{
			if (bet.getGame().getGtype().getDigits()==4)
				return true;
		}
		return false;
	}
	
	public double getBetExpo(String number)
	{// how much bet on the number
		double expo = 0.0;
		for (Dx4Bet bet : bets)
		{
			expo += bet.getStake()*bet.getProviderNum();
		}
		return expo;
	}
	
	public double getWinExpo(String number)
	{	// max can be won on the number - doubtful
		// maybe user average payouts but only sensible when volume high
		// need some other formula
		int len = number.length();
		double expo = 0.0;
		for (Dx4Bet bet : bets)
		{
			if (bet.getGame().getGtype().getDigits()==len)
			{
				Dx4PayOut payOut = bet.getGame().getPayOuts().get(0);			// highest payout
				expo += payOut.getPayOut()*bet.getProviderNum()*bet.getStake();
			}
		}
		return expo;
	}
	
	public double getTotalWin() {
		if (totalWin>=0)
			return totalWin;
		double total = 0.0;
		for (Dx4Win win : wins)
			total += win.getWin();
		return total;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	public void setType(MetaBetType type) {
		this.type = type;
	}

	public MetaBetType getType() {
		return type;
	}
	
	public double getTotalStake() 
	{
		if (totalStake>=0)
			return totalStake;
		for (Dx4Bet bet : bets)
			totalStake += bet.calcTotalStake();
		return totalStake;
	}

	public void performWinBet(Dx4Home dx4Home,List<Dx4DrawResultJson> results)
	{
		wins = new ArrayList<Dx4Win>();
		setTotalWin(0);
		for (Dx4DrawResultJson result : results)
		{
			List<Dx4Win> winBets = performWinBet(result);
			wins.addAll(winBets);
			for (Dx4Win win : winBets)
				totalWin += win.getWin();
		}
	}
	
	private List<Dx4Win> performWinBet(Dx4DrawResultJson result) {
		
		log.info("Checking win for result : " + result + " with metabet : " + this);
		List<Dx4Win> winBets = new ArrayList<Dx4Win>();
		for (Dx4Bet bet : bets)
		{
			if (bet.getProviderCodes().indexOf(result.getProvider().getCode().charValue())<0)
					continue;
			log.info("Checking win for : " + bet + " with game : " + bet.getGame());
			winBets.addAll(bet.getGame().calcWin(this, bet, result));
		}
		return winBets;
	}
	
	public List<Dx4Win> getWins() {
		return wins;
	}

	public void setWins(List<Dx4Win> wins) {
		this.wins = wins;
	}

	public Dx4Bet getBetById(long id) {
		for (Dx4Bet bet : bets)
			if (bet.getId()==id)
				return bet;
		return null;
	}
	
	public Dx4Win getWinById(long id) {
		for (Dx4Win win : wins)
			if (win.getId()==id)
				return win;
		return null;
	}

	public Date getPlayed() {
		return played;
	}

	public void setPlayed(Date played) {
		this.played = played;
	}

	public void setProvidersFromCodes(Dx4Home dx4Home) {

		for (Dx4Bet bet : bets)
		{
			bet.setProvidersFromCodes(dx4Home);
		}
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	public boolean isFloatingModel() {
		return floatingModel;
	}

	public void setFloatingModel(boolean floatingModel) {
		this.floatingModel = floatingModel;
	}
	

}
