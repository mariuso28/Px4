package org.dx4.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.secure.domain.Dx4SecureUser;
 
public class Dx4SessionFilter implements Filter {
 
	private static final Logger log = Logger.getLogger(Dx4SessionFilter.class);
	private String redirectPath = Dx4Config.getProperties().getProperty("dx4.redirectPath", "http://localhost:8780/dx4/invalid.html");
	
	   
    private ArrayList<String> urlList;
     
    public void destroy() {
    }
 
    public synchronized void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
    	
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getServletPath();
        
        if (!url.endsWith(".html"))				// images etc
        {
        	  chain.doFilter(req, res);			
        	  return;
        }
        
        String queryString = request.getQueryString();
        if (queryString==null)
        	log.info("Testing url: " + url + "\n against list : " + urlList);
        else
        	log.info("Testing url: " + url + "?" + queryString + "\n against list : " + urlList);
        
        boolean allowedRequest = false;
         
        if(urlList.contains(url)) {			
            allowedRequest = true;
        }

        
        log.info("allowedRequest is : " + allowedRequest);
       
        HttpSession session;
        
    	try {
			session = request.getSession(false);
			if (session!=null)
				log.info("session : " + session.getId());
		} catch(IllegalStateException ex)
		{
			log.error("Invalid session");
    		log.info("Invalid session redirecting to : " + redirectPath);
            response.sendRedirect(redirectPath);
            return;
    	}
        
		if (urlList.contains(url))
		{
			;
		}
		else
        if (!allowedRequest)
        {
        	if (session==null)
        	{
	        	log.info("Invalid session redirecting to : " + redirectPath);
	        	session = request.getSession(true);
	            response.sendRedirect(redirectPath);
	            return;
        	}
        	else
        	{
        		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");
        		if (currUser == null)
        		{
        			log.info("no current user in session redirecting to : " + redirectPath);
                	response.sendRedirect(redirectPath);
                    return;
        		}
        	}
        }
        
		log.info("setting no cache for : " + url);
		response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
		response.setHeader("Last-Modified", new Date().toString());
	    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
	    response.setHeader("Pragma", "no-cache");
	    
        log.info("continuing with filter");
        log.info("req : " + req);
        log.info("res : " + res);
        
        chain.doFilter(req, res);
    }
 
    public void init(FilterConfig config) throws ServletException {
        String urls = config.getInitParameter("avoid-urls");
        StringTokenizer token = new StringTokenizer(urls, ",");
        
        log.info("Dx4SessionFilter  : " + urls);
       
        urlList = new ArrayList<String>();
 
        while (token.hasMoreTokens()) {
            urlList.add(token.nextToken());
 
        }
    }
}
