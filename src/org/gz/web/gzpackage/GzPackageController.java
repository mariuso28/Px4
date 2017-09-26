package org.gz.web.gzpackage;

import java.util.Map;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.game.GzGroup;
import org.gz.game.GzPackage;
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
@SessionAttributes({"currUser", "currGroupMap" })

@RequestMapping("/gz/package")
public class GzPackageController {

	private static Logger log = Logger.getLogger(GzPackageController.class);	
	@Autowired
	private GzServices gzServices;

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public ModelAndView manage(ModelMap model)
	{
		log.info("In manage");
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		GzPackageForm packageForm = new GzPackageForm();
		
		Map<String,GzGroup> grps = gzServices.getGzHome().getGroups(currUser.getMemberId());
		model.addAttribute("currGroupMap",grps);
		
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
	
	@RequestMapping(value = "/editPackage", method = RequestMethod.POST)
	public ModelAndView editPackage(ModelMap model,@ModelAttribute("packageForm") GzPackageForm packageForm)
	{
		GzPackageCommand command = packageForm.getCommand();
		
		log.info("in editPackage for : " + command.getGname() +":"+ command.getPname());
		GzPackage pkg = getPackage(model,command.getGname(),command.getPname());
		packageForm = new GzPackageForm();
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
}
