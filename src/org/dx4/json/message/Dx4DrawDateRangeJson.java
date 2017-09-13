package org.dx4.json.message;

import java.util.Date;

public class Dx4DrawDateRangeJson {
	private Date start;
	private Date end;
	
	public Dx4DrawDateRangeJson()
	{
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
