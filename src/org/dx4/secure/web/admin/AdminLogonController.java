package org.dx4.secure.web.admin;

import org.apache.log4j.Logger;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.ErrorModelView;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;


@Controller
@SessionAttributes({"currSelected","currChanged"})

@RequestMapping(value = "/admLogon")
public class AdminLogonController {
	 
	private static final Logger log = Logger.getLogger(AdminLogonController.class);
	
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView get() {
 
        AdminLogonForm adminLogonForm = new AdminLogonForm();
        AdminLogon adminLogon = new AdminLogon();
        adminLogonForm.setAdminLogon(adminLogon);
       
        return new ModelAndView("adminLogon" , "adminLogonForm", adminLogonForm);
    }
	
	@RequestMapping(value = "/memberLogon", method = RequestMethod.GET)
    public Object memberLogon(String usercode) {
 
		log.trace("Attemping to logon usercode " + usercode);
		Dx4SecureUser user;
		try {
			user = dx4Services.getDx4Home().getUserByCode(usercode);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			return ErrorModelView.createErrorStackTrace("",e);
		}
		if (user==null)
		{
			Dx4ExceptionFatal ef = new Dx4ExceptionFatal("User : " + usercode + " not found");
			log.error("User : " + usercode + " not found");
			return ErrorModelView.createErrorStackTrace("",ef);
		}
		if (user.getRole().equals(Dx4Role.ROLE_ADMIN))
			return "redirect:../adm/logonAdmin.html?username="+user.getEmail();
		else
		if (user.getRole().equals(Dx4Role.ROLE_PLAY))
			return "redirect:../play/logonPlayer.html?username="+user.getEmail();
		else 
			return "redirect:../agnt/logonAgent.html?username="+user.getEmail();
    }
	
	
}
