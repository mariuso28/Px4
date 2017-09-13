package org.dx4.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberStoreJson;
import org.dx4.json.message.Dx4NumberStoreLevelJson;
import org.dx4.secure.web.admin.AdminController;

public class NumberGrid implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4288735528578932449L;

	private static final Logger log = Logger.getLogger(AdminController.class);
	
	private int rowCount;
	private int columnCount;
	private List<String> rowHeader;
	private List<String> columnHeader;
	private int gtype;
	private ArrayList<ArrayList<Dx4NumberStoreJson>> numbers;
	private List<Dx4NumberStoreJson> sortedNumbers;
	// can be generic for users
	private static HashMap<Integer,NumberGrid> grids = new HashMap<Integer,NumberGrid>();
	private Date resultStartDate;
	private Date resultEndDate;
	private List<Dx4NumberPageElementJson> pageElements;

	private NumberGrid(int columnCount,int rowCount,int gtype,List<Dx4NumberPageElementJson> pageElements)
	{
		setColumnCount(columnCount);
		setRowCount(rowCount);
		setGtype(gtype);
		setPageElements(pageElements);
		createGrid();
	}
	
	public NumberGrid(int gtype, List<Dx4NumberPageElementJson> pageElements)
	{
		this(10,10,gtype,pageElements);
	}
	
	public static NumberGrid create(int lookUpValue, List<Dx4NumberPageElementJson> pageElements) {
		if (lookUpValue !=1 && lookUpValue !=2 && lookUpValue!=4)
		{
			log.error("Cannot create NumberGrid with lookUpValue : " + lookUpValue + " must be 1,2,4");
			return null;
		}
		NumberGrid numberGrid = grids.get(lookUpValue);
		if (numberGrid == null)
		{
			numberGrid = new NumberGrid(lookUpValue,pageElements);
			grids.put(lookUpValue,numberGrid);
		}
		
		return numberGrid;
	}

	
	public ArrayList<ArrayList<String>> createGridDisplay()
	{
		ArrayList<ArrayList<String>> display = new ArrayList<ArrayList<String>>();
		display.add(new ArrayList<String>());
		display.get(0).add("  ");
		for (String hd : columnHeader)
			display.get(0).add("  " + hd);
		for (int index = 0; index<rowHeader.size(); index++)
		{
			ArrayList<String> dRow = new ArrayList<String>();
			dRow.add(rowHeader.get(index));
			ArrayList<Dx4NumberStoreJson> row = numbers.get(index);
			for (Dx4NumberStoreJson ns : row)
				dRow.add(ns.getNumber());
			display.add(dRow);
		}
		return display;
	}
	
	public Dx4NumberStoreJson getNumberStore(String number)
	{
		try
		{
			int index = Integer.parseInt(number);
			int row = index / rowHeader.size();
			int col = index % rowHeader.size();
			return numbers.get(row).get(col);
		}
		catch (Exception e)
		{
			log.error("NumberGrid::getNumberStore - illegal number : " + number + " - " + e);
			return null;
		}
	}
	
	public void createGrid()
	{
		numbers = new ArrayList<ArrayList<Dx4NumberStoreJson>>();
		rowHeader = generateRowHeader();
		columnHeader = generateColumnHeader();
		for (String col : columnHeader)
		{
			List<Dx4NumberStoreJson> nRow = new ArrayList<Dx4NumberStoreJson>();
			for (String row : rowHeader)
			{
				String number = col + row;
				Dx4NumberStoreJson ns = new Dx4NumberStoreJson(number);
				nRow.add(ns);
				String token="";
				if (gtype!=4)
					token = number;
				else
					token = number.substring(1);
			
				try
				{
					Integer index = Integer.parseInt(token);
					Dx4NumberPageElementJson element = pageElements.get(index);
					element.getNumbers().add(ns);
				}
				catch (NumberFormatException e)
				{
					log.error("Invalid token : " + token);
				}
			}
			numbers.add((ArrayList<Dx4NumberStoreJson>) nRow);
		}
	}
	
	public List<List<String>> generateGrid()
	{
		return null;
	}
	
	private List<String> generateRowHeader()
	{
		List<String> rowHeader = new ArrayList<String>();
		
		for (int c = 0; c<rowCount; c++)
		{
			String entry = Integer.toString(c);
			if (gtype==4)
			{
				for (int d = 0; d<columnCount; d++)
				{
					entry = Integer.toString(c)+Integer.toString(d);
					rowHeader.add(entry);
				}
			}
			else
			if (gtype==2 || gtype==1)
			{
				entry = Integer.toString(c);
				rowHeader.add(entry);
			}
		}
		return rowHeader;
	}
	
	private List<String> generateColumnHeader()
	{
		List<String> colHeader = new ArrayList<String>();
		
		for (int c = 0; c<columnCount; c++)
		{
			if (gtype!=1)
			{
				for (int d = 0; d<rowCount; d++)
				{
					String entry = Integer.toString(c)+Integer.toString(d);
					colHeader.add(entry);
				}
			}
			else 
			{
				String entry = Integer.toString(c);
				colHeader.add(entry);
			}
		}
		return colHeader;
	}

	
	public List<String> getRowHeader() {
		return rowHeader;
	}

	public void setRowHeader(List<String> rowHeader) {
		this.rowHeader = rowHeader;
	}

	public List<String> getColumnHeader() {
		return columnHeader;
	}

	public void setColumnHeader(List<String> columnHeader) {
		this.columnHeader = columnHeader;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public ArrayList<ArrayList<Dx4NumberStoreJson>> getNumbers() {
		return numbers;
	}

	public void setNumbers(ArrayList<ArrayList<Dx4NumberStoreJson>> numbers) {
		this.numbers = numbers;
	}

	public void setGtype(int gtype) {
		this.gtype = gtype;
	}

	public int getGtype() {
		return gtype;
	}

	public static HashMap<Integer, NumberGrid> getGrids() {
		return grids;
	}

	public static void setGrids(HashMap<Integer, NumberGrid> grids) {
		NumberGrid.grids = grids;
	}

	public Date getResultStartDate() {
		return resultStartDate;
	}

	public void setResultStartDate(Date resultStartDate) {
		this.resultStartDate = resultStartDate;
	}

	public Date getResultEndDate() {
		return resultEndDate;
	}

	public void setResultEndDate(Date resultEndDate) {
		this.resultEndDate = resultEndDate;
	}

	public void clearLevelsOccurences() {
		for (int col=0; col<columnHeader.size(); col++)
		{
			ArrayList<Dx4NumberStoreJson> nRow = numbers.get(col);
			for (Dx4NumberStoreJson ns : nRow)
			{
				ns.setLevel(Dx4NumberStoreLevelJson.NONE);
				ns.setOccurences(0);
			}
		}
	}
	
	public void setSortedNumbers(List<Dx4NumberStoreJson> sortedNumbers) {
		Collections.sort(sortedNumbers,new NumberStoreComparator());
		this.sortedNumbers = sortedNumbers;
	}

	public List<Dx4NumberStoreJson> getSortedNumbers() {
		return sortedNumbers;
	}

	public void setPageElements(List<Dx4NumberPageElementJson> pageElements) {
		this.pageElements = pageElements;
	}

	public List<Dx4NumberPageElementJson> getPageElements() {
		return pageElements;
	}

	public List<Dx4NumberPageElementJson> getPageElements(int pageNum) {
		int startIndex = pageNum * 100;
		int endIndex = startIndex+100;
		return pageElements.subList(startIndex, endIndex);
	}

	
}
