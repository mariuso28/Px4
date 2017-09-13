package org.dx4.test.simulate;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.Dx4Comp;
import org.dx4.agent.Dx4MA;
import org.dx4.agent.Dx4SMA;
import org.dx4.agent.Dx4ZMA;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimulateCreateModel{
	private static final Logger log = Logger.getLogger(TestSimulateCreateModel.class);
	private static int compNum;
	private static int zmaNum;
	private static int smaNum;
	private static int maNum;
	private static int agentNum;
	private static int playerNum;
	
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
	
	private static Dx4Comp createComp()
	{
		Dx4Comp user = new Dx4Comp();
		user.setRole(Dx4Role.ROLE_COMP);
		createUser(user,compNum++);
		user.setAccount(createAccount(0,5000000,99));
		Dx4Admin admin = dx4Services.getDx4Home().getAdminByUsername("drpknox@hotmail.com");
		dx4Services.storeMember( user, admin );
		createZmas(2,user);
		return user;
	}
	
	private static void createZmas(int num,Dx4Comp comp)
	{
		double creditLimit = 1000000;
		for (int i=0; i<num; i++)
		{
			Dx4ZMA user = new Dx4ZMA();
			user.setRole(Dx4Role.ROLE_ZMA);
			createUser(user,zmaNum++);
			user.setAccount(createAccount(0,creditLimit*i,2));
			dx4Services.storeMember( user, comp );
			createSmas(3,user);
		}
	}

	private static void createSmas(int num,Dx4ZMA zma)
	{
		double creditLimit = 100000;
		for (int i=1; i<=num; i++)
		{
			Dx4SMA user = new Dx4SMA();
			user.setRole(Dx4Role.ROLE_SMA);
			createUser(user,smaNum++);
			user.setAccount(createAccount(0,creditLimit*i,2));
			dx4Services.storeMember( user, zma );
			createMas(3,user);
		}
	}
	
	private static void createMas(int num,Dx4SMA sma)
	{
		double creditLimit = 10000;
		for (int i=1; i<=num; i++)
		{
			Dx4MA user = new Dx4MA();
			user.setRole(Dx4Role.ROLE_MA);
			createUser(user,maNum++);
			user.setAccount(createAccount(0,creditLimit*i,2));
			dx4Services.storeMember( user, sma );
			createAgents(3,user);
		}
	}
	
	private static void createAgents(int num,Dx4MA ma )
	{
		double creditLimit = 1000;
		for (int i=1; i<=num; i++)
		{
			Dx4Agent user = new Dx4Agent();
			user.setRole(Dx4Role.ROLE_AGENT);
			createUser(user,agentNum++);
			user.setAccount(createAccount(0,creditLimit*i,2));
			dx4Services.storeMember( user, ma );
			createPlayers(5,user );
		}
	}
	
	private static void createPlayers(int num,Dx4Agent agent )
	{
		double creditLimit = 100;
		for (int i=1; i<=num; i++)
		{
			Dx4Player user = new Dx4Player();
			user.setRole(Dx4Role.ROLE_PLAY);
			createUser(user,playerNum++);
			user.setAccount(createAccount(0,creditLimit+100,2));
			dx4Services.storeMember( user, agent );
		}
	}
	
	private static Dx4Admin createAdmin()
	{
		Dx4Admin admin = new Dx4Admin("dx4Admin@hotmail.com");
		admin.setRole(Dx4Role.ROLE_ADMIN);
		dx4Services.storeMember(admin,null);
		return admin;
	}
	
	@SuppressWarnings("unused")
	private static void createModel()
	{	
		Dx4Comp comp = createComp();
		createZmas(2,comp);
		log.info("Created comp : " + comp);
	}
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Admin admin = createAdmin();
		log.info("Created admin : " + admin);
		// createModel();
	}
}
