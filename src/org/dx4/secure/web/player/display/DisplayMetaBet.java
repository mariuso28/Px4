package org.dx4.secure.web.player.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4MetaBet;
import org.dx4.home.Dx4Home;

public class DisplayMetaBet implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7499116796965321044L;
	private Dx4MetaBet metaBet;
	private double totalStake;
	private List<String> providers;
	private String expanded;
	private double totalWin;
	
	public DisplayMetaBet(Dx4MetaBet metaBet,Dx4Home dx4Home,String expanded)
	{
		setTotalStake(metaBet.getTotalStake());
		setTotalWin(metaBet.getTotalWin());
		setMetaBet(metaBet);
		setExpanded(expanded);
		providers = new ArrayList<String>();
//		if (!expanded.equals("-"))
	//		return;
	
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public double getTotalStake() {
		return totalStake;
	}

	public void setMetaBet(Dx4MetaBet metaBet) {
		this.metaBet = metaBet;
	}

	public Dx4MetaBet getMetaBet() {
		return metaBet;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	public double getTotalWin() {
		return totalWin;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public List<String> getProviders() {
		return providers;
	}

	
	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	public String getExpanded() {
		return expanded;
	}

	

}
