package org.dx4.secure.web.analytics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dx4.json.message.Dx4NumberPageElementJson;

public class NumberViewListForm {
	private List<Dx4NumberPageElementJson> elements;
	private List<String> keywords;
	private String searchTerm;
	private char digits;
	private boolean external;
	
	
	public NumberViewListForm(List<Dx4NumberPageElementJson> elements,String searchTerm,char digits,boolean external)
	{
		setElements(elements);
		setSearchTerm(searchTerm);
		setDigits(digits);
		setExternal(external);
		createKeyWords();
	}
	
	
	private void createKeyWords() {
		Set<String> wordSet = new HashSet<String>();
		for (Dx4NumberPageElementJson element : elements)
		{
			String[] words = element.getDescription().split(" ");
			for (String word : words)
			{
				wordSet.add(word.trim());
			}
		}
		keywords = new ArrayList<String>(wordSet);
	}


	public List<Dx4NumberPageElementJson> getElements() {
		return elements;
	}


	public void setElements(List<Dx4NumberPageElementJson> elements) {
		this.elements = elements;
	}


	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public void setDigits(char digits) {
		this.digits = digits;
	}
	public char getDigits() {
		return digits;
	}


	public void setExternal(boolean external) {
		this.external = external;
	}


	public boolean isExternal() {
		return external;
	}


	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}


	public List<String> getKeywords() {
		return keywords;
	}
	
	
	
}
