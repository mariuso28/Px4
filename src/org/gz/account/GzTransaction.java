package org.gz.account;

import java.util.Date;

import org.gz.json.GzGameType;

public class GzTransaction extends GzBaseTransaction {
	
	private static final long serialVersionUID = 659183036584351555L;
	private String source;
	private GzGameType gameType;
	private String number;
	private long invoiceId;
	public final static char BET = 'B';
	public final static char WIN = 'W';
	
	public GzTransaction()
	{
	}
	
	public GzTransaction(String payer,String payee,Character type,double amount,Date timestamp,GzGameType gameType, String source,String number)
	{
		super(payer,payee,type,amount,timestamp);
		setType(type);
		setSource(source);
		setGameType(gameType);
		setNumber(number);
	}
	
	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public GzGameType getGameType() {
		return gameType;
	}

	public void setGameType(GzGameType gameType) {
		this.gameType = gameType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
}
