package org.dx4.account;

import org.apache.log4j.Logger;

public enum Dx4TransactionType {
	
	Deposit,Withdrawl,Pay,Collect;

	private static final Logger log = Logger.getLogger(Dx4TransactionType.class);
	
	public static Dx4TransactionType getFromInitial(char transType) {
	
		switch (transType)
		{
			case 'D' : return Dx4TransactionType.Deposit; 
			case 'W' : return Dx4TransactionType.Withdrawl;
			case 'P' : return Dx4TransactionType.Pay;
			case 'C' : return Dx4TransactionType.Collect;
		}
		log.error("UNKNOWN TRANSACTION TYPE : " + transType);
		return null;
	}

}
