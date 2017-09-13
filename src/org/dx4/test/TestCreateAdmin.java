package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.admin.Dx4Admin;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCreateAdmin {

	private static final Logger log = Logger.getLogger(TestCreateAdmin.class);
	private static Dx4Services dx4Services;
	
	private static Dx4Profile createProfile()
	{
		Dx4Profile profile = new Dx4Profile();
		profile.setContact("Mariuso");
		profile.setPhone("O102202005");
		profile.setEmail("");
		profile.setPassword("88888888");
		return profile;
	}
	
	private static Dx4Account createAccount(double balance,double creditLimit,int paymentDays)
	{
		Dx4Account account = new Dx4Account();
		account.setBalance(balance);
		return account;
	}
	
	private static void createUser(Dx4SecureUser user)
	{
		user.setEmail("drpknox@hotmail.com");
		user.copyProfileValues(createProfile());
		
	}
	
	private static Dx4Admin createAdmin()
	{
		Dx4Admin user = new Dx4Admin();
		user.setRole(Dx4Role.ROLE_ADMIN);
		createUser(user);
		user.setAccount(createAccount(0,0,99));
		dx4Services.storeMember(user, null);
		return user;
	}
	
	public static void main(String[] args)
	{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Admin admin = null;
		try
		{
			admin = dx4Services.getDx4Home().getAdminByUsername("drpknox@hotmail.com");
		}
		catch (Exception e)
		{
			System.exit(8);
		}
		
		if (admin != null)
		{
			log.info("Admin : " + admin + " Already exists - deleting");
			dx4Services.deleteMember(admin);
		}
			
		admin = createAdmin();
		
		log.info("Created admin : " + admin);
		
		admin = dx4Services.getDx4Home().getAdminByUsername("drpknox@hotmail.com");
		
		log.info("Retrieved admin : " + admin);
		
	}
}
