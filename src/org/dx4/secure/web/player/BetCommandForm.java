package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.player.Dx4Player;
import org.dx4.secure.web.player.display.DisplayMetaBet;

public class BetCommandForm 
{
	private String username;
	private Date startDate;
	private Date endDate;
	private List<Dx4MetaGame> currentGames;
	private List<DisplayMetaBet> displayMetaBetList;
	
	public BetCommandForm()
	{
	}
	
	public BetCommandForm(Dx4Player player,Dx4Home dx4Home)
	{
		super();
		setUsername(player.getEmail());
		List<Dx4MetaGame> currentGames = new ArrayList<Dx4MetaGame>();
		List<Dx4MetaGame> metaGameList = dx4Home.getUnplayedMetaGames();
		if (metaGameList.size()>0)
		{
			Dx4MetaGame metaGame = metaGameList.get(0);
			Date startDate = metaGame.getPlayGames().get(0).getPlayDate();
			Date endDate = metaGame.getPlayGames().get(metaGame.getPlayGames().size()-1).getPlayDate();
			currentGames.add(metaGame);
			for (int c=1; c<metaGameList.size(); c++)
			{
				metaGame = metaGameList.get(c);
				Date sd = metaGame.getPlayGames().get(0).getPlayDate();
				Date ed = metaGame.getPlayGames().get(metaGame.getPlayGames().size()-1).getPlayDate();
				if (sd.before(startDate))
					startDate = sd;
				if (ed.after(endDate))
					endDate = ed;
				currentGames.add(metaGame);
			}
			setStartDate(startDate);
			setEndDate(endDate);
		}
		setCurrentGames(currentGames);
		setDisplayMetaBetList(PlayerDetailsForm.createDisplayMetaBet(player,dx4Home,null));
	}
	
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setCurrentGames(List<Dx4MetaGame> currentGames) {
		this.currentGames = currentGames;
	}

	public List<Dx4MetaGame> getCurrentGames() {
		return currentGames;
	}

	public void setDisplayMetaBetList(List<DisplayMetaBet> displayMetaBetList) {
		this.displayMetaBetList = displayMetaBetList;
	}

	public List<DisplayMetaBet> getDisplayMetaBetList() {
		return displayMetaBetList;
	}


	

}
