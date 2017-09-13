package org.gz.web.number;

import java.io.Serializable;

public class GzNumberForm implements Serializable{
	
	private static final long serialVersionUID = -6527370807434957500L;
	private String errMsg;
	private String infoMsg;
	private GzNumberCommand command;
	
	public GzNumberForm()
	{
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public GzNumberCommand getCommand() {
		return command;
	}

	public void setCommand(GzNumberCommand command) {
		this.command = command;
	}
	
	
}
