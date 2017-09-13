package org.dx4.server;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dx4ServerBeanImpl implements Dx4ServerBean{
	
private static final Logger log = Logger.getLogger(Dx4ServerBeanImpl.class);
	
	public Dx4ServerBeanImpl(Dx4Services dx4Services)
	{
		@SuppressWarnings("unchecked")
		Enumeration<Appender> apps = log.getParent().getAllAppenders();
		while (apps.hasMoreElements())
		{
			Appender app = apps.nextElement();
			System.out.print( "Logging to appender : " + app.getName() + " class : " + app.toString());
			try
			{
				RollingFileAppender rfa = (RollingFileAppender) app;
				System.out.println("Path : " + rfa.getFile() + "    Max size : " + rfa.getMaximumFileSize());
			}catch (ClassCastException e)
			{
				System.out.println();
			}
		}
		
		@SuppressWarnings("unused")
		Dx4Server dx4s = new Dx4Server( 4096 );
		
//		TestGames.testGames(dx4s.getDx4Services().getDx4Home());
	}
	
	public static void main(String args[])						// test only
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext( "Dx4-service.xml" );
		context.getBean("dx4ServerBean");
		
	}

}
