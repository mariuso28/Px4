package org.gz.web.adm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.gz.account.GzAccount;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.gz.web.admin.GzAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currUserModify","currTargetUser"})

@RequestMapping("/gz/adm")
public class GzAdmController {

	private static Logger log = Logger.getLogger(GzAdminController.class);
	@Autowired
	private GzServices gzServices;
	
	//  /gz/adm/logon?user&user=
		@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
		public Object signin(String memberId,ModelMap model,HttpServletRequest request) {
			
			log.info("Received request to logon");
				
			GzBaseUser currUser = null;
		
			try {
				currUser = gzServices.getGzHome().getBaseUserByMemberId(memberId);
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return backToSignin(model,"Unexpected error on member sigin - contact support");
			}
		
			model.addAttribute("currUser",currUser);
			
			
			return goAdmHome("","",model);
		}
	
		private Object goAdmHome(String errMsg,String infoMsg,ModelMap model){
			
			GzMemberForm admForm = createMemberForm(model);
			admForm.setInfoMsg(infoMsg);
			return new ModelAndView("GzAdmHome","admForm", admForm);
		}
		
		@RequestMapping(value = "/registerMember", method = RequestMethod.GET)
		public ModelAndView registerMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(model);
			
			return new ModelAndView("GzAdmMemberRegister","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/manageMemberAccount", method = RequestMethod.GET)
		public ModelAndView manageMemberAccount(ModelMap model)
		{
			GzMemberForm memberForm = createMemberAccountForm(model);
			if (!memberForm.getChooseMembers().isEmpty())
			{
				GzBaseUser bu;
				try {
					bu = gzServices.getGzHome().getBaseUserByMemberId(memberForm.getChooseMembers().get(0).getMemberId());
					setUpMember(model,memberForm,bu);
				} catch (GzPersistenceException e) {
					e.printStackTrace();
					memberForm.setErrMsg("Error on Manage Member - contact support");
				}
			}
			return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
		}
		
		private void createChooseMembers(GzMemberForm memberForm,ModelMap model)
		{
			GzBaseUser currUser = (GzBaseUser) model.get("currUser");
			
			List<GzMemberSummary> msList = new ArrayList<GzMemberSummary>();
			gzServices.getGzHome().getDownstreamForParent(currUser);
			for (GzBaseUser bu : currUser.getMembers())
			{
				GzMemberSummary ms = new GzMemberSummary(bu);
				ms.setParent(memberForm.getMembers().get(ms.getParentCode()));
				msList.add(ms);
			}
			memberForm.setChooseMembers(msList);
		}
		
		@RequestMapping(value = "/manageMemberFromTree", method = RequestMethod.GET)
		public Object manageMemberFromTree(ModelMap model,String name)
		{
			GzBaseUser bu;
			try {
				bu = gzServices.getGzHome().getBaseUserByMemberId(name);
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				return goAdmHome("Error managing member - contact support","",model);
			}
			if (bu == null)
			{
				log.error("Member : " + name + " does not exist");
				return goAdmHome("Member : " + name + " does not exist","",model);
			}
			
			model.addAttribute("currTargetUser",bu);
			GzBaseUser currUser = (GzBaseUser) model.get("currUser");
			if (bu.getParentCode().equals(currUser.getCode()))
				return manageMemberAccount(model,bu);
			
			GzMemberForm memberForm = createMemberForm(model);
			createChooseMembers(memberForm,model);
			setUpMember(model,memberForm,bu);
			return new ModelAndView("GzAdmMemberDetails","memberForm", memberForm);
		}
		
		private ModelAndView manageMemberAccount(ModelMap model,GzBaseUser bu)
		{
			GzMemberForm memberForm = createMemberAccountForm(model);
			setUpMember(model,memberForm,bu);
			return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
		}
		
		private void setUpMember(ModelMap model,GzMemberForm memberForm,GzBaseUser bu)
		{
			model.addAttribute("currUserModify",bu);
			memberForm.setMemberSummary(new GzMemberSummary(bu));
			memberForm.setInCompleteCommand(new GzMemberCommand());
			memberForm.getInCompleteCommand().setProfile(bu.createProfile());
			memberForm.getInCompleteCommand().setEnabled(bu.isEnabled());
			memberForm.getInCompleteCommand().setRole(bu.getRole());
			DecimalFormat df1 = new DecimalFormat("##0.00");
			memberForm.getInCompleteCommand().setWinCommission(df1.format(bu.getAccount().getWinCommission()));
			memberForm.getInCompleteCommand().setBetCommission(df1.format(bu.getAccount().getBetCommission()));
			memberForm.getInCompleteCommand().setCredit(df1.format(bu.getAccount().getCredit()));
		}
		
		private GzMemberForm createMemberForm(ModelMap model)
		{
			GzMemberForm memberForm = new GzMemberForm();
			GzBaseUser user = (GzBaseUser) model.get("currUser");
			
			GzMemberSummary ms = new GzMemberSummary(user);
			memberForm.getMembers().put(user.getCode(), ms);
			
			memberForm.setMemberSummary(populateMemberSummary(user,null,memberForm));
			double maxCredit = user.getAccount().getCredit() - gzServices.getGzHome().getDownStreamCredit(null,user);
			memberForm.setMaxCredit(maxCredit);	
			memberForm.setRoles(user.getRole().getAvailableRoles());
			memberForm.setInCompleteCommand(new GzMemberCommand());
			DecimalFormat df1 = new DecimalFormat("##0.00");
			memberForm.getInCompleteCommand().setRole(memberForm.getRoles().get(0));
			memberForm.getInCompleteCommand().setWinCommission(df1.format(user.getAccount().getWinCommission()));
			memberForm.getInCompleteCommand().setBetCommission(df1.format(user.getAccount().getBetCommission()));
			memberForm.getInCompleteCommand().setCredit(df1.format(maxCredit));		
			memberForm.getInCompleteCommand().setEnabled(user.isEnabled());
			return memberForm;
		}
			
		private GzMemberForm createMemberAccountForm(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(model);
			GzBaseUser user = (GzBaseUser) model.get("currUser");
			createChooseMembers(memberForm,model);
			double maxCredit = gzServices.getGzHome().getDownStreamCredit(null,user);
			memberForm.setMaxCredit(maxCredit);	
			return memberForm;
		}
		
		private GzMemberSummary populateMemberSummary(GzBaseUser user, GzMemberSummary parent,GzMemberForm memberForm) {
			GzMemberSummary ms = new GzMemberSummary(user);
			ms.setParent(parent);
			
			memberForm.getMembers().put(ms.getCode(), ms);
			if (!user.getRole().equals(GzRole.ROLE_GZADMIN))
				memberForm.getFlatMembers().add(ms);
			if (user.getRole().equals(GzRole.ROLE_PLAY))
				return ms;
			
			gzServices.getGzHome().getDownstreamForParent(user);
			for (GzBaseUser bu : user.getMembers())
				ms.getMembers().add(populateMemberSummary(bu,ms,memberForm));
			return ms;
		}

		
		private ModelAndView backToSignin(ModelMap model,String errMsg) {
			
			log.info("Received request to signin");
				
			HashMap<String,String> logon = new HashMap<String,String>();
			logon.put("errMsg", errMsg);
			logon.put("infoMsg", "");
			logon.put("email", "");
			
			return new ModelAndView("logon","logon", logon);
		}
		
		
		@RequestMapping(value="/processAdm", params="activateMember", method = RequestMethod.POST)
		public ModelAndView activateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("activateMember: " + command);
			return acivate(true,model,command);
		}
		
		@RequestMapping(value="/processAdm", params="deactivateMember", method = RequestMethod.POST)
		public ModelAndView deactivateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("deactivateMember: " + command);
			return acivate(false,model,command);
		}
		
		private ModelAndView acivate(boolean activate,ModelMap model,GzMemberCommand command)
		{
			GzMemberForm memberForm = createMemberAccountForm(model);
			memberForm.setInCompleteCommand(command);
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByMemberId(command.getMemberToChangeCode());
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				
				gzServices.getGzHome().updateBaseUserProfile(bu);
				bu.setEnabled(activate);
				if (activate == false)
					deactivateSubMembers(bu);
				setUpMember(model,memberForm,bu);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error activating/deactivating member - contact support.");
				return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
			}
			memberForm.getInCompleteCommand().setEnabled(activate);
			memberForm.setInfoMsg("Member : " + command.getMemberToChangeCode() + " - Successfully " + (activate ? "activated." : " deactivated."));
			return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="changeMemberToChange", method = RequestMethod.POST)
		public ModelAndView changeMemberToChange(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			String memberId = memberForm.getCommand().getMemberToChangeCode();
			memberForm = createMemberAccountForm(model);
			if (!memberForm.getChooseMembers().isEmpty())
			{
				GzBaseUser bu;
				try {
					bu = gzServices.getGzHome().getBaseUserByMemberId(memberId);
					setUpMember(model,memberForm,bu);
				} catch (GzPersistenceException e) {
					e.printStackTrace();
					memberForm.setErrMsg("Error on Manage Member - contact support");
				}
			}
			return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
		}
		
		private void deactivateSubMembers(GzBaseUser bu) {
			// DO LATER
		}

		@RequestMapping(value="/processAdm", params="searchContact", method = RequestMethod.POST)
		public ModelAndView searchContact(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchOnType(memberForm,model,"contact");
			return returnSearchModelAndView(memberForm,model);
		}
		
		@RequestMapping(value="/processAdm", params="searchEmail", method = RequestMethod.POST)
		public ModelAndView searchEmail(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchOnType(memberForm,model,"email");
			return returnSearchModelAndView(memberForm,model);
		}
		
		@RequestMapping(value="/processAdm", params="searchPhone", method = RequestMethod.POST)
		public ModelAndView searchPhone(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchOnType(memberForm,model,"phone");
			return returnSearchModelAndView(memberForm,model);
		}
		
		private ModelAndView returnSearchModelAndView(GzMemberForm memberForm,ModelMap model)
		{
			GzBaseUser currUser = (GzBaseUser) model.get("currUser");
			GzBaseUser bu = (GzBaseUser) model.get("currTargetUser");
			if (currUser.getCode().equals(bu.getParentCode()))
				return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
			else
				return new ModelAndView("GzAdmMemberDetails","memberForm", memberForm);
		}
		
		private GzMemberForm searchOnType(GzMemberForm memberForm,ModelMap model,String type)
		{
			log.info("Searching on : " + type);
			
			GzMemberCommand command = memberForm.getCommand();
			GzBaseUser user = (GzBaseUser) model.get("currUser"); 
			
			memberForm = createMemberForm(model);
			memberForm.setInCompleteCommand(command);
			List<GzMemberSummary> mss = new ArrayList<GzMemberSummary>();
			try {
				List<GzBaseUser> bus = gzServices.getGzHome().search(user,command.getSearch(), type);						
				for (GzBaseUser bu : bus)
				{
					GzMemberSummary ms = new GzMemberSummary(bu);
					mss.add(ms);
					ms.setParent(memberForm.getMembers().get(ms.getParentCode()));
				}
				if (!bus.isEmpty())
				{
					model.addAttribute("currTargetUser",bus.get(0));
					setUpMember(model,memberForm,bus.get(0));
				}
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				log.error("Search failed");
				return memberForm;
			}
			if (!mss.isEmpty())
			{
				memberForm.getInCompleteCommand().setMemberToChangeCode(mss.get(0).getMemberId());
				memberForm.setChooseMembers(mss);
			}
			else
			{
				memberForm.setInCompleteCommand(new GzMemberCommand());
				memberForm.setErrMsg("No " + type + " like this " + command.getSearch() + " found for downstream.");
			}
			return memberForm;
		}
		
		@RequestMapping(value="/backToAdm", method = RequestMethod.GET)
		public Object backToAdm(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberCancel", method = RequestMethod.POST)
		public Object memberCancel(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberRegister", method = RequestMethod.POST)
		public Object memberRegister(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberRegister: " + command);
			GzBaseUser superior = (GzBaseUser) model.get("currUser");
			GzRole role = command.getRole();
			
			command.setvPassword("88888888");
			command.getProfile().setPassword("88888888");
		
			GzAccount account = new GzAccount();
			String errMsg = command.getProfile().validate(command.getvPassword(),role);
			
			if (superior.getRole().equals(GzRole.ROLE_GZADMIN))
			{
				account.setBetCommission(100.0);
				account.setWinCommission(100.0);
				account.setCredit(10000000.0);
			}
			else
			if (errMsg.isEmpty())
				errMsg = validateCommissionsAndCredit(command,superior,account);
			
			if (!errMsg.isEmpty())
			{
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg(errMsg);
				return new ModelAndView("GzAdmMemberRegister","memberForm", memberForm);
			}
			
			GzBaseUser newMember = null;
			try
			{
				newMember = (GzBaseUser) role.getType().getCorrespondingClass().newInstance();
								
				newMember.copyProfileValues(command.getProfile());
				newMember.setParent(superior);
				newMember.setParentCode(superior.getCode());
				newMember.setRole(role);
				newMember.setEnabled(true);
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				newMember.setPassword(encoder.encode(command.getvPassword()));
				newMember.setAccount(account);
				
				gzServices.getGzHome().storeBaseUser(newMember);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error registering member - contact support.");
				return new ModelAndView("GzAdmMemberRegister","memberForm", memberForm);
			}
			
			return goAdmHome("","Member : " +  newMember.getContact() + " successfully registered with memberId : " + newMember.getMemberId(),model);
		}
		
		@RequestMapping(value="/memberProfile", method = RequestMethod.GET)
		public Object memberProfile(ModelMap model)
		{
			log.info("In memberProfile");
			GzBaseUser user = (GzBaseUser) model.get("currUser");
			
			GzMemberForm memberForm = createMemberForm(model);
			setUpMember(model,memberForm,user);
			
			return new ModelAndView("GzAdmMemberModify","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="memberModify", method = RequestMethod.POST)
		public Object memberModify(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberModify: " + command);
			GzBaseUser modify = (GzBaseUser) model.get("currUser");
			GzRole role = command.getRole();
			
			String errMsg = command.getProfile().validate(command.getvPassword(),role);
			
			if (!errMsg.isEmpty())
			{
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg(errMsg);
				return new ModelAndView("GzAdmMemberModify","memberForm", memberForm);
			}
			
			try
			{
				modify.copyProfileValues(command.getProfile());
				gzServices.getGzHome().updateBaseUserProfile(modify);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error modifying member - contact support.");
				return new ModelAndView("GzAdmMemberModify","memberForm", memberForm);
			}
			
			return goAdmHome("","Profile : " +  modify.getMemberId() + " updated.",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberModifyAccount", method = RequestMethod.POST)
		public Object memberModifyAccount(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberRegister: " + command);
			GzBaseUser superior = (GzBaseUser) model.get("currUser");
			GzBaseUser modify = (GzBaseUser) model.get("currUserModify");
			
			GzAccount account = new GzAccount();
			String errMsg = validateCommissionsAndCredit(command,superior,account);
			
			if (!errMsg.isEmpty())
			{
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg(errMsg);
				return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
			}
			
			try
			{
				modify.setAccount(account);
				gzServices.getGzHome().updateAccount(modify.getAccount());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error modifying member account - contact support.");
				return new ModelAndView("GzAdmMemberManageAccount","memberForm", memberForm);
			}
			
			return goAdmHome("","Member : " +  modify.getMemberId() + " updated.",model);
		}
		
		private String validateCommissionsAndCredit(GzMemberCommand command,GzBaseUser superior,GzAccount account)
		{
			DecimalFormat df = new DecimalFormat("##0.00");
			String errMsg = "";
			double maxBc = 100;
			double maxWc = 100;
			double maxCredit = Double.MAX_VALUE;
			if (!superior.getRole().equals(GzRole.ROLE_GZADMIN))
			{
				maxBc = superior.getAccount().getBetCommission();
				maxWc = superior.getAccount().getWinCommission();
				maxCredit = superior.getAccount().getCredit() - gzServices.getGzHome().getDownStreamCredit(null,superior);
			}
			try
			{
				double bc = Double.parseDouble(command.getBetCommission());
				double wc = Double.parseDouble(command.getWinCommission());
				double credit = Double.parseDouble(command.getCredit());
				if (bc>superior.getAccount().getBetCommission())
					errMsg += "Bet Commission cannot exceed : " + df.format(maxBc) + ",";
				if (wc>superior.getAccount().getWinCommission())
					errMsg += "Win Commission cannot exceed : " + df.format(maxWc) + ",";
				
				if (errMsg.isEmpty())
				{
					double maxDsBc = gzServices.getGzHome().getHigestDownstreamCommission('B',superior.getCode());
					if (bc>maxDsBc)
						errMsg += "Bet Commission cannot exceed highest downstream members' : " + df.format(maxDsBc) + " please request downstream resets,";
					double maxDsWc = gzServices.getGzHome().getHigestDownstreamCommission('W',superior.getCode());
					if (bc>maxDsWc)
						errMsg += "Win Commission cannot exceed highest downstream members' : " + df.format(maxDsWc) + " please request downstream resets,";
				}
				
				if (credit > maxCredit)
					errMsg = "Credit cannot exceed : " + df.format(maxCredit) + "',";
				
				account.setBetCommission(bc);
				account.setWinCommission(wc);
				account.setCredit(credit);
			}
			catch (NumberFormatException e)
			{
				errMsg = "Invalid format for numeric values";
			}
			if (errMsg.length()!=0)
				return errMsg.substring(0,errMsg.length()-1);
			return errMsg;
		}

		
		
}
