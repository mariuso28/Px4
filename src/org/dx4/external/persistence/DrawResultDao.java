package org.dx4.external.persistence;

import java.util.Date;
import java.util.List;

import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.json.message.Dx4NumberRevenueJson;

public interface DrawResultDao 
{
	public void store(Dx4DrawResultJson result);
	public List<Dx4DrawResultJson> getResultsForProvider(String provider);
	public List<Dx4DrawResultJson> getResultsForNumber(String number, Date currStartDate, Date currEndDate);
	public List<Dx4DrawResultJson> getResults(Date startDate, Date endDate);
	public List<Dx4DrawResultJson> getLatestDrawResults();
	public Dx4DrawResultJson getResult(long id);
	public List<Date> getResultsDateRange();
	public List<Dx4DrawResultJson> getResultsForNumberPart(String number,
			Date currStartDate, Date currEndDate);
	public void populateSpecialsAndConsolations(Dx4DrawResultJson result);
	public String getNextDrawNoForProvider(String provider);
	public List<Dx4NumberRevenueJson> getNumberRevenues(Date startDate,Date endDate);
	public double getRevenueForNumber(String number,Date startDate,Date endDate);
	public List<Dx4PlacingJson> getPlacingsForNumber(String number,Date startDate,Date endDate);
	public List<Date> getDrawDates();
	public Date getPrevDrawDate(Date date);
	public Date getNextDrawDate(Date date);
	/*
	public String getDescForNumber(String number);
	public void getDescsForDrawResult(DrawResult result);
	public List<NumberPageElement> getNumberPageElements();
	public List<NumberPageElement> getNumberPageElementsByDesc(String searchTerm);
	*/
	public void removeResultsForDate(Date date);
	public void removeResultsForProviderDate(Date date, char c);
	
}
