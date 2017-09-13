package org.dx4.test;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4DateWin;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestExternalResults {
	
	private static final Logger log = Logger.getLogger(TestExternalResults.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
	//	dx4Services.getExternalService().updateExternalGameResults();		
	
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(2017,3,8);
		java.util.Date date = gc.getTime();
		ExternalGameResults xgr = dx4Services.getExternalService().getActualExternalGameResults(date);
		
		for (Dx4DrawResultJson dr : xgr.getDraws())
			log.info(dr);
		
/*		
		Dx4SecureUser user = dx4Services.getDx4Home().getUserByCode("c0");
		AgentWinForm agf = new AgentWinForm(xgr,dx4Services.getDx4Home(),user);
		log.info(agf);
		
		Dx4Player player = (Dx4Player) dx4Services.getDx4Home().getUserByCode("c0p2");
		Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetById(player,new Long(327));
		MetaBetWinForm mbf = new MetaBetWinForm(xgr,dx4Services.getDx4Home(),metaBet,"-");
		log.info(mbf);
*/
		
		Dx4SecureUser user = dx4Services.getDx4Home().getUserByCode("c0");
		
		List<Dx4DateWin> previousDraws = dx4Services.getDx4Home().getWinDates((Dx4Agent) user);
		for (Dx4DateWin dw : previousDraws)
			log.info(dw);
	}
}
