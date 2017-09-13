package org.dx4.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProviderImageImport {

	private static final Logger log = Logger.getLogger(ProviderImageImport.class);
	
	private static byte[] loadImage(String logo) throws IOException
	{
		Path path = Paths.get("/home/pmk/workspace/Dx4/WebContent/img/" + logo + ".png");
		log.info("Loading : " + path);
	    return Files.readAllBytes(path);
	}

	public static void main(String args[]) throws FileNotFoundException
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try
		{
			byte[] image = loadImage("cash");
			dx4Home.updateProviderImage("C", image);
			image = loadImage("88");
			dx4Home.updateProviderImage("8", image);
			image = loadImage("stc");
		dx4Home.updateProviderImage("D", image);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
