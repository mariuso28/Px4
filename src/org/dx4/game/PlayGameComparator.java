package org.dx4.game;

import java.util.Comparator;

public class PlayGameComparator implements Comparator<Dx4PlayGame>{

	  @Override
	  public int compare(Dx4PlayGame pg1, Dx4PlayGame pg2) {
		    if (pg1.getPlayDate().before(pg2.getPlayDate()))
		    		return -1;
		    if (pg1.getPlayDate().after(pg2.getPlayDate()))
	    		return 1;
		    return 0;
	 }
}
