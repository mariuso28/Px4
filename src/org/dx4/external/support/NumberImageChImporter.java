package org.dx4.external.support;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
 
public class NumberImageChImporter 
{
	private static void importUnits()
	{
		try
		{
			InputStream fis = new FileInputStream("C:\\_Development\\gexZZ\\4DX\\sample\\pictorials_ch-en3.txt");
			Reader rd = new InputStreamReader(fis, "UTF-16");
			BufferedReader in = new BufferedReader(rd);
			String sCurrentLine;
	
			while ((sCurrentLine = in.readLine()) != null) {
			{
				System.out.println(sCurrentLine);
				NumberImageCh numberImageCh = new NumberImageCh(sCurrentLine);
				System.out.println(numberImageCh);
			}
			in.close();
		}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	public static void main(String args[])
	{
		importUnits();
	}

}
