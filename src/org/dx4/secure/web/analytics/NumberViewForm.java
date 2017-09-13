package org.dx4.secure.web.analytics;

import java.util.Date;
import java.util.List;

import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4PlacingJson;

public class NumberViewForm 
{
	private String number;
	private String threeDigits;
	private String numberDesc;
	private String numberDescCh;
	private List<Dx4DrawResultJson> drawResults;
	private List<Dx4PlacingJson> placings;
	private Date startDate;
	private Date endDate;
	private boolean external;
	private int digits;
	private double revenue;
	private String image;

	public NumberViewForm(List<Dx4DrawResultJson> drawResults,List<Dx4PlacingJson> placings,String number,Dx4NumberPageElementJson numberPageElement,Date startDate,Date endDate,double revenue) {
		setDrawResults(drawResults);
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

	public List<Dx4DrawResultJson> getDrawResults() {
		return drawResults;
	}

	public void setDrawResults(List<Dx4DrawResultJson> drawResults) {
		this.drawResults = drawResults;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean isExternal() {
		return external;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public int getDigits() {
		return digits;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setThreeDigits(String threeDigits) {
		this.threeDigits = threeDigits;
	}

	public String getThreeDigits() {
		return threeDigits;
	}

	public void setPlacings(List<Dx4PlacingJson> placings) {
		this.placings = placings;
	}

	public List<Dx4PlacingJson> getPlacings() {
		return placings;
	}

	public void setNumberDesc(String numberDesc) {
		this.numberDesc = numberDesc;
	}

	public String getNumberDesc() {
		return numberDesc;
	}

	public void setNumberDescCh(String numberDescCh) {
		this.numberDescCh = numberDescCh;
	}

	public String getNumberDescCh() {
		return numberDescCh;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	
}

