package org.dx4.secure.web.player.display;

import java.io.Serializable;

public class DisplayMetaBetWinExpanded implements Serializable
{
	private static final long serialVersionUID = 1731279004687174194L;
	private String game;
	private String provider;
	private double stake;
	private String choice;
	private String number;
	private double win;
	private String place;
	private String image;
	
	public DisplayMetaBetWinExpanded(String game, String provider,
			double stake, String choice, String number, double win, String place, String image ){
		super();
		this.game = game;
		this.provider = provider;
		this.stake = stake;
		this.choice = choice;
		this.number = number;
		this.win = win;
		this.place = place;
		this.image = image;
	}
	
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public double getStake() {
		return stake;
	}
	public void setStake(double stake) {
		this.stake = stake;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}

}
