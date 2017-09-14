package org.gz.game;

import java.util.Map;
import java.util.TreeMap;

import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.gz.agent.GzAgent;

public class GzGameMap 
{
	private GzAgent agent;
	private Map<Integer,GzGameTypePayouts> gameTypePayouts = new TreeMap<Integer,GzGameTypePayouts>();

	public GzGameMap()
	{
	}
	/*
	private GzGameTypePayouts createD4Big()
	{
		GzGameTypePayouts gtp = new GzGameTypePayouts(Dx4GameTypeJson.D4Big);
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.First,2000));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Second,1000));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Third,500));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Spec,200));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Cons,70));
	}
	

	private GzGameTypePayouts createD4Small()
	{
		GzGameTypePayouts gtp = new GzGameTypePayouts(Dx4GameTypeJson.D4Small);
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.First,3000));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Second,2000));
		gtp.addInPayOut(new Dx4PayOutJson(Dx4PayOutTypeJson.Third,1000));
	}
	*/
	public Map<Integer,GzGameTypePayouts> getGameTypePayouts() {
		return gameTypePayouts;
	}

	public void setGameTypePayouts(Map<Integer,GzGameTypePayouts> gameTypePayouts) {
		this.gameTypePayouts = gameTypePayouts;
	}

	public GzAgent getAgent() {
		return agent;
	}

	public void setAgent(GzAgent agent) {
		this.agent = agent;
	}
	
	
}
