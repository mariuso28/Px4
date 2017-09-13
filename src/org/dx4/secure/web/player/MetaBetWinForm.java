package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.secure.web.player.display.DisplayMetaBetWin;
import org.dx4.secure.web.player.display.DrawResultWin;

public class MetaBetWinForm 
{
	private static final Logger log = Logger.getLogger(MetaBetWinForm.class);
	private List<DrawResultWin> drw;
	private DisplayMetaBetWin displayMetaBetWin;
	private String returnTarget;
	private String returnHrefCode;
	private static HashMap<String,String> returnTargetMap = new HashMap<String,String>();
	
	public static void registerReturnTarget(String hrefCode,String returnTarget)
	{// register from  different controllers
		String exist = getReturnTarget(hrefCode);
		if (exist!=null)
			if (!exist.equals(returnTarget))
				throw new RuntimeException("MetaBetWinForm:registerReturnTarget - multiple targets for same hrefCode: " + hrefCode);
		
		returnTargetMap.put(hrefCode,returnTarget);
	}
	
	public static String getReturnTarget(String hrefCode)
	{
		return returnTargetMap.get(hrefCode);
	}
	
	public MetaBetWinForm(ExternalGameResults xgr,Dx4Home dx4Home,Dx4MetaBet metaBet,String expanded)
	{
		setDisplayMetaBetWin(new DisplayMetaBetWin(metaBet,dx4Home,expanded));
		drw = new ArrayList<DrawResultWin>();
		for (Dx4DrawResultJson result : xgr.getDraws())
		{
			DrawResultWin win = createDrawResultWin(result,metaBet);
			drw.add(win);
		}
		setReturnTarget("PP","processPlayer.html?cancelDrawResults");		// default - overide for other callers
	}

	private DrawResultWin createDrawResultWin(Dx4DrawResultJson result,Dx4MetaBet metaBet)
	{
		log.trace("SETTING WINS FOR : " + result + " METABET : " + metaBet);
		DrawResultWin drw = new DrawResultWin(result);
		for (Dx4Win winBet : metaBet.getWins())
		{
			if (winBet.getProviderCode() == result.getProvider().getCode())
					drw.addWinBet(winBet,result);
		}
		return drw;
	}

	public void setDrw(List<DrawResultWin> drw) {
		this.drw = drw;
	}

	public List<DrawResultWin> getDrw() {
		return drw;
	}

	public void setDisplayMetaBetWin(DisplayMetaBetWin displayMetaBetWin) {
		this.displayMetaBetWin = displayMetaBetWin;
	}

	public DisplayMetaBetWin getDisplayMetaBetWin() {
		return displayMetaBetWin;
	}

	public void setReturnTarget(String hrefCode,String returnTarget)
	{
		MetaBetWinForm.registerReturnTarget(hrefCode,returnTarget);
		setReturnTarget(returnTarget);
		setReturnHrefCode(hrefCode);
	}
	
	private void setReturnTarget(String returnTarget) {
		this.returnTarget = returnTarget;
	}

	public String getReturnTarget() {
		return returnTarget;
	}

	private void setReturnHrefCode(String returnHrefCode) {
		this.returnHrefCode = returnHrefCode;
	}

	public String getReturnHrefCode() {
		return returnHrefCode;
	}

	@Override
	public String toString() {
		return "MetaBetWinForm [drw=" + drw + ", displayMetaBetWin="
				+ displayMetaBetWin + ", returnTarget=" + returnTarget
				+ ", returnHrefCode=" + returnHrefCode + "]";
	}

	

}
