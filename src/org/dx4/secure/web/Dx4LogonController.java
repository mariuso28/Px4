package org.dx4.secure.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/logon")

public class Dx4LogonController {

	private static Logger log = Logger.getLogger(Dx4LogonController.class);	
	@Autowired
	private Dx4Services dx4Services;
	
	
	@RequestMapping(value = "/access_denied", method = RequestMethod.GET)
	public String accessDenied(ModelMap model) {
		
		log.info("Received request to accessDenied");
			
		model.addAttribute("errMsg","Access Denied");
		
		return "Dx4Error";
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signin(ModelMap model) {
		
		log.info("Received request to signin XXX");
			
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", "");
		logon.put("infoMsg", "");
		logon.put("email", "drpknox@hotmail.com");
		
		return new ModelAndView("logon","logon", logon);
	}
	
	public ModelAndView signin(ModelMap model,String email,String infoMsg,String errMsg) {
		
		log.info("Received request to signin");
			
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", errMsg);
		logon.put("infoMsg", infoMsg);
		logon.put("email", "");
		
		return new ModelAndView("logon","logon", logon);
	}
	
	@RequestMapping(value = "/signin", params="error", method = RequestMethod.GET)
	public ModelAndView signinFail(String message,HttpServletRequest req) {
		log.info("Received request to signin - error");
		return goSignin(message,req);
	}
	
	@RequestMapping(value = "/password", params="user", method = RequestMethod.GET)
	public ModelAndView password(String email,ModelMap model) {
		
		String errMsg = dx4Services.tryResetPassword(email);
		if (!errMsg.isEmpty())
		{
			log.error("error trying reset password : " + errMsg);
			return signin(model,email,"",errMsg);
		}
		
		String infoMsg = "Your password has been reset, please check your inbox and login to change.";
		return signin(model,email,infoMsg,"");
	}
	
	private ModelAndView goSignin(String errMsg,HttpServletRequest req){
		String defaultUser = "";
		if (req!=null)
		{
			if (req.getParameter("email")!=null)
				defaultUser = req.getParameter("email");
			else
			if (req.getUserPrincipal()!=null && req.getUserPrincipal().getName()!=null)
				defaultUser = req.getUserPrincipal().getName();
		}
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", errMsg);
		logon.put("infoMsg", "");
		logon.put("email", defaultUser);
		return new ModelAndView("logon","logon", logon);
	}
	
	@RequestMapping(value = "/errStackDump", method = RequestMethod.GET)
	public Object errStackDump() {
		log.info("Received request to errStackDump");
		return "GzError";
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}
	
	/*
	@ResponseBody
	@RequestMapping(value = "/storeImage", method = RequestMethod.POST)
	public Object storeImage(@RequestParam("userId") String uid, @RequestParam("image") MultipartFile image) {
		
		log.info("Received request to storeImage");
		
		UUID userId = UUID.fromString(uid);
		
		String email;
		try {
			email = dx4Services.getDx4Home().getEmailForId(userId);
			dx4Services.getDx4Home().storeImage(email,image,image.getContentType());
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			log.info("Could not store image for : " + uid);
			return "Failed";
		}
		
		return "Success";
	}
	
	@RequestMapping(value = "/getImage", method = RequestMethod.GET)
	public void getImage(@RequestParam("userId") String uid,HttpServletResponse response) 
	{	
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(response.getOutputStream(), 1024);
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Could not get image for : " + uid);
			return;
		}
		try {
			byte[] byteToWrite = dx4Services.getDx4Home().getImage(uid);
			if (byteToWrite==null)
			{
				log.info("Image for : " + uid + " Not available. Getting default.");
				byteToWrite = dx4Services.getDx4Home().getImage("noprofile@zzzz");
			}
			if (byteToWrite!=null)
			{
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				out.write(byteToWrite);
				out.flush();
			}
			else
				log.error("Image for : " + uid + " or default could not be retrieved.");
		} catch (GzPersistenceException | IOException e) {
			log.info("Could not get image for : " + uid);
			return;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			log.info("Could not get image for : " + uid);
			return;
		}
	}
	*/
	
	@RequestMapping(value = "/getApk", method = RequestMethod.GET)
	public void getApk(HttpServletResponse response) 
	{	
		response.setHeader("Content-Disposition", "attachment; filename=update.apk");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
		response.setContentType("application/vnd.android.package-archive");
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(response.getOutputStream(), 1024);
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Could not get apk");
			return;
		}
		try {
			byte[] byteToWrite = dx4Services.getDx4Home().getVersion().getApk();
			if (byteToWrite!=null)
			{
				response.setContentLength(byteToWrite.length);
				out.write(byteToWrite);
				out.flush();
			}
			else
				log.error("apk could not be retrieved.");
		} catch (Exception e) {
			log.info("Could not get apk ");
			return;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			log.info("Could not get apk");
			return;
		}
	}
}
