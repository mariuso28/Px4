package org.gz.web.number;

import java.io.Serializable;
import java.util.List;

public class GzNumberCommand implements Serializable{

	private static final long serialVersionUID = 2479070204549730399L;
	private List<String> defaults;
	private String newNumber;
	private List<String> newValues;
	private List<List<String>> modifyValues;
	private int changeIndex;
	
	public GzNumberCommand()
	{
	}

	public List<String> getDefaults() {
		return defaults;
	}

	public void setDefaults(List<String> defaults) {
		this.defaults = defaults;
	}

	public String getNewNumber() {
		return newNumber;
	}

	public void setNewNumber(String newNumber) {
		this.newNumber = newNumber;
	}

	public List<String> getNewValues() {
		return newValues;
	}

	public void setNewValues(List<String> newValues) {
		this.newValues = newValues;
	}

	public List<List<String>> getModifyValues() {
		return modifyValues;
	}

	public void setModifyValues(List<List<String>> modifyValues) {
		this.modifyValues = modifyValues;
	}

	public int getChangeIndex() {
		return changeIndex;
	}

	public void setChangeIndex(int changeIndex) {
		this.changeIndex = changeIndex;
	}

	

}
