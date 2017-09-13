package org.dx4.secure.web.player;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller

@RequestMapping(value = "/playLogon")
public class PlayerLogonController {
	 
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PlayerLogonController.class);
	
	@RequestMapping(value = "/invalid", method = RequestMethod.GET)
    public ModelAndView invalid() {
		return new ModelAndView("Dx4SessionInvalid");
    }
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
    public ModelAndView get() {
 
        PlayerLogonForm playerLogonForm = new PlayerLogonForm();
        PlayerLogon playerLogon = new PlayerLogon();
        playerLogonForm.setPlayerLogon(playerLogon);
       
        return new ModelAndView("playerLogon" , "playerLogonForm", playerLogonForm);
    }
	
	@RequestMapping(value = "/registerNew", method = RequestMethod.GET)
    public ModelAndView registerNew() {

		PlayerRegisterForm playerRegisterForm = new PlayerRegisterForm();
	      
	    return new ModelAndView("playerRegister" , "playerRegisterForm", playerRegisterForm);
        
    }
	
	
}
