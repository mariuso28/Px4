package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberGen4D {

	private static final Logger log = Logger.getLogger(NumberGen4D.class);
	
	public static void main(String args[]) 
	{
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home home = dx4Services.getDx4Home();
		
		for (int i=0; i<10000; i++)
		{
			String num = Integer.toString(i);
			while (num.length()<4)
				num = "0" + num;
			log.info(num);
			
			home.addNumber4D(num);
		}
		
	}
}
