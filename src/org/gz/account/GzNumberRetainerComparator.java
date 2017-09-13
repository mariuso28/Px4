package org.gz.account;

import java.util.Comparator;

public class GzNumberRetainerComparator implements Comparator<GzNumberRetainer> {

	@Override
	public int compare(GzNumberRetainer o1, GzNumberRetainer o2) {
		return o1.getGameType().getIndex() - o2.getGameType().getIndex();
	}

}
