package org.dx4.secure.web.member;

import java.util.ArrayList;
import java.util.List;

import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.util.EmailValidator;
import org.dx4.util.PhoneValidator;
import org.dx4.utils.Validator;

public class UserDetailsForm {
	private Dx4Profile profile;
	private Dx4Profile newProfile;
	private String password;
	private String vPassword;
	private String message;
	
	public UserDetailsForm()
	{
		setMessage("");
	}

	public Dx4Profile getNewProfile() {
		return newProfile;
	}

	public void setNewProfile(Dx4Profile newProfile) {
		this.newProfile = newProfile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setProfile(Dx4Profile profile) {
		this.profile = profile;
	}

	public Dx4Profile getProfile() {
		return profile;
	}

	public void validate(boolean create) throws Dx4FormValidationException
	{
		EmailValidator ev = new EmailValidator();
		List<String> required = new ArrayList<String>();
		
		if (create)
		{
			getNewProfile().setEmail(getNewProfile().getEmail().toLowerCase());
			if (Validator.isEmpty(getNewProfile().getEmail()) | !ev.validate(getNewProfile().getEmail()))
				required.add("Email");
		}
		
		if (Validator.isEmpty(getNewProfile().getContact()))
			required.add("Contact");
		if (Validator.isEmpty(getNewProfile().getPhone()))
			required.add("Phone");
		else
		{
			PhoneValidator pv = new PhoneValidator();
			if (!pv.validate(getNewProfile().getPhone()))
				required.add("Phone");
		}
		
		if (Validator.isEmpty(getPassword()))
			required.add("Password");
		if (Validator.isEmpty(getvPassword()))
			required.add("Verifiy Password");
		
		if (!required.isEmpty())
			throw new Dx4FormValidationException("Missing or malformed values : " +  required );
		else
		{
			if (create && !ev.validate(getNewProfile().getEmail()))
				throw new Dx4FormValidationException("Invalid Email Address");
			else
			{
				if (!getvPassword().equals(getPassword()))
					throw new Dx4FormValidationException("Password/Verify Password mismatch");
			}
		}
	}
	
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getvPassword() {
		return vPassword;
	}

	public void setvPassword(String vPassword) {
		this.vPassword = vPassword;
	}
	
	
}
