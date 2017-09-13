package org.dx4.test;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.external.parser.ParseExternal4DHistoric;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCreateExternalResults {
	
	private static final Logger log = Logger.getLogger(TestCreateExternalResults.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(2008,0,5);
		Date fromDate = gc.getTime();
		
		Date toDate = (new GregorianCalendar()).getTime();
		/*gc = new GregorianCalendar();
		gc.clear();
		gc.set(2008,0,6);
		Date toDate = gc.getTime();
		*/
		
		log.info("UPDATING EXTRNAL RESULTS FROM : " + fromDate + " TO: " + toDate);
		
		
		ParseExternal4DHistoric.getResultsForDates(fromDate,toDate,dx4Services);
		
	}
}
