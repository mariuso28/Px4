package org.dx4.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberImageImport {

	private static final Logger log = Logger.getLogger(NumberImageImport.class);
	
	private static byte[] loadImage(String number) throws IOException
	{
		Path path = Paths.get("/home/pmk/4DX/img/" + number + ".png");
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
		
		List<Dx4NumberPageElementJson> nums = dx4Home.getNumberPageElements();
		
		for (Dx4NumberPageElementJson num : nums)
		{ 
			try {
				byte[] img = loadImage(num.getToken());
				dx4Home.updateImage(num.getToken(), img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
