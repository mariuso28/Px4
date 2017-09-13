package org.dx4.secure.web.analytics;

import java.io.Serializable;
import java.util.Date;

public class NumberGridCommand implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6064125325111126477L;
	private Date startDate;
	private Date endDate;
	private Date someDate;
	private boolean external;
	private String topNumber;
	private String useNumber;
	private String sameDayDate;
	private String searchTerm;
	private char digits;
	
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
	@Override
	public String toString() {
		return "NumberGridCommand [startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
	public void setExternal(boolean external) {
		this.external = external;
	}
	public boolean isExternal() {
		return external;
	}
	public void setTopNumber(String topNumber) {
		this.topNumber = topNumber;
	}
	public String getTopNumber() {
		return topNumber;
	}
	public void setUseNumber(String useNumber) {
		this.useNumber = useNumber;
	}
	public String getUseNumber() {
		return useNumber;
	}
	public void setSomeDate(Date someDate) {
		this.someDate = someDate;
	}
	public Date getSomeDate() {
		return someDate;
	}
	public String getSameDayDate() {
		return sameDayDate;
	}
	public void setSameDayDate(String sameDayDate) {
		this.sameDayDate = sameDayDate;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setDigits(char digits) {
		this.digits = digits;
	}
	public char getDigits() {
		return digits;
	}
	
	
	
}
