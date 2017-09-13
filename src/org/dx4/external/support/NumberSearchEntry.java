package org.dx4.external.support;

public class NumberSearchEntry{

	private String number;
	private Character dictionary;
	
	public NumberSearchEntry()
	{
	}
	
	public NumberSearchEntry(String number,Character dictionary)
	{
		setNumber(number);
		setDictionary(dictionary);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Character getDictionary() {
		return dictionary;
	}

	public void setDictionary(Character dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String toString() {
		return "NumberSearchEntry [number=" + number + ", dictionary=" + dictionary + "]";
	}
	
}
