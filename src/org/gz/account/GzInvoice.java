package org.gz.account;

import java.util.Date;

import org.gz.json.GzGameType;

public class GzInvoice extends GzDeposit {
	
	private static final long serialVersionUID = -4558471539417421128L;
	
	public static final char STATUSOPEN = 'O';
	public static final char STATUSCLOSED = 'C';
	public static final char STATUSSETTLED = 'S';
	
	private double flight;
	private double retain;
	private GzGameType gameType;
	private long parentId;
	private Date dueDate;
	private long paymentId;
	private char status;
	
	public GzInvoice()
	{
	}

	public GzInvoice(String payer,String payee,double flight,double retain,Date timestamp,Date dueDate,GzGameType gameType)
	{
		setPayer(payer);
		setPayee(payee);
		setType(GzXaction.XTYPEINVOICE);
		setTimestamp(timestamp);
		setDueDate(dueDate);
		setFlight(flight);
		setRetain(retain);
		setGameType(gameType);
		setPaymentId(-1L);
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
/*
	public void update(double amount2, double commission2, double netAmount2,double stake2,char winstake) {
		amount += amount2;
		commission += commission2;
		netAmount += netAmount2;
	}
*/
	public double getRetain() {
		return retain;
	}

	public void setRetain(double retain) {
		this.retain = retain;
	}

	public double getFlight() {
		return flight;
	}

	public void setFlight(double flight) {
		this.flight = flight;
	}

	public GzGameType getGameType() {
		return gameType;
	}

	public void setGameType(GzGameType gameType) {
		this.gameType = gameType;
	}

	
}
