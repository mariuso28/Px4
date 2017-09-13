package org.dx4.external.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.services.Dx4Services;
import org.html.parser.ParseBlock;
import org.html.parser.ParseBlockList;
import org.html.parser.ParseEntry;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberSearchTerm {

	private static final Logger log = Logger.getLogger(NumberSearchTerm.class);
	private ParseUrl url;
	private Dx4Home dx4Home;
	private List<NumberSearchEntry> numbers = new ArrayList<NumberSearchEntry>();
	private String term;
	private Exception error;
	
	private static Map<String,Character> dictionaryMapper;
	
	static{
		dictionaryMapper = new HashMap<String,Character>();
		dictionaryMapper.put("大伯公千字图",Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		dictionaryMapper.put("观音千字图",Dx4NumberPageElementJson.DICTIONARYTRADITIONAL3);
		dictionaryMapper.put("万字解梦图",Dx4NumberPageElementJson.DICTIONARYMODERN4);
	}

	public NumberSearchTerm(Dx4Home dx4Home,String term)
	{
		setDx4Home(dx4Home);
		setTerm(term);
		extract("http://www.4dmanager.com/search/"+term);
	}
	
	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "4DNumbers");

	    ParseBlock parseBlock = new ParseBlock( "numbers" );

		parseBlock.setStartToken( "<div class=\"row row-offcanvas row-offcanvas-right\">" );
		parseBlock.setEndToken("<div class=\"col-xs-6 col-sm-3 sidebar-offcanvas\" id=\"sidebar\">");
		
		ParseBlockList pbl = new ParseBlockList("entries");
		pbl.setStartToken("class=\"list-group-item search-box\">");
		pbl.setEndToken("</a>");
		ParseEntry pe1 = new ParseEntry( "value", "<h4 class=\"media-heading\">", "<h4 class=\"media-heading\">", "</h4>" );
		ParseEntry pe2 = new ParseEntry( "dict", "<p class=\"qzt\">", "/>", "</p>" );
		pbl.addParseEntry(pe1);
		pbl.addParseEntry(pe2);
		
		parseBlock.addParseBlock("entries",pbl);
		
		url.addParseBlock(parseBlock);
	}
	
	private void extract(String sourceBase)
	{
		try
		{
			setup();											
			url.parse(sourceBase, true);
			int entries = url.getSize("numbers.entries");
			for (int i=0; i<entries; i++)
			{
				String str1 = url.getValue("numbers.entries[" + i + "].value");
				String str2 = url.getValue("numbers.entries[" + i + "].dict");
				log.info("Found: " + str1 + " " + str2);
				if (str1.length()==3 || str1.length()==4)
				{
					try
					{
						Integer.parseInt(str1);
						Character dict = dictionaryMapper.get(str2.trim());
						if (dict == null)
							continue;
						NumberSearchEntry nse = new NumberSearchEntry(str1,dict);
						numbers.add(nse);
					}
					catch (NumberFormatException e)
					{
						;
					}
				}				
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			// log.error(e.getMessage(),e);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			setError(e);
		}
	}
	
	public Dx4Home getDx4Home() {
		return dx4Home;
	}

	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<NumberSearchEntry> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<NumberSearchEntry> numbers) {
		this.numbers = numbers;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try
		{
			NumberSearchTerm nd = new NumberSearchTerm(dx4Home,"cow");
			for (NumberSearchEntry nse : nd.getNumbers())
				log.info(nse);
		}
		catch (Exception e)
		{
			log.fatal(e.getClass().getSimpleName() + " - " + e.getMessage());
		}
	}	
}
