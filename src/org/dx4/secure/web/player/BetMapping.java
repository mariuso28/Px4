package org.dx4.secure.web.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BetMapping implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 107673870883704449L;
	private List<String> choices;
	private double stake;
	private long gameId;
	private String checked;
	private int lookUpValue;
	
	public BetMapping()
	{
		setChoices(new ArrayList<String>());
		for (int i=0; i<5; i++)
			choices.add("0");
		setGameId(0);
		setStake(0);
		setChecked("off");
		setLookUpValue(0);
	}
	
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

	public List<String> getChoices() {
		return choices;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}
	public void setChoices(String number)
	{
		for (int i = 0; i<number.length(); i++)
		{
			choices.add(Character.toString(number.charAt(i)));
		}
	}

	public void setLookUpValue(int lookUpValue) {
		this.lookUpValue = lookUpValue;
	}

	public int getLookUpValue() {
		return lookUpValue;
	}

	@Override
	public String toString() {
		return "BetMapping [choices=" + choices + ", stake=" + stake
				+ ", gameId=" + gameId + ", checked=" + checked
				+ ", lookUpValue=" + lookUpValue + "]";
	}

	
}