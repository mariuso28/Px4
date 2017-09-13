package org.dx4.secure.web.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4NumberWin;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.player.display.DrawResultWin;

public class AgentWinForm 
{
	private static final Logger log = Logger.getLogger(AgentWinForm.class);
	private List<DrawResultWin> drw;
	private List<WinNumberWrapper> wnwl;
	private List<WinNumberWrapper> subWnwl;
	private AgentWinCommand command;
	private WinPlaceHolder wph;
	private Date drawDate;
	private String drawDateStr;
	
	
	public AgentWinForm()
	{
		super();
	}
	
	public AgentWinForm(ExternalGameResults xgr,Dx4Home dx4Home,Dx4SecureUser user)
	{
		setDrawDate(xgr.getDraws().get(0).getDate());
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
		setDrawDateStr(df1.format(getDrawDate()));
		drw = new ArrayList<DrawResultWin>();
		
		initialise(xgr,dx4Home,user);
	}
	
	private void initialise(ExternalGameResults xgr,Dx4Home dx4Home,Dx4SecureUser user)
	{
		initialiseWrws( dx4Home, user, xgr);
		log.info(wnwl);
		if (!wnwl.isEmpty())
			initialiseWph(user);
	}
	
	public AgentWinForm(ExternalGameResults xgr,Dx4Home dx4Home,Dx4SecureUser user,List<String> newPlacings)
	{
		setDrawDate(xgr.getDraws().get(0).getDate());
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
		setDrawDateStr(df1.format(getDrawDate()));
		drw = new ArrayList<DrawResultWin>();
		
		if (newPlacings.get(0).contains(" - Total - "))
		{
			initialise(xgr,dx4Home,user);
			wph = new WinPlaceHolder(newPlacings);
			return;
		}
		
		wph = new WinPlaceHolder(newPlacings);
		createWrws( dx4Home, user, xgr );
		log.info("WPH: " + wph);
	}
	
	private void createWrws(Dx4Home dx4Home, Dx4SecureUser user, ExternalGameResults xgr) {
		
		createWnwl( dx4Home, user, xgr);	
		if (user.getRole().equals(Dx4Role.ROLE_PLAY))
		{
			if (wph.getPlacings().size()>1)
				wph.setPlacings(wph.getPlacings().subList(0,1));
			return;
		}	
		//  base wnwl
		subWnwl = getWinNumberWrappersForPlacing(dx4Home,wph.getPlacings().get(0)); 					// selected 
		if (wph.getPlacings().size()<2 && subWnwl.size()>0)
		{
			wph.getPlacings().add(subWnwl.get(0).getPlacingCode());
		}
	}
	
	private List<WinNumberWrapper> getWinNumberWrappersForPlacing(Dx4Home dx4Home,String placing) 
	{
		StringTokenizer st = new StringTokenizer(placing,WinNumberWrapper.SEPERATOR);
		String code = st.nextToken();
		String number = st.nextToken();
		String win = st.nextToken();
		String place = st.nextToken();
		// cos got spaces handle different
		int pos = placing.lastIndexOf(WinNumberWrapper.SEPERATOR);
		String provider = placing.substring(pos + WinNumberWrapper.SEPERATOR.length()); 
		Dx4SecureUser user;
		try {
			user = dx4Home.getUserByCode(code);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		List<WinNumberWrapper> wnwl1 = new ArrayList<WinNumberWrapper>();
		log.info("getWinNumberWrappersForPlacing Testing for : " + code + "#" + number 
				+ "#" + win + "#" + place + "#" + provider);
		
		dx4Home.getDownstreamForParent(user);
		for (Dx4SecureUser member : user.getMembers())
		{
			List<Dx4NumberWin> numberWins = dx4Home.getWinsForDate(member, drawDate);
			for (Dx4NumberWin numberWin : numberWins)
			{
				if (numberWin.getNumber().equals(number) && 
					numberWin.getProviderCode().equals(dx4Home.getProviderByName(provider).getCode()))
				{
						wnwl1.add(new WinNumberWrapper(member.getCode(),numberWin,dx4Home));
				}
			}
		}
		return wnwl1;
	}


	private void initialiseWph(Dx4SecureUser user) {
		wph = new WinPlaceHolder();
		wph.getPlacings().add(wnwl.get(0).getPlacingCode());
		if (user.getRole().equals(Dx4Role.ROLE_PLAY))
			return;
		wph.getPlacings().add(subWnwl.get(0).getPlacingCode());
	}

	private void createWnwl(Dx4Home dx4Home,Dx4SecureUser user,ExternalGameResults xgr)
	{
		wnwl = new ArrayList<WinNumberWrapper>();
		Date date = xgr.getDraws().get(0).getDate();
		WinNumberWrapper wnw = createTotalWnw(dx4Home,user,date);
		if (wnw==null)
			return;
		
		wnwl.add(wnw);
		List<Dx4NumberWin> numberWins = dx4Home.getWinsForDate(user, date);

		for (Dx4DrawResultJson result : xgr.getDraws())
		{
			DrawResultWin win = createDrawResultWin(result,dx4Home,numberWins);
			drw.add(win);
		}

		for (Dx4NumberWin numberWin : numberWins)
		{
			wnw = new WinNumberWrapper(user.getCode(),numberWin,dx4Home);
			wnwl.add(wnw);
		}
	}
	
	private void initialiseWrws(Dx4Home dx4Home,Dx4SecureUser user,ExternalGameResults xgr)
	{
		createWnwl( dx4Home, user, xgr);
		if (wnwl.isEmpty())
			return;
		Date date = xgr.getDraws().get(0).getDate();
		WinNumberWrapper wnw = wnwl.get(0);
		if (user.getRole().equals(Dx4Role.ROLE_PLAY))
			return;
		createTotals(dx4Home,user,wnw,date);
	}
	
	private void createTotals(Dx4Home dx4Home,Dx4SecureUser user,WinNumberWrapper wnw,Date date)
	{
		dx4Home.getDownstreamForParent(user);
		List<Dx4SecureUser> members = user.getMembers();
		subWnwl = new ArrayList<WinNumberWrapper>();
		for (Dx4SecureUser member : members)
		{
			log.info("createTotals :" + member.getCode());
			WinNumberWrapper twnw = createTotalWnw(dx4Home,member,date);
			if (twnw != null)
			{
				subWnwl.add(twnw);
			}
		}
	}
	
	private WinNumberWrapper createTotalWnw(Dx4Home dx4Home,Dx4SecureUser user,Date date)
	{
		Double total = dx4Home.getTotalWinsForDate(user, date);
		log.info("createTotalWnw :" + total);
		if (total==null)
			return null;
		WinNumberWrapper wnw = new WinNumberWrapper(user.getCode(),new Dx4NumberWin("Total",total),dx4Home);
		return wnw;
	}
	
	private DrawResultWin createDrawResultWin(Dx4DrawResultJson result,Dx4Home dx4Home,List<Dx4NumberWin> numberWins)
	{
		DrawResultWin drw = new DrawResultWin(result);
		for (Dx4NumberWin numberWin : numberWins)
		{
			if (Character.valueOf(numberWin.getProviderCode()).equals(result.getProvider().getCode()))
					drw.addNumberWin(numberWin,result,dx4Home);
		}
		return drw;
	}
	
	public void setDrw(List<DrawResultWin> drw) {
		this.drw = drw;
	}
	public List<DrawResultWin> getDrw() {
		return drw;
	}

	public void setCommand(AgentWinCommand command) {
		this.command = command;
	}

	public AgentWinCommand getCommand() {
		return command;
	}

	public WinPlaceHolder getWph() {
		return wph;
	}

	public void setWph(WinPlaceHolder wph) {
		this.wph = wph;
	}

	public List<WinNumberWrapper> getWnwl() {
		return wnwl;
	}

	public void setWnwl(List<WinNumberWrapper> wnwl) {
		this.wnwl = wnwl;
	}

	public List<WinNumberWrapper> getSubWnwl() {
		return subWnwl;
	}

	public void setSubWnwl(List<WinNumberWrapper> subWnwl) {
		this.subWnwl = subWnwl;
	}

	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	public String getDrawDateStr() {
		return drawDateStr;
	}

	public void setDrawDateStr(String drawDateStr) {
		this.drawDateStr = drawDateStr;
	}
	
	
}
