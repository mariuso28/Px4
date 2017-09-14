package org.gz.web.summary;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.dx4.json.message.Dx4GameTypeJson;
import org.gz.baseuser.GzBaseUser;

public class GzSummaryMembers {

	private Dx4GameTypeJson[] gameTypes;
	private GzBaseUser superior;
	private Map<UUID,GzSummaryRow> rowMap = new HashMap<UUID,GzSummaryRow>();
	private Map<String,GzSummaryRow> rows = new TreeMap<String,GzSummaryRow>();
	
	public GzSummaryMembers()
	{
		gameTypes=Dx4GameTypeJson.values();	
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

	public Dx4GameTypeJson[] getGameTypes() {
		return gameTypes;
	}

	public void setGameTypes(Dx4GameTypeJson[] gameTypes) {
		this.gameTypes = gameTypes;
	}

	public Map<UUID,GzSummaryRow> getRowMap() {
		return rowMap;
	}

	public void setRowMap(Map<UUID,GzSummaryRow> rowMap) {
		this.rowMap = rowMap;
	}

}
