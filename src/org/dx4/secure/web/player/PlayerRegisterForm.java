package org.dx4.secure.web.player;

import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.web.member.UserDetailsForm;

public class PlayerRegisterForm extends UserDetailsForm{

	public PlayerRegisterForm()
	{
		setProfile(new Dx4Profile());
	}

	public PlayerRegisterForm(Dx4Profile profile)
	{
		setProfile(profile);
	}
}
