package org.dx4.secure.web.account;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;

public class TransactionForm 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TransactionForm.class);
	private String contact;
	private String code;
	private Dx4Role role;
	private List<TxDisplay> txList;
	private int currentPage;
	private int lastPage;
	
	public TransactionForm(String contact,Dx4Role role,String code)
	{
		setContact(contact);
		setRole(role);
		setCode(code);
	}
	
	public void createTxList(Dx4SecureUser user,Dx4Home dx4Home,List<Dx4Transaction> transList,int currentPage,int lastPage)
	{
		HashMap<Long,String> cpEmails = new HashMap<Long,String>();
		
		txList = new ArrayList<TxDisplay>();
		for (Dx4Transaction trans : transList)
		{
			String cpEmail = cpEmails.get(trans.getCpId());
			if (cpEmail == null)
			{
				try {
					cpEmail = dx4Home.getBaseUserEmailBySeqId(trans.getCpId());
				} catch (Dx4PersistenceException e) {
					e.printStackTrace();
					throw new Dx4ExceptionFatal(e.getMessage());
				}
				cpEmails.put(trans.getCpId(),cpEmail);
			}
			TxDisplay txd = new TxDisplay(trans,user.getEmail(),cpEmail);
			txList.add(txd);
		}
		setCurrentPage(currentPage);
		setLastPage(lastPage);
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Dx4Role getRole() {
		return role;
	}
	public void setRole(Dx4Role role) {
		this.role = role;
	}

	public void setTxList(List<TxDisplay> txList) {
		this.txList = txList;
	}

	public List<TxDisplay> getTxList() {
		return txList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}	

}
