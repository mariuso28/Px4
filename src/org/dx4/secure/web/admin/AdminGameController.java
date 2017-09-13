package org.dx4.secure.web.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.extend.CustomDateEditorNullBadValues;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.services.Dx4Services;
import org.dx4.services.Dx4ServicesException;
import org.dx4.util.DateUtil;
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
@SessionAttributes({"currUser","currMetaGame","currXGR","currPlayGame"})

@RequestMapping(value = "/admGame")
public class AdminGameController {

	private static final Logger log = Logger.getLogger(AdminGameController.class);
	private Dx4Services dx4Services;
	
	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
 
		binder.registerCustomEditor(Date.class,  new CustomDateEditorNullBadValues(dateFormat, true));
	}
	
	@ModelAttribute("digitList")
	public List<Integer> populateDigitList() {
 
		List<Integer> digitList = new ArrayList<Integer>();
		for (int i=0; i<10; i++)
			digitList.add(i);
		return digitList;
	}
	
	@RequestMapping(value = "/edit_Game.html", params = "method=doThis",method = RequestMethod.GET)
    public ModelAndView doThis(String name,ModelMap model,HttpServletRequest request){
		
		log.trace("doThis method with : " + name);
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.trace("got session attribute : currUser : " +  currUser );
		
		model.addAttribute("currUser", currUser);
		
        Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
        model.addAttribute("currMetaGame", metaGame);
        AdminGamePlayDateForm adminGamePlayDateForm = initialiseAdminGamePlayDateForm();
        ModelAndView modelAndView = new ModelAndView("adminGame" );
        modelAndView.addObject("adminGamePlayDateForm", adminGamePlayDateForm);
        return modelAndView;
    }
	/*
	@RequestMapping(params = "External0",method = RequestMethod.POST)
    public String playExternal0(String params,ModelMap model){
	
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		doPlayGame(externalGameResults.getDraws().get(0),model);
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	
	@RequestMapping(params = "External1",method = RequestMethod.POST)
    public String playExternal1(String params,ModelMap model){
	
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		doPlayGame(externalGameResults.getDraws().get(1),model);
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	
	@RequestMapping(params = "External2",method = RequestMethod.POST)
    public String playExternal2(String params,ModelMap model){
	
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		doPlayGame(externalGameResults.getDraws().get(0),model);
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	*/
	
	@RequestMapping(value = "/edit_Game.html", params = "play",method = RequestMethod.POST)
    public String playALL(String params,ModelMap model){
	
		doPlayGame(model);
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	
	private void doPlayGame(ModelMap model)
	{
		Dx4MetaGame metaGame = (Dx4MetaGame) model.get("currMetaGame");
		Dx4PlayGame playGame = (Dx4PlayGame) model.get("currPlayGame");
		log.trace("savePlayDate : playGame " + playGame);
		dx4Services.setMetaBetResults(metaGame,playGame);				// set and store results for matching metabets
		
		try {
			dx4Services.mailWinWorkbooks(metaGame,playGame);
		} catch (Dx4ServicesException e) {
			e.printStackTrace();
			log.error("mailWinWorkbooks - failed.");
		}
		
		playGame = metaGame.getNextGameAvailableForBet();
		if (dx4Services.getFloatPayoutMgr() != null)
			dx4Services.getFloatPayoutMgr().initializeNumberFloatPayouts(playGame);
	}
	
	@RequestMapping(value = "/edit_Game.html", params = "invalidDraw",method = RequestMethod.POST)
    public String invalidDraw(String params,ModelMap model){
	
		Dx4PlayGame playGame = (Dx4PlayGame) model.get("currPlayGame");
		Date date = DateUtil.getDateWithZeroedTime(playGame.getPlayDate());
		dx4Services.getDx4Home().removeResultsForDate(date);
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	
	/*
	@RequestMapping(params = "Dx4", method = RequestMethod.POST)
    public Object storeGameResults(@ModelAttribute("drawResultsForm") DrawResultsForm drawResultsForm,
    								ModelMap model)
	{
		DrawResult result = drawResultsForm.getResult();
		
		log.trace("storeGameResults : " + result);
		try
		{
			drawResultsForm.validate();
		}
		catch (Dx4FormValidationException e)
		{
			log.trace("storeGameResults : " +e.getMessage());
			drawResultsForm = new DrawResultsForm();
			drawResultsForm.setInit(result);
			drawResultsForm.setMessage(e.getMessage());
			return new ModelAndView("externalGameResult", "drawResultsForm", drawResultsForm );
		}
		
		GregorianCalendar gc = new GregorianCalendar();
		result.setDate(gc.getTime());
		dx4Services.storeDrawResult(result);
		
		doPlayGame(result,model);
		
		return "redirect:../adm/processAdmin.html?return_admin";
	}
	*/
	
	@RequestMapping(value = "/edit_Game.html", params = "method=playGame",method = RequestMethod.GET)
    public Object playGame(String params,ModelMap model,HttpServletRequest request){
        
		log.trace("playGame with : " + params);
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController, or admin_home
		log.trace("got session attribute : currUser : " +  currUser );	
		model.addAttribute("currUser", currUser);
		
		int pos = params.indexOf(':');
		String metaGameName = params.substring(0,pos);
		long playGameId = Long.parseLong(params.substring(pos+1));
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(metaGameName);
		model.addAttribute("currMetaGame", metaGame);
		Dx4PlayGame playGame = metaGame.getPlayGameById(playGameId);
		model.addAttribute("currPlayGame",playGame);
		ExternalGameResults externalGameResults=null;
		
		externalGameResults =  dx4Services.getExternalService().getActualExternalGameResults(playGame.getPlayDate());
		if (externalGameResults == null)
			return "redirect:../adm/processAdmin.html?return_admin1&message=" + "No draws played for date";
		
		model.addAttribute("currXGR", externalGameResults);
		
		log.trace("AdminGameController::playGame - " + externalGameResults);
	    DrawResultsForm drawResultsForm = new DrawResultsForm();
		drawResultsForm.initialiseValues();
		drawResultsForm.getInit().setProvider(dx4Services.getDx4Home().getProviderByName("Dx4"));
		String drawNo = dx4Services.getDx4Home().getNextDrawNoForProvider("Dx4");
		drawResultsForm.getInit().setDrawNo(drawNo);
		drawResultsForm.getInit().setDate(externalGameResults.getDraws().get(0).getDate());
		
		log.trace("gameResults : " + drawResultsForm);
		return new ModelAndView("drawGameResult", "drawResultsForm", drawResultsForm );
    }
	
	
	/*
	@RequestMapping(params = "method=useGame",method = RequestMethod.GET)
    public ModelAndView useGame(String params,ModelMap model){
        
		log.trace("AdminGameController::useGame : " + params);
		int pos = params.indexOf(':');
		long playGameId = Long.parseLong(params.substring(0,pos));
		String provider = params.substring(pos+1);
		Dx4MetaGame metaGame = (Dx4MetaGame) model.get("currMetaGame");
		log.trace("AdminGameController::useGame : " + metaGame);
		Dx4PlayGame playGame = metaGame.getPlayGameById(playGameId);
		ExternalGameResults externalGameResults = (ExternalGameResults) model.get("currXGR");
		
		DrawResultsForm drawResultsForm = new DrawResultsForm();
		drawResultsForm.initialiseValues();
		drawResultsForm.getInit().setProvider("Dx4");
		String drawNo = dx4Services.getDx4Home().getNextDrawNoForProvider("Dx4");
		drawResultsForm.getInit().setDrawNo(drawNo);
		GregorianCalendar gc = new GregorianCalendar();
		drawResultsForm.getInit().setDate(gc.getTime());
		
		log.trace("gameResults : " + drawResultsForm);
		return new ModelAndView("externalGameResult", "drawResultsForm", drawResultsForm );
		
		if (playGame!=null)
		{
			log.trace("Got Play Game : " + playGame);
			playGame.setWinLine(externalGameResults.getParseResults().get(resultIndex).getFirstPrize());
			playGame.setPlayedAt(externalGameResults.getParseResults().get(resultIndex).getDate());
			
			log.trace("AdminGameController::useGame : playGame " + playGame);
			dx4Services.setMetaBetResults(metaGame,playGame);				// set and store results for matching metabets
		}
		else
			log.error("AdminGameController::useGame : playGame not found");
		
		metaGame = dx4Services.getDx4Home().getMetaGame(metaGame.getName());		// refresh
		model.addAttribute("currMetaGame", metaGame);
		AdminGamePlayDateForm adminGamePlayDateForm = initialiseAdminGamePlayDateForm();
        ModelAndView modelAndView = new ModelAndView("adminGame" );
        modelAndView.addObject("adminGamePlayDateForm", adminGamePlayDateForm);
        return modelAndView;
        
    }
	
	
	@RequestMapping(params = "savePlayGame",method = RequestMethod.POST)
    public ModelAndView savePlayDate(@ModelAttribute("adminGamePlayDateForm") AdminGamePlayDateForm adminGamePlayDateForm,
    			ModelMap model){
        
		AdminGamePlayDateCommand command = adminGamePlayDateForm.getCommand();
		Dx4MetaGame metaGame = (Dx4MetaGame) model.get("currMetaGame");
		log.trace("savePlayGame : " + metaGame);
		Dx4PlayGame playGame = metaGame.getPlayGameById(command.getPlayGameId());
		if (playGame!=null)
		{
			try
			{
				adminGamePlayDateForm.validatePlayed(playGame);
			}
			catch (Dx4FormValidationException e)
			{ 
				log.error("savePlayDate failed : " + e.getMessage());
				return goBackToGameSavePlayed( metaGame, command.getPlayGameId(), e.getMessage() );
			}
			
			log.trace("savePlayDate Got Play Game : " + playGame);
			log.trace("savePlayDate Got Choices : " + command.getChoices());
			String winLine = "";
			for (String choice : command.getChoices())
				winLine += choice;
			log.trace("savePlayDate Got Win Line : " + winLine);
//			playGame.setWinLine(winLine);
			playGame.setPlayedAt(command.getPlayDate());
			log.trace("savePlayDate : playGame " + playGame);
			dx4Services.getDx4Home().updatePlayGame(playGame);
			dx4Services.setMetaBetResults(metaGame,playGame);				// set and store results for matching metabets
		}
		else
			log.error("savePlayGame playGame for index : " + command.getPlayGameId() + " is null");
		metaGame = dx4Services.getDx4Home().getMetaGame(metaGame.getName());  		// refresh
		model.addAttribute("currMetaGame", metaGame);
		ModelAndView modelAndView = new ModelAndView("adminGame" );
        return modelAndView;
    }
	
	private ModelAndView goBackToGameSavePlayed(Dx4MetaGame metaGame,long playGameId, String message)
	{
		AdminGamePlayDateForm adminGamePlayDateForm = initialiseAdminGamePlayDateForm();
		adminGamePlayDateForm.setMessage(message);
		adminGamePlayDateForm.setPlayGame(metaGame.getPlayGameById(playGameId));
	    ModelAndView modelAndView = new ModelAndView("adminGameSavePlayed" );	    
        modelAndView.addObject("adminGamePlayDateForm", adminGamePlayDateForm);
        return modelAndView;
	}
	/*
	@RequestMapping(params = "method=playGame",method = RequestMethod.GET)
    public ModelAndView playGame(String params,ModelMap model,HttpServletRequest request){
        
		log.trace("playGame with : " + params);
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController, or admin_home
		log.trace("got session attribute : currUser : " +  currUser );	
		model.addAttribute("currUser", currUser);
		
		int pos = params.indexOf(':');
		String metaGameName = params.substring(0,pos);
		long playGameId = Long.parseLong(params.substring(pos+1));
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(metaGameName);
		model.addAttribute("currMetaGame", metaGame);
		AdminGamePlayDateForm adminGamePlayDateForm = initialiseAdminGamePlayDateForm();
		adminGamePlayDateForm.setPlayGame(metaGame.getPlayGameById(playGameId));
	    ExternalGameResults externalGameResults =  dx4Services.getExternalService().getExternalGameResults();
	    log.trace("AdminGameController::playGame - " + externalGameResults);
	    model.addAttribute("currXGR", externalGameResults);
	    ModelAndView modelAndView = new ModelAndView("adminGameSavePlayed" );
        modelAndView.addObject("adminGamePlayDateForm", adminGamePlayDateForm);
        return modelAndView;
    }
	*/
	
	/*
	@RequestMapping(params = "addPlayDate",method = RequestMethod.POST)
    public ModelAndView addPlayDate(@ModelAttribute("adminGamePlayDateForm") AdminGamePlayDateForm adminGamePlayDateForm,
    		ModelMap model){
        
		AdminGamePlayDateCommand command = adminGamePlayDateForm.getCommand();
		Dx4MetaGame metaGame = (Dx4MetaGame) model.get("currMetaGame");
		try
		{
			boolean allowPastGame = Dx4Config.getProperties().getProperty("dx4.allowPastGame", "false").equals("true");
			adminGamePlayDateForm.validate(dx4Services.getDx4Home(),metaGame,allowPastGame);
		}
		catch (Dx4FormValidationException e)
		{
			adminGamePlayDateForm = initialiseAdminGamePlayDateForm();
			adminGamePlayDateForm.setMessage(e.getMessage());
			return new ModelAndView("adminGame", "adminGamePlayDateForm", adminGamePlayDateForm );
		}
		log.trace("Got AdminGameCommand : " + command);
		Date playDate  = command.getPlayDate();
		log.trace("Got Play Date : " + playDate);
		Dx4PlayGame playGame = new Dx4PlayGame();
		playGame.setPlayDate(command.getPlayDate());
		dx4Services.getDx4Home().insertPlayGame(metaGame, playGame);
		
		Dx4Admin currUser = (Dx4Admin) model.get("currUser");
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(currUser,dx4Services.getDx4Home());
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
        
    }
*/
	
	@RequestMapping(value = "/edit_Game.html", params = "cancel",method = RequestMethod.POST)
    public ModelAndView cancel(ModelMap model){
        
		Dx4Admin currUser = (Dx4Admin) model.get("currUser");
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(currUser,dx4Services.getDx4Home());
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
	}
	
	
	private AdminGamePlayDateForm initialiseAdminGamePlayDateForm()
	{
		AdminGamePlayDateForm adminGamePlayDateForm = new AdminGamePlayDateForm();
		AdminGamePlayDateCommand command = new AdminGamePlayDateCommand();
		adminGamePlayDateForm.setCommand(command);
        GregorianCalendar gc = new GregorianCalendar();
        gc.roll(Calendar.DAY_OF_YEAR,5);
        adminGamePlayDateForm.setNextPlayDate(gc.getTime());
        return adminGamePlayDateForm;
	}
}
