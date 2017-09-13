package org.gz.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.gz.account.GzAccountMgr;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.util.NumberUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


public class GzServices 
{
	private static final Logger log = Logger.getLogger(GzServices.class);	
	private GzHome gzHome;
	private ThreadPoolTaskScheduler scheduler;
	private PlatformTransactionManager transactionManager;
	private GzAccountMgr gzAccountMgr;
	private String gzProperties;
	private Properties properties;
	private Semaphore updateInvoiceSem = new Semaphore(1);
	
	public GzServices()
	{
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("gz async scheduler- ");
		scheduler.initialize();
	}
	
	public void initServices()
	{
		gzAccountMgr.setServices(this);
		gzAccountMgr.setHome(gzHome);
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream(gzProperties));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(5);
		} 
		
//		scheduleCloseOpenInvoices();
	}
	

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	
	public GzHome getGzHome() {
		return gzHome;
	}

	public void setGzHome(GzHome gzHome) {
		this.gzHome = gzHome;
	}

	public ThreadPoolTaskScheduler getScheduler() {
		return scheduler;
	}

	
	@SuppressWarnings("unused")
	private void scheduleCloseOpenInvoices() 
	{	
		gzHome.setCloseInvoiceAfter(Integer.parseInt(gzHome.getCloseInvoiceAfterMins()));
		if (gzHome.getCloseInvoiceAfter() < 0)
			return;
		
		Date startAt = getCloseInvoiceStartAt();
		
		long interval = gzHome.getCloseInvoiceAfter() * 60 * 1000L;
		
		log.info("SCHEDULING CLOSE OPEN INVOICES AT INTERVAL : " + gzHome.getCloseInvoiceAfter() + " MINS " + interval + " MS FROM : " + startAt);
		scheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						
						log.info("Running scheduled event");
						try {
							GregorianCalendar gc = new GregorianCalendar();
							log.info("CLOSING OPEN INVOICES AT : " + gc.getTime());
							closeOpenInvoices();
						} catch (GzPersistenceException e) {
							e.printStackTrace();
							log.error("GzPersistenceException in scheduled CLOSE OPEN INVOICES " + e.getMessage());
						}
					}
				}, startAt, interval);
	}

	public synchronized void closeOpenInvoices() throws GzPersistenceException
	{
		try {
			updateInvoiceSem.acquire();
			gzHome.closeOpenInvoices();
			updateInvoiceSem.release();
		} catch (InterruptedException e1) {
			log.error(e1.getMessage());
			e1.printStackTrace();
		} 
	}
	
	private Date getCloseInvoiceStartAt() {
		String startAt = gzHome.getCloseInvoiceStartAt();
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar now = new GregorianCalendar();
		if (startAt.equals("-1"))
			return now.getTime();
		
		String[] hhmmss = startAt.split(":");
		if (hhmmss.length != 3)
		{
			log.fatal(" Invalid format for persistent property closeInvoiceStartAt :" + startAt + " must be HH:MM:SS");
			System.exit(3);
		}
		try
		{
			int hour = NumberUtil.parseIntInRange(hhmmss[0], 0, 23);
			int min = NumberUtil.parseIntInRange(hhmmss[1], 0, 59);
			int sec = NumberUtil.parseIntInRange(hhmmss[2], 0, 59);
			
			log.info(gc.getTime());
			
			gc.set(Calendar.HOUR_OF_DAY, hour); 
			gc.set(Calendar.MINUTE, min);
			gc.set(Calendar.SECOND, sec);
			
			if (gc.before(now))
				gc.add(Calendar.HOUR, 24);
			return gc.getTime();
		}
		catch (NumberFormatException e)
		{
			log.fatal(" Invalid format for persistent property closeInvoiceStartAt :" + startAt + " must be HH:MM:SS");
			System.exit(3);
		}
		return null;
	}

	
	public GzAccountMgr getGzAccountMgr() {
		return gzAccountMgr;
	}

	public void setGzAccountMgr(GzAccountMgr gzAccountMgr) {
		this.gzAccountMgr = gzAccountMgr;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount, String comment) throws GzPersistenceException {
		gzHome.performWithdrawlDeposit(currAccountUser,dwType,dwAmount);
	}
	
	public synchronized void updateEnabled(final GzBaseUser user,final boolean flag)		 // looks wrong
	{
		log.trace("%%%perform updateEnabled for:" + user.getEmail());
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
		@Override
		protected void doInTransactionWithoutResult(TransactionStatus arg0) {
			try {
				doUpdateEnabled(user,flag);
			} catch (GzPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		});
	}
	
	private void doUpdateEnabled(GzBaseUser user,boolean enable) throws GzPersistenceException
	{
		user.setEnabled(enable);
		getGzHome().updateEnabled(user);
		if (enable == true)
			return;
		getGzHome().getDownstreamForParent(user);
		for (GzBaseUser member : user.getMembers())
		{
			doUpdateEnabled(member,enable);
		}
	}

	public String getGzProperties() {
		return gzProperties;
	}

	public void setGzProperties(String gzProperties) {
		this.gzProperties = gzProperties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	

}
