package org.dx4.json.message;

import java.util.Date;
import java.util.List;

public class Dx4NumberViewJson {
	private String number;
	private String threeDigits;
	private String numberDesc;
	private String numberDescCh;
	private List<Dx4PlacingJson> placings;
	private Date startDate;
	private Date endDate;
	private int digits;
	private double revenue;
	private String image;

	public Dx4NumberViewJson()
	{
	}
	
	public Dx4NumberViewJson(List<Dx4PlacingJson> placings,String number,Dx4NumberPageElementJson numberPageElement,Date startDate,Date endDate,double revenue) {
		setNumber(number);
		setNumberDesc(numberPageElement.getDescription());
		setNumberDescCh(numberPageElement.getDescriptionCh());
		if (number.length()>=3)
			setThreeDigits(number.substring(number.length()-3));
		setStartDate(startDate);
		setEndDate(endDate);
		setDigits(number.length());
		setRevenue(revenue);
		setPlacings(placings);
		setImage(numberPageElement.getImage());
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getThreeDigits() {
		return threeDigits;
	}

	public void setThreeDigits(String threeDigits) {
		this.threeDigits = threeDigits;
	}

	public String getNumberDesc() {
		return numberDesc;
	}

	public void setNumberDesc(String numberDesc) {
		this.numberDesc = numberDesc;
	}

	public String getNumberDescCh() {
		return numberDescCh;
	}

	public void setNumberDescCh(String numberDescCh) {
		this.numberDescCh = numberDescCh;
	}

	public List<Dx4PlacingJson> getPlacings() {
		return placings;
	}

	public void setPlacings(List<Dx4PlacingJson> placings) {
		this.placings = placings;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}
