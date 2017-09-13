package org.dx4.services;

import java.util.UUID;

public class Dx4UserPasswordService {

	public static String autoGeneratePassword()
	{
		String pw = new String();
		while (pw.length()<8)
		{
			UUID uuid = UUID.randomUUID();
			String myRandom = uuid.toString();
			for (int i=0; i<myRandom.length(); i++)
			{
				char ch = myRandom.charAt(i);
				if (Character.isJavaIdentifierPart(ch) && ch!='4')
					pw += ch;
				if (pw.length()==8)
					break;
			}
		}			
		return pw;
	}
	
}
