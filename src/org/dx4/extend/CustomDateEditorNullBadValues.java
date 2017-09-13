package org.dx4.extend;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;

public class CustomDateEditorNullBadValues extends CustomDateEditor{

	private static final Logger log = Logger.getLogger(CustomDateEditorNullBadValues.class);
	
	public CustomDateEditorNullBadValues(DateFormat dateFormat,
			boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
	{
			log.info("CustomDateEditorNullBadValues : " + text);
			try
			{
				super.setAsText(text); 
			}
			catch (IllegalArgumentException e)
			{
				super.setAsText(""); 
				return;
			}
			
			Date date = (Date) this.getValue();
			log.info("Date is : " + date);
			if (date!=null)
			{
				Timestamp d2 = new Timestamp(date.getTime());
				String dstr = d2.toString();
				String year = dstr.substring(0,dstr.indexOf("-"));
				if (year.length()!=4)
				{
					log.warn("Date incompatible with SQL format : " + dstr + " - ignoring");
					super.setAsText(""); 
				}
			}
	}
}
}
