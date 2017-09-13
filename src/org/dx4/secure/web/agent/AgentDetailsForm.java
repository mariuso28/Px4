package org.dx4.secure.web.agent;

import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.web.member.UserDetailsForm;

public class AgentDetailsForm extends UserDetailsForm{
	
	
	public AgentDetailsForm()
	{
		super();
	}
	
	public AgentDetailsForm(Dx4Profile profile)
	{
		this();
		setProfile(profile);	
	}


}
