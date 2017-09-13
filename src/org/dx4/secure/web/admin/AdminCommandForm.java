package org.dx4.secure.web.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4SecureUser;

public class AdminCommandForm 
{
	private String username;
	private Date startDate;
	private Date endDate;
	private List<Dx4MetaGame> gameList;
	private List<Boolean> activeGames;
	private AdminCommand adminCommand;
	
	
	public AdminCommandForm(Dx4Home dx4Home,Dx4SecureUser currUser)
	{
		List<Dx4MetaGame> metaGameList = dx4Home.getMetaGames();
		setGameList(metaGameList);
		activeGames = new ArrayList<Boolean>();
		for (Dx4MetaGame metaGame : metaGameList)
		{
			if (currUser.getGameGroup().containsMetaGame(metaGame))
				activeGames.add(true);
			else
				activeGames.add(false);
		}
		
		/*
		if (metaGameList.size()>0)
		{	
			Dx4MetaGame metaGame = metaGameList.get(0);
			if (!metaGame.getPlayGames().isEmpty())
			{
				Date startDate = metaGame.getPlayGames().get(0).getPlayDate();
				Date endDate = metaGame.getPlayGames().get(metaGame.getPlayGames().size()-1).getPlayDate();
				for (int c=1; c<metaGameList.size(); c++)
				{
					metaGame = metaGameList.get(c);
					Date sd = metaGame.getPlayGames().get(0).getPlayDate();
					Date ed = metaGame.getPlayGames().get(metaGame.getPlayGames().size()-1).getPlayDate();
					if (sd.before(startDate))
						startDate = sd;
					if (ed.after(endDate))
						endDate = ed;
				}
			}
		}
		setStartDate(startDate);
		setEndDate(endDate);	
		*/
	}

	public AdminCommand getAdminCommand() {
		return adminCommand;
	}

	public void setAdminCommand(AdminCommand adminCommand) {
		this.adminCommand = adminCommand;
	}

	public List<Dx4MetaGame> getGameList() {
		return gameList;
	}

	public void setGameList(List<Dx4MetaGame> gameList) {
		this.gameList = gameList;
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

	public List<Boolean> getActiveGames() {
		return activeGames;
	}

	public void setActiveGames(List<Boolean> activeGames) {
		this.activeGames = activeGames;
	}

	@Override
	public String toString() {
		return "AdminCommandForm [username=" + username + ", startDate="
				+ startDate + ", endDate=" + endDate + ", gameList=" + gameList
				+ "]";
	}

}
