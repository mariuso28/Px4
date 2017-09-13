package org.dx4.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberPageElementJson;

public class NumberSearchHelper {

	private static final Logger log = Logger.getLogger(NumberSearchHelper.class);
	
	private Dx4Home dx4Home;
	private String term;
	private Character dictionary;
	private List<Dx4NumberPageElementJson> elements;
	
	public NumberSearchHelper(Dx4Home dx4Home,String term,Character dictionary)
	{
		setDx4Home(dx4Home);
		setTerm(term.trim().toLowerCase());
		setDictionary(dictionary);
		elements = new ArrayList<Dx4NumberPageElementJson>();
		if (searchNumber())
			return;
		if (searchNumberRange(dictionary))
			return;
		searchNumberDesc();
	}

	private void searchNumberDesc() {
		
		// 1 check term in numbersearch table
		List<NumberSearchEntry> numbers = dx4Home.getNumbersFormTerm(term);
		if (numbers.isEmpty())		// try on the connection
		{
			NumberSearchTerm nst = new NumberSearchTerm(dx4Home,term);
			if (nst.getError()!=null)					// error on connection to alternative site
			{
				log.info("could not connect external site - going to internal engine");
				elements = dx4Home.getNumberPageElementsByDesc(term);
				return;
			}
			// got new nst - store and use if any numbers
			if (nst.getNumbers().isEmpty())
			{
				log.info("no numbers from external site - going to internal engine");
				elements = dx4Home.getNumberPageElementsByDesc(term);
				return;
			}
			
			log.info("storing and using search term and number");
			dx4Home.storeNumberSearchTerm(nst);
			for (NumberSearchEntry nse : nst.getNumbers())
			{
				Dx4NumberPageElementJson npe = dx4Home.getNumberPageElement(nse.getNumber(),nse.getDictionary());
				elements.add(npe);
			}
			return;
		}
		
		// use the numbers from the numbersearch tables
		log.info("using numbers from internal search tables");
		for (NumberSearchEntry nse : numbers)
		{
			Dx4NumberPageElementJson npe = dx4Home.getNumberPageElement(nse.getNumber(),nse.getDictionary());
			elements.add(npe);
		}
	}

	private boolean searchNumberRange(Character dictionary) {
		if (!term.contains(":"))
			return false;
		String[] toks = term.split(":");
		if (toks.length!=2)
			return false;
		int num1;
		int num2;
		try
		{
			num1 = Integer.parseInt(toks[0]);
			num2 = Integer.parseInt(toks[1]);
		}
		catch (NumberFormatException e1)
		{
			return false;
		}
		if (num2<num1)
		{
			int tmp = num1;
			num1 = num2;
			num2 = tmp;
		}
		
		int max = 50;
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYMODERN4)
			max = 20;
		
		if (num2-num1>max)
			num2 = num1 + max;
		
		elements = dx4Home.getNumberPageElementsRange(num1,num2,dictionary);
		return true;
	}

	private boolean searchNumber() {
		int num;
		try
		{
			num = Integer.parseInt(term);
			if (num>9999 || num<0)
				return false;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		if (dictionary.charValue()==Dx4NumberPageElementJson.DICTIONARYALL)
		{
			List<Dx4NumberPageElementJson> npjs = dx4Home.getNumberPageElements(term);
			elements.addAll(npjs);
		}
		else
		{
			Dx4NumberPageElementJson np = dx4Home.getNumberPageElement(term,dictionary);	
			elements.add(np);
		}
		return true;
	}

	public Dx4Home getDx4Home() {
		return dx4Home;
	}

	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<Dx4NumberPageElementJson> getElements() {
		return elements;
	}

	public void setElements(List<Dx4NumberPageElementJson> elements) {
		this.elements = elements;
	}

	public Character getDictionary() {
		return dictionary;
	}

	public void setDictionary(Character dictionary) {
		this.dictionary = dictionary;
	}
	
}
