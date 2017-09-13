package org.dx4.secure.web.player.display;

import java.util.Date;

import org.dx4.game.Dx4MetaGame;

public class DisplayQuickBetGame 
{
		private String name;
		private Date playDate;
		
		public DisplayQuickBetGame(Dx4MetaGame metaGame)
		{
			setName(metaGame.getName());
			setPlayDate(metaGame.nextDrawCutOffTime());
		}
			
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPlayDate(Date playDate) {
			this.playDate = playDate;
		}

		public Date getPlayDate() {
			return playDate;
		}
		
}
