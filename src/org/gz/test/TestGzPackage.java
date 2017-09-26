package org.gz.test;

import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.gz.baseuser.GzBaseUser;
import org.gz.game.GzGameTypePayouts;
import org.gz.game.GzGroup;
import org.gz.game.GzPackage;
import org.gz.home.GzHome;
import org.gz.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGzPackage 
{
	private static Logger log = Logger.getLogger(TestGzPackage.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Px4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();
		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome gzHome = gzServices.getGzHome();
		
		try {
/*			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			
			GzBaseUser bu = gzHome.getBaseUserByMemberId("0001");
			GzGroup group = new GzGroup("Group A","Cats and Dogs",bu);
			
			gzHome.storeGroup(group);
			
			
			GzPackage pkg = new GzPackage("MyPackage");
			
			pkg.populateDefaults(metaGame);
			log.info("Before: " + pkg);
			for (GzGameTypePayouts gtp : pkg.getGameTypePayouts().values())
			{
				log.info(gtp);
			}
		
			
			bu = gzHome.getBaseUserByMemberId("0002");
			
			pkg.setMember(bu);
			
			gzHome.storePackage(pkg);
			
			gzHome.addPackageToGroup(group,pkg);
*/			
			Map<String,GzGroup> grps = gzHome.getGroups("0001");
			for (GzGroup grp : grps.values())
				log.info(grp);
			
			/*
			pkg = gzHome.getPackageById(pkg.getId());
			log.info("After: " + pkg);
			for (GzGameTypePayouts gtp : pkg.getGameTypePayouts().values())
			{
				log.info(gtp);
			}
			*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
