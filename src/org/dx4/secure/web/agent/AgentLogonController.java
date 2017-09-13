package org.dx4.secure.web.agent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes

@RequestMapping(value = "/agntLogon")
public class AgentLogonController {
	 
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AgentLogonController.class);
	
	@RequestMapping(value = "/agent", method = RequestMethod.GET)
    public ModelAndView get() {
 
        AgentLogonForm agentLogonForm = new AgentLogonForm();
        AgentLogon agentLogon = new AgentLogon();
        agentLogonForm.setAgentLogon(agentLogon);
       
        return new ModelAndView("agentLogon" , "agentLogonForm", agentLogonForm);
    }
	
}

