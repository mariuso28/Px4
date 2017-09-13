package org.gz.test;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestSetBaseUserPassword 
{
	private static Logger log = Logger.getLogger(TestSetBaseUserPassword.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		
		GzHome home = gzServices.getGzHome();
		if (args.length > 0)
		{
			log.info(("Running with datasource: " + args[0]));
			home.overrideDataSourceUrl(args[0]);
		}
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String encoded = encoder.encode("88888888");
		
		home.setDefaultPasswordForAll(encoded);
		
		try {
			GzBaseUser hb = home.getBaseUserByMemberId("0001");
			log.info("matches : " + encoder.matches("88888888", hb.getPassword()));
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("All passwords successfully reset to 88888888");
	}
}
