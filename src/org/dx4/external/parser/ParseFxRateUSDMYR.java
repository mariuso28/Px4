package org.dx4.external.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.html.parser.ParseBlock;
import org.html.parser.ParseEntry;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;

public class ParseFxRateUSDMYR{

	private static final Logger log = Logger.getLogger(ParseFxRateUSDMYR.class);
	private ParseUrl url;
	private String sourceBase;
	private FxRate fxRate;

	
	public ParseFxRateUSDMYR() throws Exception
	{
		extract("http://www.xe.com/currencyconverter/convert/?From=USD&To=MYR/");
	}
	
	public void setSourceBase(String sourceBase) {
		this.sourceBase = sourceBase;
	}

	public String getSourceBase() {
		return sourceBase;
	}

	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "USDMYR" );

		ParseBlock parseBlock = getEuroMyr();
		url.addParseBlock(parseBlock);
	}
	
	private ParseBlock getEuroMyr() throws UrlParserException
	{
		ParseBlock parseBlock = new ParseBlock( "fxrate" );

		parseBlock.setStartToken( "<span class='uccToCurrencyCode'>" );
		parseBlock.setEndToken("fitElements()");
	
		
		ParseEntry pe = new ParseEntry( "rate", "<a href='/currency/usd-us-dollar'>", ">1 USD = ", " MYR<");
		parseBlock.addParseEntry(pe);
		pe = new ParseEntry( "timestamp", "data-time=", ">", " UTC");
		parseBlock.addParseEntry(pe);
			
		return parseBlock;
	}
	
	private void extract(String sourceBase) throws Exception
	{
		setSourceBase(sourceBase);
		setup();											
		url.parse(sourceBase);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		fxRate = new FxRate();
		fxRate.setFrom("USD");
		fxRate.setTo("MYR");

		String dstr = url.getValue("fxrate.timestamp");
		Date date = df.parse(dstr);
		fxRate.setTimestamp(date);		

		String rstr = url.getValue("fxrate.rate");
		log.info("Rate: " + rstr);
		fxRate.setRate(Double.parseDouble(rstr));
	}

	public FxRate getFxRate() {
		return fxRate;
	}

	public void setFxRate(FxRate fxRate) {
		this.fxRate = fxRate;
	}
	
	public static void main(String[] args)
	{
		try
		{
			ParseFxRateUSDMYR pfx = new ParseFxRateUSDMYR();
			log.info(pfx.getFxRate());
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}

}

