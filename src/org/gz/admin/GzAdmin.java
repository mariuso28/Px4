package org.gz.admin;

import java.io.Serializable;

import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;

public class GzAdmin extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = -2070754452251455632L;
	
	public GzAdmin()
	{
	}
	
	public GzAdmin(String email) {
		super(email);
		setCode(getDefaultCode());						// has to be x to match with role class
		setParentCode("");
		setRole(GzRole.ROLE_GZADMIN);
	}

	public static String getDefaultCode() {
		return "x0";
	}

}
