package org.dx4.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNumberSearch 
{
	private static Logger log = Logger.getLogger(TestNumberSearch.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home home = dx4Services.getDx4Home();
	
		try {
			
			NumberSearchTerm nst = new NumberSearchTerm(dx4Services.getDx4Home(),"car crash");
			home.storeNumberSearchTerm(nst);
			List<NumberSearchEntry> numbers = home.getNumbersFormTerm("car crash");
			for (NumberSearchEntry num : numbers)
			{
				Dx4NumberPageElementJson npe = home.getNumberPageElement(num.getNumber(), num.getDictionary());
				log.info(npe);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}
}
