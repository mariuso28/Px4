package org.gz.web.gzpackage;

import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.services.Dx4Services;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.game.GzGroup;
import org.gz.game.GzPackage;
import org.gz.home.persistence.GzDuplicatePersistenceException;
import org.gz.home.persistence.GzPersistenceRuntimeException;
import org.gz.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser", "currGroupMap", "currPackage" })

@RequestMapping("/gz/package")
public class GzPackageController {

	private static Logger log = Logger.getLogger(GzPackageController.class);	
	@Autowired
	private GzServices gzServices;
	@Autowired
	private Dx4Services dx4Services;

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public Object manage(ModelMap model)
	{
		log.info("In manage");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		GzPackageForm packageForm = new GzPackageForm();
		
		try
		{
			Map<String,GzGroup> grps = gzServices.getGzHome().getGroups(currUser);
			model.addAttribute("currGroupMap",grps);
		}
		catch (GzPersistenceRuntimeException e)
		{
			e.printStackTrace();
			if (currUser.getRole().equals(GzRole.ROLE_GZADMIN))
				return "redirect:/Px4/gz/admin/exec?returnAdmin";
			else
				return null;
			
		}
		
		return new ModelAndView("GzPackage","packageForm", packageForm);
	}
	
	@RequestMapping(value = "/expand", params="expand", method = RequestMethod.GET)
	public ModelAndView expand(ModelMap model,String mode,String gname,String pname)
	{
	/*	log.info("expand is : " + mode);
		if (mode.equalsIgnoreCase("true"))
			log.info("Expanding package : " + pname + " in group: " + gname);
		else
			log.info("Shrinking package : " + pname + " in group: " + gname);
	*/	
		GzPackage pkg = getPackage(model,gname,pname);
		if (pkg == null)
			log.error("Package : " + pname + " for group : " + gname + " not found");
		if (mode.equalsIgnoreCase("true"))
			pkg.setExpanded(true);
		else
			pkg.setExpanded(false);
			
		GzPackageForm packageForm = new GzPackageForm();
		return new ModelAndView("GzPackage","packageForm", packageForm);
	}
	
	private GzPackage getPackage(ModelMap model,String gname,String pname)
	{
		@SuppressWarnings("unchecked")
		Map<String,GzGroup> grps = (Map<String, GzGroup>) model.get("currGroupMap");
		GzGroup grp = grps.get(gname);
		if (grp == null)
			return null;
		
		GzPackage pkg = grp.getPackages().get(pname);
		return pkg;
	}
	
	@RequestMapping(value = "/processPackage",  params="modify", method = RequestMethod.POST)
	public ModelAndView editPackage(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		
		log.info("in editPackage for : " + command.getGname() +":"+ command.getPname());
		GzPackage pkg = getPackage(model,command.getGname(),command.getPname());
		
		model.addAttribute("currPackage", pkg);
		packageForm = new GzPackageForm();
		return new ModelAndView("GzPackageModify","packageForm", packageForm);
	}
	
	@RequestMapping(value = "/processPackage",  params="modifyPackage", method = RequestMethod.POST)
	public ModelAndView modify(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		log.info("in modify");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");	
		GzPackage currPackage = (GzPackage) model.get("currPackage");
		
		packageForm = new GzPackageForm();
		
		String errMsg = currPackage.modifyPackageValues(command.getGameTypePayoutsEntry());
		if (!errMsg.isEmpty())
			packageForm.setErrMsg(errMsg);
		else
		{
			try
			{
				gzServices.getGzHome().updatePackage(currPackage);
				Map<String,GzGroup> grps = gzServices.getGzHome().getGroups(currUser);
				model.addAttribute("currGroupMap",grps);
				currPackage = getPackage(model,currPackage.getGroup().getName(),currPackage.getName()); 
				model.addAttribute("currPackage",currPackage);
			}
			catch (GzPersistenceRuntimeException e)
			{
				e.printStackTrace();
				packageForm.setErrMsg("Could not update package - contact support");
			}
			packageForm.setInfoMsg("Package successfully modified");
		}
		
		return new ModelAndView("GzPackageModify","packageForm", packageForm);
	}
	
	@RequestMapping(value = "/processPackage",  params="saveAsNew", method = RequestMethod.POST)
	public ModelAndView saveAsNew(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		log.info("in saveAsNew");
			
		GzPackage currPackage = (GzPackage) model.get("currPackage");
		
		packageForm = new GzPackageForm();
		String errMsg = currPackage.modifyPackageValues(command.getGameTypePayoutsEntry());
		if (!errMsg.isEmpty())
			packageForm.setErrMsg(errMsg);			
		else
		{
			model.addAttribute("currPackage",currPackage);
			packageForm.setCreateNew(true);
			packageForm.setInfoMsg("Please enter a unique package name");
		}
		
		return new ModelAndView("GzPackageModify","packageForm", packageForm);
	}
	
	@RequestMapping(value = "/processPackage",  params="storeNewPackage", method = RequestMethod.POST)
	public ModelAndView storeNewPackage(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		log.info("in storeNewPackage");
			
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");	
		GzPackage currPackage = (GzPackage) model.get("currPackage");
		
		packageForm = new GzPackageForm();
		packageForm.setCreateNew(true);
		String errMsg = currPackage.modifyPackageValues(command.getGameTypePayoutsEntry());
		if (!errMsg.isEmpty())
			packageForm.setErrMsg(errMsg);			
		else
		{
			if (command.getNewPackageName().isEmpty())
				packageForm.setErrMsg("Please enter a unique file name");
			else
			try
			{
				currPackage.setName(command.getNewPackageName());
				gzServices.getGzHome().storePackage(currPackage);
				Map<String,GzGroup> grps = gzServices.getGzHome().getGroups(currUser);
				model.addAttribute("currGroupMap",grps);
				currPackage = getPackage(model,currPackage.getGroup().getName(),currPackage.getName()); 
				model.addAttribute("currPackage",currPackage);
				packageForm.setCreateNew(false);
				packageForm.setInfoMsg("New Package successfully created");
			}
			catch (GzDuplicatePersistenceException e)
			{
				packageForm.setErrMsg("Package names must be unique for the group");
			}
			catch (GzPersistenceRuntimeException e)
			{
				e.printStackTrace();
				packageForm.setErrMsg("Could not update package - contact support");
			}
		}
		
		return new ModelAndView("GzPackageModify","packageForm", packageForm);
	}
	
	@RequestMapping(value = "/createNewPackage", params="create",  method = RequestMethod.GET)
	public ModelAndView createNewPackage(ModelMap model,String gname)
	{
		log.info("in createNewPackage");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");	
		GzPackageForm packageForm = new GzPackageForm();
		try
		{
			Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("4D With ABC");
			GzPackage currPackage = new GzPackage("MyPackage");	
			currPackage.populateDefaults(metaGame);
			
			@SuppressWarnings("unchecked")
			Map<String,GzGroup> grps = (Map<String, GzGroup>) model.get("currGroupMap");
			GzGroup group = grps.get(gname);
			currPackage.setGroup(group);
			currPackage.setMember(currUser);
			
			model.addAttribute("currPackage",currPackage);
			packageForm.setCreateNew(true);
		}
		catch (GzPersistenceRuntimeException e)
		{
			e.printStackTrace();
			packageForm.setErrMsg("Could not create package - contact support");
		}
		
		return new ModelAndView("GzPackageModify","packageForm", packageForm);
	}

	@RequestMapping(value = "/processPackage",  params="createNewGroup",  method = RequestMethod.POST)
	public ModelAndView createNewGroup(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		log.info("in storeNewPackage");
			
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");	
		packageForm = new GzPackageForm();
		try
		{
			GzGroup group = new GzGroup(command.getNewGroupName(),"Cats and Dogs",currUser);
			gzServices.getGzHome().storeGroup(group);
			Map<String,GzGroup> grps = gzServices.getGzHome().getGroups(currUser);
			model.addAttribute("currGroupMap",grps);
		}
		catch (GzDuplicatePersistenceException e)
		{
			packageForm.setErrMsg("Group : " + command.getNewGroupName() + " Already exists please choose another.");
		}
		catch (GzPersistenceRuntimeException e)
		{
			e.printStackTrace();
			packageForm.setErrMsg("Could not create group - contact support");
		}
		
		return new ModelAndView("GzPackage","packageForm", packageForm);
	}
	
	
	@RequestMapping(value = "/processPackage",  params="cancel", method = RequestMethod.POST)
	public Object cancel(ModelMap model)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		if (currUser.getRole().equals(GzRole.ROLE_GZADMIN))
			return "redirect:/Px4/gz/admin/exec?returnAdmin";
		else
			return null;	
	}
	
	@RequestMapping(value = "/processPackage",  params="cancelModify", method = RequestMethod.POST)
	public ModelAndView cancelModify(ModelMap model)
	{
		GzPackageForm packageForm = new GzPackageForm();	
		return new ModelAndView("GzPackage","packageForm", packageForm);
	}
}
