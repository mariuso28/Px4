package org.dx4.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4GameGroupJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MetaGameImageImport {

	private static final Logger log = Logger.getLogger(MetaGameImageImport.class);
	
	private static byte[] loadImage(String logo) throws IOException
	{
		Path path = Paths.get("/home/pmk/workspace/Dx4/WebContent/img/" + logo + "_sml.png");
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
			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			
			byte[] image = loadImage("SMALL");
			dx4Home.storeMetaGameImage(metaGame.getId(), "SMALL", image);
			image = loadImage("BIG");
			dx4Home.storeMetaGameImage(metaGame.getId(), "BIG", image);
			
			image = loadImage("ABA");
			dx4Home.storeMetaGameImage(metaGame.getId(), "ABA", image);
			image = loadImage("ABCA");
			dx4Home.storeMetaGameImage(metaGame.getId(), "ABCA", image);
			image = loadImage("ABCC");
			dx4Home.storeMetaGameImage(metaGame.getId(), "ABCC", image);
			
			image = loadImage("4D");
			dx4Home.storeMetaGameImage(metaGame.getId(), Dx4GameGroupJson.D4.name(), image);
			image = loadImage("3D");
			dx4Home.storeMetaGameImage(metaGame.getId(), Dx4GameGroupJson.ABC.name(), image);
			image = loadImage("iBox");
			dx4Home.storeMetaGameImage(metaGame.getId(), Dx4GameGroupJson.IBox.name(), image);
			image = loadImage("box");
			dx4Home.storeMetaGameImage(metaGame.getId(), Dx4GameGroupJson.Box.name(), image);
			image = loadImage("2D");
			dx4Home.storeMetaGameImage(metaGame.getId(), Dx4GameGroupJson.D2.name(), image);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
