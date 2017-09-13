package org.dx4.secure.web.admin;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class Dx4VersionCommand implements Serializable{

	private static final long serialVersionUID = 3343375105088474531L;
	private MultipartFile apk;
	private String code;
	
	public Dx4VersionCommand()
	{
	}

	public MultipartFile getApk() {
		return apk;
	}

	public void setApk(MultipartFile apk) {
		this.apk = apk;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
