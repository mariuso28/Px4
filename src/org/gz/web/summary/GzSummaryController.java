package org.gz.web.summary;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currSummaryMembers"})

@RequestMapping("/gz/summary")
public class GzSummaryController {

	private static Logger log = Logger.getLogger(GzSummaryController.class);	
	@Autowired
	private GzServices gzServices;
	
	@RequestMapping(value = "/members", method = RequestMethod.GET)
	public Object summaryMembers(ModelMap model) {
		
		log.info("Received request to summaryMembers");
		GzSummaryManager summaryManager = new GzSummaryManager();
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		GzSummaryMembers summaryMembers;
		try {
			summaryMembers = summaryManager.createGzSummaryMembers(gzServices.getGzHome(), currUser);
			model.addAttribute("currSummaryMembers",summaryMembers);
			return new ModelAndView("GzSummaryMembers");
		} catch (GzPersistenceException e) {
			e.printStackTrace();
		}
			
		return "redirect:../adm/backToAdm";		// need errMsg
	}
	
	// expand?id=212819739731
	@RequestMapping(value = "/expand", method = RequestMethod.GET)
	public Object expand(ModelMap model,String id) {
		
		log.info("Received request to expand");
		GzSummaryManager summaryManager = new GzSummaryManager();
		GzSummaryMembers summaryMembers = (GzSummaryMembers) model.get("currSummaryMembers");
		try {
			summaryManager.expandRow(gzServices.getGzHome(), summaryMembers, id);
			return new ModelAndView("GzSummaryMembers");
		} catch (GzPersistenceException e) {
			e.printStackTrace();
		}
			
		return "redirect:../adm/backToAdm";		// need errMsg
	}
	
	// collapse?id=212819739731
	@RequestMapping(value = "/collapse", method = RequestMethod.GET)
	public Object collapse(ModelMap model,String id) {

		log.info("Received request to collase");
		GzSummaryManager summaryManager = new GzSummaryManager();
		GzSummaryMembers summaryMembers = (GzSummaryMembers) model.get("currSummaryMembers");
		try {
			summaryManager.collapseRow(gzServices.getGzHome(), summaryMembers, id);
			return new ModelAndView("FzSummaryMembers");
		} catch (GzPersistenceException e) {
			e.printStackTrace();
		}

		return "redirect:../adm/backToAdm";		// need errMsg
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public Object cancel(ModelMap model) 
	{
		return "redirect:../adm/backToAdm";	
	}
	
}
