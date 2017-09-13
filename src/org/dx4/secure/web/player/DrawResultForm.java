package org.dx4.secure.web.player;

import java.util.Date;

public class DrawResultForm {
	
	private Date someDate;
	private String message;

	public DrawResultForm()
	{	
	}
	
	public void setSomeDate(Date someDate) {
		this.someDate = someDate;
	}

	public Date getSomeDate() {
		return someDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
