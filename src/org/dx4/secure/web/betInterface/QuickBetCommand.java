package org.dx4.secure.web.betInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuickBetCommand implements Serializable
{
	private static final long serialVersionUID = 8399407660275892874L;
	private String number;
	private List<QuickBetMapping> betMappings;
	private List<String> useProviders;
	private int changedIndex;
	private ConfirmCommand confirmCommand;
	private String confirmCommandObject;
	private Boolean viewCalc;
	private Boolean viewPrev;
	private String number4D;
	private String playDate;
	
	public QuickBetCommand()
	{
		setBetMappings(new ArrayList<QuickBetMapping>());
		useProviders = new ArrayList<String>();
	}

	public QuickBetMapping getQuickBetMapping(long gameId)
	{
		for (QuickBetMapping qbm : betMappings)
			if (qbm.getGameId()==gameId)
				return qbm;
		return null;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setBetMappings(List<QuickBetMapping> betMappings) {
		this.betMappings = betMappings;
	}

	public List<QuickBetMapping> getBetMappings() {
		return betMappings;
	}


	public int getChangedIndex() {
		return changedIndex;
	}

	public void setChangedIndex(int changedIndex) {
		this.changedIndex = changedIndex;
	}

	public void setUseProviders(List<String> useProviders) {
		this.useProviders = useProviders;
	}

	public List<String> getUseProviders() {
		return useProviders;
	}

	public void setConfirmCommand(ConfirmCommand confirmCommand) {
		this.confirmCommand = confirmCommand;
	}

	public ConfirmCommand getConfirmCommand() {
		return confirmCommand;
	}

	public void setViewCalc(Boolean viewCalc) {
		this.viewCalc = viewCalc;
	}

	public Boolean getViewCalc() {
		return viewCalc;
	}

	public void setViewPrev(Boolean viewPrev) {
		this.viewPrev = viewPrev;
	}

	public Boolean getViewPrev() {
		return viewPrev;
	}

	public String getConfirmCommandObject() {
		return confirmCommandObject;
	}

	public void setConfirmCommandObject(String confirmCommandObject) {
		this.confirmCommandObject = confirmCommandObject;
	}

	public String getNumber4D() {
		return number4D;
	}

	public void setNumber4D(String number4d) {
		number4D = number4d;
	}

	public String getPlayDate() {
		return playDate;
	}

	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}

	
	

}
