package org.dx4.secure.web.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.web.admin.display.DisplayOpenGame;
import org.dx4.secure.web.member.UserDetailsForm;

public class AdminDetailsForm extends UserDetailsForm{
	private static final Logger log = Logger.getLogger(AdminDetailsForm.class);
	
	private List<DisplayOpenGame> openGameList;
	private AdminDetailsCommand command;
	private Date lastRecordedDrawDate;
	private String infoMessage;
	
	public AdminDetailsForm()
	{
		super();
	}
	
	public AdminDetailsForm(Dx4Profile profile)
	{
		setProfile(profile);
	}
	
	public AdminDetailsForm(Dx4Admin admin,Dx4Home dx4Home)
	{
		this(admin.createProfile());
		List<DisplayOpenGame> openGameList = new ArrayList<DisplayOpenGame>();
		Dx4GameGroup group = dx4Home.getGameGroup(admin);
		log.trace("AdminDetailsForm gotGameGroup : " + group);
		if (group==null)
			return;
		for (Dx4GameActivator ga : group.getGameActivators())
		{
			openGameList.add(new DisplayOpenGame(ga.getMetaGame()));
		}
		
		setOpenGameList(openGameList);
		lastRecordedDrawDate = dx4Home.getDrawResultsDateRange().get(1);
	}

	public void setOpenGameList(List<DisplayOpenGame> openGameList) {
		this.openGameList = openGameList;
	}

	public List<DisplayOpenGame> getOpenGameList() {
		return openGameList;
	}

	public void setLastRecordedDrawDate(Date lastRecordedDrawDate) {
		this.lastRecordedDrawDate = lastRecordedDrawDate;
	}

	public Date getLastRecordedDrawDate() {
		return lastRecordedDrawDate;
	}

	public AdminDetailsCommand getCommand() {
		return command;
	}

	public void setCommand(AdminDetailsCommand command) {
		this.command = command;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

}
