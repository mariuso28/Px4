package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.List;

public class NumberPage {
	private List<List<String>> rows;

	public NumberPage()
	{
		setRows(new ArrayList<List<String>>());
	}
	
	public void addRow(List<String> row)
	{
		rows.add(row);
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

	public List<List<String>> getRows() {
		return rows;
	}
	
	public String toString()
	{
		String str = "";
		for (List<String> row : rows)
		{
			for (String number : row)
			{
				str += number + " ";
			}
			str += "\n";
		}
		return str;
	}
	
}
