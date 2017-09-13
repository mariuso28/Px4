package org.dx4.agent.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.external.parser.FxRate;
import org.dx4.external.parser.ParseFxRateUSDMYR;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.services.Dx4Services;
import org.dx4.services.Dx4ServicesException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dx4BetRollupMgrUSDMYR {

	private static final Logger log = Logger.getLogger(Dx4BetRollupMgrUSDMYR.class);
	
	private FxRate fxRate;
	private List<String> workbookPaths = new ArrayList<String>();
	
	
	public Dx4BetRollupMgrUSDMYR(Dx4Services dx4Services, Dx4MetaGame mg, Dx4PlayGame playGame,boolean win) throws Dx4ServicesException
	{
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try {
			
			FxRate rate;
			if (win)
			{
				rate = getFxRateForWin(dx4Home,playGame);
				// make it $ for test
				rate.setRate(1.0);
			}
			else
			{
					ParseFxRateUSDMYR fxRate = new ParseFxRateUSDMYR();
					rate = fxRate.getFxRate();
					log.info("Got Fx rate: " + rate);
					rate.setRate(rate.getRate() + (rate.getRate()*0.02));
					// should be the spot rate uplifted by 2%
					log.info("Using Fx rate: " + rate);
					
					FxForPlayGame fxpg = new FxForPlayGame(playGame.getId(),rate.getTimestamp(),rate.getFrom(),rate.getTo(),rate.getRate());
					dx4Home.storeFxForPlayGame(fxpg);
			}
			
			for (Dx4ProviderJson prov : dx4Home.getProviders())
			{
				if (playGame.getProviderCodes().indexOf(prov.getCode())<0)
					continue;
				
				Dx4WinRollupUSDMYR br = new Dx4WinRollupUSDMYR(mg,playGame,dx4Home,prov,rate,win);
				workbookPaths.add(br.getWorkbookPath());
			}
			Dx4WinRollupUSDMYR br = new Dx4WinRollupUSDMYR(mg,playGame,dx4Home,rate,win);
			workbookPaths.add(br.getWorkbookPath());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Dx4ServicesException("Could not create exposure rollups : " + e.getMessage());
		}
	}
	
	private FxRate getFxRateForWin(Dx4Home dx4Home,Dx4PlayGame playGame) throws Dx4ServicesException
	{
		FxForPlayGame fx = dx4Home.getFxForPlayGame(playGame.getId(),"USD","MYR");
		if (fx==null)
		{
			throw new Dx4ServicesException("Cannot find fx USD/MYR for playgameid : " + playGame.getId());
		}
		return new FxRate(fx.getFromCcy(),fx.getToCcy(),fx.getPlayGameDate(),fx.getRate());
	}
	
	public FxRate getFxRate() {
		return fxRate;
	}

	public void setFxRate(FxRate fxRate) {
		this.fxRate = fxRate;
	}
	
	public List<String> getWorkbookPaths() {
		return workbookPaths;
	}

	public void setWorkbookPaths(List<String> workbookPaths) {
		this.workbookPaths = workbookPaths;
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		try {
			Dx4Home dx4Home = dx4Services.getDx4Home();
			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			Dx4PlayGame playGame = metaGame.getPlayGameById(34);
			
			new Dx4BetRollupMgrUSDMYR(dx4Services,metaGame,playGame,true);
		} catch (Dx4ServicesException e) {
			e.printStackTrace();
		}
	}
}
