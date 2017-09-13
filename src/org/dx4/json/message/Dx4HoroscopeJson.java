package org.dx4.json.message;

import java.util.Date;
import java.util.List;

public class Dx4HoroscopeJson {

	public static String[] signs = { "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces",};
	
	private String sign;
	private int month;
	private Date startDate;				
	private Date endDate;					
	private String image;
	private List<String> numbers;
	
	public Dx4HoroscopeJson()
	{
	}

	public static String[] getSigns() {
		return signs;
	}

	public static void setSigns(String[] signs) {
		Dx4HoroscopeJson.signs = signs;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "Dx4HoroscopeJson [sign=" + sign + ", month=" + month + ", startDate=" + startDate + ", endDate="
				+ endDate + ", numbers=" + numbers + "]";
	}

	
}
