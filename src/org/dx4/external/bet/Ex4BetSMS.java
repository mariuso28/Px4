package org.dx4.external.bet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class Ex4BetSMS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 349617263450151233L;

	private static final Logger log = Logger.getLogger(Ex4BetSMS.class);
	
	private String message;
	private String userCode;
	private String providerCodes;
	private List<String> numbers;
	private List<Double> stakes;
	
	// format p11:1543,3556,8668,536,3584,4555,7556,0656-5-5-2-2. Mkt.
	// player code:number,number,number-4DSmall-4DBig-ABCA-ABCC[stakes].MKT[provider codes].
	
	public Ex4BetSMS(String msg) throws Ex4ParseException
	{
		setMessage(msg);
		log.info("Creating Ex4BetSMS from : " + message);
		try
		{
			parse();
		}
		catch (Ex4ParseException e)
		{
			log.error("Createing Ex4BetSMS - " + e.getMessage());
			throw e;
		}
		log.info("Created : " + this.toString());
	}
	
	private void parse() throws Ex4ParseException
	{
		
		int pos = message.indexOf(':');
		if (pos<0)
			throw new Ex4ParseException(message,"player code","':' not found in message");
		setUserCode(message.substring(0,pos));
		message = message.substring(pos+1);
		pos = message.indexOf('-');
		if (pos<0)
			throw new Ex4ParseException(message,"provider codes","'-' not found in message");
		String numStr = message.substring(0, pos);
		StringTokenizer st = new StringTokenizer(numStr,",");
		numbers = new ArrayList<String>();
		while (st.hasMoreTokens())
			numbers.add(st.nextToken());
		int spos = message.indexOf('.',pos);
		if (spos<0)
			throw new Ex4ParseException(message,"stakes","'.' not found in message");
		String stakeStr = message.substring(pos, spos);
		st = new StringTokenizer(stakeStr,"-");
		stakes = new ArrayList<Double>();
		while (st.hasMoreTokens())
		{
			try
			{
				String str = st.nextToken();
				double stake = Double.parseDouble(str);
				stakes.add(stake);
			}
			catch (NumberFormatException e)
			{
				throw new Ex4ParseException(message,"stakes","could not parse : " + stakeStr);
			}
		}
		pos = message.indexOf('.',spos+1);
		if (pos<0)
			throw new Ex4ParseException(message,"providers","final '.' not found in message");
		setProviderCodes(message.substring(spos+1, pos));
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getProviderCodes() {
		return providerCodes;
	}
	public void setProviderCodes(String providerCodes) {
		this.providerCodes = providerCodes;
	}
	public List<String> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}
	public List<Double> getStakes() {
		return stakes;
	}
	public void setStakes(List<Double> stakes) {
		this.stakes = stakes;
	}

	@Override
	public String toString() {
		return "Ex4BetSMS [message=" + message + ", userCode=" + userCode
				+ ", providerCodes=" + providerCodes + ", numbers=" + numbers
				+ ", stakes=" + stakes + "]";
	}

	public static void main(String agrs[])
	{
		try
		{
			new Ex4BetSMS("p11:1543,3556,8668,536,3584,4555,7556,0656-5-5-2-2. Mkt.");
		}
		catch (Exception e)
		{
			log.info(e);
		}
	}
}
