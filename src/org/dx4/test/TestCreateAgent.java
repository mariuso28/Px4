package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Comp;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCreateAgent {

	private static final Logger log = Logger.getLogger(TestCreateAgent.class);
	private static Dx4Home dx4Home;
	private static Dx4Services dx4Services;
	
	private static Dx4Profile createProfile()
	{
		Dx4Profile profile = new Dx4Profile();
		profile.setContact("New Comp");
		profile.setPhone("O12456O1");
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
		user.setEmail("newcomp@gmail.com");
		user.copyProfileValues(createProfile());
	}
	
	private static Dx4Comp createComp()
	{
		Dx4Comp user = new Dx4Comp();
		user.setRole(Dx4Role.ROLE_COMP);
		createUser(user);
		user.setAccount(createAccount(0,5000000,99));
		Dx4Admin admin = dx4Home.getAdminByUsername("drpknox@hotmail.com");
		dx4Services.storeMember(user, admin);
		return user;
	}
	
	public static void main(String[] args)
	{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		dx4Services = (Dx4Services) context.getBean("dx4Services");
		dx4Home = dx4Services.getDx4Home();
		Dx4Comp comp = createComp();
		
		log.info("Created comp : " + comp);
	}
}
