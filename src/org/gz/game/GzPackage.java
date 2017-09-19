package org.gz.game;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.gz.baseuser.GzBaseUser;

public class GzPackage 
{
	private static Logger log = Logger.getLogger(GzPackage.class);
	
	private long id;
	private GzGroup group;
	private String name;
	private GzBaseUser member;
	private String memberId;
	private Date created;
	private Map<Dx4GameTypeJson,GzGameTypePayouts> gameTypePayouts = new TreeMap<Dx4GameTypeJson,GzGameTypePayouts>();

	public GzPackage()
	{
		setCreated((new GregorianCalendar()).getTime());
	}

	public GzPackage(String name)
	{
		this();
		setName(name);
	}
	
	public void populateDefaults(Dx4MetaGame metagame)
	{
		for (Dx4GameTypeJson gt : Dx4GameTypeJson.values())
		{
			log.debug("Building for default : " + gt.name() + " placings: " + gt.getPlacings()[0] + ","  + gt.getPlacings()[1] 
											+ ","  + gt.getPlacings()[2] + ","  + gt.getPlacings()[3] + "," + gt.getPlacings()[4]);
			GzGameTypePayouts gtp = new GzGameTypePayouts(gt);
			
			Dx4Game game = metagame.getGameByType(gt);
			// log.info("Using game : " + game);
			if (gt.getPlacings()[0])
			{
				Dx4PayOutTypeJson pt = Dx4PayOutTypeJson.valueOfFromCode('F');
				Dx4PayOut po = game.getPayOutByType(pt);
				gtp.addInPayOut(new Dx4PayOut(po));
			}
			if (gt.getPlacings()[1])
			{
				Dx4PayOutTypeJson pt = Dx4PayOutTypeJson.valueOfFromCode('S');
				Dx4PayOut po = game.getPayOutByType(pt);
				gtp.addInPayOut(new Dx4PayOut(po));
			}
			if (gt.getPlacings()[2])
			{
				Dx4PayOutTypeJson pt = Dx4PayOutTypeJson.valueOfFromCode('T');
				Dx4PayOut po = game.getPayOutByType(pt);
				gtp.addInPayOut(new Dx4PayOut(po));
			}
			if (gt.getPlacings()[3])
			{
				Dx4PayOutTypeJson pt = Dx4PayOutTypeJson.valueOfFromCode('P');
				Dx4PayOut po = game.getPayOutByType(pt);
				gtp.addInPayOut(new Dx4PayOut(po));
			}
			if (gt.getPlacings()[4])
			{
				Dx4PayOutTypeJson pt = Dx4PayOutTypeJson.valueOfFromCode('C');
				Dx4PayOut po = game.getPayOutByType(pt);
				gtp.addInPayOut(new Dx4PayOut(po));
			}
			
			gameTypePayouts.put(gt,gtp);
		}
	}
	
	public String getSummaryPayouts()
	{
		return "" + gameTypePayouts.get(Dx4GameTypeJson.D4Big).getPayOuts().get(0).getPayOut()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D4Small).getPayOuts().get(0).getPayOut()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D4A).getPayOuts().get(0).getPayOut()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.ABCA).getPayOuts().get(0).getPayOut()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.ABCC).getPayOuts().get(0).getPayOut()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D2A).getPayOuts().get(0).getPayOut();
	}
	
	public String getSummaryCommissions()
	{
		return "" + gameTypePayouts.get(Dx4GameTypeJson.D4Big).getCommission()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D4Small).getCommission()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D4A).getCommission()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.ABCA).getCommission()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.ABCC).getCommission()
				+ "/" + gameTypePayouts.get(Dx4GameTypeJson.D2A).getCommission();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Dx4GameTypeJson, GzGameTypePayouts> getGameTypePayouts() {
		return gameTypePayouts;
	}

	public void setGameTypePayouts(Map<Dx4GameTypeJson, GzGameTypePayouts> gameTypePayouts) {
		this.gameTypePayouts = gameTypePayouts;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public GzBaseUser getMember() {
		return member;
	}

	public void setMember(GzBaseUser member) {
		this.member = member;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Override
	public String toString() {
		return "GzPackage [id=" + id + ", name=" + name + ", memberId=" + memberId + ", created=" + created + "]";
	}

	public GzGroup getGroup() {
		return group;
	}

	public void setGroup(GzGroup group) {
		this.group = group;
	}
	
	
}
