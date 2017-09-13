package org.dx4.secure.web.logon;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
public class Dx4LoginLogoutController {

	protected static Logger logger = Logger.getLogger("Dx4LoginLogoutController");

	@Autowired
	private Dx4Services dx4Services;

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}



	/**
	 * Handles and retrieves the login JSP page
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/dx4Logon", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam(value="error", required=false) boolean error, 
			ModelMap model) {

		logger.info("Received request to show login page");

		// Add an error message to the model if login is unsuccessful
		// The 'error' parameter is set to true based on the when the authentication has failed. 
		
   
		if (error == true) {
			// Assign an error message
			model.put("error", "You have entered an invalid username or password!");
		} else {
			model.put("error", "");
		}

		// This will resolve to /WEB-INF/jsp/loginpage.jsp
		return "loginpage";
	}

	@RequestMapping(value = "/dx4RoleBasedLogon", method = RequestMethod.GET)
	public String dx4RoleBasedLogon(ModelMap model) {

		Set<String> roles = AuthorityUtils
        .authorityListToSet(SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities());
		
		logger.info("dx4RoleBasedLogon roles are : " + roles);
		
		User ud = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		logger.info("dx4RoleBasedLogon username is : " + ud.getUsername());
		
		if (roles.contains("ROLE_ADMIN"))
			return "redirect:adm/logonAdmin.html?username="+ud.getUsername();
		else
		if (roles.contains("ROLE_PLAY"))
			return "redirect:play/logonPlayer.html?username="+ud.getUsername();
		else 
			return "redirect:agnt/logonAgent.html?username="+ud.getUsername();
	}
	
	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a regular user
	 * tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String getDeniedPage() {
		logger.info("!!! Received request to show denied page !!!");

		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "Dx4Denied";
	}
}