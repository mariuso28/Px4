package org.dx4.secure.web.agent;

import java.util.ArrayList;
import java.util.List;

public class WinPlaceHolder{

	private List<String> placings;

	public WinPlaceHolder()
	{
		placings = new ArrayList<String>();
	}
	
	public WinPlaceHolder(List<String> newPlacings) {
		placings = newPlacings;
	}

	public void setPlacings(List<String> placings) {
		this.placings = placings;
	}

	public List<String> getPlacings() {
		return placings;
	} 
}
