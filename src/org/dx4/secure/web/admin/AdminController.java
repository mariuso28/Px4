package org.dx4.secure.web.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.admin.Dx4Version;
import org.dx4.extend.CustomDateEditorNullBadValues;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.agent.AgentController;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;
import org.dx4.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@SessionAttributes({"currUser","currActiveGame"})

@RequestMapping(value = "/adm")
public class AdminController {
	private static final Logger log = Logger.getLogger(AdminController.class);
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
 
		binder.registerCustomEditor(Date.class, new CustomDateEditorNullBadValues(dateFormat, true));
	}
	
	private String getLogonPath()
	{
		String logonPath = Dx4Config.getProperties().getProperty("dx4.adminLogonPath");
		if (logonPath==null)
		{
			logonPath = Dx4Config.getProperties().getProperty("dx4.tomcat.root","dx4.linkpc.net:8090/dx4") + "/admin.html";
		}
		return logonPath;
	}
	
	@RequestMapping(value = "/goAdminHome", method = RequestMethod.GET)
	public Object goAdminHome( ModelMap model,
			HttpServletRequest req) {
		Dx4Admin admin;
		String username = "";
		
		if (req.getParameter("username")!=null)
			username = req.getParameter("username");
		else
		if (req.getUserPrincipal()!=null && req.getUserPrincipal().getName()!=null)
			username = req.getUserPrincipal().getName();
		
    	try
    	{
    		admin = dx4Services.getDx4Home().getAdminByUsername(username);
    	}
    	catch (PersistenceRuntimeException e)
		{
			log.trace("Admin : " + username + " Not found.." + e.getMessage());
			String logonPath = getLogonPath();
			return "redirect:" + logonPath;
		}
    	
		log.trace("Admin : " + username + " found..");
		
		model.addAttribute("currUser",admin);
		
		Dx4Admin currUser = (Dx4Admin) model.get("currUser");
		HttpSession session = req.getSession();						// have to set in the session as end up in 
																		// other controllers
		log.trace("Setting session attribute : sCurrUser : " +  currUser );
		session.setAttribute("sCurrUser", currUser);		
		
		if (AgentController.setActiveGame(currUser,model,req)==false)
		{
			String msg = "No Active Games for : " + username + " try later..";
			log.error(msg);
			String logonPath = getLogonPath();
			return "redirect:" + logonPath + "?error&message=" + msg.replace(" ","%20");
		}
		
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
		
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}

	// <td><a href="processAdmin.html?deleteMember&code=c0">DELETE</a></td>
	@RequestMapping(value = "/processAdmin", params = "deleteMember", method = RequestMethod.GET)
    public Object deleteMember(String code,ModelMap model,HttpServletRequest request)
	{
		Dx4SecureUser user;
		try {
			user = dx4Services.getDx4Home().getUserByCode(code);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal("Fatal - " + e.getMessage());
		}
		dx4Services.deleteMember(user);
		return "redirect:logon.html";
	}
	
	@RequestMapping(value = "/processAdmin", params = "return_admin", method = RequestMethod.GET)
    public ModelAndView returnAdmin(ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);				// returning from other controller
		Dx4Admin admin = (Dx4Admin) session.getAttribute("sCurrUser");
		model.addAttribute("currUser",admin);	
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}
	
	@RequestMapping(value = "/processAdmin", params = "return_admin1", method = RequestMethod.GET)
    public ModelAndView returnAdmin1(String message,ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession();				// returning from other controller
		Dx4Admin admin = (Dx4Admin) session.getAttribute("sCurrUser");
		model.addAttribute("currUser",admin);	
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
		adminDetailsForm.setMessage(message);
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}
	
	@RequestMapping(value = "/processAdmin", params = "edit_admin", method = RequestMethod.GET)
    public ModelAndView edit(@ModelAttribute("adminDetailsForm") AdminDetailsForm adminDetailsForm,ModelMap model)
	{
		Dx4Admin admin = (Dx4Admin) model.get("currUser");
		adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
		return new ModelAndView("adminEdit" , "adminDetailsForm", adminDetailsForm);
	}
	
	@RequestMapping(value = "/processAdmin", params = "save_admin", method = RequestMethod.POST)
    public ModelAndView saveProfile(@ModelAttribute("adminDetailsForm") AdminDetailsForm adminDetailsForm,ModelMap model)
	{
		log.trace(this.getClass().getSimpleName() + "::saveProfile - Attempting to save");
		Dx4Admin admin = (Dx4Admin) model.get("currUser");
		try
		{
			 adminDetailsForm.validate(false);
		}
		catch (Dx4FormValidationException e)
		{
			log.trace(this.getClass().getSimpleName() + "::saveProfile - Edit Profile Failed : " + e.getMessage());
			adminDetailsForm = new AdminDetailsForm(adminDetailsForm.getNewProfile());
			adminDetailsForm.setMessage("Edit Profile Failed - " + e.getMessage());
			return new ModelAndView("adminEdit" , "adminDetailsForm", adminDetailsForm);
		}
		
		adminDetailsForm.getNewProfile().setPassword(adminDetailsForm.getPassword());
		admin.copyProfileValues(adminDetailsForm.getNewProfile());
		try {
			dx4Services.getDx4Home().updateUserProfile(admin);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		
		admin = dx4Services.getDx4Home().getAdminByUsername(admin.getEmail());			// refresh
		model.addAttribute("currUser",admin);
		
		log.trace(this.getClass().getSimpleName() + "::saveProfile -profile saved : " + admin.getEmail() );
		adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
		
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}
	
	@RequestMapping(value = "/processAdmin", params = "cancel_admin", method = RequestMethod.GET)
    public ModelAndView cancel(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing might comes from AgentController
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(currUser,dx4Services.getDx4Home());      
		
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}
	
	@RequestMapping(value = "/processAdmin", params = "viewGames", method = RequestMethod.GET)
    public ModelAndView viewGames(ModelMap model,HttpServletRequest request) {
       		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		HttpSession session = request.getSession();					// have to set in the session as the next 
																	// request will end up in the agentController
		log.trace("Setting session attribute : sCurrUser : " +  currUser );
		session.setAttribute("sCurrUser",currUser);		
		
		AdminCommandForm adminCommandForm = new AdminCommandForm(dx4Services.getDx4Home(),currUser);
		log.trace(adminCommandForm);
        return new ModelAndView("adminCommand" , "adminCommandForm", adminCommandForm);
    }
	
	@RequestMapping(value = "/processAdmin", params = "createHedgeBets", method = RequestMethod.GET)
    public ModelAndView createHedgeBets(ModelMap model,HttpServletRequest request) {
       		
		Dx4Admin currUser = (Dx4Admin) model.get("currUser");
	
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(currUser,dx4Services.getDx4Home());        
		if (!dx4Services.inHedgeWindow())
		{
			adminDetailsForm.setMessage("Currently no draws in hedge window. Please try later.");
	        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
		}
		
		try
		{
			dx4Services.handleHedgeBets();
		}
		catch (Exception e)
		{
			adminDetailsForm.setMessage("Error attempting to create hedge bets. Please try later.");
	        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
		}
		
		adminDetailsForm.setInfoMessage("Hedge bets successfully created and emailed.");
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
    }
	
	@RequestMapping(value = "/processAdmin", params = "updateExternalResults", method = RequestMethod.GET)
    public ModelAndView updateExternalResults(ModelMap model) {
       			
		dx4Services.getExternalService().updateExternalGameResults();
		
		dx4Services.updatePlayGames();
		
		Dx4Admin admin = (Dx4Admin) model.get("currUser");
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());      
		
		adminDetailsForm.setInfoMessage("Draws Up to Date");
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
    }
	
	@RequestMapping(value = "/processAdmin", params = "submitNewDraw", method = RequestMethod.POST)
    public ModelAndView submitNewDraw(@ModelAttribute("adminDetailsForm") AdminDetailsForm adminDetailsForm,ModelMap model) {
       			
		log.info("In submitNewDraw");
		
		AdminDetailsCommand command =  adminDetailsForm.getCommand();
		
		int index = command.getChangedIndex();
		String drawDate = command.getDrawDate().get(index);
		String gameName = command.getGamename().get(index);
		log.info("Got new draw for : " + gameName + " date : " + drawDate);
		
		String errMsg = "";
		Date playDate = Validator.getValidDate(drawDate,"yyyy-MM-dd");
		if (playDate!=null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			Date now = gc.getTime();
			log.info("Now : " + now + " playDate : " + playDate);
			if (now.after(playDate))
			{
				errMsg = "Could not schedule draw date : " + drawDate + " for : " + gameName + " reason : date before today";
				log.error(errMsg);
			}
			else
			try
			{
				Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(gameName);
				Dx4PlayGame playGame = new Dx4PlayGame();
				playGame.setPlayDate(playDate);
				dx4Services.getDx4Home().insertPlayGame(metaGame,playGame);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				errMsg = "Could not schedule draw date : " + drawDate + " for : " + gameName + " reason : " + e.getMessage();
				log.error(errMsg);
			}
		}
		else
			errMsg = "Draw Date : " + drawDate + " Invalid use yyyy-MM-dd format or check if legal date";
		
		Dx4Admin admin = (Dx4Admin) model.get("currUser");
		adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());      
		adminDetailsForm.setMessage(errMsg);
		
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
    }
	
	@RequestMapping(value = "/processAdmin", params = "updateVersion", method = RequestMethod.GET)
    public ModelAndView updateVersion(ModelMap model,HttpServletRequest request) {
       		
		log.info("In updateVersion");
		Dx4VersionForm vf = new Dx4VersionForm();
		Dx4Version version = dx4Services.getDx4Home().getVersion();
		if (version != null)
			vf.setCode(version.getCode());
		
	    return new ModelAndView("adminVersion" , "versionForm", vf);
    }
	
	@RequestMapping(value = "/processAdmin", params = "cancelVersion", method = RequestMethod.POST)
    public ModelAndView cancelVersion(ModelMap model)
    {
		Dx4Admin admin = (Dx4Admin) model.get("currUser");
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(admin,dx4Services.getDx4Home());
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
    }
	
	@RequestMapping(value = "/processAdmin", params = "saveVersion", method = RequestMethod.POST)
    public ModelAndView saveVersion(@ModelAttribute("versionForm") Dx4VersionForm versionForm,ModelMap model) {
       			
		log.info("In saveVersion");
		
		Dx4VersionCommand command = versionForm.getCommand();
		
		Dx4Version version = new Dx4Version();
		
		Dx4VersionForm vf = new Dx4VersionForm();
		if (command.getCode()==null || command.getCode().isEmpty())
		{
			vf.setMessage("Could not update version - empty vesion code");
		    return new ModelAndView("adminVersion" , "versionForm", vf);
		}
		
		version.setCode(command.getCode());
		try {
			version.setApk(saveApk(command.getApk()));
		} catch (Exception e) {
			e.printStackTrace();
			vf.setMessage("Could not update version - check with support");
		    return new ModelAndView("adminVersion" , "versionForm", vf);
		}
		
		dx4Services.getDx4Home().updateVersion(version);
		version = dx4Services.getDx4Home().getVersion();
		if (version != null)
			vf.setCode(version.getCode());
		vf.setMessage("Version updated");
		
	    return new ModelAndView("adminVersion" , "versionForm", vf);
	}
	
	private byte[] saveApk(MultipartFile multipartFile) throws 
						Exception {
	     
		
		String saveDirectory = Dx4Config.getProperties().getProperty("dx4.apkFolder");
		if (saveDirectory==null)
			throw new Exception("dx4.apkFolder not in config");
		
		log.info("saveApk to " + saveDirectory);
			
         String fileName = multipartFile.getOriginalFilename();
         log.info("adding apk : " + fileName + " to : " + saveDirectory );
         if (!"".equalsIgnoreCase(fileName)) {
             // Handle file content - multipartFile.getInputStream()
        	 
        	 String path = saveDirectory + fileName;
             multipartFile
                     .transferTo(new File(path));
             
            return Files.readAllBytes(Paths.get(path));
         }
         throw new Exception("Empty File Name");    
	 }
}
