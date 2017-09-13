package org.dx4.external.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.services.Dx4Services;
import org.html.parser.ParseBlock;
import org.html.parser.ParseBlockList;
import org.html.parser.ParseEntry;
import org.html.parser.ParseEntryList;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ParseComingDates{

	private static final Logger log = Logger.getLogger(ParseComingDates.class);
	private ParseUrl url;
	private String sourceBase;
	private Dx4Services dx4Services;
	private List<ExternalComingDate> externalComingDates;
	
	
	public ParseComingDates(Dx4Services dx4Services)
	{
		this.setDx4Services(dx4Services);
		externalComingDates = new ArrayList<ExternalComingDate>();
	}
	
	public void setSourceBase(String sourceBase) {
		this.sourceBase = sourceBase;
	}

	public String getSourceBase() {
		return sourceBase;
	}

	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "4DComingDates" );

		ParseBlock parseBlock = getComingDraws();
		url.addParseBlock(parseBlock);
	}
	
	private ParseBlock getComingDraws() throws UrlParserException
	{
		ParseBlock parseBlock = new ParseBlock( "coming" );

		// <h2>Coming Draw Date 
		parseBlock.setStartToken( "<h4>Coming Draw Date</h4>" );
		parseBlock.setEndToken("<h4>Our Facebook Page</h4>");
	
		// <li class="recent_draw">
		ParseBlock pb1 = new ParseBlockList( "draws" );
		pb1.setStartToken( "<li class=\"list-group-item\">" );
		pb1.setEndToken("</li>");
		
		// <div class="draw-date">20/05/2017</div>
		ParseEntry pe = new ParseEntry( "date", "class=\"draw-date\">", "draw-date\">", "<" );
		pb1.addParseEntry(pe);
		
		ParseEntryList peList = new ParseEntryList( "providers", "<img title=\"", "title=\"", "\"" );
		pb1.addParseEntry(peList);
		
		parseBlock.addParseBlock( "draws", pb1);
		
		return parseBlock;
	}
	
	void extract(String sourceBase)
	{
		try
		{
			setSourceBase(sourceBase);
			setup();											
			url.parse(sourceBase);
			
			int hcNum = url.getSize("coming.draws");
			// 14/12/2013
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			for (int n=0; n<hcNum; n++)
			{
				String drawStr = "coming.draws[" + n + "]";
				String dstr = "";
				try
				{
					ExternalComingDate xcd = new ExternalComingDate();
					dstr = url.getValue(drawStr+".date");
					Date date = df.parse(dstr);
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(date);
					
					log.info("DATE STR: " + dstr + " PARSED TO : " + date);
					xcd.setDate(date);
					String pString = drawStr+".providers";
					int pNum = url.getSize(pString);
					for (int p=0; p<pNum; p++)
					{
						String provider = url.getValue(pString+"[" + p + "]");
						xcd.getProviders().add(provider);
					}
					externalComingDates.add(xcd);
				}
				catch (ParseException e)
				{
					log.error("extract - date : " + dstr + " couldn't be parsed");
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
	}
	

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}
	
	
	public List<ExternalComingDate> getExternalComingDates()
	{
		try
		{
			extract("http://www.4dmanager.com/");
			return externalComingDates;
		}
		catch (Exception e)
		{
			log.error( e + " : " + e.getMessage());
			return null;
		}
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
		"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		try
		{
			ParseComingDates parser = new ParseComingDates(dx4Services);
			for (ExternalComingDate xcd : parser.getExternalComingDates())
			{
				log.info(xcd);
			}
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}

}

