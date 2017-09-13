package org.dx4.secure.web.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.account.Dx4Rollup;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4FormValidationException;
import org.html.parser.util.ParseUtil;


public class AccountDetailsForm{
	
	private static final Logger log = Logger.getLogger(AccountDetailsForm.class);
	private AccountCommand command;
	private Dx4Account account;
	private String message;
	private String info;
	private boolean modify;
	private List<String> activeGames;
	private Boolean newAccountFlag;
	private Dx4Rollup rollup;
	
	// DON'T SIMPLY RECREATE GETTERS/SETTERS AS DEPENDENCY CODE INSIDE
	
	
	public AccountDetailsForm()
	{
		setModify(false);
		setNewAccountFlag(false);
	}
	
	private AccountDetailsForm(Dx4SecureUser user,Dx4SecureUser parent)
	{
		this();
		setAccount(user.getAccount());
	}
	
	public AccountDetailsForm(Dx4Home dx4Home,Dx4SecureUser user,Dx4SecureUser parent)
	{
		this(user,parent);
		activeGames = new ArrayList<String>();
		log.trace("AccountDetailsForm gettingGameGroup for : " + user);
		Dx4GameGroup group = dx4Home.getGameGroup(user);
		log.trace("AccountDetailsForm gotGameGroup : " + group);
		if (group==null)
			return;
		for (Dx4GameActivator ga : group.getGameActivators())
			activeGames.add(ga.getMetaGame().getName());
		rollup = dx4Home.getRollup(user);
	}
	
	public AccountDetailsForm(Dx4Home dx4Home,Dx4SecureUser user,Dx4SecureUser parent,AccountDetailsForm lastForm)
	{
		this(dx4Home,user,parent);
		setAccount(lastForm.getCommand().getNewAccount());
	}

	public void setCommand(AccountCommand command) {
		this.command = command;
	}

	public AccountCommand getCommand() {
		return command;
	}

	public void setAccount(Dx4Account account) {
		this.account = account;
	}

	public Dx4Account getAccount() {
		return account;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean isModify() {
		return modify;
	}

	public void validate(Dx4Home dx4Home,Dx4SecureUser user,Dx4SecureUser parent) throws Dx4FormValidationException
	{
		Dx4Account account = getCommand().getNewAccount();
		log.trace( " validate" + account);
		account.setBalance(ParseUtil.trimDecimals2(account.getBalance()));
		getCommand().setDwAmount(ParseUtil.trimDecimals2(getCommand().getDwAmount()));
	}

	public void validateWithDep(Dx4SecureUser user) throws Dx4FormValidationException{
		log.trace("validateWithDep " + getCommand());
		getCommand().setDwAmount(ParseUtil.trimDecimals2(getCommand().getDwAmount()));
		if (getCommand().getDwType().equals("Withdrawl"))
		{
			double diff = getCommand().getDwAmount()-user.getAccount().getBalance();
			if (diff<0)
				diff*=-1.0;
			if (diff>0.001)
			{
				log.trace("Amt: " +  getCommand().getDwAmount() + " Bal : " + user.getAccount().getBalance());
				throw new Dx4FormValidationException("Withdrawl amount cannot be more than account's balance.");
			}
		}
	}


	public List<String> getActiveGames() {
		return activeGames;
	}

	public void setActiveGames(List<String> activeGames) {
		this.activeGames = activeGames;
	}
	

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Boolean getNewAccountFlag() {
		return newAccountFlag;
	}

	public void setNewAccountFlag(Boolean newAccountFlag) {
		this.newAccountFlag = newAccountFlag;
	}

	public Dx4Rollup getRollup() {
		return rollup;
	}

	public void setRollup(Dx4Rollup rollup) {
		this.rollup = rollup;
	}
	
}
