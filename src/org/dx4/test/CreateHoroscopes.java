package org.dx4.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CreateHoroscopes {

	private static final Logger log = Logger.getLogger(CreateHoroscopes.class);
	
	private static byte[] loadImage(String img) throws IOException
	{
		Path path = Paths.get("/home/pmk/4DX/horoscope-images/"+img);
		log.info("Loading : " + path);
	    return Files.readAllBytes(path);
	}
	
	private static void createHoroscope(Dx4Home dx4Home,String sign,int month,int sm,int sd,int em,int ed,String img,String[] nums) throws IOException
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(Calendar.MONTH, sm-1);
		gc.set(Calendar.DAY_OF_MONTH, sd);
		Date startDate = gc.getTime();
		
		gc.clear();
		gc.set(Calendar.MONTH, em-1);
		gc.set(Calendar.DAY_OF_MONTH, ed);
		if (em==1)
			gc.add(Calendar.YEAR, 1);
		Date endDate = gc.getTime();
		
		Dx4HoroscopeJson hj = new Dx4HoroscopeJson();
		hj.setMonth(month);
		hj.setSign(sign);
		hj.setStartDate(startDate);
		hj.setEndDate(endDate);
		List<String> numbers = new ArrayList<String>();
		for (String n : nums)
		{
			if (!numbers.contains(n))
				numbers.add(n);
		}
		hj.setNumbers(numbers);
		
		byte[] image = loadImage(img);
		dx4Home.storeHoroscope(hj,image);
	}

	private static void createHoroscopes(Dx4Home dx4Home) throws IOException
	{
		createHoroscope(dx4Home,"Aries",3,3,21,4,19,"horoscope01.png",new String[]{ "0567" });
		createHoroscope(dx4Home,"Taurus",4,4,20,5,20,"horoscope02.png",new String[]{ "0573" });
		createHoroscope(dx4Home,"Gemini",5,5,21,6,20,"horoscope03.png",new String[]{ "2886" });
		createHoroscope(dx4Home,"Cancer",6,6,21,7,22,"horoscope04.png",new String[]{ "0654" });
		createHoroscope(dx4Home,"Leo",7,7,23,8,22,"horoscope05.png",new String[]{ "0513" });
		createHoroscope(dx4Home,"Virgo",8,8,23,9,22,"horoscope06.png",new String[]{ "2892" });
		
		createHoroscope(dx4Home,"Libra",9,9,23,10,122,"horoscope07.png",new String[]{ "1067" });
		createHoroscope(dx4Home,"Scorpio",10,10,23,11,21,"horoscope08.png",new String[]{ "0264" });
		createHoroscope(dx4Home,"Sagittarius",11,11,22,12,21,"horoscope09.png",new String[]{ "5239" });
		createHoroscope(dx4Home,"Capricorn",12,12,22,1,19,"horoscope10.png",new String[]{ "6200" });
		createHoroscope(dx4Home,"Aquarius",1,1,20,2,18,"horoscope11.png",new String[]{ "1879" });
		createHoroscope(dx4Home,"Pisces",2,2,19,2,20,"horoscope12.png",new String[]{ "0201" });
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
			createHoroscopes(dx4Home);
			List<Dx4HoroscopeJson> hjs = dx4Home.getHoroscopes();
			for (Dx4HoroscopeJson h : hjs)
				log.info(h);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
