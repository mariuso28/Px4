package org.dx4.secure.web.member;

import org.dx4.services.Dx4UserPasswordService;

public class MemberCreateForm extends UserDetailsForm{
	
	public MemberCreateForm()
	{
		setPassword(Dx4UserPasswordService.autoGeneratePassword());
		setvPassword(getPassword());
	}

	public MemberCreateForm(MemberCreateForm oldForm)
	{
		this();
		setProfile(oldForm.getNewProfile());
	}
}
