package org.dx4.utils;

import java.util.Comparator;

import org.dx4.json.message.Dx4NumberStoreJson;

public class NumberStoreComparator implements Comparator<Dx4NumberStoreJson>{

	  @Override
	  public int compare(Dx4NumberStoreJson ns1, Dx4NumberStoreJson ns2) {
		    return ns2.getOccurences()-ns1.getOccurences();
	 }
}
