package org.dx4.json.message;

import java.util.Date;
import java.util.List;

public class Dx4DrawJson {

	private long id;
	private Dx4ProviderJson provider;
	private String drawNo;
	private Date date;
	private Dx4PlaceJson first;
	private Dx4PlaceJson second;
	private Dx4PlaceJson third;
	private List<String> specials;
	private List<String> consolations;
	
	public Dx4DrawJson()
	{
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public Dx4ProviderJson getProvider() {
		return provider;
	}

	public void setProvider(Dx4ProviderJson provider) {
		this.provider = provider;
	}

	public String getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Dx4PlaceJson getFirst() {
		return first;
	}

	public void setFirst(Dx4PlaceJson first) {
		this.first = first;
	}

	public Dx4PlaceJson getSecond() {
		return second;
	}

	public void setSecond(Dx4PlaceJson second) {
		this.second = second;
	}

	public Dx4PlaceJson getThird() {
		return third;
	}

	public void setThird(Dx4PlaceJson third) {
		this.third = third;
	}

	public List<String> getSpecials() {
		return specials;
	}

	public void setSpecials(List<String> specials) {
		this.specials = specials;
	}

	public List<String> getConsolations() {
		return consolations;
	}

	public void setConsolations(List<String> consolations) {
		this.consolations = consolations;
	}
}
