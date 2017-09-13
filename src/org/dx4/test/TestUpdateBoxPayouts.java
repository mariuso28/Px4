package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestUpdateBoxPayouts {
	
	private static final Logger log = Logger.getLogger(TestUpdateBoxPayouts.class);
	
	/*
	First("1st Prize"),Second("2nd Prize"),Third("3rd Prize"),Spec("Special"),Cons("Consulation"),
	
	// IBOX
	FirstIB24("1st 24 Perm"),SecondIB24("2nd 24 Perm"),ThirdIB24("3rd 24 Perm"),SpecIB24("Spec 24 Perm"),ConsIB24("Cons 24 Perm"),
	FirstIB12("1st 12 Perm"),SecondIB12("2nd 12 Perm"),ThirdIB12("3rd 12 Perm"),SpecIB12("Spec 12 Perm"),ConsIB12("Cons 12 Perm"),
	FirstIB6("1st 6 Perm"),SecondIB6("2nd 6 Perm"),ThirdIB6("3rd 6 Perm"),SpecIB6("Spec 6 Perm"),ConsIB6("Cons 6 Perm"),
	FirstIB4("1st 4 Perm"),SecondIB4("2nd 4 Perm"),ThirdIB4("3rd 4 Perm"),SpecIB4("Spec 4 Perm"),ConsIB4("Cons 4 Perm");
	*/
	
	private static void storeBoxBig(Dx4MetaGame metaGame,Dx4Home dx4Home)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(Dx4GameTypeJson.D4Big);
		Dx4Game gameIBBig = metaGame.getGameByType(Dx4GameTypeJson.D4BoxBig);
	
		Dx4PayOut pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.First);
		Dx4PayOut po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Second);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Third);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Spec);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Cons);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB4);
		po.setPayOut(pd.getPayOut());
		
		dx4Home.updatePayOuts(gameIBBig);
	}
	
	
	private static void storeBoxSmall(Dx4MetaGame metaGame,Dx4Home dx4Home)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(Dx4GameTypeJson.D4Small);
		Dx4Game gameIBBig = metaGame.getGameByType(Dx4GameTypeJson.D4BoxSmall);
		
		Dx4PayOut pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.First);
		Dx4PayOut po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Second);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB4);
		po.setPayOut(pd.getPayOut());
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Third);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB24);
		po.setPayOut(pd.getPayOut());	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB12);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB6);
		po.setPayOut(pd.getPayOut());
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB4);
		po.setPayOut(pd.getPayOut());
		
		dx4Home.updatePayOuts(gameIBBig);
	}
	
	private static void storeIBoxBig(Dx4MetaGame metaGame,Dx4Home dx4Home)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(Dx4GameTypeJson.D4Big);
		Dx4Game gameIBBig = metaGame.getGameByType(Dx4GameTypeJson.D4IBoxBig);
		
		Dx4PayOut pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.First);
		Dx4PayOut po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Second);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Third);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Spec);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SpecIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Cons);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ConsIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		dx4Home.updatePayOuts(gameIBBig);
	}
	
	private static void storeIBoxSmall(Dx4MetaGame metaGame,Dx4Home dx4Home)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(Dx4GameTypeJson.D4Small);
		Dx4Game gameIBBig = metaGame.getGameByType(Dx4GameTypeJson.D4IBoxSmall);
		
		Dx4PayOut pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.First);
		Dx4PayOut po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.FirstIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Second);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.SecondIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		pd = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.Third);
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB24);
		po.setPayOut(round(pd.getPayOut(),24));	
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB12);
		po.setPayOut(round(pd.getPayOut(),12));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB6);
		po.setPayOut(round(pd.getPayOut(),6));
		po = gameIBBig.getPayOutByType(Dx4PayOutTypeJson.ThirdIB4);
		po.setPayOut(round(pd.getPayOut(),4));
		
		dx4Home.updatePayOuts(gameIBBig);
	}
	
	private static double round(double payout,double perm)
	{
		return Math.round(payout/perm);
	}
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("4D With ABC");
		
		storeIBoxBig(metaGame,dx4Services.getDx4Home());
		storeIBoxSmall(metaGame,dx4Services.getDx4Home());
		
		storeBoxBig(metaGame,dx4Services.getDx4Home());
		storeBoxSmall(metaGame,dx4Services.getDx4Home());
		
		log.info("Done");
	}
}
