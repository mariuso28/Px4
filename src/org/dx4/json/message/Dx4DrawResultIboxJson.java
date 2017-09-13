package org.dx4.json.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4IboxMatch;

public class Dx4DrawResultIboxJson {

	private static final Logger log = Logger.getLogger(Dx4DrawResultIboxJson.class);
	
	private HashSet<String> firstPlace;
	private HashSet<String> secondPlace;
	private HashSet<String> thirdPlace;
	private List<HashSet<String>> specials = new ArrayList<HashSet<String>>();
	private List<HashSet<String>> consolations = new ArrayList<HashSet<String>>();
	private Dx4DrawResultJson dr;
	
	public Dx4DrawResultIboxJson()
	{
	}
		
	public void create(Dx4DrawResultJson dr)
	{
		setDr(dr);
		firstPlace = Dx4GameTypeJson.createCombos(dr.getFirstPlace());
		secondPlace = Dx4GameTypeJson.createCombos(dr.getSecondPlace());
		thirdPlace = Dx4GameTypeJson.createCombos(dr.getThirdPlace());
		for (String sp : dr.getSpecials())
		{
			HashSet<String> hs = Dx4GameTypeJson.createCombos(sp);
			specials.add(hs);
		}
		for (String cn : dr.getConsolations())
		{
			HashSet<String> hs = Dx4GameTypeJson.createCombos(cn);
			consolations.add(hs);
		}
	}
	
	public Dx4IboxMatch isFirst(String choice)
	{
		if (firstPlace.contains(choice))
			return new Dx4IboxMatch(dr.getFirstPlace(),firstPlace.size());
		
		return null;
	}
	
	public Dx4IboxMatch isSecond(String choice)
	{
		if (secondPlace.contains(choice))
			return new Dx4IboxMatch(dr.getSecondPlace(),secondPlace.size());
		
		return null;
	}
	
	public Dx4IboxMatch isThird(String choice)
	{
		if (thirdPlace.contains(choice))
			return new Dx4IboxMatch(dr.getThirdPlace(),thirdPlace.size());
		
		return null;
	}
	
	public Dx4IboxMatch isSpecial(String choice)
	{
		Dx4IboxMatch matched = null;
		int index=0;;
		for (HashSet<String> hs : specials)
		{
			if (hs.contains(choice))
			{
				if (matched == null)
					matched = new Dx4IboxMatch(dr.getSpecials().get(index),hs.size());
				else
					matched.addMatch(dr.getSpecials().get(index),hs.size());
			}
			index++;
		}
		return matched;
	}
	
	public Dx4IboxMatch isConsolation(String choice)
	{
		Dx4IboxMatch matched = null;
		int index=0;;
		for (HashSet<String> hs : consolations)
		{
			if (hs.contains(choice))
			{
				if (matched == null)
					matched = new Dx4IboxMatch(dr.getConsolations().get(index),hs.size());
				else
					matched.addMatch(dr.getConsolations().get(index),hs.size());
			}
			index++;
		}
		return matched;
	}
	
	public static void main(String[] args)
	{
		String num = "1224";
		
		HashSet<String> combs = Dx4GameTypeJson.createCombos(num);
		for (String c : combs)
			log.info(c);
		log.info("Size: " + combs.size());
	}

	public Dx4DrawResultJson getDr() {
		return dr;
	}

	public void setDr(Dx4DrawResultJson dr) {
		this.dr = dr;
	}

}
