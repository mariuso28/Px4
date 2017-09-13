package org.dx4.secure.web.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.secure.web.Dx4FormValidationException;

public class AdminGamePlayDateForm 
{
	private static final Logger log = Logger.getLogger(AdminGamePlayDateForm.class);
	
	private AdminGamePlayDateCommand command;
	private Dx4PlayGame playGame;
	private Date nextPlayDate;
	private String message;
	
	public AdminGamePlayDateForm()
	{
		GregorianCalendar gc = new GregorianCalendar();
        gc.roll(Calendar.DAY_OF_YEAR,5);
        setNextPlayDate(gc.getTime());
	}

	public Date getNextPlayDate() {
		return nextPlayDate;
	}

	public void setNextPlayDate(Date nextPlayDate) {
		this.nextPlayDate = nextPlayDate;
	}

	public void setCommand(AdminGamePlayDateCommand command) {
		this.command = command;
	}

	public AdminGamePlayDateCommand getCommand() {
		return command;
	}

	public void setPlayGame(Dx4PlayGame playGame) {
		this.playGame = playGame;
	}

	public Dx4PlayGame getPlayGame() {
		return playGame;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void validate(Dx4Home dx4Home,Dx4MetaGame metaGame,boolean allowPastGame) throws Dx4FormValidationException
	{
		Date playDate = command.getPlayDate();
		GregorianCalendar gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
		Date now = gc.getTime();
		if (now.after(playDate) && !allowPastGame)
		{
				throw new Dx4FormValidationException("Play Date cannot be historic");
		}
		Dx4PlayGame pg = metaGame.getPlayGameByPlayDate(playDate);
		if (pg!=null)
			throw new Dx4FormValidationException("Play Date already exists for game");
	}

	public void validatePlayed(Dx4PlayGame playGame) throws Dx4FormValidationException {
		log.info("validatePlayed PlayedAt: " + getCommand().getPlayDate() + " Play Date: " + playGame.getPlayDate());
		if (getCommand().getPlayDate().before(playGame.getPlayDate()))
			throw new Dx4FormValidationException("Game cannot be played cannot be played before play date");
	}
}
