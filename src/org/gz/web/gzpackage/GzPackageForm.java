package org.gz.web.gzpackage;

import java.io.Serializable;

public class GzPackageForm implements Serializable{
	
	private static final long serialVersionUID = 228182518682367917L;
	private String errMsg;
	private String infoMsg;
	private GzPackageCommand command;
	
	public GzPackageForm()
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

	public GzPackageCommand getCommand() {
		return command;
	}

	public void setCommand(GzPackageCommand command) {
		this.command = command;
	}

	
	
}
