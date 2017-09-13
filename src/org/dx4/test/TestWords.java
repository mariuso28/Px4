package org.dx4.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class TestWords {	
	private static final Logger log = Logger.getLogger(TestWords.class);
	private CharHolder root;
	private String word;
	private Set<String> words;
	
	public TestWords(String word)
	{
		setWord(word);
		root = this.new CharHolder();
		root.addSubChars(word.toCharArray());
		setWords(new TreeSet<String>());
		setWordsForNode(root);
		for (String wordEntry : root.getWordList())
			words.add(wordEntry);
			
	}
	
	class CharHolder{
		private Character ch;
		private List<CharHolder> subChars;
		private String word;
		private List<String> wordList;
		
		CharHolder()
		{
			setCh(' ');
			subChars = new ArrayList<CharHolder>();
			wordList = new ArrayList<String>();
		}
		
		CharHolder(Character ch)
		{
			this();
			setCh(ch);
		}
		
		public void addSubChars(char[] word)
		{
			int pos = 0;
			for (char ch : word)
			{
				CharHolder chd = new CharHolder(ch);
				subChars.add(chd);
				if (word.length==1)
					return;
				char[] newWord = new char[word.length-1];
				int p=0;
				for (int x=0; x<word.length; x++)
				{
					if (x!=pos)
						newWord[p++] = word[x];
				}
				chd.addSubChars(newWord);
				pos++;
			}
		}
		
		public Character getCh() {
			return ch;
		}
		public void setCh(Character ch) {
			this.ch = ch;
		}
		public List<CharHolder> getSubChars() {
			return subChars;
		}
		public void setSubChars(List<CharHolder> subChars) {
			this.subChars = subChars;
		}
		
		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public List<String> getWordList() {
			return wordList;
		}

		public void setWordList(List<String> wordList) {
			this.wordList = wordList;
		}

		@Override
		public String toString() {
			return "CharHolder [ch=" + ch + ", subChars=" + subChars + "]";
		}
		
	}
	
	private void setWordsForNode(CharHolder root)
	{
		if (root.getSubChars().isEmpty())
		{
			root.getWordList().add(root.getCh().toString());
		}
		else
		for (CharHolder ch : root.getSubChars())
		{
			setWordsForNode(ch);
			for (String word : ch.getWordList())	
			{
				root.getWordList().add(root.getCh().toString()+word);
			}
		}
	}
	
	private void printCombos(CharHolder root)
	{
		
		for (String word : root.getWordList())
			if (word.contains("ci") || word.contains("ti"))
				log.info(word);
		/*
		{
		Iterator<String> iter = words.iterator();
		while (iter.hasNext())
			log.info(iter.next());
		}
		*/
	}
	

	public void setRoot(CharHolder root) {
		this.root = root;
	}

	public CharHolder getRoot() {
		return root;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setWords(Set<String> words) {
		this.words = words;
	}

	public Set<String> getWords() {
		return words;
	}
	
	public static void main(String[] args) {
		log.info("HELLO");
		
		DecimalFormat df = new DecimalFormat("#0.00");
		log.info( df.format(3.4) + "  " + df.format(0.4));
		
	
		TestWords ts = new TestWords("pscliae");
		log.info("My Word is " + ts.getWord());
		log.info("Combos are:");
		ts.printCombos(ts.getRoot());
	
	}

}
