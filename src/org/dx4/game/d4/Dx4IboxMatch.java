package org.dx4.game.d4;

import java.util.ArrayList;
import java.util.List;

public class Dx4IboxMatch {

	private List<Integer> sizes;
	private List<String> matches;
	
	Dx4IboxMatch()
	{
		sizes = new ArrayList<Integer>();
		matches  = new ArrayList<String>();
	}

	public Dx4IboxMatch(String match, int size) {
		this();
		addMatch(match,size);
	}

	public void addMatch(String match, int size) {
		matches.add(match);
		sizes.add(size);
	}

	public List<Integer> getSizes() {
		return sizes;
	}

	public void setSizes(List<Integer> sizes) {
		this.sizes = sizes;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	
	
	
}
