package org.dx4.secure.web.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class NumberBook {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(NumberBook.class);
	
	private List<NumberPage> pages;
	private int pageWidth;
	private int pageHeight;
	
	public NumberBook()
	{
		pages = new ArrayList<NumberPage>();
		setPageWidth(3);
		setPageHeight(3);
		int pw = getPageWidth();
		int ph = getPageHeight();
		NumberPage page = new NumberPage();
		List<String> row = new ArrayList<String>();
		for (int n=0; n<100; n++)
		{
			String vstr = makeValue(n);
			row.add(vstr);
			pw--;
			if (pw==0)
			{
				page.addRow(row);
				row = new ArrayList<String>();
				pw = getPageWidth();
				ph--;
				if (ph==0)
				{
					pages.add(page);
					ph = getPageHeight();
					page = new NumberPage();
				}
			}
		}
	}

	private String makeValue(int value) {
		
		String vstr = Integer.toString(value);
		while (vstr.length()<4)
			vstr = "0" + vstr;
		return vstr;
	}
	
	public List<NumberPage> getPages() {
		return pages;
	}

	public void setPages(List<NumberPage> pages) {
		this.pages = pages;
	}

	public int getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	public void setPageHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}

	public int getPageHeight() {
		return pageHeight;
	}

	public String toString()
	{
		String str="";
		int cnt = 0;
		for (NumberPage page : pages)
		{
			str += "PAGE : " + (cnt++) + "\n";
			str += page.toString() + "\n";
		}
		return str;
	}
	
	public static void main(String[] args)
	{
		NumberBook book = new NumberBook();
		System.out.print(book);
	}

	
}
