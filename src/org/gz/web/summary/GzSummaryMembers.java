package org.gz.web.summary;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.gz.baseuser.GzBaseUser;
import org.gz.json.GzGameType;

public class GzSummaryMembers {

	private GzGameType[] gameTypes;
	private GzBaseUser superior;
	private Map<UUID,GzSummaryRow> rowMap = new HashMap<UUID,GzSummaryRow>();
	private Map<String,GzSummaryRow> rows = new TreeMap<String,GzSummaryRow>();
	
	public GzSummaryMembers()
	{
		gameTypes=GzGameType.values();	
	}

	public GzSummaryMembers(GzBaseUser superior) {
		this();
		setSuperior(superior);
	}

	public GzBaseUser getSuperior() {
		return superior;
	}

	public void setSuperior(GzBaseUser superior) {
		this.superior = superior;
	}

	public Map<String, GzSummaryRow> getRows() {
		return rows;
	}

	public void setRows(Map<String, GzSummaryRow> rows) {
		this.rows = rows;
	}

	public GzGameType[] getGameTypes() {
		return gameTypes;
	}

	public void setGameTypes(GzGameType[] gameTypes) {
		this.gameTypes = gameTypes;
	}

	public Map<UUID,GzSummaryRow> getRowMap() {
		return rowMap;
	}

	public void setRowMap(Map<UUID,GzSummaryRow> rowMap) {
		this.rowMap = rowMap;
	}

}
