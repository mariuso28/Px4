package org.dx4.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPlacings {

	private static final Logger log = Logger.getLogger(TestPlacings.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		Date startDate = df.parse("02-Jan-2008");
		Date endDate  = df.parse("15-Dec-2013");
		List<Dx4PlacingJson> placings = dx4Services.getDx4Home().getPlacingsForNumber("0017",startDate,endDate);
		for (Dx4PlacingJson mg : placings)
			log.info(mg);
	}
}
