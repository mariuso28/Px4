package org.dx4.secure.web.agent;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.bet.persistence.MetaBetExpoRowMapperPaginated;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currOrdering","currActiveGame","currMetaBetExpoPg",
	"currMetaBetExposForCode","currMetaBetExposForChoice",
	"currPage0","currPage1","currPage2"})

@RequestMapping(value = "/currNumExpo")
public class CurrentNumberExposureController {
	private static final Logger log = Logger.getLogger(CurrentNumberExposureController.class);
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}

	@RequestMapping(value = "/processNumberExpo", params = "currNumberExposure", method = RequestMethod.GET)
	public Object currNumberExposure(ModelMap model,HttpServletRequest request) {
	
		HttpSession session = request.getSession();	
		String activeGame = (String) session.getAttribute("sCurrActiveGame");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");
		model.put("currActiveGame",activeGame);
		model.put("currUser",currUser);
		
		log.trace("currNumberExposure with game : " + activeGame);
		
		List<Dx4MetaBetExpoOrder> ordering = new ArrayList<Dx4MetaBetExpoOrder>();
		ordering.add(Dx4MetaBetExpoOrder.choice);
		ordering.add(Dx4MetaBetExpoOrder.tbet);
		ordering.add(Dx4MetaBetExpoOrder.code);
		model.put("currOrdering", ordering);
		
		String ret = initialiseCurrMetaBetExpoPg(model);
		if (ret!=null)
	    {
	    	log.error("No next play game for active game: " + activeGame);
	    	return ret;
	    }
		
		model.addAttribute("currPage1",new Integer(-1));
		model.addAttribute("currPage2",new Integer(-1));
	    
	    CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	private String initialiseCurrMetaBetExpoPg(ModelMap model)
	{
		String activeGame = (String) model.get("currActiveGame");
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(activeGame);
	    Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet();
	    @SuppressWarnings("unchecked")
		List<Dx4MetaBetExpoOrder> ordering = (List<Dx4MetaBetExpoOrder>) model.get("currOrdering");
	    if (playGame==null)
	    {
	    	log.error("No next play game for active game: " + activeGame);
	    	return "redirect:../agnt/processAgent.html?cancelNumberExpo";
	    }
	    
	    model.addAttribute("currPage0",new Integer(-1));
		MetaBetExpoRowMapperPaginated metaBetExpoPg = dx4Services.getDx4Home().getMetaBetExpoRowMapperPaginated((Dx4Agent) currUser,playGame,ordering,32);
		
		model.addAttribute("currMetaBetExpoPg",metaBetExpoPg);
		return null;
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoNext0", method = RequestMethod.GET)
	public ModelAndView currNumberExpoNext0(ModelMap model,HttpServletRequest request)
	{
		Integer currPage0 = (Integer) model.get("currPage0");
		currPage0++;
		model.addAttribute("currPage0",currPage0);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoPrev0", method = RequestMethod.GET)
	public ModelAndView currNumberExpoPrev0(ModelMap model,HttpServletRequest request)
	{
		Integer currPage0 = (Integer) model.get("currPage0");
		currPage0--;
		model.addAttribute("currPage0",currPage0);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoNext1", method = RequestMethod.GET)
	public ModelAndView currNumberExpoNext1(ModelMap model,HttpServletRequest request)
	{
		Integer currPage1 = (Integer) model.get("currPage1");
		currPage1++;
		model.addAttribute("currPage1",currPage1);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoPrev1", method = RequestMethod.GET)
	public ModelAndView currNumberExpoPrev1(ModelMap model,HttpServletRequest request)
	{
		Integer currPage1 = (Integer) model.get("currPage1");
		currPage1--;
		model.addAttribute("currPage1",currPage1);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoNext2", method = RequestMethod.GET)
	public ModelAndView currNumberExpoNext2(ModelMap model,HttpServletRequest request)
	{
		Integer currPage2 = (Integer) model.get("currPage2");
		currPage2++;
		model.addAttribute("currPage2",currPage2);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExpoPrev2", method = RequestMethod.GET)
	public ModelAndView currNumberExpoPrev2(ModelMap model,HttpServletRequest request)
	{
		Integer currPage2 = (Integer) model.get("currPage2");
		currPage2--;
		model.addAttribute("currPage2",currPage2);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExposureByCode", method = RequestMethod.GET)
	public ModelAndView currNumberExposureByCode(String direction,ModelMap model,HttpServletRequest request) {
		
		log.info("currNumberExposureByChoice with direction : " + direction);
		resetOrdering(model,Dx4MetaBetExpoOrder.code,direction);
		
		initialiseCurrMetaBetExpoPg(model);
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExposureByChoice", method = RequestMethod.GET)
	public ModelAndView currNumberExposureByChoice(String direction,ModelMap model,HttpServletRequest request) {
	
		log.trace("currNumberExposureByChoice with direction : " + direction);
		resetOrdering(model,Dx4MetaBetExpoOrder.choice,direction);
		initialiseCurrMetaBetExpoPg(model);
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "currNumberExposureByStake", method = RequestMethod.GET)
	public ModelAndView currNumberExposureByStake(String direction,ModelMap model,HttpServletRequest request) {
	
		log.trace("currNumberExposureByStake with direction : " + direction);
		
		resetOrdering(model,Dx4MetaBetExpoOrder.tbet,direction);
		
		initialiseCurrMetaBetExpoPg(model);
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	private void resetOrdering(ModelMap model,Dx4MetaBetExpoOrder order,String direction)
	{
		@SuppressWarnings("unchecked")
		List<Dx4MetaBetExpoOrder> ordering = (List<Dx4MetaBetExpoOrder>) model.get("currOrdering");
		
		log.trace("current order : " + ordering);
		int pos = ordering.indexOf(order);
		int newPos = pos+1;
		if (direction.toLowerCase().equals("up"))
			newPos = pos-1;
		
		if (newPos<0 || newPos>=ordering.size())
		{
			log.trace("Invalid ordering thru probable page refresh ignored");
			return;
		}
		ordering.remove(order);
		ordering.add(newPos,order);
			
		log.trace("new order : " + ordering);
		
		model.put("currOrdering", ordering);
	}

	@RequestMapping(value = "/processNumberExpo", params = "blockExpo", method = RequestMethod.GET)
	public ModelAndView blockExpo(String number,ModelMap model,HttpServletRequest request)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		char digits = Integer.toString(number.length()).toString().charAt(0);
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser,number);
		if (expo==null)
		{
			expo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
			expo.setNumber(number);
			expo.setBlocked(1);
			dx4Services.getDx4Home().storeDx4NumberExpo(expo, digits);
		}
		else
		{
			expo.setBlocked(1);
			dx4Services.getDx4Home().updateNumberExpoBlocked(expo);
		}
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	@RequestMapping(value = "/processNumberExpo", params = "unblockExpo", method = RequestMethod.GET)
	public ModelAndView unblockExpo(String number,ModelMap model,HttpServletRequest request)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		char digits = Integer.toString(number.length()).toString().charAt(0);
		Dx4NumberExpo expo = dx4Services.getDx4Home().getDx4NumberExpoForUser(currUser, number );
		if (expo!=null)
		{
			Dx4NumberExpo defaultExpo = dx4Services.getDx4Home().getDx4DefaultNumberExpoForUser(currUser, digits);
			if (expo.getBetExpo()==defaultExpo.getBetExpo() && expo.getWinExpo()==defaultExpo.getWinExpo())
			{
				dx4Services.getDx4Home().deleteDx4NumberExpo(expo, digits);
				CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
				return new ModelAndView("numberExpo","expoForm",expoForm);
			}
			expo.setBlocked(0);
			dx4Services.getDx4Home().updateNumberExpoBlocked(expo);
		}
		
		CurrentNumberExposureForm expoForm = new CurrentNumberExposureForm(dx4Services.getDx4Home(),model);
		return new ModelAndView("numberExpo","expoForm",expoForm);
	}
	
	
}
