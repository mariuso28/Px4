package org.gz.test;

import org.apache.log4j.Logger;
import org.gz.account.GzNumberRetainerSet;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNumberRetainers 
{
	private static Logger log = Logger.getLogger(TestNumberRetainers.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		
		GzHome home = gzServices.getGzHome();
		
		
		try {
			GzBaseUser hb = home.getBaseUserByMemberId("0001");
			
			GzNumberRetainerSet nrs = gzServices.getGzAccountMgr().getGzNumberRetainerSet(4, hb);
			
			log.info(nrs);
			
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
