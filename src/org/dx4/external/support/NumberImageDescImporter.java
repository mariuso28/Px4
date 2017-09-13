package org.dx4.external.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.services.Dx4Services;
import org.html.parser.ParseBlock;
import org.html.parser.ParseBlockList;
import org.html.parser.ParseEntryList;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberImageDescImporter {

	private static final Logger log = Logger.getLogger(NumberImageDescImporter.class);
	private ParseUrl url;
	private Dx4Home dx4Home;

	NumberImageDescImporter(Dx4Home dx4Home)
	{
		setDx4Home(dx4Home);
	}
	
	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "4DNumbers" );

	    ParseBlock parseBlock = new ParseBlock( "numbers" );

		// <h2>Coming Draw Date 
		parseBlock.setStartToken( "<ul class=\"qzt-page-list\">" );
		parseBlock.setEndToken("</ul>");
		
		ParseBlock pb1 = new ParseBlockList( "numberList" );
		pb1.setStartToken( "<li>" );
		pb1.setEndToken("</li>");
		
		ParseEntryList peList = new ParseEntryList( "content", "<span class=\"content\">", "<span class=\"content\">", "</span>" );
		pb1.addParseEntry(peList);
		parseBlock.addParseBlock("numberList",pb1);
		
		url.addParseBlock(parseBlock);
	}
	
	private void storeDx4NumberPageElementJson(int number,String desc,String descriptionCh) throws IOException
	{
		if (number==1000)
			number = 0;
		String tok = Integer.toString(number);
		while (tok.length()<3)
			tok = "0" + tok;
		
		Dx4NumberPageElementJson npe = new Dx4NumberPageElementJson();
		npe.setNumber(number);
		npe.setDescription(desc);
		npe.setDescriptionCh(descriptionCh);
		npe.setToken(tok);
		npe.setDictionary(Dx4NumberPageElementJson.DICTIONARYTRADITIONAL3);
		
		byte[] image = loadImage(tok);
		dx4Home.storeDx4NumberPageElementJson(npe,image);
	}
	
	private byte[] loadImage(String number) throws IOException
	{
		Path path = Paths.get("/home/pmk/4DX/number-images-trad/" + number + ".jpeg");
		log.info("Loading : " + path);
	    return Files.readAllBytes(path);
	}

	private int extract(String sourceBase,int startNum)
	{
		try
		{
			setup();											
			url.parse(sourceBase);
			for (int i=0; i<25; i++)
			{
				String str1 = url.getValue("numbers.numberList[" + i + "].content[1]");
				String str2 = url.getValue("numbers.numberList[" + i + "].content[2]");
				log.info("" + startNum + "  " + str1 + "  " + str2);
				
				storeDx4NumberPageElementJson(startNum,str2,str1);
				startNum++;
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
		return startNum;
	}
	
	

	public static void main(String args[])
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try
		{
			NumberImageDescImporter nd = new NumberImageDescImporter(dx4Home);
			int num = 1;
			for (int j=0; j<40; j++)
			{
				num = nd.extract("http://www.4dmanager.com/qzt/gym/" + (j+1),num);
			}
		}
		catch (Exception e)
		{
			log.fatal(e.getClass().getSimpleName() + " - " + e.getMessage());
		}
	}

	public Dx4Home getDx4Home() {
		return dx4Home;
	}

	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}
	
}
