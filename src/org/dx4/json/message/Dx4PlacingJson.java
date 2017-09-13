package org.dx4.json.message;

import java.util.Date;
import java.util.HashMap;

public class Dx4PlacingJson 
{
	private Date date;
	private String drawNo;
	private String provider;
	private String place;
	private Dx4PayOutTypeJson payOutType;
	
	private static HashMap<Character,String> places;
	
	public Dx4PlacingJson()
	{
	}
	
	public static String getPlace(Character code)
	{
		if (places==null)
		{
			places = new HashMap<Character,String>();
			places.put('F', "First");
			places.put('S', "Second");
			places.put('T', "Third");
			places.put('P', "Special");
			places.put('C', "Consolation");
		}
		return places.get(code);
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setPayOutType(Dx4PayOutTypeJson payOutType) {
		this.payOutType = payOutType;
	}

	public Dx4PayOutTypeJson getPayOutType() {
		return payOutType;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "Placing [date=" + date + ", drawNo=" + drawNo + ", provider="
				+ provider + ", place=" + place + ", payOutType=" + payOutType
				+ "]";
	}

	
}
