package org.dx4.secure.web.agent;

import java.text.DecimalFormat;

import org.dx4.bet.Dx4NumberWin;
import org.dx4.home.Dx4Home;

public class WinNumberWrapper 
{
	public static String SEPERATOR = " - ";
	private String placingCode;
	
	public WinNumberWrapper(String user, Dx4NumberWin numberWin,
			Dx4Home dx4Home) {
		 createPlacingCode( user, numberWin, dx4Home);
	}
	
	private void createPlacingCode(String user, Dx4NumberWin numberWin, Dx4Home dx4Home)
	{
		placingCode = "";
		if (user != null)
			placingCode += user + " - ";
		placingCode += numberWin.getNumber() + WinNumberWrapper.SEPERATOR;
		DecimalFormat df = new DecimalFormat("#.00");
		placingCode += df.format(numberWin.getWin());
		if (numberWin.getNumber().equals("Total"))
			return;
		placingCode += WinNumberWrapper.SEPERATOR + numberWin.getPlace();
		placingCode += WinNumberWrapper.SEPERATOR + dx4Home.getProviderByCode(numberWin.getProviderCode()).getName();
	}
	
	public void setPlacingCode(String placingCode) {
		this.placingCode = placingCode;
	}

	public String getPlacingCode() {
		return placingCode;
	}

	@Override
	public String toString() {
		return "WinNumberWrapper [placingCode=" + placingCode + "]";
	}
	

}
