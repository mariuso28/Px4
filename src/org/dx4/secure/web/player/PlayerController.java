package org.dx4.secure.web.player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.extend.CustomDateEditorNullBadValues;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.ErrorModelView;
import org.dx4.secure.web.agent.AgentController;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currXGR","currExpandedBets","currExpandedWins","currActiveGame"})

@RequestMapping(value = "/play")
public class PlayerController
{
	private static final Logger log = Logger.getLogger(PlayerController.class);
	private Dx4Services dx4Services;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
 
		binder.registerCustomEditor(Date.class,  new CustomDateEditorNullBadValues(dateFormat, true));
	}
	
	private String getLogonPath()
	{
		String logonPath = Dx4Config.getProperties().getProperty("dx4.playerLogonPath");
		if (logonPath==null)
		{
			logonPath = Dx4Config.getProperties().getProperty("dx4.tomcat.root","dx4.linkpc.net:8090/dx4") + "/get.html";
		}
		return logonPath;
	}
	
	@RequestMapping(value = "/goPlayer", method = RequestMethod.GET)
	public Object goAgent( ModelMap model,
			HttpServletRequest req) {
		String username = "";
		
		if (req.getParameter("username")!=null)
			username = req.getParameter("username");
		else
		if (req.getUserPrincipal()!=null && req.getUserPrincipal().getName()!=null)
			username = req.getUserPrincipal().getName();
		
		return goPlayerHome(username,model,req);
	}
	
	@RequestMapping(value = "/goPlayerHome", method = RequestMethod.GET)
	private Object goPlayerHome(String username,ModelMap model,HttpServletRequest request)
	{
		Dx4Player player;
    	try
    	{
    		player = dx4Services.getDx4Home().getPlayerByUsername(username);
		}
		catch (PersistenceRuntimeException e)
		{
			log.trace("Player : " + username + " Not found.." + e.getMessage());
			String logonPath = getLogonPath();
			return "redirect:" + logonPath;
		}
		
		log.trace("Player : " + player.getEmail() + " found..");
		
		model.addAttribute("currUser",player);
		log.trace("Setting session attribute : sCurrUser : " +  player.getCode() );
		HttpSession session = request.getSession();	
		session.setAttribute("sCurrUser", player);			
		
		if (AgentController.setActiveGame(player,model,request)==false)
		{
			log.error("No Active Games for : " + username + " try later..");
			String logonPath = getLogonPath();
			return "redirect:" + logonPath;
		}
		
		
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
    	HashSet<Long> currExpandedWins = new HashSet<Long>();
    	model.addAttribute("currExpandedWins",currExpandedWins);
		PlayerDetailsForm playerDetailsForm = new PlayerDetailsForm(player,dx4Services,currExpandedBets,currExpandedWins);
		log.trace("playerDetailsForm : " + playerDetailsForm);
		
        return new ModelAndView("playerHome" , "playerDetailsForm", playerDetailsForm);
	}
	
	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@RequestMapping(value = "/processPlayer", params = "register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("playerRegisterForm") PlayerRegisterForm playerRegisterForm,ModelMap model)
	{
		String userName = playerRegisterForm.getNewProfile().getEmail();
		log.trace("Attempting to register : " + userName);
		try
		{
			playerRegisterForm.validate(true);
		}
		catch (Dx4FormValidationException e)
		{
			playerRegisterForm = new PlayerRegisterForm(playerRegisterForm.getNewProfile());
			playerRegisterForm.setMessage("Player Registration Failed - " + e.getMessage());
			return new ModelAndView("playerRegister" , "playerRegisterForm", playerRegisterForm);
		}
		
		Dx4SecureUser bu;
		try {
			bu = dx4Services.getDx4Home().getByUsername(userName, Dx4SecureUser.class);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
		
		if (bu!=null)
		{
			log.trace(userName + " Already Exists...");		
			playerRegisterForm = new PlayerRegisterForm(playerRegisterForm.getNewProfile());
			playerRegisterForm.setMessage("Create Player Failed - " + userName + " already exists on system...");
			return new ModelAndView("playerRegister" , "playerRegisterForm", playerRegisterForm);
		}
    	
		PlayerLogonForm playerLogonForm = new PlayerLogonForm();
	    PlayerLogon playerLogon = new PlayerLogon();
	    playerLogonForm.setPlayerLogon(playerLogon);
	    /*
		Dx4RegistrationService dx4RegistrationService = dx4Services.getRegistrationService();
		try
		{
			dx4RegistrationService.registerPlayer(playerRegisterForm.getNewProfile(),userName);
			playerLogon.setMessage("Player : " + userName + "Successfully Registered - check email to proceed");
		}
		catch (Dx4RegistrationServiceException e)
		{
			playerLogon.setMessage(e.getMessage());
		}
		*/
	    
		return new ModelAndView("playerLogon" , "playerLogonForm", playerLogonForm);
	}
	
	@RequestMapping(value = "/processPlayer", params = "previousDraw", method = RequestMethod.POST)
    public Object previousDraw(@ModelAttribute("playerDetailsForm") PlayerDetailsForm playerDetailsForm,ModelMap model)
	{
		PlayerDetailsCommand command = playerDetailsForm.getCommand();
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			df.parse(command.getPreviousDraw());
		}
		catch (ParseException e) {
			log.error("previousDraw - " + e.getMessage());
			return returnPlayerHome(model);
		}
		
		return "redirect:../anal/processAnalytic.html?chooseDrawDate&date=" + command.getPreviousDraw();
	}
	
	private ModelAndView returnPlayerHome(ModelMap model)
	{
		Dx4Player player =  (Dx4Player) model.get("currUser");
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedWins = (HashSet<Long>) model.get("currExpandedWins");
		PlayerDetailsForm playerDetailsForm = new PlayerDetailsForm(player,dx4Services,currExpandedBets,currExpandedWins);
		return new ModelAndView("playerHome" , "playerDetailsForm", playerDetailsForm);

	}
	
	@RequestMapping(value = "/processPlayer", params = "edit", method = RequestMethod.GET)
    public ModelAndView edit(@ModelAttribute("playerDetailsForm") PlayerDetailsForm playerDetailsForm,ModelMap model)
	{
		Dx4Player player =  (Dx4Player) model.get("currUser");
		playerDetailsForm = new PlayerDetailsForm(player.createProfile());
		return new ModelAndView("playerEdit" , "playerDetailsForm", playerDetailsForm);
	}
	
	@RequestMapping(value = "/processPlayer", params = "save_player", method = RequestMethod.POST)
    public ModelAndView savePlayer(@ModelAttribute("playerDetailsForm") PlayerDetailsForm playerDetailsForm,
    		ModelMap model,HttpServletRequest request)
	{
		Dx4Player player = (Dx4Player) model.get("currUser");
		log.trace("saveProfile - Attempting to save profile: " + playerDetailsForm.getNewProfile());
		try
		{
			playerDetailsForm.validate(false);
		}
		catch (Dx4FormValidationException e)
		{
			log.trace("saveProfile - Edit Profile Failed - " + e.getMessage());
			playerDetailsForm = new PlayerDetailsForm(playerDetailsForm.getNewProfile());
			playerDetailsForm.setMessage("Edit Profile Failed - " + e.getMessage());
			return new ModelAndView("playerEdit" , "playerDetailsForm", playerDetailsForm);
		}
		
		playerDetailsForm.getNewProfile().setPassword(playerDetailsForm.getPassword());
		
		player.copyProfileValues(playerDetailsForm.getNewProfile());
		try {
			dx4Services.getDx4Home().updateUserProfile(player);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
		
		player = dx4Services.getDx4Home().getPlayerByUsername(player.getEmail());			// refresh
		model.addAttribute("currUser",player);
		HttpSession session = request.getSession();					
		log.trace("Setting session attribute : sCurrUser : " +  player );
		session.setAttribute("sCurrUser", player);					
		
		return returnPlayerHome(model);
	}
	
	@RequestMapping(value = "/processPlayer", params = "cancel_player", method = RequestMethod.POST)
    public ModelAndView cancelPlayer(ModelMap model)
	{
		return returnPlayerHome(model);
	}
	
	@RequestMapping(value = "/processPlayer", params = "cancelDrawResults", method = RequestMethod.GET)
    public ModelAndView cancelDrawResults(ModelMap model)
	{
		return returnPlayerHome(model);
	}
	
	
	@RequestMapping(value = "/processPlayer", params = "viewGames", method = RequestMethod.POST)
    public ModelAndView processViewGames(ModelMap model,HttpServletRequest request)
	{
		Dx4Player player = (Dx4Player) model.get("currUser");
		HttpSession session = request.getSession();					// have to set in the session as the next request will end up in the
																	// BetCommandController
		log.trace("Setting session attribute : sCurrUser : " +  player );
		session.setAttribute("sCurrUser", player);						
		
		BetCommandForm betCommandForm = new BetCommandForm(player,dx4Services.getDx4Home());
		return new ModelAndView("betCommand" , "betCommandForm", betCommandForm);
		
	}
	
	@RequestMapping(value = "/expandBet.html",  params = "method=expandBets", method = RequestMethod.GET)
	public ModelAndView expandBet(String id,ModelMap model)
	{
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		Long metaBetId = Long.parseLong(id);
		
		if (currExpandedBets.contains(metaBetId))
			currExpandedBets.remove(metaBetId);
		else
			currExpandedBets.add(metaBetId);

		model.addAttribute("currExpandedBets",currExpandedBets);
		return returnPlayerHome(model);
	}
	
	@RequestMapping(value = "/expandWin.html",  params = "method=expandWins", method = RequestMethod.GET)
	public ModelAndView expandWin(String id,ModelMap model)
	{
		log.trace("expandWin with : " + id);
		
		int pos = id.indexOf('|');
		String hrefCode = null;
		if (pos>0)
		{
			hrefCode = id.substring(pos+1);
			id = id.substring(0,pos);
		}
		
		pos = id.indexOf(':');
		String expand = "+";
		if (pos>0)
		{
			String expandStr = id.substring(pos+1);
			if (expandStr.equals("plus"))    	// toggle between plus/minus
				expand="-";
				
			id = id.substring(0,pos);
		}
		
		Long metaBetId = Long.parseLong(id);
		Dx4Player player = (Dx4Player) model.get("currUser");
		Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetById(player, metaBetId);
		
		ExternalGameResults externalGameResults =  dx4Services.getExternalService().getActualExternalGameResults(metaBet.getPlayGame().getPlayedAt());
		
		if (externalGameResults==null)
		{
			Dx4ExceptionFatal ef = new Dx4ExceptionFatal("External results for : " + metaBet.getPlayGame().getPlayedAt() + " not found");
			log.error("External results for : " + metaBet.getPlayGame().getPlayedAt() + " not found");
			return ErrorModelView.createErrorStackTrace("",ef);
		}
		
		MetaBetWinForm metaBetWinForm = new MetaBetWinForm(externalGameResults,dx4Services.getDx4Home(),metaBet,expand);
		model.addAttribute("currXGR", externalGameResults);
		if (hrefCode!=null)
			metaBetWinForm.setReturnTarget(hrefCode, MetaBetWinForm.getReturnTarget(hrefCode));
		
		log.trace("expandWin : " + metaBetWinForm);
		
		return new ModelAndView("externalPlayerWinResult", "metaBetWinForm", metaBetWinForm);
	}

}
