package org.dx4.secure.web.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameTypePayoutAccessor;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4ProviderJson;
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
@SessionAttributes({"currUser","currMetaGame","gameTypeList","payOutList","multiStakeList"}) 

@RequestMapping(value = "/admCmd")
public class AdminCommandController {
	 
	private static final Logger log = Logger.getLogger(AdminCommandController.class);
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@ModelAttribute("stakeList")
	public List<Double> populateStakeList() {
 
		List<Double> stakeList = new ArrayList<Double>();
		stakeList.add(1.00);
		stakeList.add(5.00);
		stakeList.add(10.00);
		stakeList.add(15.00);
		stakeList.add(20.00);
		stakeList.add(50.00);
		stakeList.add(100.00);
		return stakeList;
	}
	
	private Dx4GameTypeJson populateGameTypeList(ModelMap model,Dx4MetaGame metaGame) {
 
		List<Dx4GameTypeJson> gameTypeList = new ArrayList<Dx4GameTypeJson>();
		addGameType(gameTypeList,metaGame,Dx4GameTypeJson.D4Small);
		addGameType(gameTypeList,metaGame,Dx4GameTypeJson.D4Big);
		addGameType(gameTypeList,metaGame,Dx4GameTypeJson.ABCA);
		addGameType(gameTypeList,metaGame,Dx4GameTypeJson.ABCC);
		addGameType(gameTypeList,metaGame,Dx4GameTypeJson.D2A);
		model.addAttribute("gameTypeList",gameTypeList);
		if (gameTypeList.size()>0)
			return gameTypeList.get(0);
		return null;
	}

	private void addGameType(List<Dx4GameTypeJson> gameTypeList,Dx4MetaGame metaGame,Dx4GameTypeJson type)
	{
		if (metaGame.getGameByType(type) != null)
			return;
		gameTypeList.add(type);
	}
	
	private void populatePayOutList(ModelMap model,Dx4GameTypeJson gameType)
	{
		model.addAttribute("payOutList",Dx4GameTypePayoutAccessor.getPayOuts(gameType));
		log.debug("Using payOutList for gameType : " + gameType);
		for (Dx4PayOut payOut : Dx4GameTypePayoutAccessor.getPayOuts(gameType))
			log.debug(payOut);		
	}
	
	private void populatePayOutsFromGame(ModelMap model,Dx4Game game)
	{
		model.addAttribute("payOutList",game.getPayOuts());
			
	}
	
	@RequestMapping(value = "/add_Game.html",params = "method=activate",method = RequestMethod.GET)
    public ModelAndView activateGame(String name,ModelMap model,HttpServletRequest request)
	{
		log.info("Activating game : " + name);
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
		
		HttpSession session = request.getSession(false);
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.debug("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		
		if (!currUser.getGameGroup().containsMetaGame(metaGame))						// check to stop refresh
			dx4Services.addMetaGameToGroup(currUser,metaGame);
		else
			log.error("ERROR ACTIVATING GAME:" + metaGame.getName() + " ALREADY ACTIVE");
		
		AdminCommandForm adminCommandForm = new AdminCommandForm(dx4Services.getDx4Home(),currUser);
		log.debug(adminCommandForm);
		return new ModelAndView("adminCommand" , "adminCommandForm", adminCommandForm);
	}
	
	@RequestMapping(value = "/add_Game.html",params = "method=deactivate",method = RequestMethod.GET)
    public ModelAndView deactivateGame(String name,ModelMap model,HttpServletRequest request)
	{
		log.info("Deactivating game : " + name);
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
		
		HttpSession session = request.getSession(false);
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.debug("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		
		if (currUser.getGameGroup().containsMetaGame(metaGame))
			dx4Services.removeMetaGameFromGroup(currUser,metaGame);
		else
			log.error("ERROR DEACTIVATING GAME:" + metaGame.getName() + " ALREADY INACTIVE");		// check to stop refresh
		
		AdminCommandForm adminCommandForm = new AdminCommandForm(dx4Services.getDx4Home(),currUser);
		log.debug(adminCommandForm);
		return new ModelAndView("adminCommand" , "adminCommandForm", adminCommandForm);
	}
	
	// @RequestMapping(value = "/add_Game.html", params = "addNewGame", method = RequestMethod.POST)
	@RequestMapping(value = "/add_Game.html",params = "method=addNewGame",method = RequestMethod.GET)
    public ModelAndView methodAddGame(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.debug("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.debug("got session attribute : currUser : " +  currUser );
		
		model.addAttribute("currUser",currUser);
		Dx4MetaGame currMetaGame = new Dx4MetaGame();
		List<Dx4ProviderJson> providerList = dx4Services.getDx4Home().getProviders();
		List<String> providers = new ArrayList<String>();
		for (Dx4ProviderJson provider : providerList)
			providers.add(provider.getName());
		currMetaGame.setProviders(providers);			// default to all providers;
		
		model.addAttribute("currMetaGame",currMetaGame);
		AdminAddGameForm adminAddGameForm = new AdminAddGameForm(dx4Services.getDx4Home(),currMetaGame);
		Dx4GameTypeJson gameType = populateGameTypeList(model,currMetaGame);
		adminAddGameForm.setGameType(gameType);
		populatePayOutList(model,gameType);
		
        return new ModelAndView("addGame" , "adminAddGameForm", adminAddGameForm);
    }
	
	@RequestMapping(value = "add_Game.html", params = "cancelNewGame", method = RequestMethod.GET)
    public ModelAndView cancelNewGame(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.debug("getting session attribute : sCurrUser ");
		Dx4Admin currUser = (Dx4Admin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.debug("got session attribute : currUser : " +  currUser );
		
		AdminDetailsForm adminDetailsForm = new AdminDetailsForm(currUser,dx4Services.getDx4Home());
        return new ModelAndView("adminHome" , "adminDetailsForm", adminDetailsForm);
    }
	
	@RequestMapping(value = "/save_Game.html", params = "addGame", method = RequestMethod.POST)
    public ModelAndView methodSaveGame(@ModelAttribute("adminAddGameForm") AdminAddGameForm adminAddGameForm,ModelMap model) {
       
		AdminAddGameCommand adminAddGameCommand = adminAddGameForm.getCommand();
		Dx4MetaGame metaGame = adminAddGameCommand.getMetaGame();
		Dx4MetaGame currMetaGame = (Dx4MetaGame) model.get("currMetaGame");
		currMetaGame.setName(metaGame.getName());
		currMetaGame.setDescription(metaGame.getDescription());
		currMetaGame.setProviders(adminAddGameCommand.getUseProviders());
		log.debug("GOT: " + adminAddGameCommand);
		String message = validateDx4MetaGame(currMetaGame);
		if (message.isEmpty())
		{
			Dx4Admin currUser = (Dx4Admin) model.get("currUser");
			log.debug("Storing metagame: " + currMetaGame);
			try
			{
				dx4Services.storeMetaGame(currUser,currMetaGame);
			}
			catch (PersistenceRuntimeException e)
			{
				return storeGameFailed(model,currMetaGame,e.getMessage());
			}
			
			AdminCommandForm adminCommandForm = new AdminCommandForm(dx4Services.getDx4Home(),currUser);
			log.debug(adminCommandForm);
			return new ModelAndView("adminCommand" , "adminCommandForm", adminCommandForm);
		}
		
		return storeGameFailed(model,currMetaGame,message);
    }
	
	private ModelAndView storeGameFailed(ModelMap model,Dx4MetaGame currMetaGame,String message)
	{
		AdminAddGameForm adminAddGameForm = new AdminAddGameForm(dx4Services.getDx4Home(),currMetaGame);
		model.addAttribute("currMetaGame",currMetaGame);
		Dx4GameTypeJson gameType = populateGameTypeList(model,currMetaGame);
		adminAddGameForm.setGameType(gameType);
		populatePayOutList(model,gameType);
		adminAddGameForm.setMessage(message);
        return new ModelAndView("addGame" , "adminAddGameForm", adminAddGameForm);
	}
	
	private String validateDx4MetaGame(Dx4MetaGame metaGame)
	{
		if (metaGame.getName()==null || metaGame.getName().length()==0)
			return "Game Name must be set";
		if (metaGame.getDescription()==null || metaGame.getDescription().length()==0)
			return "Game Description must be set";
		if (metaGame.getProviders().size()==0)
			return "At least one provider must be checked";
		if (metaGame.getGames().isEmpty())
			return "At least one bet type must be set";	
		
		return "";
	}
	
	
	@RequestMapping(value = "/save_Game.html", params = "cancel", method = RequestMethod.POST)
	public ModelAndView methodCancelGame(@ModelAttribute("adminAddGameForm") AdminAddGameForm adminAddGameForm,ModelMap model)
	{
		Dx4Admin currUser = (Dx4Admin) model.get("currUser");
		AdminCommandForm adminCommandForm = new AdminCommandForm(dx4Services.getDx4Home(),currUser);
		log.debug(adminCommandForm);
		return new ModelAndView("adminCommand" , "adminCommandForm", adminCommandForm);
	}
	
	
	@RequestMapping(value = "/save_Game.html", params = "refreshPrizes", method = RequestMethod.POST)
    public ModelAndView methodRefreshPrizes(@ModelAttribute("adminAddGameForm") AdminAddGameForm adminAddGameForm,
    		ModelMap model) {
       
		Dx4MetaGame currMetaGame = (Dx4MetaGame) model.get("currMetaGame");
		log.debug("methodRefreshPrizes currMetaGame : " + currMetaGame);
		AdminAddGameCommand adminAddGameCommand = adminAddGameForm.getCommand();
		log.debug("GOT methodRefreshPrizes: " + adminAddGameCommand);
		Dx4MetaGame metaGame = adminAddGameCommand.getMetaGame();
		Dx4Game game = metaGame.getGames().get(0);
		currMetaGame.setName(metaGame.getName());
		currMetaGame.setDescription(metaGame.getDescription());
		currMetaGame.setProviders(adminAddGameCommand.getUseProviders());
		log.info("methodRefreshPrizes currMetaGame : metaGame " + currMetaGame);
		populateGameTypeList(model,currMetaGame);
		adminAddGameForm = new AdminAddGameForm(dx4Services.getDx4Home(),currMetaGame);
		adminAddGameForm.setGameType(game.getGtype());
		populatePayOutList(model,game.getGtype());
		log.debug("methodRefreshPrizes metagame: " + currMetaGame);
		model.addAttribute("currMetaGame", currMetaGame);
		adminAddGameForm.setCommand(adminAddGameCommand);
		
		return new ModelAndView("addGame" , "adminAddGameForm", adminAddGameForm);
    }
	
	@RequestMapping(value = "/save_Game.html", params = "addGameBet", method = RequestMethod.POST)
    public ModelAndView methodSaveGameBet(@ModelAttribute("adminAddGameForm") AdminAddGameForm adminAddGameForm,
    		ModelMap model) {
      
		
		AdminAddGameCommand adminAddGameCommand = adminAddGameForm.getCommand();
		log.debug("GOT: " + adminAddGameCommand);
		Dx4MetaGame metaGame = adminAddGameCommand.getMetaGame();
		Dx4MetaGame currMetaGame = (Dx4MetaGame) model.get("currMetaGame");
		currMetaGame.setName(metaGame.getName());
		currMetaGame.setDescription(metaGame.getDescription());
		currMetaGame.setProviders(adminAddGameCommand.getUseProviders());
		log.debug("CURRMETAGAME BEFORE: " + currMetaGame);
		Dx4Game game = metaGame.getGames().get(0);
		String msg = checkPayouts(game);
		adminAddGameForm = new AdminAddGameForm(dx4Services.getDx4Home(),currMetaGame);
		if (msg != null)
		{
			log.debug("methodSaveGameBet : " + msg);
			populateGameTypeList(model,currMetaGame);
			adminAddGameForm.setGameType(game.getGtype());			
			populatePayOutsFromGame(model,game);				// return to failed gtype
			adminAddGameForm.setMessage(msg);
			return new ModelAndView("addGame" , "adminAddGameForm", adminAddGameForm);
		}
		
		log.debug("Adding game to currMetaGame: " + game);
		currMetaGame.getGames().add(game);
		log.debug("Added game to currMetaGame: " + currMetaGame);
		model.addAttribute("currMetaGame", currMetaGame);
		log.debug("CURRMETAGAME: " + currMetaGame);
		Dx4GameTypeJson gameType = populateGameTypeList(model,currMetaGame);
		adminAddGameForm.setGameType(gameType);
		populatePayOutList(model,gameType);
		
        return new ModelAndView("addGame" , "adminAddGameForm", adminAddGameForm);
    }

	private String checkPayouts(Dx4Game game) {
		String msg = "";
		for (Dx4PayOut payOut : game.getPayOuts())
		{
			if (payOut.getPayOut()<=0.0)
			{
				msg += payOut.getType().name() + ",";
			}
		}
		if (msg.isEmpty())
			return null;
		
		return msg.substring(0,msg.length()-1) + " prize for " + game.getGtype() + " not set..";
	}
	
	

}

