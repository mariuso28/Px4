package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.List;

public class AlphaForm 
{
	private List<Character> letters;
	private String word;
	private AlphaCommand command;
	
	public AlphaForm()
	{
		letters = new ArrayList<Character>();
		for (char ch = 'A'; ch<='Z'; ch++)
		{
			letters.add(ch);
		}
		word = "";
	}

	public List<Character> getLetters() {
		return letters;
	}

	public void setLetters(List<Character> letters) {
		this.letters = letters;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setCommand(AlphaCommand command) {
		this.command = command;
	}

	public AlphaCommand getCommand() {
		return command;
	}
	
	
}
