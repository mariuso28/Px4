package org.dx4.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class NumberGen {

	
	public static void main(String args[]) throws FileNotFoundException
	{
		
		InputStreamReader ins = new InputStreamReader( new FileInputStream("C:\\_Development\\gexZZ\\4DX\\sql\\numbers.txt"));
		BufferedReader bins = new BufferedReader( ins );
		while (true)
		{
			String str;
			try {
				str = bins.readLine();
				if (str==null)
					break;
				int index = str.indexOf(' ');
				if (index<0)
					System.out.println("PROBLEM ON " + str);
				else
					System.out.println("INSERT INTO NUMBER VALUES (" + str.substring(0,index) + "," 
													+ str.substring(index+1) + ");");
			} catch (IOException e) {
				break;
			}
			
		}
		try {
			bins.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
