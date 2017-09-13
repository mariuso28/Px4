package org.dx4.secure.web.admin;

import java.io.Serializable;

public class Dx4VersionForm implements Serializable{

	private static final long serialVersionUID = -5564309340854464155L;
	private String code;
	private Dx4VersionCommand command;
	private String message;
	
	public Dx4VersionForm()
	{
	}

	public Dx4VersionCommand getCommand() {
		return command;
	}

	public void setCommand(Dx4VersionCommand command) {
		this.command = command;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
