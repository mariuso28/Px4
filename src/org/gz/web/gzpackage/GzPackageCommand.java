package org.gz.web.gzpackage;

import java.io.Serializable;

public class GzPackageCommand implements Serializable{

	private static final long serialVersionUID = 7279602431428718044L;
	private String gname;
	private String pname;
	
	public GzPackageCommand()
	{
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

}
