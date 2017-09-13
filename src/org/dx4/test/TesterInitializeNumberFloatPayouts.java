package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TesterInitializeNumberFloatPayouts{
private static final Logger log = Logger.getLogger(TesterInitializeNumberFloatPayouts.class);
	
	public static void main(String[] args) {
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
		"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");

		Dx4NumberFloatPayoutJson nfp = dx4Services.getFloatPayoutMgr().getDx4NumberFloatPayoutJson("2828");
				
		log.info(nfp);
	}
}
