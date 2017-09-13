package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.web.member.UserDetailsForm;
import org.dx4.secure.web.player.display.DisplayMetaBet;
import org.dx4.secure.web.player.display.DisplayMetaBetWin;
import org.dx4.secure.web.player.display.DisplayMetaBetWinExpanded;
import org.dx4.secure.web.player.display.DisplayQuickBetGame;
import org.dx4.services.Dx4Services;

public class PlayerDetailsForm extends UserDetailsForm
{
	private static final Logger log = Logger.getLogger(PlayerDetailsForm.class);
	
	private List<DisplayMetaBet> displayMetaBetList;
	private List<DisplayMetaBetWin> displayMetaBetWinList;
	private List<DisplayQuickBetGame> displayQuickBetGameList;
	private boolean enable4D;
	private boolean enable3D;
	private List<Date> previousDraws;
	private PlayerDetailsCommand command;
	private ExternalGameResults externalGameResults;
	private Date lastWinDate;
	private List<DisplayMetaBetWinExpanded> lastWin;
	
	
	public PlayerDetailsForm()
	{
		super();
	}
	
	public PlayerDetailsForm(Dx4Profile profile)
	{
		setProfile(profile);
	}
	
	public PlayerDetailsForm(Dx4Player player,Dx4Services dx4Services,HashSet<Long> currExpandedBets,HashSet<Long> currExpandedWins)
	{
		this(player.createProfile());
		setDisplayQuickBetGameList(createDisplayQuickBetGameList(dx4Services.getDx4Home(),player));
		displayMetaBetList = PlayerDetailsForm.createDisplayMetaBet(player,dx4Services.getDx4Home(),currExpandedBets);
		displayMetaBetWinList = PlayerDetailsForm.createDisplayMetaBetWin(player,dx4Services.getDx4Home(),currExpandedWins);
		previousDraws = dx4Services.getDx4Home().getDrawDates();
		createLastWin(dx4Services.getDx4Home(),player);
		
		ExternalGameResults externalGameResults =  dx4Services.getExternalService().getActualExternalGameResults(previousDraws.get(0));
		if (externalGameResults==null)
		{
			log.error("External results for : " + previousDraws.get(0) + " not found");
		}
		setExternalGameResults(externalGameResults);
	}

	private List<DisplayQuickBetGame> createDisplayQuickBetGameList(Dx4Home dx4Home,Dx4Player player) {
		
		enable3D = false;
		enable4D = false;
		List<DisplayQuickBetGame> dList = new  ArrayList<DisplayQuickBetGame>();
		List<Dx4MetaGame> gameList = dx4Home.getUnplayedMetaGames();
		for (Dx4MetaGame metaGame : gameList)
		{
			if (player.getGameGroup().getGameActivator(metaGame)!=null)
			{
				synchronized(metaGame)
				{
					if (metaGame.getPlayGamesAvailableForBets().isEmpty())
						continue;
					dList.add(new DisplayQuickBetGame(metaGame));
				}
				setEnables(metaGame);
			}
		}
		return dList;
	}

	private void setEnables(Dx4MetaGame metaGame)
	{
		for (Dx4Game game : metaGame.getGames())
		{
			if (game.getGtype().getDigits()==3)
				enable3D = true;
			if (game.getGtype().getDigits()==4)
				enable4D = true;
		}
	}
	
	static List<DisplayMetaBet> createDisplayMetaBet(Dx4Player player,Dx4Home dx4Home,HashSet<Long> currExpandedBets)
	{
		List<DisplayMetaBet> displayMetaBets = new ArrayList<DisplayMetaBet>();
		for (Dx4MetaBet metaBet : player.getCurrentMetaBets())
		{
			String expanded = "+";
			if (currExpandedBets!=null)
				if (currExpandedBets.contains(metaBet.getId()))
					expanded = "-";
			displayMetaBets.add(new DisplayMetaBet(metaBet,dx4Home,expanded));
		}
		return displayMetaBets;
	}
	
	public static List<DisplayMetaBetWin> createDisplayMetaBetWin(Dx4Player player,Dx4Home dx4Home,HashSet<Long> currExpandedWins)
	{
		List<DisplayMetaBetWin> displayMetaBetWins = new ArrayList<DisplayMetaBetWin>();
		List<Dx4MetaBet> metaBets = dx4Home.getMetaBetsForPlayer(player, Dx4BetRetrieverFlag.HISTORICWINONLY,null);
		for (Dx4MetaBet metaBet : metaBets)
		{
			String expanded = "+";
			if (currExpandedWins!=null)
			{
				if (currExpandedWins.contains(metaBet.getId()))
					expanded = "-";
			}
			displayMetaBetWins.add(new DisplayMetaBetWin(metaBet,dx4Home,expanded));
		}
		return displayMetaBetWins;
	}
	
	private void createLastWin(Dx4Home dx4Home,Dx4Player player)
	{
		List<Dx4MetaBet> metaBets = dx4Home.getMetaBetsForPlayer(player, Dx4BetRetrieverFlag.HISTORICWINONLY,null);
		if (metaBets.isEmpty())
		{
			setLastWin(new ArrayList<DisplayMetaBetWinExpanded>());
			return;
		}
		
		DisplayMetaBetWin win = new DisplayMetaBetWin(metaBets.get(0),dx4Home,"-");
		setLastWinDate(win.getPlayedAt());
		setLastWin(win.getExpandedWins());
	}
	
	
	public List<DisplayMetaBet> getDisplayMetaBetList() {
		return displayMetaBetList;
	}

	public void setDisplayMetaBetList(List<DisplayMetaBet> displayMetaBetList) {
		this.displayMetaBetList = displayMetaBetList;
	}

	public void setDisplayMetaBetWinList(List<DisplayMetaBetWin> displayMetaBetWinList) {
		this.displayMetaBetWinList = displayMetaBetWinList;
	}

	public List<DisplayMetaBetWin> getDisplayMetaBetWinList() {
		return displayMetaBetWinList;
	}

	public void setDisplayQuickBetGameList(List<DisplayQuickBetGame> displayQuickBetGameList) {
		this.displayQuickBetGameList = displayQuickBetGameList;
	}

	public List<DisplayQuickBetGame> getDisplayQuickBetGameList() {
		return displayQuickBetGameList;
	}

	public void setPreviousDraws(List<Date> previousDraws) {
		this.previousDraws = previousDraws;
	}

	public List<Date> getPreviousDraws() {
		return previousDraws;
	}

	
	public void setCommand(PlayerDetailsCommand command) {
		this.command = command;
	}

	public PlayerDetailsCommand getCommand() {
		return command;
	}
	
	public boolean isEnable4D() {
		return enable4D;
	}

	public void setEnable4D(boolean enable4d) {
		enable4D = enable4d;
	}

	public boolean isEnable3D() {
		return enable3D;
	}

	public void setEnable3D(boolean enable3d) {
		enable3D = enable3d;
	}

	@Override
	public String toString() {
		return "PlayerDetailsForm [displayMetaBetList=" + displayMetaBetList
				+ ", displayMetaBetWinList=" + displayMetaBetWinList
				+ ", displayQuickBetGameList=" + displayQuickBetGameList +
			"]";
	}

	public void setExternalGameResults(ExternalGameResults externalGameResults) {
		this.externalGameResults = externalGameResults;
	}

	public ExternalGameResults getExternalGameResults() {
		return externalGameResults;
	}

	public void setLastWin(List<DisplayMetaBetWinExpanded> lastWin) {
		this.lastWin = lastWin;
	}

	public List<DisplayMetaBetWinExpanded> getLastWin() {
		return lastWin;
	}

	public void setLastWinDate(Date lastWinDate) {
		this.lastWinDate = lastWinDate;
	}

	public Date getLastWinDate() {
		return lastWinDate;
	}

	

	
}
