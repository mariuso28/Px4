package org.dx4.json.message;

import java.util.Date;
import java.util.List;

public class Dx4BetJson {

	private long id;
	private String choice;  
	private List<String> providers;  
	private Dx4BetStakeCombo stakeCombo = Dx4BetStakeCombo.STRAIGHT;  			
	private double big;  			//only applicable for 3D, 4D
	private double small;
	private long playGameId;
	private Date playDate;
	private double odds;			//only for floating model
	private double payout;			// ditto
	
	public Dx4BetJson()
	{
	}

	public boolean canConsolidate(Dx4BetJson bet)
	{
		if (!bet.getChoice().equals(choice))
			return false;
		if (!bet.getStakeCombo().equals(stakeCombo))
			return false;
		if (bet.getProviders().size()!=providers.size())
			return false;
		for (String prov : bet.getProviders())
			if (!providers.contains(prov))
				return false;
		return true;
	}
	
	public void consolidate(Dx4BetJson bet)
	{
		big += bet.getBig();
		small += bet.getSmall();
	}
	
	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public List<String> getProviders() {
		return providers;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public Dx4BetStakeCombo getStakeCombo() {
		return stakeCombo;
	}

	public void setStakeCombo(Dx4BetStakeCombo stakeCombo) {
		this.stakeCombo = stakeCombo;
	}

	public double getBig() {
		return big;
	}

	public void setBig(double big) {
		this.big = big;
	}

	public double getSmall() {
		return small;
	}

	public void setSmall(double small) {
		this.small = small;
	}

	public long getPlayGameId() {
		return playGameId;
	}

	public void setPlayGameId(long playGameId) {
		this.playGameId = playGameId;
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	public double getPayout() {
		return payout;
	}

	public void setPayout(double payout) {
		this.payout = payout;
	}

	@Override
	public String toString() {
		return "Dx4BetJson [id=" + id + ", choice=" + choice + ", providers=" + providers + ", stakeCombo=" + stakeCombo
				+ ", big=" + big + ", small=" + small + ", playGameId=" + playGameId + ", playDate=" + playDate + "]";
	}

}
