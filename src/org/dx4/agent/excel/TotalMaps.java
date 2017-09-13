package org.dx4.agent.excel;

import java.util.Map;
import java.util.TreeMap;

public class TotalMaps {

	private Map<String,TotalMap> map = new TreeMap<String,TotalMap>();
	
	public TotalMaps()
	{
	}

	public Map<String, TotalMap> getMap() {
		return map;
	}

	public void setMap(Map<String, TotalMap> map) {
		this.map = map;
	}
	
	
}
