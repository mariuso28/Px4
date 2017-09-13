package org.dx4.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4ZodiacJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ZodiacImageImport {

	private static final Logger log = Logger.getLogger(ZodiacImageImport.class);
	
	private static byte[] loadImage(int imgNum,int set) throws IOException
	{
		String image = Integer.toString(imgNum);
		if (image.length()<2)
			image = "0" + image;
		image = "set" + (new Character((char) ('A' + (set-1)))).toString() + "_" + image; 
		
		Path path = Paths.get("/home/pmk/workspace/Dx4/WebContent/img/zodiac/Set" + set + "/" + image + ".png");
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
			for (int set=1; set<=3; set++)
			{
				for (int year = 1; year<=12; year++)
				{
					byte[] image = loadImage(year,set);
					dx4Home.storeZodiacImage(Dx4ZodiacJson.animalNames[year-1], set, year, image);
				}
			}
			
		
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
