package org.dx4.secure.web.player.display;

import java.io.Serializable;

public class DivFlag implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3880173907647248575L;
	private String color;
	private String str;
	private String flag;
	
	public DivFlag(String color, String str, String flag) {
		super();
		this.color = color;
		this.str = str;
		this.flag = flag;
	}
	
	public DivFlag()
	{
		this("color:black","","");
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "DivFlag [color=" + color + ", str=" + str + ", flag=" + flag
				+ "]";
	}
	
	
}
