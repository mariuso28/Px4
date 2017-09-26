package org.gz.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.gz.admin.GzAdmin;
import org.gz.baseuser.GzProfile;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.gz.util.StackDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"currUser","currStackDump"})

@RequestMapping("/gz/admin")
public class GzAdminController {

	private static Logger log = Logger.getLogger(GzAdminController.class);	
	@Autowired
	private GzServices gzServices;
	
	
	// /gz/admin/logon?user&email=
	@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
	public Object signin(String memberId,ModelMap model,HttpServletRequest request,RedirectAttributes stack) {
		
		log.info("Received request to logon");
			
		GzAdmin currUser = null;
	
		try {
			currUser = gzServices.getGzHome().getAdminByMemberId(memberId);
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/Px4/logon/signin";
		}
	
		model.addAttribute("currUser",currUser);
		
		
		HttpSession session = request.getSession();						// have to set in the session as end up in other controllers
		log.trace("Setting session attribute : sCurrUser : " +  currUser );
		session.setAttribute("sCurrUser", currUser);	
		
		return goAdminHome("",model,stack);
	}
	
	// exec?returnAdmin
	@RequestMapping(value = "/exec", params = "returnAdmin", method = RequestMethod.GET)
    public Object returnAdmin(ModelMap model,RedirectAttributes stack)
    {
			return goAdminHome("",model,stack);
    }
	
	
	@RequestMapping(value = "/exec", params="modifyAdmin", method = RequestMethod.POST)
	public Object modifyAdmin(@ModelAttribute("gzAdminForm") GzAdminForm gzAdminForm,ModelMap model,RedirectAttributes stack)
	{
		GzAdminCommand command = gzAdminForm.getCommand();
		GzProfile profile = command.getProfile();
		
		log.info("Modify profile");
		
		String errMsg = profile.validate(command.getVerifyPassword(),GzRole.ROLE_GZADMIN); 
		if (errMsg.isEmpty())
		{
			GzAdmin currUser = (GzAdmin) model.get("currUser");
			currUser.copyProfileValues(profile);
			try {
				gzServices.getGzHome().updateBaseUserProfile(currUser);
			} catch (GzPersistenceException e) {
				String stackDump = StackDump.toString(e);
				log.error(stackDump);
				stack.addFlashAttribute("errMsg",stackDump);
				return "redirect:/Px4/logon/errStackDump";
			}
		}
		
		return goAdminHome(errMsg,model,stack);
	}
	
	private Object goAdminHome(String errMsg,ModelMap model,RedirectAttributes stack){
		
		GzAdminForm adminForm = new GzAdminForm();
		
		return new ModelAndView("GzAdminHome","adminForm", adminForm);
	}
	
	
}
