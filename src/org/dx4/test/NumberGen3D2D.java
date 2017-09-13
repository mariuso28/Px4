package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberGen3D2D {

	private static final Logger log = Logger.getLogger(NumberGen3D2D.class);
	
	public static void main(String args[]) 
	{
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home home = dx4Services.getDx4Home();
		
		for (int i=0; i<1000; i++)
		{
			String num = Integer.toString(i);
			while (num.length()<3)
				num = "0" + num;
			log.info(num);
			
			home.addNumber4D(num);
		}
		
		for (int i=0; i<100; i++)
		{
			String num = Integer.toString(i);
			while (num.length()<2)
				num = "0" + num;
			log.info(num);
			
			home.addNumber4D(num);
		}
	}
}
