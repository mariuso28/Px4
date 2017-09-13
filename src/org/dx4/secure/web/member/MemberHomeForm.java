package org.dx4.secure.web.member;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4DateWin;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;


public class MemberHomeForm 
{
	private static final Logger log = Logger.getLogger(MemberHomeForm.class);
	
	private String createRole;
	private String parent;
	private List<String> activeGames;
	private String activeGame;
	private boolean enable4D;
	private boolean enable3D;
	private boolean enable2D;
	private List<Dx4DateWin> previousDraws;
	private MemberCommand command;
	
	
	public MemberHomeForm()
	{
		activeGames= new ArrayList<String>();
	}

	public MemberHomeForm(Dx4Home dx4Home,Dx4SecureUser currUser)
	{
		this();
		
		Dx4GameGroup group = currUser.getGameGroup();
		log.trace("MemberHomeForm gotGameGroup : " + group);
		if (group==null)
			return;
		for (Dx4GameActivator ga : group.getGameActivators())
		{
			Dx4MetaGame metaGame = ga.getMetaGame();
			for (Dx4Game game : metaGame.getGames())
			{
				if (game.getGtype().getDigits()==4)
					enable4D = true;
				else
				if (game.getGtype().getDigits()==3)
					enable3D = true;
				else
				if (game.getGtype().getDigits()==2)
					enable2D = true;
			}
			activeGames.add(ga.getMetaGame().getName());
		}
		
		if (currUser.getRole().equals(Dx4Role.ROLE_ADMIN))
			return;
		Dx4SecureUser parentUser;
		try {
			parentUser = dx4Home.getParentForUser(currUser);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		parent = parentUser.getEmail();
		previousDraws = dx4Home.getWinDates((Dx4Agent) currUser);
	}
	
	public void setCreateRole(String createRole) {
		this.createRole = createRole;
	}

	public String getCreateRole() {
		return createRole;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return parent;
	}

	public void setActiveGames(List<String> activeGames) {
		this.activeGames = activeGames;
	}

	public List<String> getActiveGames() {
		return activeGames;
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

	public boolean isEnable2D() {
		return enable2D;
	}

	public void setEnable2D(boolean enable2d) {
		enable2D = enable2d;
	}

	public List<Dx4DateWin> getPreviousDraws() {
		return previousDraws;
	}

	public void setPreviousDraws(List<Dx4DateWin> previousDraws) {
		this.previousDraws = previousDraws;
	}

	public MemberCommand getCommand() {
		return command;
	}

	public void setCommand(MemberCommand command) {
		this.command = command;
	}

	public void setActiveGame(String activeGame) {
		this.activeGame = activeGame;
	}

	public String getActiveGame() {
		return activeGame;
	}

	
}
