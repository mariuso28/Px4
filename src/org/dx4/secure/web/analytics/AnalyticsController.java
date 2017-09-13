package org.dx4.secure.web.analytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.admin.Dx4Admin;
import org.dx4.extend.CustomDateEditorNullBadValues;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberStoreJson;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.member.MemberHomeForm;
import org.dx4.secure.web.player.DrawResultForm;
import org.dx4.services.Dx4ExternalServiceException;
import org.dx4.services.Dx4Services;
import org.dx4.utils.NumberGrid;
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
@SessionAttributes({"currUser","currGridType","currStartDate","currEndDate","external",
	"numberGridExpo","externalReturnAddress","currXGR","currPlayDate","numberBasket",
	"inPreviousDraw","previousDrawReturnAddress", "currPageElement","currActiveGame"})

@RequestMapping(value = "/anal")
public class AnalyticsController {
	private static final Logger log = Logger.getLogger(AnalyticsController.class);
	@Autowired
	private Dx4Services dx4Services;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
 
		binder.registerCustomEditor(Date.class,  new CustomDateEditorNullBadValues(dateFormat, true));
	}
		
	@RequestMapping(value = "/processAnalytic", params = "maintainExposures", method = RequestMethod.POST)
    public ModelAndView maintainExposures(ModelMap model)
	{
		String gType = (String) model.get("currGridType");
		model.put( "externalReturnAddress", "processAnalytic.html?cancelViewNumber" );
		NumberGrid numberGridExpo = dx4Services.getExternalService().getNumberGridExposures(Integer.parseInt(gType));
		model.addAttribute("numberGridExpo", numberGridExpo);
		return goMaintainExposures(model);
	}
	
	private ModelAndView goMaintainExposures(ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		NumberExpoForm numberExpoForm = new NumberExpoForm(dx4Services,currUser,numberGridExpo);
		String externalReturnAddress = (String) model.get("externalReturnAddress");
		numberExpoForm.setReturnAdress(externalReturnAddress);
		return new ModelAndView("maintainExposures" , "numberExpoForm", numberExpoForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyBetExposure", method = RequestMethod.POST)
    public ModelAndView modifyBetExposure(@ModelAttribute("numberExpoForm") NumberExpoForm numberExpoForm,ModelMap model)
	{
		NumberExpoCommand command = numberExpoForm.getCommand();
		return updateDefaultExposure(command.getBetExpo(),true,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyWinExposure", method = RequestMethod.POST)
    public ModelAndView modifyWinExposure(@ModelAttribute("numberExpoForm") NumberExpoForm numberExpoForm,ModelMap model)
	{
		NumberExpoCommand command = numberExpoForm.getCommand();
		return updateDefaultExposure(command.getWinExpo(),false,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyUnLimitedBetExposure", method = RequestMethod.POST)
    public ModelAndView modifyUnlimitedBetExposure(@ModelAttribute("numberExpoForm") NumberExpoForm numberExpoForm,ModelMap model)
	{
		return updateDefaultExposure(-1,true,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyUnLimitedWinExposure", method = RequestMethod.POST)
    public ModelAndView modifyUnLimitedWinExposure(@ModelAttribute("numberExpoForm") NumberExpoForm numberExpoForm,ModelMap model)
	{
		return updateDefaultExposure(-1,false,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyBetExpo", method = RequestMethod.POST)
    public ModelAndView modifyBetExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		return updateExposure(command.getNumber(),command.getBetExpo(),true,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyWinExpo", method = RequestMethod.POST)
    public ModelAndView modifyWinExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		return updateExposure(command.getNumber(),command.getWinExpo(),false,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyUnLimitedBetExpo", method = RequestMethod.POST)
    public ModelAndView modifyUnlimitedBetExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		log.trace("modifyUnlimitedBetExpo : " + command);
		return updateExposure(command.getNumber(),-1,true,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "modifyUnLimitedWinExpo", method = RequestMethod.POST)
    public ModelAndView modifyUnLimitedWinExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		return updateExposure(command.getNumber(),-1,false,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "resetExpoToDefault", method = RequestMethod.POST)
    public ModelAndView resetExpoToDefault(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, command.getNumber());
		if (expo!=null)
			dx4Services.getDx4Home().deleteDx4NumberExpo(expo, digits);
		Dx4NumberStoreJson ns = numberGridExpo.getNumberStore(command.getNumber());
		ns.clearExposures();
		NumberExpoForm numberExpoForm = new NumberExpoForm(dx4Services,currUser,numberGridExpo);
		return new ModelAndView("maintainExposures" , "numberExpoForm", numberExpoForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "blockExpo", method = RequestMethod.POST)
    public ModelAndView blockExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, command.getNumber());
		if (expo==null)
		{
			expo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
			expo.setNumber(command.getNumber());
			expo.setBlocked(1);
			dx4Services.getDx4Home().storeDx4NumberExpo(expo, digits);
		}
		else
		{
			expo.setBlocked(1);
			dx4Services.getDx4Home().updateNumberExpoBlocked(expo);
		}
		NumberExpoForm numberExpoForm = new NumberExpoForm(dx4Services,currUser,numberGridExpo);
		return new ModelAndView("maintainExposures" , "numberExpoForm", numberExpoForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "unblockExpo", method = RequestMethod.POST)
    public ModelAndView unblockExpo(@ModelAttribute("numberViewExposureForm") NumberViewExposureForm numberViewExposureForm,ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		NumberExpoCommand command = numberViewExposureForm.getCommand();
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, command.getNumber());
		if (expo!=null)
		{
			Dx4NumberExpo defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
			if (expo.getBetExpo()==defaultExpo.getBetExpo() && expo.getWinExpo()==defaultExpo.getWinExpo())
				return resetExpoToDefault(numberViewExposureForm,model);
			expo.setBlocked(0);
			dx4Services.getDx4Home().updateNumberExpoBlocked(expo);
		}
		// otherwise do nothing must be default;
		
		NumberExpoForm numberExpoForm = new NumberExpoForm(dx4Services,currUser,numberGridExpo);
		return new ModelAndView("maintainExposures" , "numberExpoForm", numberExpoForm);
	}
	
	private ModelAndView updateDefaultExposure(double amount,boolean bet,ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		Dx4NumberExpo defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
		if (bet)
			defaultExpo.setBetExpo(amount);
		else
			defaultExpo.setWinExpo(amount);
		dx4Services.getDx4Home().udateDx4NumberExpo(defaultExpo, digits);
		NumberExpoForm numberExpoForm = new NumberExpoForm(dx4Services,currUser,numberGridExpo);
		return new ModelAndView("maintainExposures" , "numberExpoForm", numberExpoForm);
	}
	
	private ModelAndView updateExposure(String number,double amount,boolean bet,ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		char digits = NumberExpoForm.getDigits(numberGridExpo.getGtype());
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, number);
		Dx4NumberExpo defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
		if (expo==null)
		{
			expo = new Dx4NumberExpo(defaultExpo);
			expo.setNumber(number);
		}
		
		if (bet)
			expo.setBetExpo(amount);
		else
			expo.setWinExpo(amount);
		dx4Services.getDx4Home().deleteDx4NumberExpo(expo, digits);
		// don't store if using default bet/win values
		if (defaultExpo.getBetExpo()!=expo.getBetExpo() || defaultExpo.getWinExpo()!=expo.getWinExpo())
		{
			dx4Services.getDx4Home().storeDx4NumberExpo(expo, digits);
			// values in the grid will be modified automatically cos the expos 
			// will appear in the bet/win expo lists
		}
		else
		{
			// have to clear as these won't
			Dx4NumberStoreJson ns = numberGridExpo.getNumberStore(number);
			ns.clearExposures();
		}
		
		String currActiveGame = (String) model.get("currActiveGame");
		NumberViewExposureForm numberViewExposureForm = new NumberViewExposureForm(dx4Services,currUser,currActiveGame,numberGridExpo.getGtype(),number);	
		return new ModelAndView("numberViewExposure" , "numberViewExposureForm", numberViewExposureForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "method=viewNumberExposure", method = RequestMethod.GET)
    public ModelAndView viewNumberExposure(String number,ModelMap model)
	{
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		String currActiveGame = (String) model.get("currActiveGame");
		
		NumberViewExposureForm numberViewExposureForm = new NumberViewExposureForm(dx4Services,currUser,currActiveGame,numberGridExpo.getGtype(),number);
		
		return new ModelAndView("numberViewExposure" , "numberViewExposureForm", numberViewExposureForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "method=viewDefaultNumberExposure", method = RequestMethod.GET)
    public ModelAndView viewDefaultNumberExposure(String number,ModelMap model)
	{
		NumberGrid numberGridExpo = (NumberGrid) model.get("numberGridExpo");
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		String currActiveGame = (String) model.get("currActiveGame");
		NumberViewExposureForm numberViewExposureForm = new NumberViewExposureForm(dx4Services,currUser,currActiveGame,numberGridExpo.getGtype(),number);
		
		return new ModelAndView("numberViewExposure" , "numberViewExposureForm", numberViewExposureForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "cancel_viewExposure", method = RequestMethod.POST)
    public ModelAndView cancelViewExposures(ModelMap model)
	{
		return goMaintainExposures(model);
	}
	
	
	@RequestMapping(value = "/processAnalytic", params = "numbers_refine", method = RequestMethod.POST)
    public ModelAndView numbersRefine(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		String gType = (String) model.get("currGridType");
		
		NumberGridCommand command = numberGridForm.getCommand();
		log.trace(this.getClass().getSimpleName()+"::numbersRefine with : " + command);
		String message = "";
		if (numberGridForm.validate())
		{
			List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
			if (command.getStartDate().before(dates.get(0)))
				command.setStartDate(dates.get(0));
			if (command.getEndDate().after(dates.get(1)))
				command.setEndDate(dates.get(1));
			
			model.addAttribute("currStartDate", command.getStartDate());
			model.addAttribute("currEndDate", command.getEndDate());
		}
		else
		{
			message = "Invalid or incomplete refine date(s)";
		}
		
		log.trace(this.getClass().getSimpleName()+"::numbersRefine set message : " + numberGridForm);
		Date startDate = (Date) model.get("currStartDate");
		Date endDate = (Date) model.get("currEndDate");
		Integer currPageElement = (Integer) model.get("currPageElement");
		numberGridForm = new NumberGridForm(dx4Services,gType,startDate,endDate,currPageElement);
		numberGridForm.setMessage(message);
		numberGridForm.setExternal(command.isExternal());
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		numberGridForm.setExposures(currUser.getRole().isAgentRole());
		
		log.trace(this.getClass().getSimpleName()+"::numbersRefine sending with number_refresh : " + numberGridForm);
		return new ModelAndView("numberRefreshX" , "numberGridForm", numberGridForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "numbers_refresh", method = RequestMethod.GET)
    public ModelAndView numbersRefresh(String type,ModelMap model,HttpServletRequest request)
	{
		return numbersRefresh(type,model,request,false);
	}
	
	@RequestMapping(value = "/processAnalyticExternal", params = "numbers_refresh", method = RequestMethod.GET)
    public ModelAndView numbersRefreshExternal(String type,ModelMap model,HttpServletRequest request)
	{
		return numbersRefresh(type,model,request,true);
	}
	
	private ModelAndView numbersRefresh(String type,ModelMap model,HttpServletRequest request,boolean external)
	{
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : currUser ");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");				// as the thing comes from another controller
																								// admin, agent or player
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser", currUser);
		
		String currActiveGame = (String) session.getAttribute("sCurrActiveGame");
		model.addAttribute("currActiveGame", currActiveGame);
		
		log.trace("numbersRefresh with gridType: " + type);
		model.addAttribute("currGridType", type);
		List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
		model.addAttribute("currStartDate", dates.get(0));
		model.addAttribute("currEndDate", dates.get(1));
		
		model.addAttribute("currPageElement",new Integer(0));
		NumberGridForm numberGridForm = new NumberGridForm(dx4Services,type,dates.get(0),dates.get(1),0);
		numberGridForm.setExternal(external);
		model.addAttribute("external", external);
		numberGridForm.setExposures(currUser.getRole().isAgentRole());
		Set<String> numberBasket = new HashSet<String>();
		model.addAttribute("numberBasket",numberBasket);
		
		model.addAttribute("previousDrawReturnAddress","processAnalytic.html?cancelDrawResults");
		
		return new ModelAndView("numberRefreshX" , "numberGridForm", numberGridForm);
	}
	
	private ModelAndView goBackNumbersRefresh(ModelMap model,String message)
	{																			
		String type = (String) model.get("currGridType");
		Date currStartDate = (Date) model.get("currStartDate");
		Date currEndDate = (Date) model.get("currEndDate");
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Integer currPageElement = (Integer) model.get("currPageElement");
		NumberGridForm numberGridForm = new NumberGridForm(dx4Services,type,currStartDate,currEndDate,currPageElement);
		numberGridForm.setExternal((Boolean) model.get("external"));
		numberGridForm.setExposures(currUser.getRole().isAgentRole());
		numberGridForm.setMessage(message);
		
		return new ModelAndView("numberRefreshX" , "numberGridForm", numberGridForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "cancel_betrefresh", method = RequestMethod.POST)
    public Object numbersCancelRefresh(ModelMap model,HttpServletRequest request)
	{
		return "redirect:../qkBet/number_refresh.html?method=cancel";
	}
	
	@RequestMapping(value = "/processAnalytic", params = "cancel_refresh", method = RequestMethod.POST)
    public Object numbersCancel(ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		if (currUser.getRole().equals(Dx4Role.ROLE_ADMIN))
		{
			Dx4Admin admin = (Dx4Admin) currUser;
			return "redirect:../adm/goAdminHome.html?username="+admin.getEmail();
		}
		else
		if (currUser.getRole().equals(Dx4Role.ROLE_PLAY))
		{
			Dx4Player player = (Dx4Player) currUser;
			return "redirect:../play/goPlayerHome.html?username="+player.getEmail();
		}
		else
		// agent,ma,sma,zma,company
		{
			return new ModelAndView("memberHome","memberHomeForm", new MemberHomeForm(dx4Services.getDx4Home(),currUser));
		}
	}
	
	@RequestMapping(value = "/processAnalytic", params = "cancelDrawResults", method = RequestMethod.GET)
	public ModelAndView cancelDrawResults(ModelMap model)
	{
		return numbersReturn(model);
	}

	@RequestMapping(value = "/processAnalytic", params = "cancelViewNumber", method = RequestMethod.GET)
    public ModelAndView cancelViewNumber(ModelMap model)
	{
		return numbersReturn(model);
	}
	
	// "processAnalytic.html?method=newPage&page=${index}"
	@RequestMapping(value = "/processAnalytic", params = "method=newPage", method = RequestMethod.GET)
    public ModelAndView newPageElement(String page,ModelMap model)
	{
		Integer currPageElement = 0;
		try
		{
			currPageElement = Integer.parseInt(page);
			if (currPageElement<0 || currPageElement>9)
				throw new NumberFormatException("Invalid Range 0..9");
		}	
		catch (NumberFormatException e)
		{
			log.error("Invalid page value : " + page + e.getMessage());
		}
			
		model.addAttribute("currPageElement",currPageElement);
		return numbersReturn(model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "cancel_view", method = RequestMethod.POST)
    public ModelAndView numbersReturn(ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		boolean external =  (Boolean) model.get("external");
		String gType = (String) model.get("currGridType");
		Date startDate = (Date) model.get("currStartDate");
		Date endDate = (Date) model.get("currEndDate");
		Integer currPageElement = (Integer) model.get("currPageElement");
		NumberGridForm numberGridForm = new NumberGridForm(dx4Services,gType,startDate,endDate,currPageElement);
		numberGridForm.setExternal(external);
		numberGridForm.setExposures(currUser.getRole().isAgentRole());
		return new ModelAndView("numberRefreshX" , "numberGridForm", numberGridForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "keyNumberExpo", method = RequestMethod.POST)
	public ModelAndView keyNumberExpo(NumberExpoForm numberExpoForm,ModelMap model)
	{
		NumberExpoCommand command = numberExpoForm.getCommand();
		String number = command.getUseNumber();
		log.trace("keyNumberExpo ### number is : " + number);
		return viewNumberExposure(number,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "selectBetExpo", method = RequestMethod.POST)
	public ModelAndView selectBetExpo(NumberExpoForm numberExpoForm,ModelMap model)
	{
		log.trace("selectBetExpo number is : " + numberExpoForm.getCommand().getBetNumber());
		String number = numberExpoForm.getCommand().getBetNumber();
		return viewNumberExposure(number,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "selectWinExpo", method = RequestMethod.POST)
	public ModelAndView selectWinExpo(NumberExpoForm numberExpoForm,ModelMap model)
	{
		log.trace("selectWinExpo : " + numberExpoForm.getCommand().getWinNumber());
		String number = numberExpoForm.getCommand().getWinNumber();
		return viewNumberExposure(number,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "viewKeyNumber", method = RequestMethod.POST)
    public ModelAndView viewKeyNumber(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		String number = command.getUseNumber();
		boolean external = command.isExternal();
		
		return viewNumber(number,external,model);
	}
	
	// <td><a href="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}">${column.number}</td>
	@RequestMapping(value = "/processAnalytic", params = "method=viewNumber", method = RequestMethod.GET)
    public ModelAndView viewNumber(String number,ModelMap model)
	{
		int pos = number.indexOf(':');
		String extn = number.substring(pos+1);
		number = number.substring(0,pos);
		boolean external = Boolean.parseBoolean(extn);
		
		return viewNumber(number,external,model);
	}
	
	private ModelAndView viewNumber(String number,boolean external,ModelMap model)
	{
		model.addAttribute("external",external);
		String type = (String) model.get("currGridType");
		Date currStartDate = (Date) model.get("currStartDate");
		Date currEndDate = (Date) model.get("currEndDate");
		List<Dx4DrawResultJson> drawResults = dx4Services.getDx4Home().getDrawResultsForNumber(number,type,currStartDate,currEndDate);
		
		double revenue = 0;
		List<Dx4PlacingJson> placings = new ArrayList<Dx4PlacingJson>();
		if (type.equals("4"))
		{
			revenue = dx4Services.getDx4Home().getRevenueForNumber(number,currStartDate,currEndDate);
			placings = dx4Services.getDx4Home().getPlacingsForNumber(number,currStartDate,currEndDate);
		}
		Dx4NumberPageElementJson numberDesc = null;
		if (type.equals("4") || type.equals("2"))
		{
			numberDesc = dx4Services.getDx4Home().getNumberPageElement(number,Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		}
		NumberViewForm numberViewForm = new NumberViewForm(drawResults,placings,number,numberDesc,currStartDate,currEndDate,revenue);
		numberViewForm.setExternal(external);
		model.addAttribute("inPreviousDraw",new Boolean(false));
		
		return new ModelAndView("numberView" , "numberViewForm", numberViewForm);
	}
	
	
	@RequestMapping(value = "/processAnalytic", params = "topNumber", method = RequestMethod.POST)
    public ModelAndView viewTopNumber(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		String number = command.getTopNumber();
		boolean external = command.isExternal();
		model.addAttribute("external", external);
		
		String type = (String) model.get("currGridType");
		Date currStartDate = (Date) model.get("currStartDate");
		Date currEndDate = (Date) model.get("currEndDate");
		List<Dx4DrawResultJson> drawResults = dx4Services.getDx4Home().getDrawResultsForNumber(number,type,currStartDate,currEndDate);
		double revenue = 0;
		List<Dx4PlacingJson> placings = new ArrayList<Dx4PlacingJson>();
		if (type.equals("4"))
		{
			revenue = dx4Services.getDx4Home().getRevenueForNumber(number,currStartDate,currEndDate);
			placings = dx4Services.getDx4Home().getPlacingsForNumber(number,currStartDate,currEndDate);
		}
		Dx4NumberPageElementJson numberDesc = null;
		if (type.equals("4") || type.equals("2"))
		{
			numberDesc = dx4Services.getDx4Home().getNumberPageElement(number,Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		}
		NumberViewForm numberViewForm = new NumberViewForm(drawResults,placings,number,numberDesc,currStartDate,currEndDate,revenue);
		numberViewForm.setExternal(external);
		return new ModelAndView("numberView" , "numberViewForm", numberViewForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "numbers_sameday", method = RequestMethod.POST)
    public ModelAndView viewSameDayNumbers(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		boolean external = command.isExternal();
		
		model.addAttribute("external", external);
		List<Dx4DrawResultJson> drawResults = dx4Services.getExternalService().getExternalSameDayGameResults();
		SameDayNumberViewForm sameDayNumberViewForm = new SameDayNumberViewForm(drawResults);
		sameDayNumberViewForm.setExternal(external);
		
		return new ModelAndView("sameDayNumberView" , "sameDayNumberViewForm", sameDayNumberViewForm);
	}
	
	@RequestMapping(value = "/processAnalytic.html", params = "chooseDrawDate", method = RequestMethod.GET)
    public Object chooseDrawDate(String date,ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : currUser ");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");				// as the thing comes from another controller
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser", currUser);
		
		String returnAddr = "../play/goPlayerHome.html?username=" + currUser.getEmail();
		Date drawDate;
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			drawDate = df.parse(date);
		}
		catch (ParseException e)
		{
			return returnAddr;
		}
		
		model.addAttribute("external",false);
		model.addAttribute("previousDrawReturnAddress",returnAddr);
		return previousDraw(drawDate,model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "chooseDate", method = RequestMethod.POST)
    public ModelAndView chooseDate(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		if (command.getSomeDate()==null)
		{
			return goBackNumbersRefresh(model,"Invalid or incomplete draw date");
		}
		
		List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
		if (command.getSomeDate().before(dates.get(0)))
			command.setSomeDate(dates.get(0));
		if (command.getSomeDate().after(dates.get(1)))
			command.setSomeDate(dates.get(1));
		
		log.trace("going draw at date: " + command.getSomeDate());
		return previousDraw(command.getSomeDate(),model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "chooseDateFromDraw", method = RequestMethod.POST)
    public ModelAndView chooseDateFromDraw(@ModelAttribute("DrawResultForm") DrawResultForm drawResultForm,ModelMap model)
	{
		if (drawResultForm.getSomeDate()==null)
		{
			return previousDraw((new GregorianCalendar()).getTime(),model,"Invalid date");
		}
		
		List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
		if (drawResultForm.getSomeDate().before(dates.get(0)))
			drawResultForm.setSomeDate(dates.get(0));
		if (drawResultForm.getSomeDate().after(dates.get(1)))
			drawResultForm.setSomeDate(dates.get(1));
		
		log.trace("going draw at date: " + drawResultForm.getSomeDate());
		return previousDraw(drawResultForm.getSomeDate(),model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "prevDraw", method = RequestMethod.GET)
    public ModelAndView prevDraw(ModelMap model)
	{
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		return previousDraw(externalGameResults.getPrevDate(),model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "nextDraw", method = RequestMethod.GET)
    public ModelAndView nextDraw(ModelMap model)
	{
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		return previousDraw(externalGameResults.getNextDate(),model);
	}
	
	private ModelAndView previousDraw(Date playDate,ModelMap model)
	{
		return previousDraw(playDate,model,"");
	}
	
	private ModelAndView previousDraw(Date playDate,ModelMap model,String message)
	{
		ExternalGameResults externalGameResults;
		try
		{
			externalGameResults =  dx4Services.getExternalService().getNearestExternalGameResults(playDate);
		}
		catch (Dx4ExternalServiceException e)
		{
			log.error("previousDraw - " + e.getMessage());
			return numbersReturn(model);
		} 

		model.addAttribute("currXGR", externalGameResults);
		model.addAttribute("inPreviousDraw",new Boolean(true));
		model.addAttribute("currPlayDate",playDate);
		DrawResultForm drawResultForm = new DrawResultForm();
		drawResultForm.setMessage(message);
		Boolean external = (Boolean) model.get("external");
		if (external==true)
			return new ModelAndView("externalDrawResultUse","drawResultForm",drawResultForm);
		return new ModelAndView("externalDrawResult","drawResultForm",drawResultForm);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "method=addNumber", method = RequestMethod.GET)
	public ModelAndView addNumber(String number,ModelMap model)
	{
		Date playDate = (Date) model.get("currPlayDate");
		@SuppressWarnings("unchecked")
		Set<String> numberBasket = (Set<String>) model.get("numberBasket");
		String gType = (String) model.get("currGridType");
		Boolean inPreviousDraw = (Boolean) model.get("inPreviousDraw");
		
		if (gType.equals("4"))
			numberBasket.add(number);
		else
		if (inPreviousDraw!=null && inPreviousDraw.equals(true))
			numberBasket.add(number.substring(1));
		else
			numberBasket.add(number);
		
		if (inPreviousDraw!=null && inPreviousDraw.equals(true))
			return previousDraw(playDate,model);
		return numbersReturn(model);
	}
	
	// processAnalytic.html?method=cancelNumber&number=${number}
	@RequestMapping(value = "/processAnalytic", params = "method=cancelNumber", method = RequestMethod.GET)
	public ModelAndView cancelNumber(String number,ModelMap model)
	{
		Date playDate = (Date) model.get("currPlayDate");
		@SuppressWarnings("unchecked")
		Set<String> numberBasket = (Set<String>) model.get("numberBasket");
		numberBasket.remove(number);
		Boolean inPreviousDraw = (Boolean) model.get("inPreviousDraw");
		if (inPreviousDraw.equals(true))
			return previousDraw(playDate,model);
		return numbersReturn(model);
	}
	
	@RequestMapping(value = "/processAnalytic", params = "submitBasket", method = RequestMethod.POST)
	public String submitBasket(ModelMap model)
	{
		@SuppressWarnings("unchecked")
		Set<String> numberBasket = (Set<String>) model.get("numberBasket");
		String numberList = "";
		for (String number : numberBasket)
			numberList += number + ":";
		
		return "redirect:../qkBet/number_basket.html?method=setBasket&numbers="+numberList;
	}
	
	@RequestMapping(value = "/processAnalytic", params = "numberSearch", method = RequestMethod.POST)
	public ModelAndView numberSearch(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		log.trace("Number Search With : " + command.getSearchTerm() + " digits : " + command.getDigits());
		List<Dx4NumberPageElementJson> npe = dx4Services.getDx4Home().getNumberPageElementsByDesc(command.getSearchTerm().toLowerCase());
		populateNumberPageElements(model,npe);
		for (Dx4NumberPageElementJson np : npe)
			log.trace(np);
		
		Boolean external = (Boolean) model.get("external");
		NumberViewListForm numberViewListForm = new NumberViewListForm(npe,command.getSearchTerm().toLowerCase(),command.getDigits(),external);
		return new ModelAndView("numberViewList","numberViewListForm",numberViewListForm);
	}
	
	private void populateNumberPageElements(ModelMap model,List<Dx4NumberPageElementJson> npeList) {
		
		String gType = (String) model.get("currGridType");
		Date startDate = (Date) model.get("currStartDate");
		Date endDate = (Date) model.get("currEndDate");
		NumberGrid numberGrid = dx4Services.getExternalService().getNumberGridValues(Integer.parseInt(gType),startDate,endDate);
		
		for (Dx4NumberPageElementJson npe : npeList)
		{
			npe.setNumbers(new ArrayList<Dx4NumberStoreJson>());
			if (numberGrid.getGtype()!=4)
			{
				Dx4NumberStoreJson ns = numberGrid.getNumberStore(npe.getToken());
				npe.getNumbers().add(ns);
				continue;
			}
			for (int c=0; c<10; c++)
			{
				String tok = Integer.toString(c) + npe.getToken();
				Dx4NumberStoreJson ns = numberGrid.getNumberStore(tok);
				npe.getNumbers().add(ns);
			}
		}
	}

	@RequestMapping(value = "/processAnalytic", params = "sameDayDate", method = RequestMethod.POST)
	public ModelAndView sameDayDate(@ModelAttribute("numberGridForm") NumberGridForm numberGridForm,ModelMap model)
	{
		NumberGridCommand command = numberGridForm.getCommand();
		log.trace("going draw at date: " + command.getSameDayDate());
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
		try
		{
			Date date = df1.parse(command.getSameDayDate());
			return previousDraw(date,model);
		}
		catch (ParseException e)
		{
			log.trace("Couldn't parse date : " + command.getSameDayDate());
		}
		return numbersReturn(model);
	}
}