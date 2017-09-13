package org.dx4.secure.web.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.Dx4Comp;
import org.dx4.agent.Dx4MA;
import org.dx4.agent.Dx4SMA;
import org.dx4.agent.Dx4ZMA;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.ErrorModelView;
import org.dx4.secure.web.agent.display.HotNumber;
import org.dx4.secure.web.member.MemberCommand;
import org.dx4.secure.web.member.MemberCreateForm;
import org.dx4.secure.web.member.MemberHomeForm;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;


@Controller
@SessionAttributes({"currUser","currRole","currExpandedMembers","currNumberExposBet",
		"currNumberExposWin","currXGR","rootUserCode", "currHotNumbers",
		"currActiveGame"})

@RequestMapping(value = "/agnt")
public class AgentController {
	private static final Logger log = Logger.getLogger(AgentController.class);
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	private String getLogonPath()
	{
		String logonPath = Dx4Config.getProperties().getProperty("dx4.agentLogonPath");
		if (logonPath==null)
		{
			logonPath = Dx4Config.getProperties().getProperty("dx4.tomcat.root","dx4.linkpc.net:8090/dx4") + "/agent.html";
		}
		return logonPath;
	}
	
	@RequestMapping(value = "/goAgent", method = RequestMethod.GET)
	public Object goAgent( ModelMap model,
			HttpServletRequest req) {
		String username = "";
		
		if (req.getParameter("username")!=null)
			username = req.getParameter("username");
		else
		if (req.getUserPrincipal()!=null && req.getUserPrincipal().getName()!=null)
			username = req.getUserPrincipal().getName();
		
		return goAgentHome(username,model,req);
	}
	
	@RequestMapping(value = "/goAgentHome", method = RequestMethod.GET)
	private Object goAgentHome(String username,ModelMap model,
			HttpServletRequest request)
	{
		Dx4Agent agent;
		try
		{
			log.trace("Getting Agent : " + username);
			agent = dx4Services.getDx4Home().getAgentByUsername(username);
		}
		catch (PersistenceRuntimeException | Dx4PersistenceException e)
		{
			log.trace("Agent : " + username + " Not found.." + e.getMessage());
			log.error("goAgentHome User : " + username + " failed try again");
			String logonPath = getLogonPath();
			return "redirect:" + logonPath;
		}
		
		log.trace("Agent : " + agent.getCode() + " found..");
		
		if (setActiveGame(agent,model,request)==false)
		{
			log.error("No Active Games for : " + username + " try later..");
			String logonPath = getLogonPath();
			return "redirect:" + logonPath;
		}
		
		return goMemberHome(agent,model,request);
	}
	
	public static boolean setActiveGame(Dx4SecureUser user,ModelMap model,
			HttpServletRequest request) {
		
		String activeGame = (String) request.getAttribute("sCurrActiveGame");
		if (activeGame == null)
		{
			Dx4GameGroup group = user.getGameGroup();
			List<Dx4GameActivator> gaList = group.getGameActivators();
			if (gaList.isEmpty())
				return false;
			activeGame = gaList.get(0).getMetaGame().getName();
			HttpSession session = request.getSession();	
			session.setAttribute("sCurrActiveGame",activeGame);
		}
		model.addAttribute("currActiveGame",activeGame);
		return true;
	}

	private ModelAndView goMemberHome(Dx4SecureUser currUser,ModelMap model,
			HttpServletRequest request)
	{
		model.addAttribute("currUser",currUser);
		HttpSession session;					// already invalid
		try {
			session = request.getSession();
		} catch(IllegalStateException ex) {
			log.error("SESSION INVALID");
			return null;
		}
		
		// other controllers
		log.trace("Setting session attribute : sCurrUser : " +  currUser.getCode() +
				" session = " + session);
		session.setAttribute("sCurrUser", currUser);		

		HashMap<String, Dx4SecureUser> expandedMembers = new HashMap<String, Dx4SecureUser>();
		model.addAttribute("currExpandedMembers", expandedMembers);

		setExposures( currUser, model);
	
		
		MemberHomeForm memberHomeForm = new MemberHomeForm(dx4Services.getDx4Home(),currUser);
		
		
		return new ModelAndView("memberHome" , "memberHomeForm", memberHomeForm);
	}
	
	public static double getDefaultBetExpo()
	{
		double bet = 0.0;
		String num = Dx4Config.getProperties().getProperty("dx4.exposure.bet", "0.0");
		try
		{
			bet = Double.parseDouble(num);
		}
		catch (NumberFormatException e)
		{
			log.error("getDefaultBetExpo parsing num - setting to 0.0");
		}
		return bet;
	}
	
	public static double getDefaultWinExpo()
	{
		double win = 0.0;
		String num = Dx4Config.getProperties().getProperty("dx4.exposure.win", "0.0");
		try
		{
			win = Double.parseDouble(num);
		}
		catch (NumberFormatException e)
		{
			log.error("getDefaultBetExpo parsing num - setting to 0.0");
		}
		return win;
	}
	
	private void setExposures(Dx4SecureUser currUser,ModelMap model)
	{
		if (currUser.getRole().equals(Dx4Role.ROLE_ADMIN))
			return;

		String activeGame = (String) model.get("currActiveGame");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(activeGame);
		Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet();
		
		List<NumberExpo> currNumberExposBet = dx4Services.getDx4Home().getNumberExpo( currUser.getCode(), playGame, currUser.getRole(), getDefaultBetExpo(), false);
		List<NumberExpo> currNumberExposWin = dx4Services.getDx4Home().getNumberExpo( currUser.getCode(), playGame, currUser.getRole(), getDefaultWinExpo(), true);
		List<HotNumber> currHotNumbers = HotNumber.createHotNumbers(currUser, playGame, dx4Services.getDx4Home(),currNumberExposBet);
		
		model.addAttribute("currHotNumbers",currHotNumbers);
		model.addAttribute("currNumberExposWin",currNumberExposWin);
		model.addAttribute("currNumberExposBet",currNumberExposBet);
		
	}
	
	@RequestMapping(value = "/processAgent", params = "save_agent", method = RequestMethod.POST)
    public ModelAndView saveProfile(@ModelAttribute("agentDetailsForm") AgentDetailsForm agentDetailsForm,
    		ModelMap model,HttpServletRequest request)
	{
		log.trace(this.getClass().getSimpleName() + "::saveProfile - Attempting to save");
		Dx4SecureUser user = (Dx4SecureUser ) model.get("currUser");
		try
		{
			 agentDetailsForm.validate(false);
		}
		catch (Dx4FormValidationException e)
		{
			log.trace(this.getClass().getSimpleName() + "::saveProfile - Edit Profile Failed : " + e.getMessage());
			agentDetailsForm = new AgentDetailsForm(agentDetailsForm.getNewProfile());
			agentDetailsForm.setMessage("Edit Profile Failed - " + e.getMessage());
			return new ModelAndView("agentEdit" , "agentDetailsForm", agentDetailsForm);
		}
		
		user.copyProfileValues(agentDetailsForm.getNewProfile());
		user.setPassword(agentDetailsForm.getPassword());
		try {
			dx4Services.getDx4Home().updateUserProfile(user);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
		
		log.trace(this.getClass().getSimpleName() + "::saveProfile -profile saved : " + user.getEmail() );
		
		HttpSession session = request.getSession(false);	
		Dx4SecureUser currParentUser = (Dx4SecureUser)  session.getAttribute("sCurrParentUser");
		if (currParentUser != null)			// must have come from other controller i.e. account
		{
			user = currParentUser;
			session.removeAttribute("sCurrParentUser");
		}
		try {
			user = dx4Services.getDx4Home().getUserByCode(user.getCode());
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}				// refresh
//		dx4Services.getDx4Home().getDownstreamForParent(user);
		
		return goMemberHome(user,model,request);
	}
	
	@RequestMapping(value = "/processAgent", params = "member_cancel", method = RequestMethod.POST)
    public ModelAndView cancelProfile(ModelMap model,HttpServletRequest request)
	{
		log.trace(this.getClass().getSimpleName() + "::cancelProfile");
		Dx4SecureUser user = (Dx4SecureUser ) model.get("currUser");
		
		HttpSession session = request.getSession(false);	
		Dx4SecureUser currParentUser = (Dx4SecureUser)  session.getAttribute("sCurrParentUser");
		if (currParentUser != null)			// must have come from other controller i.e. account
		{
			user = currParentUser;
			session.removeAttribute("sCurrParentUser");
		}
		
		try {
			user = dx4Services.getDx4Home().getUserByCode(user.getCode());
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}				// refresh
		dx4Services.getDx4Home().getDownstreamForParent(user);
		
		return goMemberHome(user,model,request);
	}
	
	
	// processAccount.html?method=update&code=c0z0
	
	@RequestMapping(value = "/processAgent", params = "edit_agent", method = RequestMethod.GET)
    public ModelAndView edit(ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);	
		Dx4SecureUser currParentUser = (Dx4SecureUser)  session.getAttribute("sCurrParentUser");	
		if (currParentUser != null)			// must have come from other controller i.e. account
		{
			Dx4SecureUser currUser = (Dx4SecureUser)  session.getAttribute("sCurrUser");
			
			// overide currUser
			model.addAttribute("currUser",currUser);
		}
		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		AgentDetailsForm agentDetailsForm = new AgentDetailsForm(currUser.createProfile());
		log.trace("::edit " + currUser);
		
		return new ModelAndView("agentEdit" , "agentDetailsForm", agentDetailsForm);
	}
	
	// processAgent.html?method=expand&code
	@RequestMapping(value = "/processAgent", params = "method=expand", method = RequestMethod.GET)
	public ModelAndView expandMember(String code,ModelMap model) {
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		
		
		@SuppressWarnings("unchecked")
		HashMap<String, Dx4SecureUser> expandedMembers = (HashMap<String, Dx4SecureUser>) model.get("currExpandedMembers");
		
		if (expandedMembers.containsKey(code))
		{
							// behaves like a toggle but remove sub ones as well
			log.trace("expandMember: Removing code : " + code);
			Iterator<String> iter = expandedMembers.keySet().iterator();
			List<String> removeKeys = new ArrayList<String>();
			while (iter.hasNext())
			{
				String key = iter.next();
				log.trace("expandMember: testing : " + key);
				if (key.startsWith(code))
					removeKeys.add(key);
			}
			for (String key:removeKeys)
			{
				log.trace("expandMember: Removing key : " + key);
				expandedMembers.remove(key);	
			}
		}
		else
		{
			Dx4SecureUser user;
			try {
				user = dx4Services.getDx4Home().getUserByCode(code);
			} catch (Dx4PersistenceException e) {
				e.printStackTrace();
				throw new Dx4ExceptionFatal(e.getMessage());
			}
			log.trace("expandMember: getting downstream for : " + code);
			dx4Services.getDx4Home().getDownstreamForParent(user);
			expandedMembers.put(code,user);
		}
		
		model.addAttribute("currExpandedMembers",expandedMembers);	
		model.addAttribute("currUser",currUser);
		
		log.trace("expandMembers : " + expandedMembers);
		
		setExposures( currUser, model);
		
		return new ModelAndView("memberHome","memberHomeForm", new MemberHomeForm(dx4Services.getDx4Home(),currUser));
	}
			
			
	@RequestMapping(value = "/processAgent", params = "member_create", method = RequestMethod.POST)
	public ModelAndView storeNewMember(MemberCreateForm memberCreateForm,ModelMap model,HttpServletRequest request) {

		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4Role role = (Dx4Role) model.get("currRole");
		try
		{
			memberCreateForm.validate(true);
		}
		catch (Dx4FormValidationException e)
		{
			log.trace("::createMember Failed - " + e.getMessage());
			memberCreateForm = new MemberCreateForm(memberCreateForm);
			memberCreateForm.setMessage("Create " + role.getDesc() + " Failed " + e.getMessage());
			return new ModelAndView("memberCreate" , "memberCreateForm", memberCreateForm);
		}
		
		String userName = memberCreateForm.getNewProfile().getEmail();
		
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
			memberCreateForm = new MemberCreateForm(memberCreateForm);
			memberCreateForm.setMessage("Create " + role.getDesc() + " Failed - " + userName + " already exists on system...");
			return new ModelAndView("memberCreate" , "memberCreateForm", memberCreateForm);
		}

		Dx4SecureUser user = null;
		if (role.equals(Dx4Role.ROLE_COMP))
			user = new Dx4Comp();
		else
			if (role.equals(Dx4Role.ROLE_ZMA))
				user = new Dx4ZMA();
			else
				if (role.equals(Dx4Role.ROLE_SMA))
					user = new Dx4SMA();
				else
					if (role.equals(Dx4Role.ROLE_MA))
						user = new Dx4MA();
					else
						if (role.equals(Dx4Role.ROLE_AGENT))
							user = new Dx4Agent();
						else
							if (role.equals(Dx4Role.ROLE_PLAY))
								user = new Dx4Player();
							else
							{
								log.error("Unknow role : " + role);
								return null;
							}
		
		memberCreateForm.getNewProfile().setPassword(memberCreateForm.getPassword());
		
		user.setEmail(userName);
		user.copyProfileValues(memberCreateForm.getNewProfile());
		user.setRole(role);
		user.setAccount(new Dx4Account());
		Dx4GameGroup gameGroup = new Dx4GameGroup(user);
		Dx4GameGroup parentGroup = dx4Services.getDx4Home().getGameGroup(currUser);
		gameGroup.setGameActivators(parentGroup.getGameActivators());			// copy parent group for default
		user.setGameGroup(gameGroup);
		
		try
		{
			dx4Services.storeMember(user,currUser);
		}
		catch (Exception e)
		{
			log.error("Storing new member : " + user.getEmail() + " failed : " + e + " : " + e.getMessage());
			return ErrorModelView.createErrorStackTrace("Storing new member : " + user.getEmail() + " failed", e);
		}
		
		try {
			currUser = dx4Services.getDx4Home().getUserByCode(currUser.getCode());
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}				// refresh
		dx4Services.getDx4Home().getDownstreamForParent(currUser);
		return goMemberHome(currUser,model,request);
	}
	
	@RequestMapping(value = "/processAgent", params = "memberCreate", method = RequestMethod.POST)
	public ModelAndView createMember(MemberHomeForm memberHomeForm,ModelMap model) {
	
		log.trace("createMember attempting to create member role : " + memberHomeForm.getCreateRole());
		Dx4Role role = Dx4Role.valueOf(memberHomeForm.getCreateRole());
		model.addAttribute("currRole",role);
		MemberCreateForm memberCreateForm = new MemberCreateForm();
		log.trace(memberCreateForm);
        return new ModelAndView("memberCreate" , "memberCreateForm", memberCreateForm);
    }
	
	@RequestMapping(value = "/processAgent", params = "memberFind", method = RequestMethod.POST)
	public ModelAndView findMember(MemberHomeForm memberHomeForm,ModelMap model,HttpServletRequest request) {
	
		Dx4Agent agent = (Dx4Agent) model.get("currUser");
		
		MemberCommand command = memberHomeForm.getCommand();
		Dx4Player player = dx4Services.getDx4Home().getPlayerByUsername(command.getEmail());
		List<Dx4Player> players = new ArrayList<Dx4Player>();
		if (player!=null)
			players.add(player);
		agent.setPlayers(players);
		
		return goMemberHome(agent,model,request);
    }
	
	@RequestMapping(value = "/processAgent", params = "memberFindPhone", method = RequestMethod.POST)
	public ModelAndView memberFindPhone(MemberHomeForm memberHomeForm,ModelMap model,HttpServletRequest request) {
	
		Dx4Agent agent = (Dx4Agent) model.get("currUser");
		
		MemberCommand command = memberHomeForm.getCommand();
		List<Dx4Player> players = dx4Services.getDx4Home().getPlayersByPhone(command.getPhone());
		agent.setPlayers(players);
		
		return goMemberHome(agent,model,request);
    }
	
	@RequestMapping(value = "/processAgent", params = "viewCompanies", method = RequestMethod.GET)
    public ModelAndView viewCompanies(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdmiController
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		HashMap<String, Dx4SecureUser> expandedMembers = new HashMap<String, Dx4SecureUser>();
		model.addAttribute("currExpandedMembers", expandedMembers);
		setExposures( currUser, model);
	
		MemberHomeForm memberHomeForm = new MemberHomeForm(dx4Services.getDx4Home(),currUser);
		return new ModelAndView("memberHome", "memberHomeForm", memberHomeForm );
    }
	
	@RequestMapping(value = "/processAgent", params = "subPreviousDrawAndWins", method = RequestMethod.POST)
	public ModelAndView subPreviousDrawAndWins(AgentWinForm agentWinForm,ModelMap model,
			HttpServletRequest request) 
	{
		AgentWinCommand command = agentWinForm.getCommand();
		int pos = command.getSubUserPlace().indexOf(WinNumberWrapper.SEPERATOR);
		String subUserCode = command.getSubUserPlace().substring(0,pos);
		Dx4SecureUser currUser;
		try {
			currUser = dx4Services.getDx4Home().getUserByCode(subUserCode);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
		model.addAttribute("currUser",currUser);
		try
		{
			SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
			return previousDrawAndWins(currUser,df1.parse(command.getDrawDateStr()),model,request);
		}
		catch (Exception e) {
			log.error("subPreviousDrawAndWins - " + e.getClass().getName() + " - " + e.getMessage());
			
			pos = command.getUserPlace().indexOf(WinNumberWrapper.SEPERATOR);
			String userCode = command.getUserPlace().substring(0,pos);
			try {
				currUser = dx4Services.getDx4Home().getUserByCode(userCode);
			} catch (Dx4PersistenceException e1) {
				e1.printStackTrace();
				throw new Dx4ExceptionFatal(e.getMessage());
			}
			return goMemberHome(currUser,model,request);
		}
	}
	
	@RequestMapping(value = "/processAgent", params = "previousDrawAndWins", method = RequestMethod.POST)
	public ModelAndView previousDrawAndWins(MemberHomeForm memberHomeForm,ModelMap model,HttpServletRequest request) 
	{
		MemberCommand command = memberHomeForm.getCommand();
		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		model.addAttribute("rootUserCode",currUser.getCode());
		
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			Date playDate = df.parse(command.getPreviousDraw());
			return previousDrawAndWins(currUser,playDate,model,request);
		}
		catch (Exception e) {
			log.error("previousDrawAndWins - " + e.getClass().getName() + " - " + e.getMessage());
			return goMemberHome(currUser,model,request);
		}
	}
	
	private ModelAndView previousDrawAndWins(Dx4SecureUser currUser,Date playDate,
			ModelMap model,HttpServletRequest request) throws Exception
	{
		model.addAttribute("currUser",currUser);
		ExternalGameResults currXGR;
		currXGR =  dx4Services.getExternalService().getActualExternalGameResults(playDate);
		if (currXGR==null)
		{
			Dx4ExceptionFatal ef = new Dx4ExceptionFatal("External results for : " + playDate + " not found");
			log.error("External results for : " + playDate + " not found");
			return ErrorModelView.createErrorStackTrace("",ef);
		}

		model.addAttribute("currXGR", currXGR);
		AgentWinForm agentWinForm = new AgentWinForm(currXGR,dx4Services.getDx4Home(),currUser);
		
		log.trace("previousDrawAndWins : " + agentWinForm);
		return new ModelAndView("externalAgentWinResult","agentWinForm",agentWinForm);
	}
	
	@RequestMapping(value = "/processAgent.html",  params = "refreshPlacings", method = RequestMethod.POST)
	public ModelAndView refreshPlacings(AgentWinForm agentWinForm,ModelMap model)
	{
		AgentWinCommand command = agentWinForm.getCommand();
		log.trace("refreshPlacings with : " + command.getWinPlaceHolder().getPlacings());
		
		ExternalGameResults currXGR = (ExternalGameResults) model.get("currXGR");
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		
		agentWinForm = new AgentWinForm(currXGR,dx4Services.getDx4Home(),currUser, command.getWinPlaceHolder().getPlacings());
		
		log.trace("refreshPlacings : " + agentWinForm);
		
		return new ModelAndView("externalAgentWinResult","agentWinForm",agentWinForm);
	}
	
	@RequestMapping(value = "/processAgent", params = "returnPreviousDrawAndWins", method = RequestMethod.POST)
	public ModelAndView returnPreviousDrawAndWins(AgentWinForm agentWinForm,ModelMap model,HttpServletRequest request) {
		
		AgentWinCommand command = agentWinForm.getCommand();
		int pos = command.getUserPlace().indexOf(WinNumberWrapper.SEPERATOR);
		String userCode = command.getUserPlace();
		if (pos>0)
		{
			 userCode = command.getUserPlace().substring(0,pos);
		}
		Dx4SecureUser currUser;
		try {
			currUser = dx4Services.getDx4Home().getUserByCode(userCode);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
		String rootUserCode = (String) model.get("rootUserCode");
		
		if (rootUserCode.equals(currUser.getCode()))
		{
	//		dx4Services.getDx4Home().getDownstreamForParent(currUser);
			return goMemberHome(currUser,model,request);
		}
		
		try {
			currUser = dx4Services.getDx4Home().getParentForUser(currUser);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new Dx4ExceptionFatal(e1.getMessage());
		}
	
		try
		{
			SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
			return previousDrawAndWins(currUser,df1.parse(command.getDrawDateStr()),model,request);
		}
		catch (Exception e) {
			log.error("returnPreviousDrawAndWins - " + e.getClass().getName() + " - " + e.getMessage());
			try {
				currUser = dx4Services.getDx4Home().getUserByCode(rootUserCode);
			} catch (Dx4PersistenceException e1) {
				e1.printStackTrace();
				throw new Dx4ExceptionFatal(e.getMessage());
			}
			return goMemberHome(currUser,model,request);
		}
	}
	
	@RequestMapping(value = "/processAgent", params = "changeActiveGame", method = RequestMethod.POST)
	public ModelAndView changeActiveGame(MemberHomeForm memberHomeForm,ModelMap model,HttpServletRequest request) {
	
		MemberCommand command = memberHomeForm.getCommand();
		log.trace("changeActiveGame with game : " + command.getActiveGame());
		model.addAttribute("currActiveGame",command.getActiveGame());
		HttpSession session = request.getSession();	
		session.setAttribute("sCurrActiveGame",command.getActiveGame());
		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		return goMemberHome(currUser,model,request);
	}
	
		
	@RequestMapping(value = "/processAgent.html", params = "cancelNumberExpo", method = RequestMethod.GET)
	public ModelAndView cancelNumberExpo(ModelMap model,HttpServletRequest request) {

		Dx4SecureUser user = (Dx4SecureUser ) model.get("currUser");
		return goMemberHome(user,model,request);
	}	
}
