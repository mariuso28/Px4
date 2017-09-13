package org.dx4.secure.web.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.account.persistence.TransactionRowMapperPaginated;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.extend.CustomDateEditorNullBadValues;
import org.dx4.extend.StrictNumberPropertyEditor;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.agent.AgentGamesForm;
import org.dx4.secure.web.member.MemberHomeForm;
import org.dx4.secure.web.pdf.PDFStoreTransaction;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;


@Controller
@SessionAttributes({"currUser","currAccountUser","currMetaGame","currTransaction","currXtrmp","currMetaBet", "currNpes", "currImages"})

@RequestMapping(value = "/acc")
public class AccountController 
{
	private static final Logger log = Logger.getLogger(AccountController.class);
	private Dx4Services dx4Services;

	@Autowired
	public void setDx4Services(Dx4Services dx4Services)
	{
		this.dx4Services = dx4Services;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		binder.registerCustomEditor(Date.class,  new CustomDateEditorNullBadValues(dateFormat, true));
		binder.registerCustomEditor(Double.class,  new StrictNumberPropertyEditor());
	}
	
	@RequestMapping(value = "/processAccount.html", params=("cancelAccount"), method = RequestMethod.GET)
	public String cancelAccount(ModelMap model,HttpServletRequest request)
	{
		
		HttpSession session = request.getSession(false);	
		log.trace("cancelAccount getting session attribute : sCurrUser ");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");		// as the thing maybe Dx4Agent or Dx4Admin
		log.trace("cancelAccount got session attribute : currUser : " +  currUser );
		/*
		dx4Services.getDx4Home().getDownstreamForParent(currUser);
		model.addAttribute("currUser",currUser);
		return new ModelAndView("memberHome", "memberHomeForm", new MemberHomeForm(dx4Services.getDx4Home(),currUser));
		*/
		if (currUser.getRole().equals(Dx4Role.ROLE_ADMIN))
			return "redirect:../adm/goAdminHome.html?username=" + currUser.getEmail();
			
		return "redirect:../agnt/goAgentHome.html?username=" + currUser.getEmail();
	}
	
	@RequestMapping(value = "/processAccount.html", params=("saveAccount"), method = RequestMethod.POST)
	public ModelAndView saveAccount(ModelMap model,AccountDetailsForm accountDetailsForm,
										HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");		// as the thing maybe Dx4Agent or Dx4Admin
		log.trace("got session attribute : currUser : " +  currUser );
		
		Dx4SecureUser currAccountUser = (Dx4SecureUser) session.getAttribute("sCurrAccountUser");	// maybe Dx4Agent or Dx4Player
		log.trace("got session attribute : currAccountUser : " +  currAccountUser );
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		log.trace("saveAccount accountDetailsForm newAccount : " + accountDetailsForm.getCommand().getNewAccount());
		log.trace("saveAccount currUser : " + currUser);
		log.trace("saveAccount currAccountUser : " + currAccountUser);
		try
		{
			accountDetailsForm.validate(dx4Services.getDx4Home(),currAccountUser,currUser);				// test against some parent values
		}
		catch (Dx4FormValidationException e){
			accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser,accountDetailsForm);
			accountDetailsForm.setMessage("Validate of Account failed : " + e.getMessage());
			log.trace("saveAccount - " + accountDetailsForm);
			return new ModelAndView("accountEdit" , "accountDetailsForm", accountDetailsForm);
		}
		
		AccountCommand command = accountDetailsForm.getCommand();
		log.info("New account flag : "+command.getNewAccountFlag());
		
		command.getNewAccount().setId(currAccountUser.getAccount().getId());			// won't be set in form
		command.getNewAccount().setUserId(currAccountUser.getAccount().getUserId());
		log.trace("saveAccount ; "  + command.getNewAccount());
		currAccountUser.setAccount(command.getNewAccount());
		if (command.getDwAmount()>0.0)
			dx4Services.performWithdrawlDeposit(currAccountUser,command.getDwType(),command.getDwAmount(),command.getComment());
		
		dx4Services.getDx4Home().updateAccount(currAccountUser);
		dx4Services.getDx4Home().getDownstreamForParent(currUser);
		model.addAttribute("currUser",currUser);
		
		
		if (command.getNewAccountFlag()==true)
		{
			if (currUser.getGameGroup().getGameActivators().size()>0)
				return goModifyGames(currAccountUser);
		}
		
		return new ModelAndView("memberHome", "memberHomeForm", new MemberHomeForm(dx4Services.getDx4Home(),currUser) );
	}
	
	@RequestMapping(value = "/maintainAccount", method = RequestMethod.GET)
	public ModelAndView maintainAccount(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");				// as the thing comes from AgentController
		log.trace("got session attribute : currUser : " +  currUser );
		
		Dx4SecureUser currAccountUser = (Dx4SecureUser) session.getAttribute("sCurrAccountUser");				// the osner of the account
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),
															currAccountUser,currUser);
		
		log.trace("maintainAccount : " + accountDetailsForm);
        return new ModelAndView("maintainAccount" , "accountDetailsForm", accountDetailsForm);
    }
	
	// <td><a href="processAccount.html?updateComp>Update Account</a></td>
	@RequestMapping(value = "/processAccount.html", params=("method=updateComp"), method = RequestMethod.GET)
	public ModelAndView updateCompAccount(ModelMap model,HttpServletRequest request) 
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");				
		
		Dx4SecureUser currAccountUser = currUser;		// same user/owner
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		HttpSession session = request.getSession(false);	
		session.setAttribute("sCurrUser",currUser);	
		session.setAttribute("sCurrAccountUser",currAccountUser);	
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser);		
		return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	// processAccount.html?method=update&code=p0
	@RequestMapping(value = "/processAccount.html", params=("method=update"), method = RequestMethod.GET)
	public ModelAndView updateAccount(String code,ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");				
		
		Dx4SecureUser currAccountUser;
		try {
			currAccountUser = (Dx4SecureUser) dx4Services.getDx4Home().getUserByCode(code);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}		
		// the owner of the account
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		session.setAttribute("sCurrAccountUser",currAccountUser);	
		
		if (!currUser.getRole().equals(Dx4Role.ROLE_COMP) &&
				!currUser.getCode().equals(currAccountUser.getParentCode()))
		{
			log.fatal("maintainExistAccount - SOMETHING GONE VERY WRONG PARENT/CHILD CODE MISMATCH :" + 
					currUser.getCode() +"/" + currAccountUser.getParentCode());
			return null;
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser);
		
		log.trace("updateAccount : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
    }
	
	
	@RequestMapping(value = "/processAccount.html", params=("saveWithDep"), method = RequestMethod.POST)
	public ModelAndView saveWithDep(AccountDetailsForm accountDetailsForm,ModelMap model) {
       
		Dx4SecureUser currUser =  (Dx4SecureUser) model.get("currUser");
		Dx4SecureUser currAccountUser =  (Dx4SecureUser) model.get("currAccountUser");
		String message = "";
		try
		{
			accountDetailsForm.validateWithDep(currAccountUser);	
			AccountCommand command = accountDetailsForm.getCommand();
			dx4Services.performWithdrawlDeposit(currAccountUser,command.getDwType(),command.getDwAmount(),command.getComment());
		}
		catch (Dx4FormValidationException e){
			message = "Validate failed : " + e.getMessage();
		}
		
		accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser);
		accountDetailsForm.setMessage(message);
		return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
    }
	
	@RequestMapping(value = "/processAccount.html", params=("modifyAccount"), method = RequestMethod.POST)
	public ModelAndView createAccount(ModelMap model) 
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),
														currAccountUser,currUser);
		accountDetailsForm.setModify(true);
		log.trace("createAccount : " + accountDetailsForm);
		return new ModelAndView("accountEdit" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("enableMember"), method = RequestMethod.POST)
	public ModelAndView enableMember(ModelMap model) 
	{
		return setEnabled(model,true);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("disableMember"), method = RequestMethod.POST)
	public ModelAndView disableMember(ModelMap model) 
	{
		return setEnabled(model,false);
	}
	
	private ModelAndView setEnabled(ModelMap model,boolean flag)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		
		dx4Services.updateEnabled(currAccountUser,flag);
		
		// refresh
		try {
			currAccountUser = (Dx4SecureUser) dx4Services.getDx4Home().getUserByCode(currAccountUser.getCode());
		} catch (Dx4PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser);
		
		log.trace("setEnabled : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	
	@RequestMapping(value = "/processAccount.html", params=("modifyProfile"), method = RequestMethod.POST)
	// modify the profile for the account holder involves hanging on to currUser 
	// and overriding it with currAccountUser before edit profile - will unravel on save/cancel
	public String modifyProfile(ModelMap model,HttpServletRequest request) 
	{
		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
	
		HttpSession session = request.getSession(false);	
		session.setAttribute("sCurrParentUser",currUser);		
		session.setAttribute("sCurrUser",currAccountUser);
		
		return "redirect:../agnt/processAgent.html?edit_agent";
	}
	
	@RequestMapping(value = "/processAccount.html", params=("method=betDetails"), method = RequestMethod.GET)
	public ModelAndView betDetails(ModelMap model,String transactionId)
	{
		long transactionNum = Long.parseLong(transactionId);
		Dx4Transaction currTransaction = dx4Services.getDx4Home().getTransactionForId(transactionNum);
		Dx4SecureUser currAccountUser;
		try {
			currAccountUser = dx4Services.getDx4Home().getBySeqId(currTransaction.getUserId());
		} catch (Dx4PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		model.addAttribute("currAccountUser",currAccountUser);
		WinBetListForm winBetListForm = new WinBetListForm(currTransaction,dx4Services.getDx4Home());
		
		return new ModelAndView("transWinBetList" , "winBetListForm",winBetListForm);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("cancelBetDetails"), method = RequestMethod.GET)
	public ModelAndView cancelNetDetails(ModelMap model)
	{
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		TransactionRowMapperPaginated xtrmp = (TransactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,dx4Services.getDx4Home(),xtrmp.getPage(xtrmp.getCurrentPage()), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount.html", params=("transactionPDF"), method = RequestMethod.GET)
	public ModelAndView transactionPDF(ModelMap model,String transactionId,HttpServletRequest request)
	{
		try {
			setUpPdfModelValues(model,transactionId);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		return new ModelAndView("pdfView", model );
	}
	
	private void setUpPdfModelValues(ModelMap model,String transactionId) throws Dx4PersistenceException
	{
		long transactionNum = Long.parseLong(transactionId);
		Dx4Transaction currTransaction = dx4Services.getDx4Home().getTransactionForId(transactionNum);
		model.addAttribute("currTransaction",currTransaction);
		Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetForTransaction(currTransaction);
		dx4Services.setUpPdfModelValues(model, metaBet);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("transactionPDFEmail"), method = RequestMethod.GET)
	public ModelAndView transactionPDFEmail(ModelMap model,String transactionId,HttpServletRequest request)
	{
		
		Dx4SecureUser currUser = (Dx4SecureUser) model.get("currUser");
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		try {
			setUpPdfModelValues(model,transactionId);
			PDFStoreTransaction pst = new PDFStoreTransaction(model,currUser,currAccountUser);
			String text = "Dear " + currAccountUser.getContact() + ", please review bet transaction attached.";
			List<String> attachments = new ArrayList<String>();
			attachments.add(pst.getPdfPath());
			dx4Services.getMail().sendMail(currAccountUser, "Dx4 bet transaction", text, attachments );
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error sending mail - " + e.getMessage());
		}
		return betDetails( model,transactionId);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("method=cancelAccDetails"), method = RequestMethod.GET)
	public ModelAndView cancelAccDetails(ModelMap model,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);	
		Dx4SecureUser currUser = (Dx4SecureUser) session.getAttribute("sCurrUser");				
		Dx4SecureUser currAccountUser = (Dx4SecureUser) session.getAttribute("sCurrAccountUser");
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		if (currUser.getRole().equals(Dx4Role.ROLE_COMP))
		{
			currAccountUser = currUser;
		}
		else
		if (!currUser.getCode().equals(currAccountUser.getParentCode()))
		{
			log.fatal("maintainExistAccount - SOMETHING GONE VERY WRONG PARENT/CHILD CODE MISMATCH :" + 
					currUser.getCode() +"/" + currAccountUser.getParentCode());
			return null;
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),currAccountUser,currUser);
		
		log.trace("updateAccount : " + accountDetailsForm);
	    return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount.html", params=("method=accDetails"), method = RequestMethod.GET)
	public ModelAndView accDetails(ModelMap model,String code)
	{
		Dx4SecureUser currUser;
		try {
			currUser = (Dx4SecureUser) dx4Services.getDx4Home().getUserByCode(code);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		TransactionRowMapperPaginated xtrmp = createXTransactionRowMapperPaginated(model,currUser,null);
		transactionForm.createTxList(currUser,dx4Services.getDx4Home(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		model.addAttribute("currUser",currUser);
		model.addAttribute("currXtrmp",xtrmp);
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	private TransactionRowMapperPaginated createXTransactionRowMapperPaginated(ModelMap model,Dx4SecureUser currUser,Dx4Agent partner)
	{
		int pageSize = 12;
		String pages = Dx4Config.getProperties().getProperty("dx4.paginate.accdetails","12");
		try
		{
			pageSize = Integer.parseInt(pages);
		}
		catch (NumberFormatException e)
		{
			log.error("Could not parse dx4.paginate.accdetails : " + pages + " using pagesize 12");
			pageSize = 12;
		}
		TransactionRowMapperPaginated xtrmp;
		xtrmp = dx4Services.getDx4Home().getXTransactionRowMapperPaginated(currUser.getSeqId(),pageSize);
		return xtrmp;
	}
	
	
	@RequestMapping(value = "/processAccount.html", params=("method=accDetailsNext"), method = RequestMethod.GET)
	public ModelAndView accDetailsNext(ModelMap model)
	{
		Dx4Player currUser = (Dx4Player) model.get("currUser");
		TransactionRowMapperPaginated xtrmp = (TransactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,dx4Services.getDx4Home(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount.html", params=("method=accDetailsLast"), method = RequestMethod.GET)
	public ModelAndView accDetailsLast(ModelMap model)
	{
		Dx4Player currUser = (Dx4Player) model.get("currUser");
		TransactionRowMapperPaginated xtrmp = (TransactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,dx4Services.getDx4Home(),xtrmp.getPrevPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount.html", params = "modifyGames", method = RequestMethod.POST)
	public ModelAndView modifyGames(MemberHomeForm memberHomeForm,ModelMap model) {
	
		// modifying for the account user
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		return goModifyGames(currAccountUser);
    }
	
	private ModelAndView goModifyGames(Dx4SecureUser user)
	{
		log.trace("goModifyGames : user is : " + user);
		Dx4SecureUser parent;
		try {
			parent = dx4Services.getDx4Home().getParentForUser(user);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		
		AgentGamesForm agentGamesForm = new AgentGamesForm(parent,user);
		log.trace("modifyGames : " + agentGamesForm);
        return new ModelAndView("modifyGames" , "agentGamesForm", agentGamesForm );
	}
	
	@RequestMapping(value = "/processAccount.html", params = "method=cancelModifyGames", method = RequestMethod.GET)
	public ModelAndView cancelModifyGames(ModelMap model,HttpServletRequest request)
	{
		log.trace(this.getClass().getSimpleName() + "::cancelModifyGames");
		Dx4SecureUser currUser = (Dx4SecureUser ) model.get("currUser");
		Dx4SecureUser currAccountUser = (Dx4SecureUser ) model.get("currAccountUser");
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(dx4Services.getDx4Home(),
															currAccountUser,currUser);
		
		log.trace("cancelModifyGames : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	// <td><a href="processAgent.html?method=useGame&name=${mgame.metaGame.name}">Use Game</a></td>
	@RequestMapping(value = "/processAccount.html", params = "method=useGame",method = RequestMethod.GET)
    public ModelAndView useGame(String name,ModelMap model){
		
		log.trace("useGame method with : " + name);
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
		Dx4GameGroup group = currAccountUser.getGameGroup();
		
		
		if (currAccountUser.getRole()!=Dx4Role.ROLE_COMP)
		{
			Dx4SecureUser parent;
			try {
				parent = dx4Services.getDx4Home().getParentForUser(currAccountUser);
			} catch (Dx4PersistenceException e) {
				e.printStackTrace();
				throw new Dx4ExceptionFatal(e.getMessage());
			}
			Dx4GameActivator parentGameActivator = parent.getGameGroup().getGameActivator(name);
			group.getGameActivators().add(new Dx4GameActivator(parentGameActivator));
		}
		else
			group.getGameActivators().add(new Dx4GameActivator(metaGame,group));
		
		dx4Services.addMetaGameToGroup(currAccountUser,metaGame);
		
		return goModifyGames(currAccountUser);
	}
	
	@RequestMapping(value = "/processAccount.html", params = "method=deactivateGame",method = RequestMethod.GET)
    public ModelAndView deactivateGame(String name,ModelMap model){
		
		log.trace("deactivateGame method with : " + name);
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame(name);
		dx4Services.removeMetaGameFromGroup(currAccountUser,metaGame);
		
		return goModifyGames(currAccountUser);
	}
	
	
	
}
