package org.dx4.secure.web.agent.display;

import java.util.Comparator;

class HotNumberComparator implements Comparator<HotNumber> {
    @Override
    public int compare(HotNumber a, HotNumber b) {
    	return (int) (b.getBet()-a.getBet());
    }
}

