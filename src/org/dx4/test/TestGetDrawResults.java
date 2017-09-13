package org.dx4.test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetDrawResults {
	
	private static final Logger log = Logger.getLogger(TestGetDrawResults.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
			
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(2008,0,6);
		Date fromDate = gc.getTime();
		
	//	Date toDate = (new GregorianCalendar()).getTime();
		gc = new GregorianCalendar();
		gc.clear();
		gc.set(2008,0,6);
		Date toDate = gc.getTime();
		
		List<Dx4DrawResultJson> dd = dx4Services.getDx4Home().getDrawResults(fromDate,toDate);
		
		for (Dx4DrawResultJson d : dd)
			log.info(d);
	}
}
