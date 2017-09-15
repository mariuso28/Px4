package org.gz.test;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.gz.game.GzGameTypePayouts;
import org.gz.game.GzPackage;
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
		
		try {
			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			GzPackage pkg = new GzPackage();
			pkg.populateDefaults(metaGame);
			
			for (GzGameTypePayouts gtp : pkg.getGameTypePayouts().values())
			{
				log.info(gtp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
