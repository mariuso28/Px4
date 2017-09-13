package org.dx4.secure.web.analytics;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.dx4.services.Dx4Services;
import org.dx4.utils.NumberGrid;

public class NumberGridForm 
{
	private static final Logger log = Logger.getLogger(NumberGridForm.class);
	
	private NumberGrid numberGrid;
	private NumberGridCommand command;
	private String message;
	private boolean external;
	private boolean exposures;
	private List<Dx4NumberRevenueJson> topNumbers; 
	private Date today;
	private List<Date> sameDayDrawDates;
	private List<Dx4NumberPageElementJson> pageElements;
	
	public NumberGridForm()
	{	
	}
	
	public NumberGridForm(Dx4Services dx4Services,String type, Date startDate, Date endDate)
	{
		 this(dx4Services,type,startDate,endDate,-1);
	}
	
	public NumberGridForm(Dx4Services dx4Services,String type, Date startDate, Date endDate,Integer currPageElement)
	{	
		NumberGrid numberGrid = dx4Services.getExternalService().getNumberGridValues(Integer.parseInt(type),startDate,endDate);
		
		if (currPageElement<0)
			setNumberGrid(numberGrid);
		else
			setPageElements(numberGrid.getPageElements(currPageElement));
		log.trace("GOT PAGE ELEMENTS : " + pageElements.size());
		
		List<Dx4NumberRevenueJson> topNumbers1 = dx4Services.getDx4Home().getNumberRevenues(startDate, endDate);
		int toIndex = topNumbers1.size();
		if (toIndex > 200)
			toIndex = 200;
		setTopNumbers(topNumbers1.subList(0, toIndex));
		setMessage("");
		GregorianCalendar gc = new GregorianCalendar();
		today = gc.getTime();
		sameDayDrawDates = dx4Services.getExternalService().getExternalSameDayGameDates();
		log.trace("CREATED NumberGridForm");
	}
	
	public void setNumberGrid(NumberGrid numberGrid) {
		this.numberGrid = numberGrid;
	}

	public NumberGrid getNumberGrid() {
		return numberGrid;
	}


	public void setCommand(NumberGridCommand command) {
		this.command = command;
	}

	public NumberGridCommand getCommand() {
		return command;
	}
	
	public boolean validate()
	{
		log.trace(this.getClass().getSimpleName()+"::validate with : " + command);
		return command.getStartDate()!=null 
					&& command.getEndDate()!=null;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "NumberGridForm [command=" + command
				+ ", message=" + message + "]";
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean isExternal() {
		return external;
	}

	public void setTopNumbers(List<Dx4NumberRevenueJson> topNumbers) {
		this.topNumbers = topNumbers;
	}

	public List<Dx4NumberRevenueJson> getTopNumbers() {
		return topNumbers;
	}

	public void setExposures(boolean exposures) {
		this.exposures = exposures;
	}

	public boolean isExposures() {
		return exposures;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public Date getToday() {
		return today;
	}

	public List<Date> getSameDayDrawDates() {
		return sameDayDrawDates;
	}

	public void setSameDayDrawDates(List<Date> sameDayDrawDates) {
		this.sameDayDrawDates = sameDayDrawDates;
	}

	public void setPageElements(List<Dx4NumberPageElementJson> pageElements) {
		this.pageElements = pageElements;
	}

	public List<Dx4NumberPageElementJson> getPageElements() {
		return pageElements;
	}

}
