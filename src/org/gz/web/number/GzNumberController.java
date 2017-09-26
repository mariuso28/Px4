package org.gz.web.number;

import org.apache.log4j.Logger;
import org.gz.account.GzNumberRetainerSet;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.persistence.GzPersistenceRuntimeException;
import org.gz.services.GzServices;
import org.gz.services.GzServicesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser", "currNumberRetainerSet", "currNumberDigits"})

@RequestMapping("/gz/number")
public class GzNumberController {

	private static Logger log = Logger.getLogger(GzNumberController.class);	
	@Autowired
	private GzServices gzServices;

	@RequestMapping(value = "/manage4D", method = RequestMethod.GET)
	public ModelAndView manage4D(ModelMap model)
	{
		log.info("In manage4D");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		GzNumberForm numberForm = new GzNumberForm();
		model.addAttribute("currNumberDigits",new Integer(4));
		GzNumberRetainerSet currNumberRetainerSet = gzServices.getGzAccountMgr().getGzNumberRetainerSet(4, currUser);
		model.addAttribute("currNumberRetainerSet",currNumberRetainerSet);
		
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
	
	@RequestMapping(value = "/manage3D", method = RequestMethod.GET)
	public ModelAndView manage3D(ModelMap model)
	{
		log.info("In manage3D");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		GzNumberForm numberForm = new GzNumberForm();
		model.addAttribute("currNumberDigits",new Integer(3));
		GzNumberRetainerSet currNumberRetainerSet = gzServices.getGzAccountMgr().getGzNumberRetainerSet(3, currUser);
		model.addAttribute("currNumberRetainerSet",currNumberRetainerSet);
		
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
	

	@RequestMapping(value = "/manage2D", method = RequestMethod.GET)
	public ModelAndView manage2D(ModelMap model)
	{
		log.info("In manage2D");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		GzNumberForm numberForm = new GzNumberForm();
		model.addAttribute("currNumberDigits",new Integer(2));
		GzNumberRetainerSet currNumberRetainerSet = gzServices.getGzAccountMgr().getGzNumberRetainerSet(2, currUser);
		model.addAttribute("currNumberRetainerSet",currNumberRetainerSet);
		
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
	
	@RequestMapping(value="/processNumbers", params="numberCancel", method = RequestMethod.POST)
	public Object numberCancel(@ModelAttribute("numberForm") GzNumberForm numberForm,ModelMap model)
	{
		return "redirect:../adm/backToAdm";
	}
	
	@RequestMapping(value="/processNumbers", params="modify4DDefaults", method = RequestMethod.POST)
	public ModelAndView modify4DDefaults(@ModelAttribute("numberForm") GzNumberForm numberForm,ModelMap model)
	{
		GzNumberCommand command = numberForm.getCommand();
		log.info("modify4DDefaults: " + command.getDefaults());
		numberForm = new GzNumberForm();
		
		GzNumberRetainerSet currNumberRetainerSet =  (GzNumberRetainerSet) model.get("currNumberRetainerSet");
		try
		{
			currNumberRetainerSet.updateDefaults(command.getDefaults(),gzServices.getGzHome());
		}
		catch (NumberFormatException | GzPersistenceRuntimeException e)
		{
			numberForm.setErrMsg("Error setting defaults - " + e.getMessage());
			return new ModelAndView("numberRetainers4D","numberForm", numberForm);
		}
		
		numberForm.setInfoMsg("Successfully modified.");
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
	
	@RequestMapping(value="/processNumbers", params="create4DNumber", method = RequestMethod.POST)
	public ModelAndView create4DNumber(@ModelAttribute("numberForm") GzNumberForm numberForm,ModelMap model)
	{
		GzNumberCommand command = numberForm.getCommand();
		log.info("create4DNumber: " + command.getNewNumber());
		numberForm = new GzNumberForm();
		
		GzNumberRetainerSet currNumberRetainerSet =  (GzNumberRetainerSet) model.get("currNumberRetainerSet");
		try
		{
			currNumberRetainerSet.createNewNumber(command.getNewNumber(),command.getNewValues(),gzServices.getGzHome());
		}
		catch (NumberFormatException | GzPersistenceRuntimeException | GzServicesException e)
		{
			numberForm.setErrMsg("Error adding new number - " + e.getMessage());
			return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
		}
		
		// refresh
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		Integer digits = (Integer) model.get("currNumberDigits");
		currNumberRetainerSet = gzServices.getGzAccountMgr().getGzNumberRetainerSet(digits, currUser);
		model.addAttribute("currNumberRetainerSet",currNumberRetainerSet);
		numberForm.setInfoMsg("Successfully created.");
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
	
	@RequestMapping(value="/processNumbers", params="modify4DNumber", method = RequestMethod.POST)
	public ModelAndView modify4DNumber(@ModelAttribute("numberForm") GzNumberForm numberForm,ModelMap model)
	{
		
		GzNumberCommand command = numberForm.getCommand();
		log.info("modify4DNumber index : " + command.getChangeIndex());
		
		numberForm = new GzNumberForm();
		
		GzNumberRetainerSet currNumberRetainerSet =  (GzNumberRetainerSet) model.get("currNumberRetainerSet");
		try
		{
			currNumberRetainerSet.modifyNumber(command.getChangeIndex(),command.getModifyValues(),gzServices.getGzHome());
		}
		catch (NumberFormatException | GzPersistenceRuntimeException e)
		{
			numberForm.setErrMsg("Error adding modifying number - " + e.getMessage());
			return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
		}
		
		// refresh
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		Integer digits = (Integer) model.get("currNumberDigits");
		currNumberRetainerSet = gzServices.getGzAccountMgr().getGzNumberRetainerSet(digits, currUser);
		model.addAttribute("currNumberRetainerSet",currNumberRetainerSet);
		numberForm.setInfoMsg("Successfully modified.");
		return new ModelAndView("GzNumberRetainers4D","numberForm", numberForm);
	}
}
