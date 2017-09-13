package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
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
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home home = dx4Services.getDx4Home();
	
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String encoded = encoder.encode("88888888");
		
		home.setDefaultPasswordForAll(encoded);
		
		try {
			Dx4SecureUser hb = home.getByUsername("drpknox@hotmail.com", Dx4SecureUser.class);
			log.info("matches : " + encoder.matches("88888888", hb.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
