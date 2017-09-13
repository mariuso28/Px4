package org.gz.test;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.gz.admin.GzAdmin;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;

public class TestCreateModel {

private static Logger log = Logger.getLogger(TestCreateModel.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome home = gzServices.getGzHome();
		
		GzAdmin admin = new GzAdmin("gzadmin@test.com");
		admin.setContact("danny");
		admin.setPhone("01238625");
		PasswordEncoder encoder = new BCryptPasswordEncoder();	
		String encoded = encoder.encode("88888888");
		admin.setPassword(encoded);
		
		try {		
			home.storeBaseUser(admin);
			GzBaseUser bu = home.getBaseUserByEmail("gzadmin@test.com");
			log.info(bu.getEmail() + " : " + bu.getContact() + " : " + bu.getMemberId());
			bu = home.getBaseUserByMemberId(bu.getMemberId());
			log.info(bu.getEmail() + " : " + bu.getContact() + " : " + bu.getMemberId());
			
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
