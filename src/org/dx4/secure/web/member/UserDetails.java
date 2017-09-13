package org.dx4.secure.web.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDetails implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 744640376334899885L;
	private String username;
	private String contact;
	private String phone;
	private String email;
	private String password;
		
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> validate() {	

		List<String> requiredFields = new ArrayList<String>();
		if (getUsername()==null || getUsername().isEmpty())
			requiredFields.add("Email");;
		if (getPhone()==null || getPhone().isEmpty())
			requiredFields.add("Contact");
		if (getContact()==null || getContact().isEmpty())
			requiredFields.add("Phone");
		return requiredFields;
	}
	
	@Override
	public String toString() {
		return "UserDetails [contact=" + contact + ", phone=" + phone
				+ ", email=" + email + "]";
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	

}
