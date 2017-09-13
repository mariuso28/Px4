package org.dx4.json.numerology;

public class NumerologyCalculator {

	private NumerologyType type;
	
	public NumerologyCalculator(NumerologyType type)
	{
		setType(type);
	}
	
	private int getWordValue(String word)
	{
		int sum = 0;
		for (int i=0; i<word.length(); i++)
		{
			sum += type.getNumberForChar(word.charAt(i));
		}
		int mod = sum % 9;
		if (mod == 0)
			return 9;
		return mod;
	}
	
	public String createNumber(String phrase)
	{
		String[] toks = phrase.toLowerCase().split(" ");
		String number = "";
		for (String tok : toks)
		{
			int val = getWordValue(tok.trim());
			number+=Integer.toString(val);
		}
		if (number.length()>4)
			return number.substring(0,4);
		return number;
	}

	public NumerologyType getType() {
		return type;
	}

	public void setType(NumerologyType type) {
		this.type = type;
	}
}
