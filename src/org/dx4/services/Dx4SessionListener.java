package org.dx4.services;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class Dx4SessionListener implements HttpSessionListener {
	
	private final static ConcurrentHashMap<Long, HttpSession> userSessions = new ConcurrentHashMap<Long, HttpSession>();
	private final static ConcurrentHashMap<String, Long> userIds = new ConcurrentHashMap<String, Long>();
	
	
	private static final Logger log = Logger.getLogger(Dx4SessionListener.class);
    private static int sessionCount = 0;
 
    private static void printSessions()
    {
    	
    	log.info("##############");
    	log.info("USER SESSIONS : SESSION COUNT : " + sessionCount);
    	for (Long id : userSessions.keySet())
    	{
    		HttpSession sess = userSessions.get(id);
    		log.info(id + " : " + sess.getId());
    	}
    	log.info("USERID SESSIONS :");
    	for (String sessionId : userIds.keySet())
    	{
    		Long userId = userIds.get(sessionId);
    		log.info(sessionId + " : " + userId);;
    	}
    	log.info("##############");
    }
    
    
    public void sessionCreated(HttpSessionEvent event) {
        synchronized (this) {
    //    	Exception e = new Exception("DIAG STACK");
    //    	e.printStackTrace();
        	
        	userIds.put(event.getSession().getId(),-1L);
            sessionCount++;
        }
 
        log.info("%%% Session Created: " + event.getSession().getId());
        log.info("%%% Total Sessions: " + sessionCount);
        printSessions();
    }
 
    public void sessionDestroyed(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount--;
            freeUserSession(event.getSession());
        }
        
        log.info("%%% Session Destroyed: " + event.getSession().getId());
        log.info("%%% Total Sessions: " + sessionCount);
        printSessions();
    }
    
    private static synchronized void freeUserSession(HttpSession session)
    {
    	log.info("freeing session : " + session.getId());
    	Long userId = userIds.remove(session.getId());
		userSessions.remove(userId);
		printSessions();
    }

	public static synchronized boolean registerUserSession(Long userId,HttpServletRequest request) {
		if (userId==null)
			return false;
		
		HttpSession session;					// check already invalid
		try {
			session = request.getSession(false);
		} catch(IllegalStateException ex) {
			log.info("!! IllegalStateException getting session for request:" + request.toString());
			return false;
		}
		
		Long existId = null;
		if (session!=null)
			existId = userIds.get(session.getId());
		if (existId==null)
		{
			log.error("Session : " + session.getId() + " NOT EXISTS IN USERIDS - CANNOT HAPPEN");
			return false;
		}
		
		log.info("attempting to register session : " + session.getId() + " for userId: " + userId);
		HttpSession exist = userSessions.get(userId);
		if (exist != null)
		{
			log.info("!! invalidating existing session : " + exist.getId() + " to kick off user : " + userId);
			try {
				exist.invalidate();					// kick off existing user
				session = request.getSession(true);		// get new session
			} catch(IllegalStateException ex) {
				;
			}
		}
		else
		{
			Long existingUser = userIds.get(session.getId());
			if (existingUser!=-1L)
			{
				log.info("!! session : " + session.getId() + " has existing user : " + existingUser + " invalidating");
				
				session.invalidate();		// can't have same session on server and can't replace
				session = request.getSession(true);
				printSessions();
				// return false;
			}
		}
		
		log.info("registering session : " + session.getId() + " for userId: " + userId);
		userIds.put(session.getId(),userId);
		userSessions.put(userId,session); 
		printSessions();
		return true;
	}
}
