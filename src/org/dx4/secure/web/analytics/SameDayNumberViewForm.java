package org.dx4.secure.web.analytics;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dx4.json.message.Dx4DrawResultJson;

public class SameDayNumberViewForm 
{
		private List<Dx4DrawResultJson> drawResults;
		private Date date;
		private boolean external;
		
		public SameDayNumberViewForm(List<Dx4DrawResultJson> drawResults) {
			setDrawResults(drawResults);
			if (drawResults.isEmpty())
				setDate(new GregorianCalendar().getTime());
			else
				setDate(drawResults.get(0).getDate());
		}

		public List<Dx4DrawResultJson> getDrawResults() {
			return drawResults;
		}

		public void setDrawResults(List<Dx4DrawResultJson> drawResults) {
			this.drawResults = drawResults;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public boolean isExternal() {
			return external;
		}

		public void setExternal(boolean external) {
			this.external = external;
		}

}
