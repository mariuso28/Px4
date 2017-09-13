package org.dx4.test;

import org.dx4.json.numerology.NumerologyCalculator;
import org.dx4.json.numerology.NumerologyType;

public class TestNumerology {

	public static void main(String args[])
	{
		NumerologyCalculator nc = new NumerologyCalculator(NumerologyType.INDIAN);
		String number = nc.createNumber("car crash at dawn");
		System.out.println(number);
		
		NumerologyCalculator nc1 = new NumerologyCalculator(NumerologyType.LATIN);
		number = nc1.createNumber("car crash");
		System.out.println(number);
	}
}
