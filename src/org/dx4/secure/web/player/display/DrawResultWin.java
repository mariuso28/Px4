package org.dx4.secure.web.player.display;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4NumberWin;
import org.dx4.bet.Dx4Win;
import org.dx4.bet.Dx4WinNumberSummary;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class DrawResultWin
{
	private static final Logger log = Logger.getLogger(DrawResultWin.class);
	
	public static String FIRSTCOLOR = "color:00B00F";
	public static String SECONDCOLOR = "color:FFA600";
	public static String THIRDCOLOR = "color:BA006D";
	public static String SPECIALCOLOR = "color:4055C9";
	public static String CONSOLATIONCOLOR = "color:74757A";
	
	private DivFlag fpw;
	private DivFlag spw;
	private DivFlag tpw;
	private List<DivFlag> sws;
	private List<DivFlag> cws;
	
	
	public DrawResultWin(Dx4DrawResultJson drawResult)
	{
		sws = new ArrayList<DivFlag>(); 
		cws = new ArrayList<DivFlag>(); 	
		fpw = new DivFlag();
		spw = new DivFlag();
		tpw = new DivFlag();
		for (@SuppressWarnings("unused") String special : drawResult.getSpecials())
			sws.add(new DivFlag());
		for (@SuppressWarnings("unused") String consolation : drawResult.getConsolations())
			cws.add(new DivFlag());
	}

	public void addNumberWin(Dx4NumberWin numberWin, Dx4DrawResultJson result,Dx4Home dx4Home) {
		List<Dx4WinNumberSummary> summary = dx4Home.getWinNumberSummary(numberWin.getNumber(), 
									numberWin.getProviderCode(), result.getDate());
		
		String message = "";
		if (summary.isEmpty())
			log.error("Unexpected 0 summaries for : " + numberWin );
		else
			message = buildMessageForNumberSummaries(summary,numberWin);
			
		if (result.getFirstPlace().equals(numberWin.getNumber()))
		{
			fpw.setColor(DrawResultWin.FIRSTCOLOR);
			fpw.setFlag("winner");
			fpw.setStr(message);
			return;
		}	
		if (result.getSecondPlace().equals(numberWin.getNumber()))
		{
			spw.setColor(DrawResultWin.SECONDCOLOR);
			spw.setFlag("winner");
			spw.setStr(message);
			return;
		}	
		if (result.getThirdPlace().equals(numberWin.getNumber()))
		{
			tpw.setColor(DrawResultWin.THIRDCOLOR);
			tpw.setFlag("winner");
			tpw.setStr(message);
			return;
		}	
		int index=0;
		for (String res : result.getSpecials())
		{
			if (res.equals(numberWin.getNumber()))
			{
				DivFlag dfl = sws.get(index);
				dfl.setColor(DrawResultWin.SPECIALCOLOR);
				dfl.setFlag("winner");
				dfl.setStr(message);	
				return;
			}
			index++;
		}
		index=0;
		for (String res : result.getConsolations())
		{
			if (res.equals(numberWin.getNumber()))
			{
				DivFlag dfl = cws.get(index);
				dfl.setColor(DrawResultWin.CONSOLATIONCOLOR);
				dfl.setFlag("winner");
				dfl.setStr(message);	
				return;
			}
			index++;
		}
	}
	
	private String buildMessageForNumberSummaries(List<Dx4WinNumberSummary> summaries,Dx4NumberWin numberWin) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ps.printf("%-14s%12.2f", "Total Win", numberWin.getWin() );
		ps.printf("<br>");
		ps.printf("%-14s%-14s%-14s", "Bet", "Stake", "Win" );
		
		for (Dx4WinNumberSummary summary : summaries)
		{
			ps.printf("<br>");
			ps.printf("%-14s%12.2f%12.2f",summary.getGtype(),summary.getStake(),summary.getWin()); 
		}
		return baos.toString();
	}

	public void addWinBet(Dx4Win winBet,Dx4DrawResultJson drawResult) {
		String winStr = makeWinString(winBet);
		if (winBet.getPlace().equals(Dx4PayOutTypeJson.First))
		{
			fpw.setColor(DrawResultWin.FIRSTCOLOR);
			fpw.setFlag("winner");
			fpw.setStr(fpw.getStr()+winStr);
			return;
		}
		if (winBet.getPlace().equals(Dx4PayOutTypeJson.Second))
		{
			spw.setColor(DrawResultWin.SECONDCOLOR);
			spw.setFlag("winner");
			spw.setStr(spw.getStr()+winStr);	
			return;
		}
		if (winBet.getPlace().equals(Dx4PayOutTypeJson.Third))
		{
			tpw.setColor(DrawResultWin.THIRDCOLOR);
			tpw.setFlag("winner");
			tpw.setStr(tpw.getStr()+winStr);	
			return;
		}
		if (winBet.getPlace().equals(Dx4PayOutTypeJson.Spec))
		{
			int index=0;
			for (String result : drawResult.getSpecials())
			{
				if (result.equals(winBet.getResult()))
				{
					DivFlag df = sws.get(index);
					df.setColor(DrawResultWin.SPECIALCOLOR);
					df.setFlag("winner");
					df.setStr(df.getStr()+winStr);	
				}
				index++;
			}
		}
		if (winBet.getPlace().equals(Dx4PayOutTypeJson.Cons))
		{
			int index=0;
			for (String result : drawResult.getConsolations())
			{
				if (result.equals(winBet.getResult()))
				{
					DivFlag df = cws.get(index);
					df.setColor(DrawResultWin.CONSOLATIONCOLOR);
					df.setFlag("winner");
					df.setStr(df.getStr()+winStr);	
				}
				index++;
			}
		}
	}
	
	private String makeWinString(Dx4Win winBet)
	{
		return "Bet :" + winBet.getBet().getGame().getGtype().name() +
				" Stake :" + winBet.getBet().getStake() + " Choice :" + winBet.getBet().getChoice() + 
 			" Win :" + winBet.getWin() + "<br>";
	}

	public DivFlag getFpw() {
		return fpw;
	}

	public void setFpw(DivFlag fpw) {
		this.fpw = fpw;
	}

	public DivFlag getSpw() {
		return spw;
	}

	public void setSpw(DivFlag spw) {
		this.spw = spw;
	}

	public DivFlag getTpw() {
		return tpw;
	}

	public void setTpw(DivFlag tpw) {
		this.tpw = tpw;
	}

	public List<DivFlag> getSws() {
		return sws;
	}

	public void setSws(List<DivFlag> sws) {
		this.sws = sws;
	}

	public List<DivFlag> getCws() {
		return cws;
	}

	public void setCws(List<DivFlag> cws) {
		this.cws = cws;
	}

	@Override
	public String toString() {
		return "DrawResultWin [fpw=" + fpw + ", spw=" + spw + ", tpw=" + tpw
				+ ", sws=" + sws + ", cws=" + cws + "]";
	}

}
