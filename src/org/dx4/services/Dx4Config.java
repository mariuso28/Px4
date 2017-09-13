package org.dx4.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Dx4Config {
	private static final Logger log = Logger.getLogger(Dx4Config.class);
	private static Properties properties;

	public static void init(String propertiesFile)
	{
		loadProperties(propertiesFile);
	}
	
	private static void loadProperties(String propertiesFile) {
		log.info("Loading properties from : " + propertiesFile);
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(5);
		} 
		for (Object key : properties.keySet().toArray())
		{
			log.info("Loaded property : " + (String) key + " = " + properties.getProperty((String) key));
		}
	}

	public static Date getDrawTime()
	{
		String drawTime = Dx4Config.getProperties().getProperty("dx4.drawTime","17:00");
		
		int hour = 17;
		int min = 0;
		try
		{
			String[] toks = drawTime.split(":");
			hour = Integer.parseInt(toks[0]);
			min = Integer.parseInt(toks[1]);
		}
		catch (Exception e)
		{
			log.error("Couldn't parse draw time:" + drawTime + " using default 17:00");
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, hour);
		gc.set(Calendar.MINUTE, min);
		gc.set(Calendar.SECOND, 0);
		return gc.getTime();
	}

	public static Date getActualDrawTime()
	{
		String actualDrawTime = Dx4Config.getProperties().getProperty("dx4.actualDrawTime","19:00");
		
		int hour = 19;
		int min = 0;
		try
		{
			String[] toks = actualDrawTime.split(":");
			hour = Integer.parseInt(toks[0]);
			min = Integer.parseInt(toks[1]);
		}
		catch (Exception e)
		{
			log.error("Couldn't parse draw time:" + actualDrawTime + " using default 19:00");
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, hour);
		gc.set(Calendar.MINUTE, min);
		gc.set(Calendar.SECOND, 0);
		return gc.getTime();
	}
	
	public static Properties getProperties() {
		return properties;
	}
		
}

