package org.dx4.external.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.services.Dx4Services;
import org.html.parser.UrlParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ParseExternal4DHistoric {
	private static final Logger log = Logger.getLogger(ParseExternal4D.class);
	
	private static void getMainHistoricResults(Date date,Dx4Services dx4Services) throws UrlParserException
	{
		try
		{
			log.info("%%% getHistoricResults for Date : " + date);
			ParseExternal4D parser = new ParseExternal4D(dx4Services);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String dStr = df.format(date);
			String url = "http://www.check4d.com/past-results?drawpastdate="+dStr+"/";
			log.info("Getting historic dates for :" + url);
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'M');
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'K');
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'T');
			parser.extract(url);
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}
	
	private static void getSabahHistoricResults(Date date,Dx4Services dx4Services) throws UrlParserException
	{
		try
		{
			log.info("%%% getHistoricResultsSabah for Date : " + date);
			ParseExternal4D parser = new ParseExternal4D(dx4Services);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String dStr = df.format(date);
			String url = "http://www.check4d.com/past-results?drawpastdate="+dStr+"/";
			log.info("Getting historic dates for :" + url);
			
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'8');
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'D');
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'C');
			parser.extractSabahEtc(url);
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}
	
	private static void getSingaporeHistoricResults(Date date,Dx4Services dx4Services) throws UrlParserException
	{
		try
		{
			log.info("%%% getSingaporeHistoricResults for Date : " + date);
			ParseExternal4D parser = new ParseExternal4D(dx4Services);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String dStr = df.format(date);
			String url = "http://singapore.check4d.com/past-results?drawpastdate="+dStr+"/";
			log.info("Getting historic dates for :" + url);
			
			dx4Services.getDx4Home().removeResultsForProviderDate(date,'S');
			parser.extractSingapore(url);
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}
	
	public static void getResultsForDates(Date fromDateExclusive,Date toDate,Dx4Services dx4Services)
	{
		log.info("%%%  from Date : " + fromDateExclusive + " To : " + toDate);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fromDateExclusive);
		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DAY_OF_MONTH));
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTime(toDate);
		gc1.set(gc1.get(Calendar.YEAR),gc1.get(Calendar.MONTH),gc1.get(Calendar.DAY_OF_MONTH));
		
		while (true)
		{
			try
			{
				Date date = gc.getTime();
				gc.roll(Calendar.DAY_OF_YEAR, true);
				if (gc.getTime().before(date))			// will happen on 1/1/yyyy where year stays same
				{
					gc.set(gc.get(Calendar.YEAR)+1,gc.get(Calendar.MONTH),gc.get(Calendar.DAY_OF_MONTH));
				}
				if (gc.getTime().after(gc1.getTime()))
					return;
				getMainHistoricResults(gc.getTime(),dx4Services);
				getSabahHistoricResults(gc.getTime(),dx4Services);
				getSingaporeHistoricResults(gc.getTime(),dx4Services);
			}
			catch (UrlParserException e)
			{
				log.info(e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void getResultsForYear(int year,Dx4Services dx4Services)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(year,11,31);
		//	gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DAY_OF_MONTH));

		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.clear();
		gc1.set(year,11,31);
		while (true)
		{
			try
			{
				getMainHistoricResults(gc.getTime(),dx4Services);
				gc.roll(Calendar.DAY_OF_YEAR, false);
				if (gc.getTime().equals(gc1.getTime()))
					return;
			}
			catch (UrlParserException e)
			{
				log.error(e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void getSingaporeResultsForYear(int year,Dx4Services dx4Services)
	{
		GregorianCalendar ngc = new GregorianCalendar();
		
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		if (ngc.get(Calendar.YEAR)!=year)
			gc.set(year,11,31);
		else
			gc.set(ngc.get(Calendar.YEAR),ngc.get(Calendar.MONTH),ngc.get(Calendar.DAY_OF_MONTH));

		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.clear();
		gc1.set(year,11,31);
		while (true)
		{
			try
			{
				getSingaporeHistoricResults(gc.getTime(),dx4Services);
				gc.roll(Calendar.DAY_OF_YEAR, false);
				if (gc.getTime().equals(gc1.getTime()))
					return;
			}
			catch (UrlParserException e)
			{
				log.error(e.getMessage());
			}
		}
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
	
		try
		{

			GregorianCalendar gc1 = new GregorianCalendar();
			gc1.clear();
			gc1.set(2017,7,23);
	//		getHistoricResults(gc1.getTime(),dx4Services,false);
			
			Date toDate = gc1.getTime();
			
			gc1.clear();
			gc1.set(2008,0,2);
			Date fromDateExclusive = gc1.getTime();
			
			getResultsForDates(fromDateExclusive,toDate,dx4Services);
		
			
			
		//	getSingaporeHistoricResults(gc1.getTime(),dx4Services);
			
		//	for (int year = 2017; year>2007; year--)
		//		getSingaporeResultsForYear(year,dx4Services);
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}
}
