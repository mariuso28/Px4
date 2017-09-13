package org.dx4.secure.web.betInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.persistence.MetaBetRowMapperPaginated;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.player.Dx4Player;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.player.MetaBetWinForm;
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
@SessionAttributes({"currPlayer","currQuickBet","currExpandedBets","currXGR","currMbrmp"}) 

@RequestMapping(value = "/qkBet")
public class QuickBetController
{
	private static final Logger log = Logger.getLogger(QuickBetController.class);
	private Dx4Services dx4Services;
	

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@RequestMapping(value = "/create_QuickBet.html", params = "method=createQuickBet",method = RequestMethod.GET)
    public ModelAndView createQuickBet(String name,ModelMap model,HttpServletRequest request){
		
		log.trace("createQuickBet method with : " + name);
		HttpSession session = null;
		
		try {
			session = request.getSession(false);
			if (session!=null)
				log.info("session : " + session.getId());
		} catch(IllegalStateException ex)
		{
			log.error("Invalid session");
    	}
		
		
		log.info("getting session attribute : sCurrUser ");
		Dx4Player currPlayer = (Dx4Player) session.getAttribute("sCurrUser");				// as the thing comes from PlayerController
		log.info("got session attribute : currUser : " +  currPlayer );
	
		model.addAttribute("currPlayer",currPlayer);
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
    	
        Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
        QuickBet currQuickBet = new QuickBet(metaGame);
        model.addAttribute("currQuickBet",currQuickBet);
       
        initializeMetaBetRowMapperPaginated( model, currPlayer, currQuickBet);
        
        return goQuickBet(model);
    }
	
	private void initializeMetaBetRowMapperPaginated(ModelMap model,Dx4Player currPlayer,QuickBet currQuickBet)
	{
		int pageSize = 12;
		String pages = Dx4Config.getProperties().getProperty("dx4.paginate.quickbet","12");
		try
		{
			pageSize = Integer.parseInt(pages);
		}
		catch (NumberFormatException e)
		{
			log.error("Could not parse dx4.paginate.quickbet : " + pages + " using pagesize 12");
			pageSize = 12;
		}
		MetaBetRowMapperPaginated currMbrmp = dx4Services.getDx4Home().getMetaBetRowMapperPaginated(pageSize,currPlayer,Dx4BetRetrieverFlag.ALL,currQuickBet.getMetaGame());
		currMbrmp.getNextPage();

		model.addAttribute("currMbrmp",currMbrmp);
	}
	
	@RequestMapping(value="/quickBet.html", params="viewCalcRefresh", method = RequestMethod.POST)
	public ModelAndView viewCalcRefresh(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("viewCalc with : " + command);
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.setViewCalc(command.getViewCalc());
		
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
		
		quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	@RequestMapping(value="/quickBet.html", params="viewPrev", method = RequestMethod.POST)
	public ModelAndView viewPrev(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("viewPrev with : " + command);
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
		
    	currQuickBet.setViewPrev(command.getViewPrev());
    	
    	initializeMetaBetRowMapperPaginated( model, currPlayer, currQuickBet);
		
		
		return goQuickBet(model);
	}
	
	@RequestMapping(value="/quickBet.html", params="prevBetsLast", method = RequestMethod.GET)
	public ModelAndView prevBetsLast(ModelMap model)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
		
    	MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
    	currMbrmp.getPrevPage();
		
		model.addAttribute("currMbrmp",currMbrmp);
		
		QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	@RequestMapping(value="/quickBet.html", params="prevBetsNext", method = RequestMethod.GET)
	public ModelAndView prevBetsNext(ModelMap model)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
		
    	MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
    	currMbrmp.getNextPage();
		
		model.addAttribute("currMbrmp",currMbrmp);
		
		QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "addRandomNumber", method = RequestMethod.POST)
	public ModelAndView addRandomNumber(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.info("addNumber with : " + command);
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		while (true)
		{
			String number = getRandomNumber(command,currQuickBet);
			log.info("addRandomNumber generated random : "  + number);
			if (addNumber(model,currQuickBet,number))
				break;
		}
		return goQuickBet(model);
	}
	
	private String getRandomNumber(QuickBetCommand command,
			QuickBet currQuickBet) {
		
		String number = command.getNumber();
		int start = 0;
		String rand = "";
		if (number!=null)
		{
			start = number.length();
			rand = number;
		}
		int digits = 4;
		if (command.getNumber4D().equals("3D"))
			digits = 3;
		Random random = new Random();
		while (start < digits)
		{
			Integer num = random.nextInt(10);
			rand = num.toString() + rand; 
			start++;
		}
		
		return rand;
	}
	
	private boolean addNumber(ModelMap model,QuickBet currQuickBet,String number)
	{
		if (currQuickBet.hasNumber(number))
			return false;
		if (check3DigitOverlap(currQuickBet,number))
			return false;
		
		List<String> invalidNos = validateNumber(number,currQuickBet,model);
		if (!invalidNos.isEmpty())
			return false;
		
		currQuickBet.addNumber(number, dx4Services);
		return true;
	}

	@RequestMapping(value = "/quickBet.html", params = "switchDigits", method = RequestMethod.POST)
	public ModelAndView switchDigits(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.info("switchDigits with : " + command.getNumber4D());
		quickBetForm = setUpQuickBetForm(model);
		quickBetForm.setNumber4D(command.getNumber4D());
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "setPlayDate", method = RequestMethod.POST)
	public ModelAndView setPlayDate(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.info("setPlayDate with : " + command.getPlayDate());
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
		try
		{
			Date date = df1.parse(command.getPlayDate());
			Dx4PlayGame playGame = currQuickBet.getMetaGame().getPlayGame(date);
			log.info("Play game is : " + playGame);
			currQuickBet.setPlayGame(playGame);
		}
		catch (ParseException e)
		{
			log.error("Couldn't parse date : " + command.getPlayDate());
		}
		quickBetForm = setUpQuickBetForm(model);
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	
	@RequestMapping(value = "/quickBet.html", params = "addNumber", method = RequestMethod.POST)
	public ModelAndView addNumber(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.info("addNumber with : " + command);
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		String num = getValidNumber(command,currQuickBet);
		if (num!=null)
			return addNumber(num,model);
		return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	private String getValidNumber(QuickBetCommand command,QuickBet quickBet)
	{
		String use = "";
		String num = command.getNumber();
		for (int i=0; i<num.length(); i++)
		{
			Character ch = num.charAt(i);
			if (Character.isDigit(ch))
				use += (new Character(ch)).toString();
		}
		if (use.length()==3)
			if (quickBet.isEnable3D())
				return use;
		if (use.length()==4)
			if (quickBet.isEnable4D())
				return use;
		return null;
	}
	
	//<a href="maintainNumber.html?method=cancelNumber&number=${number}
	@RequestMapping(value = "/maintainNumber.html", params = "method=cancelNumber",method = RequestMethod.GET)
    public ModelAndView maintainNumber(String number,ModelMap model,HttpServletRequest request)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.removeNumber(number);
		return goQuickBet(model);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "refreshQuickPayouts", method = RequestMethod.POST)
    public ModelAndView refreshQuickPayouts(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("refreshQuickPayouts with : " + command);
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.updateStakes(command);
		
		return refreshUseGame(command, model);
    }
	
	@RequestMapping(value = "/quickBet.html", params = "refreshQuickPayoutsQ", method = RequestMethod.POST)
    public ModelAndView refreshQuickPayoutsQ(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("refreshQuickPayouts with : " + command);
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.updateStakesQ(command);
		
		return refreshUseGame(command, model);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "refreshUseGame", method = RequestMethod.POST)
    public ModelAndView refreshUseGame(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("refreshUseGame with : " + command);
		return refreshUseGame(command, model);
    }
	
	@RequestMapping(value = "/quickBet.html", params = "cancelUseGame", method = RequestMethod.POST)
    public ModelAndView cancelUseGame(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("cancelUseGame with : " + command);
		return refreshUseGame(command, model);
    }
	
	private ModelAndView refreshUseGame(QuickBetCommand command,ModelMap model)
	{
		log.trace("refreshUseGame with : " + command);

		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.updateUseGames(command);
		try
		{
			currQuickBet.verifyUseGames();
		}
		catch (Dx4FormValidationException e)
		{
			log.trace("refreshUseGame : " + e.getMessage());
			Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
			@SuppressWarnings("unchecked")
			HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");

			MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
			
			QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
			
			currQuickBet.setConfirmCommand(ConfirmCommand.UseGames);
			quickBetForm.setMessage(e.getMessage());
			return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
		}
		return goQuickBet(model);
	}

	@RequestMapping(value = "/quickBet.html", params = "refreshProviders", method = RequestMethod.POST)
    public ModelAndView refreshProviders(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		log.trace("refreshProviders with : " + command);
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		currQuickBet.updateProviders(command);
		return goQuickBet(model);
    }
	
	public ModelAndView goQuickBet(ModelMap model)
	{
		QuickBetForm quickBetForm = setUpQuickBetForm(model);
			
        // log.debug("Calling quickBetCreate : with " + quickBetForm);
        return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	private QuickBetForm setUpQuickBetForm(ModelMap model)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		
		MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
		
		return new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
	}
	
	//<a href="lookup.html?method=4D
	@RequestMapping(value = "/quickBet.html", params = "lookup4D", method = RequestMethod.POST)
	public String lookup4D(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,
			ModelMap model,HttpServletRequest request){
		log.trace("quickBet method lookup4D");
		
		storeSessionAttributes(model,request);
		return "redirect:../anal/processAnalyticExternal.html?numbers_refresh&type="+4;
	}
	
	@RequestMapping(value = "/quickBet.html", params = "lookup3D", method = RequestMethod.POST)
	public String lookup3D(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,
			ModelMap model,HttpServletRequest request){
		log.trace("quickBet method lookup4D");
		
		storeSessionAttributes(model,request);
		return "redirect:../anal/processAnalyticExternal.html?numbers_refresh&type="+2;
	}
	
	private void storeSessionAttributes(ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);				// xfer to analytics controller
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		
		session.setAttribute("sCurrQuickBet",currQuickBet);
		session.setAttribute("sCurrPlayer",currPlayer);
		session.setAttribute("sCurrActiveGame",currQuickBet.getMetaGame().getName());
	}
	
	@RequestMapping(value = "/number_bet.html", params = "method=setNumber", method = RequestMethod.GET)	
    public ModelAndView quickNumberBet(String number,ModelMap model,HttpServletRequest request){
		
		log.trace("quickNumberBet method with  " + number);
		
		restoreSessionAttributes(model,request);
		return addNumber(number,model);
	}
	
	@RequestMapping(value = "/number_basket.html", params = "method=setBasket", method = RequestMethod.GET)	
    public ModelAndView quickNumberBasket(String numbers,ModelMap model,HttpServletRequest request){
		log.info("quickNumberBet method with  " + numbers);
		
		List<String> numberList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(numbers,":");
		while (st.hasMoreTokens())
			numberList.add(st.nextToken());
		restoreSessionAttributes(model,request);
		return addBasket(numberList,model);
	}
	
	private ModelAndView addBasket(List<String> numberList, ModelMap model) {
		
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		List<String> allInvalidNos = new ArrayList<String>();
			
		for (String number : numberList)
		{
			if (check3DigitOverlap(currQuickBet,number))
				continue;

			List<String> invalidNos = validateNumber(number,currQuickBet,model);
			if (invalidNos.isEmpty())
				currQuickBet.addNumber(number,dx4Services);
			else
				allInvalidNos.addAll(invalidNos);
		}

		if (allInvalidNos.isEmpty())
		{
			return goQuickBet(model);
		}
		
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
		
		QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
		quickBetForm.createInvalidNosMessage(allInvalidNos);
	    return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}

	private ModelAndView addNumber(String number,ModelMap model)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		if (check3DigitOverlap(currQuickBet,number))
			return goQuickBet(model);
		
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		List<String> invalidNos = validateNumber(number,currQuickBet,model);
		if (invalidNos.isEmpty())
		{
			currQuickBet.addNumber(number,dx4Services);
			return goQuickBet(model);
		}
		
		@SuppressWarnings("unchecked")
		HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
		MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
		
		QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
		quickBetForm.createInvalidNosMessage(invalidNos);
	    return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	private boolean check3DigitOverlap(QuickBet currQuickBet,String number)
	{
		for (String num : currQuickBet.getNumbers())
		{
			if (num.length()==4 && number.length()==3)
			{
				if (number.equals(num.substring(1)))
					return true;
			}
			else
			if (num.length()==3 && number.length()==4)
			{	
				if (num.equals(number.substring(1)))
				{
					 currQuickBet.getNumbers().remove(num);
					 return false;						// get rid of the existing 3 digits
				}
			}
		}
		return false;
	}
	
	private List<String> validateNumber(String number,QuickBet currQuickBet,ModelMap model)
	{
		String strictValidation = Dx4Config.getProperties().getProperty("dx4.strictNumberValidation", "off");
		
		log.info("validateNumber is : " + strictValidation);
		if (strictValidation.equalsIgnoreCase("off"))
			return new ArrayList<String>();
		
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		Dx4MetaBet metaBet = currQuickBet.createMetaBet(dx4Services, currPlayer,false);
		return currQuickBet.validateNumberExposure(dx4Services,metaBet,number);
	}

	private void restoreSessionAttributes(ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);						// xfer from analytics controller
		QuickBet currQuickBet = (QuickBet) session.getAttribute("sCurrQuickBet");
		Dx4Player currPlayer = (Dx4Player) session.getAttribute("sCurrPlayer");
		
		model.addAttribute("currPlayer",currPlayer);
		model.addAttribute("currQuickBet",currQuickBet);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "method=cancelQuickBet", method = RequestMethod.GET)
    public Object cancelQuickBetGet(ModelMap model)
	{	
		return cancelQuickBet(model);
    }
	
	@RequestMapping(value = "/quickBet.html", params = "cancelQuickBet", method = RequestMethod.POST)
    public Object cancelQuickBet(ModelMap model)
	{	
		Dx4Player player = (Dx4Player) model.get("currPlayer");
		return returnToPlayerHome(player);
    }
	
	@RequestMapping(value = "/quickBet.html", params = "confirmQuickBet", method = RequestMethod.POST)
    public ModelAndView confirmQuickBet(ModelMap model,HttpServletRequest request)
	{	
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Dx4MetaBet metaBet = null;
		try
		{
			currQuickBet.validate();
			metaBet = currQuickBet.createMetaBet(dx4Services,currPlayer,true);
			if (currQuickBet.checkAvailableFunds(dx4Services,currPlayer,metaBet)==false)
			{
				throw new Dx4FormValidationException("Insufficient funds to support current bet");
			}
			if (metaBet.getPlayGame().getId()==currQuickBet.getMetaGame().getNextGameAvailableForBet().getId())
			{// can only be blocked for next game
				List<String> blockedNumbers = dx4Services.getDx4Home().getBlockedNumbers(metaBet);
				if (!blockedNumbers.isEmpty())
				{
					throw new Dx4FormValidationException("Number choices : " + blockedNumbers + 
							" Maxed out for bet.");
				}
			}
			dx4Services.makePlayerBet(currPlayer,metaBet, UUID.randomUUID());
			sendEmailConfirmation(metaBet);
			// return new ModelAndView("pdfMetaBetView", "currMetaBet", metaBet);
		}
		catch (Dx4FormValidationException e)
		{
			@SuppressWarnings("unchecked")
			HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");
			MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
			
			QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
			
			quickBetForm.setMessage(e.getMessage());
	        // log.debug("Calling quickBetCreate : with " + quickBetForm);
	        return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
		}
		return createQuickBet(metaBet.getMetaGame().getName(),model,request);
    }
	
	private void sendEmailConfirmation(Dx4MetaBet metaBet)
	{
		try
		{
//			PDFStoreMetaBet psb = new PDFStoreMetaBet(metaBet,dx4Services);
//			String text = "Dear " + metaBet.getPlayer().getContact() + " confirmed bets attached.";
//			dx4Services.getMessageService().postMail( metaBet.getPlayer(), "Dx4 Bet Confirmation", text, psb.getPdfPath() );
			log.trace("Sucessfully bet confirmation");
		}
		catch (Exception e)
		{
			log.error("Sending PDF of bet confirmation failed : " + e.getMessage());
		}
	}
	
	@RequestMapping(value = "/quickBet.html", params = "confirmQuickBetCommand", method = RequestMethod.POST)
    public ModelAndView confirmQuickBetCommand(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{
		QuickBetCommand command = quickBetForm.getCommand();
		
		if (command.getConfirmCommand().equals(ConfirmCommand.UseBet))
		{
			log.trace("confirmQuickBetCommand : Confirm UseBet");
			Long metaBetId = Long.parseLong(command.getConfirmCommandObject());
			QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
			useMetaBet(metaBetId,currQuickBet,model);
		}
		
		log.trace("confirmQuickBetCommand with : " + command);
		/*
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		if (command.getConfirmCommand().equals(ConfirmCommand.UseGames))
			currQuickBet.removeEmptyNumbers();
		*/
		
		return goQuickBet(model);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "cancelQuickBetCommand", method = RequestMethod.POST)
    public ModelAndView cancelQuickBetCommand(@ModelAttribute("quickBetForm") QuickBetForm quickBetForm,ModelMap model)
	{	
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		QuickBetCommand command = quickBetForm.getCommand();
		
		if (command.getConfirmCommand().equals(ConfirmCommand.UseGames))
		{
			log.trace("cancelQuickBetCommand : Restoring use games : " + currQuickBet.getRestoreUseGames());
			currQuickBet.restoreUseGames();
		}
		else
		if (command.getConfirmCommand().equals(ConfirmCommand.UseBet))
		{
			log.trace("cancelQuickBetCommand : Do nothing");
		}
		
		return goQuickBet(model);
	}
	
	@RequestMapping(value = "/number_refresh.html", params = "method=cancel", method = RequestMethod.GET)
	public ModelAndView numbersCancelRefresh(ModelMap model,HttpServletRequest request)
	{
		restoreSessionAttributes(model,request);
		return goQuickBet(model);
	}
	
	// quickBet.html?method=useMetaBet&id=${dispBet.metaBet.id}
	@RequestMapping(value = "/quickBet.html",  params = "method=useMetaBet", method = RequestMethod.GET)
	public ModelAndView useMetaBet(String id,ModelMap model)
	{
		QuickBet currQuickBet = (QuickBet) model.get("currQuickBet");
		Long metaBetId = Long.parseLong(id);
		
		if (!currQuickBet.getNumbers().isEmpty())
		{
			Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
			@SuppressWarnings("unchecked")
			HashSet<Long> currExpandedBets = (HashSet<Long>) model.get("currExpandedBets");

			MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
			
			QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
			
			currQuickBet.setConfirmCommand(ConfirmCommand.UseBet);
			currQuickBet.setConfirmCommandObject(metaBetId.toString());
			quickBetForm.setMessage("Using previous bet will overwrite any choices selected");
			return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
		}
		
		return useMetaBet(metaBetId,currQuickBet,model);
	}
	
	public ModelAndView useMetaBet(Long metaBetId,QuickBet currQuickBet,ModelMap model)
	{
		log.trace("metaBetId : " + metaBetId);
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		
		Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetById(currPlayer,metaBetId);
		
		log.trace("metaBet is: " + metaBet);
		
		currQuickBet = new QuickBet(metaBet,dx4Services);
		
		model.addAttribute("currQuickBet",currQuickBet);
		HashSet<Long> currExpandedBets = new HashSet<Long>();
    	model.addAttribute("currExpandedBets",currExpandedBets);
    	
		log.trace("quickBet is: " + currQuickBet);
		
		MetaBetRowMapperPaginated currMbrmp = (MetaBetRowMapperPaginated) model.get("currMbrmp");
		
		QuickBetForm quickBetForm = new QuickBetForm(dx4Services,currPlayer,currQuickBet,currExpandedBets,currMbrmp);
		
        log.trace("Calling quickBetCreate : with " + quickBetForm);
        return new ModelAndView("quickBetCreate" , "quickBetForm", quickBetForm);
	}
	
	@RequestMapping(value = "/quickBet.html",  params = "method=expandBets", method = RequestMethod.GET)
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
		return goQuickBet(model);
	}
	
	public Object returnToPlayerHome(Dx4Player player)
	{
		return "redirect:../play/goPlayerHome.html?username="+player.getEmail();
	}
	
	@RequestMapping(value = "/quickBet.html",  params = "method=expandWins", method = RequestMethod.GET)
	public ModelAndView expandWin(String id,ModelMap model)
	{
		Long metaBetId = Long.parseLong(id);
		Dx4Player currPlayer = (Dx4Player) model.get("currPlayer");
		Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetById(currPlayer, metaBetId);
		
		ExternalGameResults externalGameResults =  dx4Services.getExternalService().getActualExternalGameResults(metaBet.getPlayGame().getPlayedAt()); 
		
		MetaBetWinForm metaBetWinForm = new MetaBetWinForm(externalGameResults,dx4Services.getDx4Home(),metaBet,"+");
		metaBetWinForm.setReturnTarget("QB","quickBet.html?cancelDrawResults");
		model.addAttribute("currXGR", externalGameResults);
		return new ModelAndView("externalPlayerWinResult", "metaBetWinForm", metaBetWinForm);
	}
	
	@RequestMapping(value = "/quickBet.html", params = "cancelDrawResults",method = RequestMethod.GET)
    public ModelAndView cancelDrawResults(String number,ModelMap model,HttpServletRequest request)
	{
		return goQuickBet(model);
	}
	
}
	