package org.dx4.account;

import java.io.Serializable;
import java.util.Date;

import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4Transaction implements Serializable{
	
	private static final long serialVersionUID = -6881755037812569859L;
	private long id;
	private long userId;
	private long cpId;
	private Double amount;
	private Date date;
	private Dx4TransactionType type;
	private long refId;
	
	public Dx4Transaction(long userId,long cpId,Dx4TransactionType xType,Date date,double amount)
	{
		setUserId(userId);
		setCpId(cpId);
		setDate(date);
		setAmount(amount);
		setType(xType);
		setRefId(-1L);
	}
	
	public Dx4Transaction(Dx4SecureUser user,Dx4SecureUser cp,Dx4TransactionType xType,Date date,double amount)
	{
		this(user.getSeqId(),cp.getSeqId(),xType,date,amount);
	}
	
	
	public Dx4Transaction() {
	}
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCpId() {
		return cpId;
	}

	public void setCpId(long cpId) {
		this.cpId = cpId;
	}

	public long getRefId() {
		return refId;
	}

	public void setRefId(long refId) {
		this.refId = refId;
	}

	public Dx4TransactionType getType() {
		return type;
	}

	public void setType(Dx4TransactionType type) {
		this.type = type;
	}

	
	
}
