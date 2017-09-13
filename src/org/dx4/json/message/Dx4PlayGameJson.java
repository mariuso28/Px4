package org.dx4.json.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dx4PlayGameJson {

	private long id;
	private Date playDate;
	private boolean played;
	private Date playedAt;
	private List<String> providers;
	
	public Dx4PlayGameJson()
	{
		providers = new ArrayList<String>();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public Date getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(Date playedAt) {
		this.playedAt = playedAt;
	}

	public List<String> getProviders() {
		return providers;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	
	
}
