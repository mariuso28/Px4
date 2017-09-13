package org.dx4.secure.web.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4MetaBetJson;

public class BetJsonGroups {

	private static final Logger log = Logger.getLogger(BetJsonGroups.class);
	
	private List<Dx4BetJson> d4 = new ArrayList<Dx4BetJson>();
	private List<Dx4BetJson> d3 = new ArrayList<Dx4BetJson>();
	private List<Dx4BetJson> d2 = new ArrayList<Dx4BetJson>();
	
	public BetJsonGroups(List<Dx4BetJson> allBetsJson)
	{
		for (Dx4BetJson bj : allBetsJson)
		{
			switch (bj.getChoice().length())
			{
				case 4: d4.add(bj); break;
				case 3: d3.add(bj); break;
				case 2 : d2.add(bj); break;
				default : log.error("Unsupported number of didits : " + bj.getChoice().length() + " bet ignored.");
			}
		}
		d4 = Dx4MetaBetJson.consolidateBets(d4);
		d3 = Dx4MetaBetJson.consolidateBets(d3);
		d2 = Dx4MetaBetJson.consolidateBets(d2);
	}
	
	public List<Dx4BetJson> getD4() {
		return d4;
	}
	public void setD4(List<Dx4BetJson> d4) {
		this.d4 = d4;
	}
	public List<Dx4BetJson> getD3() {
		return d3;
	}
	public void setD3(List<Dx4BetJson> d3) {
		this.d3 = d3;
	}
	public List<Dx4BetJson> getD2() {
		return d2;
	}
	public void setD2(List<Dx4BetJson> d2) {
		this.d2 = d2;
	}
	
	
}
