package org.dx4.utils;

import java.util.ArrayList;
import java.util.List;

import org.dx4.json.message.Dx4NumberStoreJson;

public class NumberRow {
	public List<Dx4NumberStoreJson> columns;

	public NumberRow()
	{
		columns = new ArrayList<Dx4NumberStoreJson>();
	}

	public List<Dx4NumberStoreJson> getColumns() {
		return columns;
	}

	public void setColumns(List<Dx4NumberStoreJson> columns) {
		this.columns = columns;
	}

	
	
	
	
}
