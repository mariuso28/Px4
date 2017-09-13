package org.dx4.test.simulate;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.Dx4Comp;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimCreateModelPlayerN{
	private static final Logger log = Logger.getLogger(TestSimCreateModelPlayerN.class);
	
	private static Dx4Services dx4Services;
	
	private static Dx4Profile createProfile(Dx4SecureUser user,int num)
	{
		Dx4Profile profile = new Dx4Profile();
		profile.setContact(user.getRole().getShortCode()+"_"+num);
		profile.setPhone("O12456O" + user.getId());
		profile.setEmail("");
		return profile;
	}
	
	private static Dx4Account createAccount(double balance,double creditLimit,int paymentDays)
	{
		Dx4Account account = new Dx4Account();
		account.setBalance(balance);
		return account;
	}
	
	private static void createUser(Dx4SecureUser user,int useNum)
	{
		user.setEmail(user.getRole().getShortCode()+useNum+"@gmail.com");
		user.copyProfileValues(createProfile(user,useNum));
		user.setPassword("88888888");
	}
	
	private static void createPlayers(int startNum,int endNum,Dx4Agent agent )
	{
		double creditLimit = 500;
		for (int i=startNum; i<=endNum; i++)
		{
			Dx4Player user = new Dx4Player();
			user.setRole(Dx4Role.ROLE_PLAY);
			createUser(user,i);
			user.setAccount(createAccount(0,creditLimit+100,2));
			dx4Services.storeMember( user, agent );
		}
	}
	
	private static void createModel() throws Dx4PersistenceException
	{	
		Dx4Comp comp = (Dx4Comp) dx4Services.getDx4Home().getAgentByUsername("company0@gmail.com");
		createPlayers(2,15,comp);
		log.info("Created model : " + comp);
	}
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		createModel();
	}
}
