package org.gz.account;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4GameTypeJson;
import org.gz.agent.GzAgent;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.baseuser.GzRoleType;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;

public class GzAccountMgr {

	protected static Logger log = Logger.getLogger(GzAccountMgr.class);
	private GzServices services;
	private GzHome home;
	
	public GzAccountMgr()
	{
	}
	
	public GzNumberRetainerSet getGzNumberRetainerSet(int digits,GzBaseUser user)
	{
		List<GzNumberRetainer> defaults = home.getGzDefaultNumberRetainersForUser(user, digits);
		GzNumberRetainerSet nrs = new GzNumberRetainerSet();
		nrs.storeDefaults(defaults);
		List<GzNumberRetainer> indivs = home.getGzIndividualNumberRetainersForUser(user, digits);
		for (GzNumberRetainer nr : indivs)
			nrs.storeIndividual(nr);
		nrs.setDigits(digits);
		return nrs;
	}
	
	public void createTransactions(GzBaseUser player,double amount,String source,Dx4GameTypeJson gameType,String number) throws GzPersistenceException
	{
		log.info("Creating transactions for : " + player + " source: " + source);
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		
		
		if (amount<=0)
			return;
		
		getMemberChain(player);			
		GzAgent agent = (GzAgent) player.getParent();				// need to populate the parent chain if not already		
		createTransactionsForPlayer(player,amount,agent,source,gameType,now,number);
	}

	private void createTransactionsForPlayer(GzBaseUser player,double amount,GzAgent agent,String source,Dx4GameTypeJson gameType,Date now,String number) throws GzPersistenceException
	{
		GzTransaction transaction = new GzTransaction(player.getMemberId(),agent.getMemberId(),GzTransaction.BET,amount,now,gameType,source,number);
		
		double flight = amount;
		GzInvoice invoice = storeOrUpdateInvoice(player,agent,flight,0,now,null,gameType);
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
//		home.updateAccountBalance(agent.getAccount(),amount);
//		home.updateAccountBalance(player.getAccount(),-1*amount);
		
		while (true)
		{
			GzAgent parent = (GzAgent) agent.getParent();
			if (parent==null || parent.getRole().equals(GzRole.ROLE_GZADMIN))
				break;
			
			GzNumberRetainer nr = home.getGzNumberRetainerForUser(agent, gameType, number);
			double retain = calcRetain(flight,agent.getAccount().getBetCommission(),nr.getRetain());
			flight = calcFlight(flight,retain);
			invoice = storeOrUpdateInvoice(agent,parent,flight,retain,now,invoice,gameType);
			
	//		home.updateAccountBalance(agent.getAccount(),-1*netAmount);
	//		home.updateAccountBalance(parent.getAccount(),netAmount);
			agent = parent;
		}
	}
	
	private double calcRetain(double amount,double commission,double retain)
	{
		commission = commission / 100.0;
		if (amount * commission > retain)
			return retain;
		return amount * commission;
	}
	
	private double calcFlight(double amount,double retain)
	{
		return amount - retain;
	}
	
	private GzInvoice storeOrUpdateInvoice(GzBaseUser payer,GzBaseUser payee,double flight,double retain,
			Date now,GzInvoice subInvoice,Dx4GameTypeJson gameType) throws GzPersistenceException
	{
//		GzInvoice invoice = home.getOpenInvoice(payer.getEmail(),payee.getEmail());
	//	if (invoice == null)
	//	{
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.HOUR,5*24);											// currently arbitrary
			GzInvoice invoice = new GzInvoice(payer.getMemberId(),payee.getMemberId(),flight,retain,now,gc.getTime(),gameType);
			home.storeInvoice(invoice);
	/*	}
		else
		{
			double useAmount = amount;
			home.updateInvoice(useAmount, commission, netAmount,invoice.getId());
		}
		*/
		if (subInvoice != null)
			home.updateSubInvoice(subInvoice,invoice);
		return invoice;
	}
	
	
	private GzBaseUser getMemberChain(GzBaseUser bu) throws GzPersistenceException
	{
		GzBaseUser parent = bu;
		while (!bu.getRole().getType().equals(GzRoleType.DUSTBIN))
		{
			parent = bu.getParent();
			if (parent == null)												// get from db
			{
				parent = home.getBaseUserByCode(bu.getParentCode());
				bu.setParent(parent);
			}
			bu = parent;
		}
		return parent;
	}
	
	/*
	public void collectStake(double stake,GzBaseUser player,GzBaseUser banker,boolean houseBanker,UUID gameId) throws GzPersistenceException
	{
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		
		log.info("Collecting stake for : " + player + " : " + stake);
		double netStake = playerCollectStake(player,stake,gameId,gc,now);
		
		if (!houseBanker)							// leave with the company
			bankerCollectNetStake(banker,netStake,gameId,gc,now);
		
	}
	
	private void bankerCollectNetStake(GzBaseUser banker, double netStake, UUID gameId, GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		@SuppressWarnings("unused")
		GzBaseUser topAgent = getMemberChain(banker);			// need to populate the parent chain if not already - send to banker
		
		GzBaseUser agent = banker.getParent();
		GzTransaction transaction = new GzTransaction(agent.getEmail(),banker.getEmail(),GzTransaction.BETTYPEBANKERCOLLECTSTAKE,netStake,now,gameId);
		GzInvoice invoice = storeOrUpdateInvoice(agent,banker,netStake,0,netStake,now,null,0,GzInvoice.WINSTAKESTAKE);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		home.updateAccountBalance(agent.getAccount(),-1*netStake);
		home.updateAccountBalance(banker.getAccount(),netStake);
		
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent==null)
				break;
			
			invoice = storeOrUpdateInvoice(parent,agent,netStake,0.0,netStake,now,invoice,0,GzInvoice.WINSTAKESTAKE);
			
			home.updateAccountBalance(parent.getAccount(),-1*netStake);
			home.updateAccountBalance(agent.getAccount(),netStake);
			agent = parent;
		}
		
	}

	private double playerCollectStake(GzBaseUser player,double stake,UUID gameId,GregorianCalendar gc,Date now) throws GzPersistenceException
	{
		@SuppressWarnings("unused")
		GzBaseUser topAgent = getMemberChain(player);								// need to populate the parent chain if not already
		GzBaseUser agent = player.getParent();
		GzTransaction transaction = new GzTransaction(player.getEmail(),agent.getEmail(),GzTransaction.BETTYPEPLAYERSTAKE,stake,now,gameId);
		
		double commission = (player.getAccount().getBetCommission()/100.0) * stake;
		double netStake = stake - commission;
		GzInvoice invoice = storeOrUpdateInvoice(player,agent,stake,commission,netStake,now,null,0,GzInvoice.WINSTAKESTAKE);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
	
		home.updateAccountBalance(player.getAccount(),-1*netStake);
		home.updateAccountBalance(agent.getAccount(),netStake);
		
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent == null)
				break;
			commission = (agent.getAccount().getBetCommission()/100.0) * stake;
			netStake = stake - commission;
			invoice = storeOrUpdateInvoice(agent,parent,stake,commission,netStake,now,invoice,0,GzInvoice.WINSTAKESTAKE);
			
			home.updateAccountBalance(agent.getAccount(),-1*netStake);
			home.updateAccountBalance(parent.getAccount(),netStake);
			agent = parent;
		}
		
		return netStake;
	}
	
	/*
	public void distributeWin(GzBetRollupElement element,GzBaseUser player,GzBaseUser banker,boolean houseBanker,UUID gameId) throws GzPersistenceException
	{
		// assume comes in as excommission	
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		
		log.info("Distributing win for : " + player + " : " + element.getWin() + " : " + element.getStake());
		if (houseBanker)									
			playerCollectHouseWin(element.getWin(),element.getStake(),player,gameId,gc,now);
		else
			playerCollectBankerWin(element.getWin(),element.getStake(),player,banker,gameId,gc,now);
		
		
	}
	
	public void distributeTie(GzBetRollupElement element, GzBaseUser player, GzBaseUser banker,
			boolean houseBanker, UUID gameId) throws GzPersistenceException {
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		
		log.info("Distributing tie for : " + player + " : " + element.getWin() + " : " + element.getStake());
		if (houseBanker)									
			playerCollectHouseTie(element.getStake(),player,gameId,gc,now);
		else
			playerCollectBankerTie(element.getStake(),player,banker,gameId,gc,now);
		
	}

	private void playerCollectBankerTie(double stake, GzBaseUser player, GzBaseUser banker,
			UUID gameId, GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		double netWin = bankerDistributeTie(banker,stake,gameId,gc,now);		
		playerCollectNetTie(player,stake,netWin,gameId,gc,now);
	}

	private double bankerDistributeTie(GzBaseUser banker, double stake, UUID gameId,GregorianCalendar gc, Date now) throws GzPersistenceException
	{
		GzBaseUser topAgent = getMemberChain(banker);	

		GzBaseUser agent = banker.getParent();
		double commission = (banker.getAccount().getWinCommission()/100.0) * stake;
		double netWin = stake - commission;

		GzTransaction transaction = new GzTransaction(banker.getEmail(),agent.getEmail(),GzTransaction.BETTYPEBANKERPAYTIE,netWin,now,gameId);
		GzInvoice invoice = storeOrUpdateInvoice(banker,agent,0.0,-1*commission,netWin,now,null,stake,GzInvoice.WINSTAKETIE);

		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);

		home.updateAccountBalance(banker.getAccount(),-1*netWin);
		home.updateAccountBalance(agent.getAccount(),netWin);

		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent == null)
				break;
			commission = (agent.getAccount().getWinCommission()/100.0) * stake;
			netWin = stake - commission;
			invoice = storeOrUpdateInvoice(agent,parent,0.0,-1*commission,netWin,now,invoice,stake,GzInvoice.WINSTAKETIE);

			home.updateAccountBalance(agent.getAccount(),-1*netWin);
			home.updateAccountBalance(parent.getAccount(),netWin);

			agent = parent;
		}

		commission = (topAgent.getAccount().getWinCommission()/100.0) * stake;				// final number is for distribution
		netWin = stake - commission;
		return netWin;
	}

	private void playerCollectHouseTie(double stake, GzBaseUser player, UUID gameId,
			GregorianCalendar gc, Date now) throws GzPersistenceException {
		GzBaseUser topAgent = getMemberChain(player);	
		
		double commission = ((topAgent.getAccount().getWinCommission() - player.getAccount().getWinCommission())/100.0) * stake;
		double netWin = stake - commission;
		
		
		GzBaseUser agent = player.getParent();
		GzTransaction transaction = new GzTransaction(agent.getEmail(),player.getEmail(),GzTransaction.BETTYPEPLAYERTIE,netWin,now,gameId);
		
		GzInvoice invoice = storeOrUpdateInvoice(agent,player,0.0,-1*commission,netWin,now,null,stake,GzInvoice.WINSTAKETIE);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		
		home.updateAccountBalance(agent.getAccount(),-1*netWin);
		home.updateAccountBalance(player.getAccount(),netWin);
				
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent == null)
				break;
			commission = ((topAgent.getAccount().getWinCommission() - agent.getAccount().getWinCommission())/100.0) * stake;
			netWin = stake - commission;
			
			invoice = storeOrUpdateInvoice(parent,agent,0.0,-1*commission,netWin,now,invoice,stake,GzInvoice.WINSTAKETIE);
			
			home.updateAccountBalance(parent.getAccount(),-1*netWin);
			home.updateAccountBalance(agent.getAccount(),netWin);
			agent = parent;
		}
		
	}

	private void playerCollectBankerWin(double win,double stake,GzBaseUser player, GzBaseUser banker, UUID gameId,
			GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		double netWin = bankerDistributeWin(banker,stake,win,gameId,gc,now);		
		playerCollectNetWin(player,stake,netWin,gameId,gc,now);
	}

	private void playerCollectNetTie(GzBaseUser player, double stake,double netWin, UUID gameId, GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		// commission already paid on banker side - just send down the win
		getMemberChain(player);			// need to populate the parent chain if not already - send to player
		
		GzBaseUser agent = player.getParent();
		GzTransaction transaction = new GzTransaction(agent.getEmail(),player.getEmail(),GzTransaction.BETTYPEPLAYERTIE,netWin,now,gameId);
		GzInvoice invoice = storeOrUpdateInvoice(agent,player,netWin,0,netWin,now,null,0,GzInvoice.WINSTAKETIE);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		home.updateAccountBalance(agent.getAccount(),-1*netWin);
		home.updateAccountBalance(player.getAccount(),netWin);
		
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent==null)
				break;
			
			invoice = storeOrUpdateInvoice(parent,agent,netWin,0.0,netWin,now,invoice,0,GzInvoice.WINSTAKETIE);
			
			home.updateAccountBalance(parent.getAccount(),-1*netWin);
			home.updateAccountBalance(agent.getAccount(),netWin);
			
			agent = parent;
		}
		
	}
	
	private void playerCollectNetWin(GzBaseUser player, double stake,double netWin, UUID gameId, GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		// commission already paid on banker side - just send down the win
		getMemberChain(player);			// need to populate the parent chain if not already - send to player
		
		GzBaseUser agent = player.getParent();
		GzTransaction transaction = new GzTransaction(agent.getEmail(),player.getEmail(),GzTransaction.BETTYPEPLAYERWIN,netWin,now,gameId);
		GzInvoice invoice = storeOrUpdateInvoice(agent,player,netWin,0,netWin,now,null,0,GzInvoice.WINSTAKEWIN);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		home.updateAccountBalance(agent.getAccount(),-1*netWin);
		home.updateAccountBalance(player.getAccount(),netWin);
				
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent==null)
				break;
			
			invoice = storeOrUpdateInvoice(parent,agent,netWin,0.0,netWin,now,invoice,0,GzInvoice.WINSTAKEWIN);
			
			home.updateAccountBalance(parent.getAccount(),-1*netWin);
			home.updateAccountBalance(agent.getAccount(),netWin);
			
		agent = parent;
		}
		
	}

	private double bankerDistributeWin(GzBaseUser banker, double stake, double win, UUID gameId, GregorianCalendar gc, Date now) throws GzPersistenceException {
		
		GzBaseUser topAgent = getMemberChain(banker);	
		
		GzBaseUser agent = banker.getParent();
		double commission = (banker.getAccount().getWinCommission()/100.0) * win;
		double netWin = win - commission + stake;
		
		GzTransaction transaction = new GzTransaction(banker.getEmail(),agent.getEmail(),GzTransaction.BETTYPEBANKERPAYWIN,netWin,now,gameId);
		GzInvoice invoice = storeOrUpdateInvoice(banker,agent,win,-1*commission,netWin,now,null,stake,GzInvoice.WINSTAKEWIN);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		
		home.updateAccountBalance(banker.getAccount(),-1*netWin);
		home.updateAccountBalance(agent.getAccount(),netWin);
		
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent == null)
				break;
			commission = (agent.getAccount().getWinCommission()/100.0) * win;
			netWin = win - commission + stake;
			invoice = storeOrUpdateInvoice(agent,parent,win,-1*commission,netWin,now,invoice,stake,GzInvoice.WINSTAKEWIN);
			
			home.updateAccountBalance(agent.getAccount(),-1*netWin);
			home.updateAccountBalance(parent.getAccount(),netWin);
			
			agent = parent;
		}
		
		commission = (topAgent.getAccount().getWinCommission()/100.0) * win;				// final number is for distribution
		netWin = win - commission + stake;
		return netWin;
	}

	private void playerCollectHouseWin(double win,double stake,GzBaseUser player,UUID gameId,GregorianCalendar gc,Date now) throws GzPersistenceException
	{
		GzBaseUser topAgent = getMemberChain(player);	
		
		double commission = ((topAgent.getAccount().getWinCommission() - player.getAccount().getWinCommission())/100.0) * win;
		double netWin = win - commission + stake;
		
		
		GzBaseUser agent = player.getParent();
		GzTransaction transaction = new GzTransaction(agent.getEmail(),player.getEmail(),GzTransaction.BETTYPEPLAYERWIN,netWin,now,gameId);
		
		GzInvoice invoice = storeOrUpdateInvoice(agent,player,win,-1*commission,netWin,now,null,stake,GzInvoice.WINSTAKEWIN);
		
		transaction.setInvoiceId(invoice.getId());
		home.storeTransaction(transaction);
		
		home.updateAccountBalance(agent.getAccount(),-1*netWin);
		home.updateAccountBalance(player.getAccount(),netWin);
		
		while (true)
		{
			GzBaseUser parent = agent.getParent();
			if (parent == null)
				break;
			commission = ((topAgent.getAccount().getWinCommission() - agent.getAccount().getWinCommission())/100.0) * win;
			netWin = win - commission + stake;
			
			invoice = storeOrUpdateInvoice(parent,agent,win,-1*commission,netWin,now,invoice,stake,GzInvoice.WINSTAKEWIN);
			
			home.updateAccountBalance(parent.getAccount(),-1*netWin);
			home.updateAccountBalance(agent.getAccount(),netWin);
			agent = parent;
		}
	}
	
	private GzInvoice storeOrUpdateInvoice(GzBaseUser payer,GzBaseUser payee,double amount,double commission,
			double netAmount,Date now,GzInvoice subInvoice,double stake,char winstake) throws GzPersistenceException
	{
		GzInvoice invoice = home.getOpenInvoice(payer.getEmail(),payee.getEmail(),winstake);
		if (invoice == null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.HOUR,payer.getAccount().getPaymentDays()*24);
			if (winstake==GzInvoice.WINSTAKEWIN)
				amount += stake;
			invoice = new GzInvoice(payer.getEmail(),payee.getEmail(),amount,commission,netAmount,now,gc.getTime(),stake,winstake);
			home.storeInvoice(invoice);
		}
		else
		{
			// invoice.update(amount,commission,netAmount,stake,winstake);
			// home.updateInvoice(invoice);
			
			double useAmount = amount;
			if (winstake==GzInvoice.WINSTAKEWIN)
				useAmount+=stake;
				
			home.updateInvoice(useAmount, commission, netAmount, stake, invoice.getId());
			
		}
		if (subInvoice != null)
			home.updateSubInvoice(subInvoice,invoice);
		return invoice;
	}

	@SuppressWarnings("unused")
	private void storeAccounts(GzBaseUser member) throws GzPersistenceException
	{
		log.info("Updating accounts for : " + member.getEmail());
		while (member!=null)
		{
			home.updateAccount(member.getAccount());
			member = member.getParent();
		}
	}

	public double getWinExComm(double win, GzBaseUser player,GzBaseUser banker,boolean houseBanker) throws GzPersistenceException {
		
		GzBaseUser topAgent = null;
		if (!houseBanker)									
			topAgent = getMemberChain(banker);
		else
			topAgent = getMemberChain(player);
		
		double commission = win * ((topAgent.getAccount().getWinCommission() - player.getAccount().getWinCommission())/ 100.0);
		return win - commission;
	}
	
	public double getTieExComm(double stake, GzBaseUser player, GzBaseUser banker, boolean houseBanker) throws GzPersistenceException {
		
		GzBaseUser topAgent = null;
		if (!houseBanker)									
			topAgent = getMemberChain(banker);
		else
			topAgent = getMemberChain(player);
		double commission = stake * ((topAgent.getAccount().getWinCommission() - player.getAccount().getWinCommission())/ 100.0);
		return stake - commission;
	}
	
	private GzBaseUser getMemberChain(GzBaseUser bu) throws GzPersistenceException
	{
		GzBaseUser parent = bu;
		while (bu.getParentCode().charAt(0) != GzRole.ROLE_ADMIN.getCode())
		{
			parent = bu.getParent();
			if (parent == null)												// get from db
			{
				parent = home.getAgentByCode(bu.getParentCode());
				bu.setParent(parent);
			}
			bu = parent;
		}
		return parent;
	}
	
	*/
	
	public GzHome getHome() {
		return home;
	}

	public void setHome(GzHome home) {
		this.home = home;
	}

	public GzServices getServices() {
		return services;
	}

	public void setServices(GzServices services) {
		this.services = services;
	}

	
}
