package org.dx4.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.external.parser.ExternalComingDate;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetComingDraws {
	
	private static final Logger log = Logger.getLogger(TestGetComingDraws.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
			
		List<ExternalComingDate> ecds = dx4Services.getExternalService().getExternalComingDates();
		
		for (ExternalComingDate ecd : ecds)
			log.info(ecd);
	}
}
