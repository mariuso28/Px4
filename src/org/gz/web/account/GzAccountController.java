package org.gz.web.account;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.gz.account.GzInvoice;
import org.gz.account.persistence.GzXactionRowMapperPaginated;
import org.gz.agent.GzAgent;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.gz.util.StackDump;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currAccountUser","codeQue","currInvoice", "firstInQueUser", "currChecked", "currXtrmp", "currBaseUser"})

@RequestMapping("/gz/acc")
public class GzAccountController {


	private static Logger log = Logger.getLogger(GzAccountController.class);	
	private GzServices gzServices;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setAutoGrowCollectionLimit(2048);
	}
	
	private boolean integrityCheck2(GzBaseUser currUser,GzBaseUser currAccountUser)
	{
		
		if (currUser.getRole().equals(GzRole.ROLE_DUSTBINA) && currUser.getCode().equals(currAccountUser.getCode()))
			return true;
		
		if (!currUser.getCode().equals(currAccountUser.getParentCode()) && !currAccountUser.getCode().equals(currUser.getParentCode()))
		{
			log.fatal("maintainExistAccount - PARENT/CHILD CODE MISMATCH :" + 
					currUser.getCode() +"/" + currAccountUser.getParentCode());
			return false;
		}
		
	 return true;
	}
	
	@RequestMapping(value = "/processAccount", params=("invoiceDetails"), method = RequestMethod.GET)
	public Object viewInvoiceDetails(ModelMap model,String invoiceId)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		codeQue.addFirst(invoiceId);
		model.addAttribute("codeQue", codeQue);
		
		long invoiceNum = new Long(invoiceId);
		try
		{
			GzInvoice currInvoice = gzServices.getGzHome().getInvoiceForId(invoiceNum);
			GzBaseUser currAccountUser = gzServices.getGzHome().getBaseUserByEmail(currInvoice.getPayee());
			GzBaseUser currUser = gzServices.getGzHome().getBaseUserByEmail(currInvoice.getPayer());
			model.addAttribute("currUser",currUser);
			model.addAttribute("currAccountUser",currAccountUser);
			model.addAttribute("currInvoice",currInvoice);
			
			if (!integrityCheck2(currAccountUser,currUser))				// bidirectional checking
			{
				return "redirect:../logon/access_denied";
			}
					
			if (currAccountUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currAccountUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			if (currUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			
			InvoiceListForm invoiceListForm = new InvoiceListForm(currAccountUser,currInvoice,gzServices.getGzHome());
			return new ModelAndView("invoiceSubList" , "invoiceListForm", invoiceListForm);
		}
		catch (GzPersistenceException e)
		{
			log.error(StackDump.toString(e));
			return null;
		}
	}
	
	
	@RequestMapping(value = "/processAccount", params=("method=rollup"), method = RequestMethod.GET)
	public ModelAndView processRollup(ModelMap model)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		if (codeQue==null)
			codeQue = new ArrayDeque<String>();
		else
			codeQue.clear();
		codeQue.addFirst(currUser.getCode());
		model.addAttribute("codeQue", codeQue);
		model.addAttribute("currUser",currUser);
		
		model.addAttribute("firstInQueUser",currUser);
		RollupForm rollupForm = null;
		try {
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			log.error(StackDump.toString(e));
		}
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=subRollup"), method = RequestMethod.GET)
	public ModelAndView processSubRollup(ModelMap model,String code)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		codeQue.addFirst(code);
		model.addAttribute("codeQue", codeQue);
		
		RollupForm rollupForm = null;
		GzAgent currUser = null;
		try {
			currUser = (GzAgent) gzServices.getGzHome().getAgentByCode(code);
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			log.info(StackDump.toString(e));
		}
		
		log.trace("processAccount : " + rollupForm);
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=cancelRollup"), method = RequestMethod.GET)
	public Object cancelRollup(ModelMap model)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		String code = codeQue.removeFirst();
		if (codeQue.isEmpty())
		{
			GzBaseUser currUser = (GzBaseUser) model.get("firstInQueUser");
			model.addAttribute("currUser",currUser);
			return "redirect:../agnt/backtoMemberHome";
		}
		code = codeQue.removeFirst();
		model.addAttribute("codeQue", codeQue);
		return processSubRollup(model,code);
	}
		
	@RequestMapping(value = "/processAccount", params=("method=cancelRollupDetails"), method = RequestMethod.GET)
	public ModelAndView cancelRollupDetails(ModelMap model)
	{
		return processRollup( model );
	}
	
	@RequestMapping(value = "/processAccount", params=("method=backToLastRollup"), method = RequestMethod.GET)
	public Object backToLastRollup(ModelMap model)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		
		if (codeQue.isEmpty())
		{
			GzBaseUser currUser = (GzBaseUser) model.get("firstInQueUser");
			model.addAttribute("currUser",currUser);
			return "redirect:../agnt/backtoMemberHome";
		}
		
		RollupForm rollupForm = null;
		GzAgent currUser = null;
		try {
			String code = codeQue.peekFirst();
			currUser = (GzAgent) gzServices.getGzHome().getAgentByCode(code);
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
		
		log.trace("processAccount : " + rollupForm);
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetails"), method = RequestMethod.GET)
	public ModelAndView accDetails(ModelMap model,String code)
	{
		GzBaseUser currUser = null;
		try {
			currUser = (GzBaseUser) gzServices.getGzHome().getAgentByCode(code);
		} catch (GzPersistenceException e) {
			log.equals(StackDump.toString(e));
		}
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		int pageSize = 16;
		GzXactionRowMapperPaginated xtrmp = gzServices.getGzHome().getGzInvoiceRowMapperPaginated(currUser,pageSize);
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		model.addAttribute("currUser",currUser);
		model.addAttribute("currXtrmp",xtrmp);
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetailsNext"), method = RequestMethod.GET)
	public ModelAndView accDetailsNext(ModelMap model)
	{
		GzAgent currUser = (GzAgent) model.get("currUser");
		GzXactionRowMapperPaginated xtrmp = (GzXactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetailsLast"), method = RequestMethod.GET)
	public ModelAndView accDetailsLast(ModelMap model)
	{
		GzAgent currUser = (GzAgent) model.get("currUser");
		GzXactionRowMapperPaginated xtrmp = (GzXactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getPrevPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
}
