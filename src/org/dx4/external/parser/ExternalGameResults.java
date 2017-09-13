package org.dx4.external.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dx4.json.message.Dx4DrawResultJson;

public class ExternalGameResults implements Serializable
{
	private static final long serialVersionUID = 1155340373378430386L;
	private List<Dx4DrawResultJson> draws;
	private Date prevDate;
	private Date nextDate;
	
	ExternalGameResults()
	{
		setDraws(new ArrayList<Dx4DrawResultJson>());
	}

	public ExternalGameResults(List<Dx4DrawResultJson> results) {
		setDraws(results);
	}

	public void setDraws(List<Dx4DrawResultJson> draws) {
		this.draws = draws;
	}

	public List<Dx4DrawResultJson> getDraws() {
		return draws;
	}
	
	public Date getPrevDate() {
		return prevDate;
	}

	public void setPrevDate(Date prevDate) {
		this.prevDate = prevDate;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	@Override
	public String toString() {
		return "ExternalGameResults [draws=" + draws + "]";
	}
	
}
