package org.dx4.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.external.parser.ExternalComingDate;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.external.parser.ParseComingDates;
import org.dx4.external.parser.ParseExternal4DHistoric;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberStoreJson;
import org.dx4.json.message.Dx4NumberStoreLevelJson;
import org.dx4.util.DateUtil;
import org.dx4.utils.NumberGrid;

public class Dx4ExternalService {
	
	private static final Logger log = Logger.getLogger(Dx4ExternalService.class);
	private Dx4Services dx4Services;
	private Map<Integer,NumberGrid> numberGrids = new HashMap<Integer,NumberGrid>();
	
	Dx4ExternalService(Dx4Services dx4Services)
	{
		setDx4Services(dx4Services);
	}
	
	public NumberGrid getNumberGridExposures(int numberGridType)
	{
		if (numberGridType!=4 && numberGridType!=2 && numberGridType!=1)
		{
			log.error("ONLY SET UP FOR GRID TYPES 4,2,1");
			return null;
		}
		// have to be recreated as user specific
		List<Dx4NumberPageElementJson> pageElements = dx4Services.getDx4Home().getNumberPageElements();
		
		NumberGrid numberGrid = new NumberGrid(numberGridType,pageElements);
		return numberGrid;
	}
	
	public NumberGrid getNumberGridValues(int numberGridType,Date startDate,Date endDate)
	{
		if (numberGridType!=4 && numberGridType!=2 && numberGridType!=1)
		{
			log.error("ONLY SET UP FOR GRID TYPES 4,2,1");
			return null;
		}
		
		if (startDate==null && endDate==null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			endDate = gc.getTime();
			gc.clear();
			gc.set(2008, 0, 1);
			startDate = gc.getTime();
		}
		
		NumberGrid numberGrid = numberGrids.get(numberGridType);
		if (numberGrid != null && numberGrid.getResultStartDate()!=null && numberGrid.getResultEndDate()!=null)
		{
			if (numberGrid.getResultStartDate().equals(startDate) && numberGrid.getResultEndDate().equals(endDate))
				return numberGrid;				// up to date already
		}
		
		List<Dx4NumberPageElementJson> pageElements = dx4Services.getDx4Home().getNumberPageElements();
		numberGrid = NumberGrid.create(numberGridType,pageElements);
		numberGrids.put(numberGridType,numberGrid);
		
		List<Dx4NumberStoreJson> sortedNumbers = new ArrayList<Dx4NumberStoreJson>();
		numberGrid.clearLevelsOccurences();
		List<Dx4DrawResultJson> drs = dx4Services.getDx4Home().getDrawResults(startDate,endDate);
		for (Dx4DrawResultJson dr : drs)
		{
			Dx4NumberStoreJson ns;
			if (numberGridType==1)
				ns = numberGrid.getNumberStore(dr.getFirstPlace().substring(2));
			else
			if (numberGridType==2)
				ns = numberGrid.getNumberStore(dr.getFirstPlace().substring(1));
			else
				ns = numberGrid.getNumberStore(dr.getFirstPlace());
			if (ns==null)
				continue;
			ns.setLevel(Dx4NumberStoreLevelJson.FIRST);
			ns.setOccurences(ns.getOccurences()+1);
			if (numberGridType==1)
				ns = numberGrid.getNumberStore(dr.getSecondPlace().substring(2));
			else
			if (numberGridType==2)
				ns = numberGrid.getNumberStore(dr.getSecondPlace().substring(1));
			else
				ns = numberGrid.getNumberStore(dr.getSecondPlace());
			if (ns==null)
				continue;
			if (ns.getLevel()!=Dx4NumberStoreLevelJson.FIRST)
				ns.setLevel(Dx4NumberStoreLevelJson.SECOND);
			ns.setOccurences(ns.getOccurences()+1);
			if (numberGridType==1)
				ns = numberGrid.getNumberStore(dr.getThirdPlace().substring(2));
			else
			if (numberGridType==2)
				ns = numberGrid.getNumberStore(dr.getThirdPlace().substring(1));
			else
				ns = numberGrid.getNumberStore(dr.getThirdPlace());
			if (ns==null)
				continue;
			if (ns.getLevel()!=Dx4NumberStoreLevelJson.FIRST && ns.getLevel()!=Dx4NumberStoreLevelJson.SECOND)
				ns.setLevel(Dx4NumberStoreLevelJson.THIRD);
			ns.setOccurences(ns.getOccurences()+1);
			sortedNumbers.add(ns);
		}
		
		numberGrid.setSortedNumbers(sortedNumbers);
		numberGrid.setResultStartDate(startDate);
		numberGrid.setResultEndDate(endDate);
		return numberGrid;
	}
	
	public void updateExternalGameResults()
	{
		Date fromDate = dx4Services.getDx4Home().getDrawResultsDateRange().get(1);
		log.info("UPDATING EXTERNAL RESULTS FROM : " + fromDate);
		ParseExternal4DHistoric.getResultsForDates(fromDate,(new GregorianCalendar()).getTime(),dx4Services);
	}
	
	public void setExternalGameResultsPrevNext(ExternalGameResults xgr)
	{
		xgr.setPrevDate(dx4Services.getDx4Home().getPrevDrawDate(xgr.getDraws().get(0).getDate()));
		xgr.setNextDate(dx4Services.getDx4Home().getNextDrawDate(xgr.getDraws().get(0).getDate()));
	}
	
	public ExternalGameResults getNearestExternalGameResults(Date date) throws Dx4ExternalServiceException
	{
		
		log.trace("Getting results for date: " + date);
		
		ExternalGameResults xgr = getActualExternalGameResults(date);
		if (xgr==null)
		{	
			Date nearest = dx4Services.getDx4Home().getPrevDrawDate(date);
			if (nearest == null)
				nearest = dx4Services.getDx4Home().getNextDrawDate(date);
			if (nearest == null)
				throw new Dx4ExternalServiceException("No draw found for this date");
			log.trace("Getting results for nearest date: " + nearest);
			xgr = getActualExternalGameResults(nearest);
			if (xgr==null)						// should not happen
			{
				log.error("INCONSISTENT DRAW RESULT ERROR - IMPLIES NO DRAW RESULTS");
				throw new Dx4ExternalServiceException("No draw found for this date");
			}
		}
		return xgr;
	}
	
	public List<Dx4DrawResultJson> getActualLatestExternalGameResults()
	{
		return dx4Services.getDx4Home().getLatestDrawResults();
	}
	
	public ExternalGameResults getActualExternalGameResults(Date date)
	{
		date = DateUtil.getDateWithZeroedTime(date);
		log.info("Getting results for date: " + date);
		
		List<Dx4DrawResultJson> results = dx4Services.getDx4Home().getDrawResults(date,date);
		if (results.isEmpty())
		{	
			return null;
		}
		
		return createExternalGameResults(results);
	}
	
	public ExternalGameResults createExternalGameResults(List<Dx4DrawResultJson> results)
	{
		for (Dx4DrawResultJson result : results)
			dx4Services.getDx4Home().populateDrawResultSpecialsAndConsolations(result);
		for (Dx4DrawResultJson result : results)
		{
			dx4Services.getDx4Home().getDescsForDrawResult(result);
			dx4Services.getDx4Home().getImagesForDrawResult(result);
		}
		List<Dx4ProviderJson> providers = dx4Services.getDx4Home().getProviders();
		for (Dx4DrawResultJson result : results)
		{
			for (Dx4ProviderJson provider : providers)
				if (result.getProviderId()==provider.getId())
				{
					result.setProvider(provider);
				}
		}
		ExternalGameResults xgr = new ExternalGameResults(results);
		setExternalGameResultsPrevNext(xgr);
		return xgr;
	}

	public List<Dx4DrawResultJson> getExternalSameDayGameResults()
	{
		List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
		Date startDate = dates.get(0);
		GregorianCalendar gc = new GregorianCalendar();
		if (Dx4Config.getProperties().getProperty("dx4.testSameDay", "false").equals("true"))
		{
			String dateStr =  Dx4Config.getProperties().getProperty("dx4.testSameDate");
			if (dateStr != null)
			{
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				try
				{
					Date date = df.parse(dateStr);
					gc.setTime(date);
					log.info("%%% Using test same date : " + date);
				}
				catch (ParseException e)
				{
					log.error("getExternalSameDayGameResults - date : " + dateStr + " couldn't be parsed");
				}
			}
		}
		
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.clear();
		gc1.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DAY_OF_MONTH));
		List<Dx4DrawResultJson> results = new ArrayList<Dx4DrawResultJson>();
		while (true)
		{
			gc1.roll(Calendar.YEAR, false);
			Date lastYear = gc1.getTime();
			if (lastYear.before(startDate))
				break;
			log.info("getting results for last year:" + lastYear);
			List<Dx4DrawResultJson> result = dx4Services.getDx4Home().getDrawResults(lastYear,lastYear);
			results.addAll(result);
		}
		
		for (Dx4DrawResultJson res : results)
			log.info("Got DrawResult : " + res);

		return results;
	}
	
	public List<Date> getExternalSameDayGameDates()
	{
		List<Date> dates = new ArrayList<Date>();
		Date startDate = dx4Services.getDx4Home().getDrawResultsDateRange().get(0);
		GregorianCalendar gc = new GregorianCalendar();
		if (Dx4Config.getProperties().getProperty("dx4.testSameDay", "false").equals("true"))
		{
			String dateStr =  Dx4Config.getProperties().getProperty("dx4.testSameDate");
			if (dateStr != null)
			{
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				try
				{
					Date date = df.parse(dateStr);
					gc.setTime(date);
					log.info("%%% Using test same date : " + date);
				}
				catch (ParseException e)
				{
					log.error("getExternalSameDayGameResults - date : " + dateStr + " couldn't be parsed");
				}
				return dates;
			}
		}
		
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.clear();
		gc1.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DAY_OF_MONTH));
		while (true)
		{
			gc1.roll(Calendar.YEAR, false);
			Date lastYear = gc1.getTime();
			if (lastYear.before(startDate))
				break;
			log.trace("getting results for last year:" + lastYear);
			List<Dx4DrawResultJson> results = dx4Services.getDx4Home().getDrawResults(lastYear,lastYear);
			if (results.isEmpty())
				continue;
			dates.add(results.get(0).getDate());
		}
		
		return dates;
	}
	
	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}
	
	public List<ExternalComingDate> getExternalComingDates()
	{
		ParseComingDates parser = new ParseComingDates(dx4Services);
		return parser.getExternalComingDates();
	}

}
