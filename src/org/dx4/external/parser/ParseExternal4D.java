package org.dx4.external.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.services.Dx4Services;
import org.html.parser.ParseBlock;
import org.html.parser.ParseEntry;
import org.html.parser.ParseEntryList;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;
import org.html.parser.UrlParserExceptionEof;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ParseExternal4D{

	private static final Logger log = Logger.getLogger(ParseExternal4D.class);
	private ParseUrl url;
	private String sourceBase;
	private Dx4Services dx4Services;
	private ExternalGameResults externalResults;
	
	
	public ParseExternal4D(Dx4Services dx4Services)
	{
		this.setDx4Services(dx4Services);
		externalResults = new ExternalGameResults();
	}
	
	public void setSourceBase(String sourceBase) {
		this.sourceBase = sourceBase;
	}

	public String getSourceBase() {
		return sourceBase;
	}

	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "4D" );

	    ParseBlock magnum = setUp4DParseBlock("Magnum4D","img src=\"/images/logo_magnum.gif");
	    url.addParseBlock(magnum);
	    url.addParseBlock(setUp4DParseBlock("DaMaCai4D","img src=\"/images/logo_damacai.gif"));
	    url.addParseBlock(setUp4DParseBlock("SportsToto4D","img src=\"/images/logo_toto.gif"));
	}
	
	private void setupSabahEtc() throws UrlParserException
	{
	    url = new ParseUrl( "4D" );

	    ParseBlock sabah = setUp4DParseBlock("Sabah884D","img src=\"/images/logo_sabah88.gif");
	    url.addParseBlock(sabah);
	    url.addParseBlock(setUp4DParseBlock("Sandakan4D","img src=\"/images/logo_stc4d.gif"));
	    url.addParseBlock(setUp4DParseBlock("CashSweep","img src=\"/images/logo_cashsweep.gif"));
	}
	
	
	private void setupCashSweep() throws UrlParserException
	{
	    url = new ParseUrl( "4D" );
	    url.addParseBlock(setUp4DParseBlock("CashSweep","img src=\"/images/logo_cashsweep.gif"));
	}
	private void setupSingapore() throws UrlParserException
	{
	    url = new ParseUrl( "4D" );

	    ParseBlock singapore = setUp4DParseBlock("Singapore4D","Singapore 4D");
	    url.addParseBlock(singapore);
	}

	private ParseBlock setUp4DParseBlock(String name, String startToken) throws UrlParserException
	{
		ParseBlock parseBlock = new ParseBlock( name );

		// <div class="outerbox"><table class="resultTable" 
		// parseBlock.setStartToken( "<div class=\"outerbox\"><table class=\"resultTable\"" );
		// parseBlock.setEndToken("<td style=\"width: 50%;vertical-align:top");
		
		if (name.equals("Singapore4D"))
		{
			parseBlock.setStartToken( "<table class=\"resultTable\"" );
			parseBlock.setEndToken("<td style=\"width: 50%;vertical-align:top\"");
		}
		else
		{
			parseBlock.setStartToken( startToken );
			parseBlock.setEndToken("</div>");
		}
		
		ParseEntry pe;
		//img src="/images/logo_magnum.gif"
		if (!name.equals("Singapore4D"))
			pe = new ParseEntry( "image", "img src=\"/images/", "/images/", "\"/></td>" );
		
		// <tr><td class="resultdrawdate">Date: 03-11-2013 (Sun)</td>

		pe = new ParseEntry( "date", "class=\"resultdrawdate\">", ">Date: ", " " );
		parseBlock.addParseEntry(pe);

		// class="resultdrawdate">Draw No: 187/13</td></tr>
		pe = new ParseEntry( "drawNo", "class=\"resultdrawdate", "Draw No: ", "</td>" );
		parseBlock.addParseEntry(pe);
		
		/*
		<tr><td style="width:45%" class="resultprizelable">1st Prize </td><td class="resulttop">0626</td></tr>
		<tr><td style="width:45%" class="resultprizelable">2nd Prize </td><td class="resulttop">0412</td></tr>
		<tr><td style="width:45%" class="resultprizelable">3rd Prize </td><td class="resulttop">8726</td></tr>
		 */

		pe = new ParseEntry( "firstPlace", ">1st Prize ", "resulttop\">", "</td>" );
		parseBlock.addParseEntry(pe);

		pe = new ParseEntry( "secondPlace", ">2nd Prize ", "resulttop\">", "</td>" );
		parseBlock.addParseEntry(pe);

		pe = new ParseEntry( "thirdPlace", ">3rd Prize ", "resulttop\">", "</td>" );
		parseBlock.addParseEntry(pe);
		
		/*
	 	class="resultprizelable">Special </td></tr><tr><td class="resultbottom">1909</td>
	 	<td class="resultbottom">1290</td><td class="resultbottom">8031</td><td class="resultbottom">5346</td>
	 	<td class="resultbottom">5440</td></tr><tr><td class="resultbottom">7763</td>
	 	<td class="resultbottom">1947</td><td class="resultbottom">7550</td><td class="resultbottom">1055</td>
	 	<td class="resultbottom">8170</td></tr><tr><td style="width:20%"></td><td class="resultbottom">&nbsp;
	 */
	
		ParseBlock parseBlock1 = new ParseBlock("special");

		parseBlock1.setStartToken("class=\"resultprizelable\">Special");
		parseBlock1.setEndToken("<td colspan=\"5\"");
	
		ParseEntryList peList = new ParseEntryList( "numbers", "<td class=\"resultbottom\">", "<td class=\"resultbottom\">", "</td>" );
		parseBlock1.addParseEntry(peList);
		
		parseBlock.addParseBlock( "special", parseBlock1 );

		ParseBlock parseBlock2 = new ParseBlock("consolation");

		parseBlock2.setStartToken(">Consolation");
		parseBlock2.setEndToken("</tr></table>");
	
		ParseEntryList peList2 = new ParseEntryList( "numbers", "<td class=\"resultbottom\">", "<td class=\"resultbottom\">", "</td>" );
		parseBlock2.addParseEntry(peList2);
		
		parseBlock.addParseBlock( "consolation", parseBlock2 );
		
		return parseBlock;
	}
	
	void extract(String sourceBase)
	{
		try
		{
			setSourceBase(sourceBase);
			setup();											
			url.parseUnordered(sourceBase);
			
			Dx4DrawResultJson magnumResults = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Magnum 4D"));
			
			String dateStr = url.getValue("Magnum4D.date");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			magnumResults.setDate(df.parse(dateStr));
			magnumResults.setDrawNo(url.getValue("Magnum4D.drawNo"));
			magnumResults.setFirstPlace(checkNumberFilter(url.getValue("Magnum4D.firstPlace")));
			magnumResults.setSecondPlace(checkNumberFilter(url.getValue("Magnum4D.secondPlace")));
			magnumResults.setThirdPlace(checkNumberFilter(url.getValue("Magnum4D.thirdPlace")));
			setSpecialsAndConsultions(magnumResults,"Magnum4D");
			
			log.info(magnumResults);
			
			Dx4DrawResultJson sportsToto = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Sports Toto 4D"));
			
			dateStr = url.getValue("SportsToto4D.date");
			df = new SimpleDateFormat("dd-MM-yyyy");
			sportsToto.setDate(df.parse(dateStr));
			sportsToto.setDrawNo(url.getValue("SportsToto4D.drawNo"));
			sportsToto.setFirstPlace(checkNumberFilter(url.getValue("SportsToto4D.firstPlace")));
			sportsToto.setSecondPlace(checkNumberFilter(url.getValue("SportsToto4D.secondPlace")));
			sportsToto.setThirdPlace(checkNumberFilter(url.getValue("SportsToto4D.thirdPlace")));
			setSpecialsAndConsultions(sportsToto,"SportsToto4D");
			
			log.info(sportsToto);
			
			Dx4DrawResultJson DiMaCai = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Da Ma Cai 1+3D"));
			
			dateStr = url.getValue("DaMaCai4D.date");
			df = new SimpleDateFormat("dd-MM-yyyy");
			DiMaCai.setDate(df.parse(dateStr));
			DiMaCai.setDrawNo(url.getValue("DaMaCai4D.drawNo"));
			DiMaCai.setFirstPlace(checkNumberFilter(url.getValue("DaMaCai4D.firstPlace")));
			DiMaCai.setSecondPlace(checkNumberFilter(url.getValue("DaMaCai4D.secondPlace")));
			DiMaCai.setThirdPlace(checkNumberFilter(url.getValue("DaMaCai4D.thirdPlace")));
			setSpecialsAndConsultions(DiMaCai,"DaMaCai4D");
			
			log.info(DiMaCai);
			
			externalResults.getDraws().add(magnumResults);
			externalResults.getDraws().add(sportsToto);
			externalResults.getDraws().add(DiMaCai);
			
			dx4Services.getDx4Home().storeDrawResult(magnumResults);
			dx4Services.getDx4Home().storeDrawResult(sportsToto);
			dx4Services.getDx4Home().storeDrawResult(DiMaCai);
		}
		catch (UrlParserException e)
		{
			log.error("URL PARSE EXCEPTION : " + e.getMessage(),e);
		}
		catch (Exception e)
		{
			log.error("OTHER PARSE EXCEPTION : " + e.getMessage(),e);
		}
	}
	
	void extractCashSweep(String sourceBase)
	{
		try
		{
			setSourceBase(sourceBase);
			setupCashSweep();	
			try
			{
				url.parseUnordered(sourceBase);
			}
			catch (UrlParserExceptionEof e)
			{
				log.error("URL PARSE EOF");
			}
			

			
			Dx4DrawResultJson cashsweep = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Special CashSweep"));
			
			String dateStr = url.getValue("CashSweep.date");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			cashsweep.setDate(df.parse(dateStr));
			cashsweep.setDrawNo(url.getValue("CashSweep.drawNo"));
			cashsweep.setFirstPlace(checkNumberFilter(url.getValue("CashSweep.firstPlace")));
			cashsweep.setSecondPlace(checkNumberFilter(url.getValue("CashSweep.secondPlace")));
			cashsweep.setThirdPlace(checkNumberFilter(url.getValue("CashSweep.thirdPlace")));
			setSpecialsAndConsultions(cashsweep,"CashSweep");
			
			log.info(cashsweep);
			
			externalResults.getDraws().add(cashsweep);
			
		
		}
		catch (UrlParserException e)
		{
			log.error("URL PARSE EXCEPTION : " + e.getMessage(),e);
		}
		catch (Exception e)
		{
			log.error("OTHER PARSE EXCEPTION : " + e.getMessage(),e);
		}
	}
	
	void extractSabahEtc(String sourceBase)
	{
		try
		{
			setSourceBase(sourceBase);
			setupSabahEtc();	
			try
			{
				url.parseUnordered(sourceBase);
			}
			catch (UrlParserExceptionEof e)
			{
				log.error("URL PARSE EOF");
			}
			
			Dx4DrawResultJson sabah = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Sabah 88 4D"));
			
			String dateStr = url.getValue("Sabah884D.date");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			sabah.setDate(df.parse(dateStr));
			sabah.setDrawNo(url.getValue("Sabah884D.drawNo"));
			sabah.setFirstPlace(checkNumberFilter(url.getValue("Sabah884D.firstPlace")));
			sabah.setSecondPlace(checkNumberFilter(url.getValue("Sabah884D.secondPlace")));
			sabah.setThirdPlace(checkNumberFilter(url.getValue("Sabah884D.thirdPlace")));
			setSpecialsAndConsultions(sabah,"Sabah884D");
			
			log.info(sabah);
			
			Dx4DrawResultJson sandakan = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Sandakan 4D"));
			
			dateStr = url.getValue("Sandakan4D.date");
			sandakan.setDate(df.parse(dateStr));
			sandakan.setDrawNo(url.getValue("Sandakan4D.drawNo"));
			sandakan.setFirstPlace(checkNumberFilter(url.getValue("Sandakan4D.firstPlace")));
			sandakan.setSecondPlace(checkNumberFilter(url.getValue("Sandakan4D.secondPlace")));
			sandakan.setThirdPlace(checkNumberFilter(url.getValue("Sandakan4D.thirdPlace")));
			setSpecialsAndConsultions(sandakan,"Sandakan4D");
			
			log.info(sandakan);
			
			externalResults.getDraws().add(sandakan);
			
			Dx4DrawResultJson cashsweep = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Special CashSweep"));
			
			dateStr = url.getValue("CashSweep.date");
			cashsweep.setDate(df.parse(dateStr));
			cashsweep.setDrawNo(url.getValue("CashSweep.drawNo"));
			cashsweep.setFirstPlace(checkNumberFilter(url.getValue("CashSweep.firstPlace")));
			cashsweep.setSecondPlace(checkNumberFilter(url.getValue("CashSweep.secondPlace")));
			cashsweep.setThirdPlace(checkNumberFilter(url.getValue("CashSweep.thirdPlace")));
			setSpecialsAndConsultions(cashsweep,"CashSweep");
			
			log.info(cashsweep);
			
			externalResults.getDraws().add(cashsweep);
			
			dx4Services.getDx4Home().storeDrawResult(sabah);
			dx4Services.getDx4Home().storeDrawResult(sandakan);
			dx4Services.getDx4Home().storeDrawResult(cashsweep);
		}
		catch (UrlParserException e)
		{
			log.error("URL PARSE EXCEPTION : " + e.getMessage(),e);
		}
		catch (Exception e)
		{
			log.error("OTHER PARSE EXCEPTION : " + e.getMessage(),e);
		}
	}
	
	void extractSingapore(String sourceBase)
	{
		try
		{
			setSourceBase(sourceBase);
			setupSingapore();											
			url.parseUnordered(sourceBase);
			
			Dx4DrawResultJson singResults = new Dx4DrawResultJson(dx4Services.getDx4Home().getProviderByName("Singapore 4D"));
			
			String dateStr = url.getValue("Singapore4D.date");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			singResults.setDate(df.parse(dateStr));
			singResults.setDrawNo(url.getValue("Singapore4D.drawNo"));
			singResults.setFirstPlace(checkNumberFilter(url.getValue("Singapore4D.firstPlace")));
			singResults.setSecondPlace(checkNumberFilter(url.getValue("Singapore4D.secondPlace")));
			singResults.setThirdPlace(checkNumberFilter(url.getValue("Singapore4D.thirdPlace")));
			setSpecialsAndConsultions(singResults,"Singapore4D");
			
			log.info(singResults);
			
			
			externalResults.getDraws().add(singResults);
			
			dx4Services.getDx4Home().storeDrawResult(singResults);
			
		}
		catch (UrlParserException e)
		{
			log.error("URL PARSE EXCEPTION : " + e.getMessage(),e);
		}
		catch (Exception e)
		{
			log.error("OTHER PARSE EXCEPTION : " + e.getMessage(),e);
		}
	}
	
	private String checkNumberFilter(String number) throws UrlParserException
	{
		if (number.length()==4)
		{
			try
			{
				Integer.parseInt(number);
				return number;
			}
			catch (NumberFormatException e)
			{
				;
			}
		}
		
		
		// KNOWN POSSIBLE VALUES: <script>document.write(''+f+g+h+h);</script>
		// <script>document.write(''+h+f+h+e);</script> == 8385
		
		int pos1 = number.indexOf("<script>document.write(''+");
		int pos2 = number.lastIndexOf(")");
		int start = pos1+"<script>document.write(''+".length();
		if (pos1<0 || pos2<start)
			throw new UrlParserException("NUMBER FILTER OF VALUE : " + number);
		
		String filter = number.substring(start,pos2);
		// f+g+h+h = 3188
		if (filter.length()<7)
			throw new UrlParserException("NUMBER FILTER OF VALUE : " + number);
		char ch1 = filter.charAt(0);
		char ch2 = filter.charAt(2);
		char ch3 = filter.charAt(4);
		char ch4 = filter.charAt(6);
		Integer d1 = digitMap.get(ch1);
		Integer d2 = digitMap.get(ch2);
		Integer d3 = digitMap.get(ch3);
		Integer d4 = digitMap.get(ch4);
		if (d1==null || d2==null || d3==null || d4==null)
			throw new UrlParserException("NUMBER FILTER OF VALUE : " + number);
		
		return d1.toString()+d2.toString()+d3.toString()+d4.toString();
	}
	
	static Map<Character,Integer> digitMap = new HashMap<Character,Integer>();
	static{ 
		digitMap.put('i',0);
		digitMap.put('g',1);
		digitMap.put('a',2);
		digitMap.put('f',3);
		digitMap.put('j',4);
		digitMap.put('e',5);
		digitMap.put('b',6);
		digitMap.put('d',7);
		digitMap.put('h',8);
		digitMap.put('c',9);
	}
	
	private void setSpecialsAndConsultions(Dx4DrawResultJson results,String code) throws UrlParserException
	{
		int hcNum = url.getSize(code + ".special.numbers");
		List<String> specials = new ArrayList<String>(); 
		for (int n=0; n<hcNum; n++)
		{
			String value = url.getValue(code + ".special.numbers[" + n + "]");
			try
			{
				specials.add( checkNumberFilter(value) );
			}
			catch (UrlParserException e)
			{
				;
			}
		}
		results.setSpecials(specials);
		
		hcNum = url.getSize(code + ".consolation.numbers");
		List<String> consolations = new ArrayList<String>(); 
		for (int n=0; n<hcNum; n++)
		{
			String value = url.getValue(code + ".consolation.numbers[" + n + "]");
			try
			{
				consolations.add( checkNumberFilter(value) );
			}
			catch (UrlParserException e)
			{
				;
			}
		}
		results.setConsolations(consolations);
	}

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}
	
	
	public ExternalGameResults getExternalGameResults()
	{
		try
		{
			extract("http://www.check4d.com/");
			return externalResults;
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
			ParseExternal4D parser = new ParseExternal4D(dx4Services);
			parser.extract("http://www.check4d.com/past-results?drawpastdate=2013-01-01/");
			// parser.extract("http://www.check4d.com");
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}

	
	
}
