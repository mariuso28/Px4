package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDeleteAllMembers {

	private static final Logger log = Logger.getLogger(TestDeleteAllMembers.class);
	private static Dx4Services dx4Services;
	
	public static void main(String[] args)
	{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Admin admin = dx4Services.getDx4Home().getAdminByUsername("drpknox@hotmail.com");
		
		dx4Services.deleteMember(admin);
		
		log.info("DELETED");
		
	}
}
