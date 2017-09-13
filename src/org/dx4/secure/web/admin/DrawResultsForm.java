package org.dx4.secure.web.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.secure.web.Dx4FormValidationException;

public class DrawResultsForm
{
	private Dx4DrawResultJson result;
	private Dx4DrawResultJson init;
	private String message;
	
	public DrawResultsForm()
	{
	}
	
	public void initialiseValues()
	{
		Set<String> nums = new HashSet<String>();
		while (nums.size()<23)
		{
			Random rand = new Random();
			Integer num = rand.nextInt(10000);
			String str = num.toString();
			while (str.length()<4)
				str = "0" + str;
			nums.add(str);
		}
		Iterator<String> iter = nums.iterator();
		init = new Dx4DrawResultJson();
		init.setFirstPlace(iter.next());
		init.setSecondPlace(iter.next());
		init.setThirdPlace(iter.next());
		List<String> numList = new ArrayList<String>();
		for (int i=0; i<10; i++)
			numList.add(iter.next());
		init.setSpecials(numList);
		numList = new ArrayList<String>();
		for (int i=0; i<10; i++)
			numList.add(iter.next());
		init.setConsolations(numList);
	}

	public void validate() throws Dx4FormValidationException
	{
		Set<String> nums = new HashSet<String>();
		addNum(nums,result.getFirstPlace());
		addNum(nums,result.getSecondPlace());
		addNum(nums,result.getThirdPlace());
		for (String number : result.getSpecials())
			addNum(nums,number);
		for (String number : result.getConsolations())
			addNum(nums,number);
	}
	
	private void addNum(Set<String> nums,String number) throws Dx4FormValidationException
	{
		if (number.length()!=4)
			throw new Dx4FormValidationException("Invalid 4D number : " + number );
		try
		{
			Integer.parseInt(number);
		}
		catch (NumberFormatException e)
		{
			throw new Dx4FormValidationException("Invalid 4D number : " + number );
		}
		if (!nums.add(number))
			throw new Dx4FormValidationException("Number clash for : " + number + " draw result numbers must be unique");
	}
	
	public void setResult(Dx4DrawResultJson result) {
		this.result = result;
	}

	public Dx4DrawResultJson getResult() {
		return result;
	}

	public void setInit(Dx4DrawResultJson init) {
		this.init = init;
	}

	public Dx4DrawResultJson getInit() {
		return init;
	}

	@Override
	public String toString() {
		return "DrawResultsForm [result=" + result + ", init=" + init + "]";
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
