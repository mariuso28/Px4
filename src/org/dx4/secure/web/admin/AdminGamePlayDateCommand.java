package org.dx4.secure.web.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminGamePlayDateCommand implements Serializable{
		
	private static final long serialVersionUID = 287843249294309478L;
		private long playGameId;
		private List<String> choices;
		private Date playDate;
		
		public AdminGamePlayDateCommand()
		{
			 setChoices(new ArrayList<String>(12));
		}
		
		public void setPlayDate(Date playDate) {
			this.playDate = playDate;
		}

		public Date getPlayDate() {
			return playDate;
		}

		public void setPlayGameId(long playGameId) {
			this.playGameId = playGameId;
		}

		public long getPlayGameId() {
			return playGameId;
		}

		public void setChoices(List<String> choices) {
			this.choices = choices;
		}

		public List<String> getChoices() {
			return choices;
		}


		
}
