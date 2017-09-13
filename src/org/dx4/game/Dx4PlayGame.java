package org.dx4.game;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class Dx4PlayGame implements Serializable,Cloneable{
	
	private static final long serialVersionUID = -9153493804134387529L;

	private static final Logger log = Logger.getLogger(Dx4PlayGame.class);
	
	private long id;
	private Date playDate;
	private boolean played;
	private Date playedAt;
	private String providerCodes;
	
	public Dx4PlayGame()
	{
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
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public synchronized boolean availableForBets()
	{
		if (played)
			return false;
	
//		return true;
		
		GregorianCalendar gc = new GregorianCalendar();
		
		gc = new GregorianCalendar();
		Date now = gc.getTime();
		log.trace("Cut off time : " + playDate + " now : " + now + " cutoff is : " + now.before(playDate));
		
		return now.before(playDate);
	}
	
	public Dx4PlayGame clone()
	{
		try
		{
			return (Dx4PlayGame) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
	
	
	@Override
	public String toString() {
		return "Dx4PlayGame [id=" + id + ", playDate=" + playDate + ", played="
				+ played + ", playedAt=" + playedAt 
				+ "]";
	}

	public String getProviderCodes() {
		return providerCodes;
	}

	public void setProviderCodes(String providerCodes) {
		this.providerCodes = providerCodes;
	}
	
}
