package org.gz.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gz.home.GzHome;
import org.gz.services.GzServicesException;

public class GzNumberRetainerSet {

	private int digits;
	private List<GzNumberRetainer> defaults = new ArrayList<GzNumberRetainer>();
	private Map<String,List<GzNumberRetainer>> individuals = new TreeMap<String,List<GzNumberRetainer>>();
	
	public GzNumberRetainerSet()
	{
	}
	
	public void updateDefaults(List<String> numbers,GzHome gzHome) throws NumberFormatException
	{
		int index = 0;
		for (String num : numbers)
		{
			double val = Double.parseDouble(num);
			if (val<0.0)
				throw new NumberFormatException("Value cannot be negative");
			defaults.get(index++).setRetain(val);
		}
		for (GzNumberRetainer nr : defaults)
			gzHome.updateGzNumberRetainer(nr);
	}
	
	public void createNewNumber(String newNumber, List<String> newValues, GzHome gzHome) throws GzServicesException,NumberFormatException {
		
		for (int i=0; i<newNumber.length(); i++)
		{
			if (!Character.isDigit(newNumber.charAt(i)))
				throw new GzServicesException("New number contains a non-digit value");
		}
		
		if (newNumber.length()!=defaults.get(0).getGameType().getDigits())
			throw new GzServicesException("New number must have " + defaults.get(0).getGameType().getDigits() + " digits");
		
		if (individuals.containsKey(newNumber))
			throw new GzServicesException("New number already exists - please modify");
		
		int index = 0;
		for (String num : newValues)
		{
			double val = Double.parseDouble(num);
			if (val<0.0)
				throw new NumberFormatException("Value cannot be negative");
			GzNumberRetainer nr = new GzNumberRetainer(defaults.get(index).getMemberId(),defaults.get(index).getGameType(),newNumber,false,val);
			gzHome.storeGzNumberRetainer(nr);
			index++;
		}
	
	}
	
	public void modifyNumber(int changeIndex, List<List<String>> modifyValues, GzHome gzHome) throws NumberFormatException {
		List<GzNumberRetainer> nrs = null;
		int index = 0;
		for (List<GzNumberRetainer> find : individuals.values())
		{
			if (index == changeIndex)
			{
				nrs = find;
				break;
			}
			index++;
		}
		
		List<String> newValues = modifyValues.get(index);
		index = 0;
		for (String num : newValues)
		{
			double val = Double.parseDouble(num);
			if (val<0.0)
				throw new NumberFormatException("Value cannot be negative");
			GzNumberRetainer nr = nrs.get(index++);
			nr.setRetain(val);
			gzHome.updateGzNumberRetainer(nr);
		}
	}

	public void storeIndividual(GzNumberRetainer nr)
	{
		List<GzNumberRetainer> individual = individuals.get(nr.getNumber());
		if (individual == null)
		{
			individual = new ArrayList<GzNumberRetainer>();
			individuals.put(nr.getNumber(), individual);
		}
		individual.add(nr);
		Collections.sort(individual,new GzNumberRetainerComparator());
	}

	public void storeDefaults(List<GzNumberRetainer> defaults)
	{
		setDefaults(defaults);
		Collections.sort(defaults,new GzNumberRetainerComparator());
	}
		
	public List<GzNumberRetainer> getDefaults() {
		return defaults;
	}

	public void setDefaults(List<GzNumberRetainer> defaults) {
		this.defaults = defaults;
	}

	public Map<String, List<GzNumberRetainer>> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Map<String, List<GzNumberRetainer>> individuals) {
		this.individuals = individuals;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	@Override
	public String toString() {
		return "GzNumberRetainerSet [defaults=" + defaults + ", individuals=" + individuals + "]";
	}

	

	
}
