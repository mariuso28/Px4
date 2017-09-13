package org.dx4.secure.web.player.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;

public class DisplayMetaBetWin implements Serializable{
	
	private static final long serialVersionUID = 6229800662894453890L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DisplayMetaBetWin.class);
	
	private String metaName;
	private long metaId;
	private Date placed;
	private Date playedAt;
	private Double totalWin;
	private Double totalStake;
	private String expanded;
	private List<DisplayMetaBetWinExpanded> expandedWins;
	private Dx4MetaBet metaBet;
	
	private DisplayMetaBetWin(Dx4MetaBet metaBet) 
	{	
		setMetaBet(metaBet);
		setMetaId(metaBet.getId());
		setMetaName(metaBet.getMetaGame().getName());
		setPlayedAt(metaBet.getPlayGame().getPlayedAt());
		setTotalWin(metaBet.getTotalWin());
		setTotalStake(metaBet.getTotalStake());
		setPlaced(metaBet.getPlaced());
	}
	
	public DisplayMetaBetWin(Dx4MetaBet metaBet,Dx4Home dx4Home,String expanded) 
	{
		this(metaBet);
		
		metaBet.setProvidersFromCodes(dx4Home);
		
		setExpanded(expanded);
		expandedWins = new ArrayList<DisplayMetaBetWinExpanded>();
		if (!expanded.equals("-"))
			return;
			
		for (Dx4Win win : metaBet.getWins())
		{
			String image = null;
			if (win.getBet().getChoice().length()>2)
			{
				Dx4NumberPageElementJson npe = dx4Home.getNumberPageElement(win.getBet().getChoice(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
				image = npe.getImage();
			}
			String provider = dx4Home.getProviderByCode(win.getProviderCode()).getName();
			DisplayMetaBetWinExpanded dwe = new DisplayMetaBetWinExpanded(win.getBet().getGame().getGtype().name(), 
					provider,
					win.getBet().getStake(), win.getBet().getChoice(), win.getResult(), win.getWin(), 
					win.getPlace(),image);
			expandedWins.add(dwe);
		}
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public Date getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(Date playedAt) {
		this.playedAt = playedAt;
	}

	public List<DisplayMetaBetWinExpanded> getExpandedWins() {
		return expandedWins;
	}

	public void setExpandedWins(List<DisplayMetaBetWinExpanded> expandedWins) {
		this.expandedWins = expandedWins;
	}
	
	public void setMetaId(long metaId) {
		this.metaId = metaId;
	}

	public long getMetaId() {
		return metaId;
	}

	public Date getPlaced() {
		return placed;
	}

	public void setPlaced(Date placed) {
		this.placed = placed;
	}

	public Double getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(Double totalWin) {
		this.totalWin = totalWin;
	}

	public Double getTotalStake() {
		return totalStake;
	}

	public void setTotalStake(Double totalStake) {
		this.totalStake = totalStake;
	}

	public String getExpanded() {
		return expanded;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	public Dx4MetaBet getMetaBet() {
		return metaBet;
	}

	public void setMetaBet(Dx4MetaBet metaBet) {
		this.metaBet = metaBet;
	}

}
