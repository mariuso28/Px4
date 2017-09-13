package org.dx4.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dx4.agent.Dx4Comp;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4Admin extends Dx4SecureUser implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3450144147643823401L;
	private List<Dx4Comp> comps;
	private UUID versionCode;
	private Date scheduledDowntime;
	private Boolean scheduledDowntimeSet;
	
	public Dx4Admin()
	{
	}
	
	public Dx4Admin(String email) {
		super(email);
		setCode(getDefaultCode());						// has to be x to match with role class
		setParentCode("");
		setRole(Dx4Role.ROLE_ADMIN);
	}

	public void initializeGameGroups(Dx4Home dx4Home)
	{
		Dx4GameGroup group = new Dx4GameGroup(this);
		for (Dx4MetaGame metaGame : dx4Home.getMetaGames())
		{
			group.getGameActivators().add(new Dx4GameActivator(metaGame,group));
		}
		setGameGroup(group);
	}
	
	public static String getDefaultCode() {
		return "x0";
	}

	public List<Dx4Comp> getComps() {
		return comps;
	}

	public void setComps(List<Dx4Comp> comps) {
		this.comps = comps;
	}

	public UUID getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(UUID versionCode) {
		this.versionCode = versionCode;
	}

	public Date getScheduledDowntime() {
		return scheduledDowntime;
	}

	public void setScheduledDowntime(Date scheduledDowntime) {
		this.scheduledDowntime = scheduledDowntime;
	}

	public Boolean getScheduledDowntimeSet() {
		return scheduledDowntimeSet;
	}

	public void setScheduledDowntimeSet(Boolean scheduledDowntimeSet) {
		this.scheduledDowntimeSet = scheduledDowntimeSet;
	}

}
