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
	private String name;
	private GzBaseUser member;
	private String memberId;
	private Date created;
	private Map<Dx4GameTypeJson,GzGameTypePayouts> gameTypePayouts = new TreeMap<Dx4GameTypeJson,GzGameTypePayouts>();

	public GzPackage()
	{
		setCreated((new GregorianCalendar()).getTime());
	}

	public void populateDefaults(Dx4MetaGame metagame)
	{
		for (Dx4GameTypeJson gt : Dx4GameTypeJson.values())
		{
			log.info("Building for default : " + gt.name() + " placings: " + gt.getPlacings()[0] + ","  + gt.getPlacings()[1] 
												+ ","  + gt.getPlacings()[2] + ","  + gt.getPlacings()[3] + "," + gt.getPlacings()[4]);
			GzGameTypePayouts gtp = new GzGameTypePayouts(gt);
			
			Dx4Game game = metagame.getGameByType(gt);
			log.info("Using game : " + game);
			if (gt.equals(Dx4GameTypeJson.D2C))
				log.info("found");
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
	
	
}
