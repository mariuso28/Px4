package org.dx4.json.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4AccountJson;
import org.dx4.json.message.Dx4BetDetailJson;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4BetStakeCombo;
import org.dx4.json.message.Dx4BetSummaryJson;
import org.dx4.json.message.Dx4DrawDateRangeJson;
import org.dx4.json.message.Dx4DrawJson;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameGroupJson;
import org.dx4.json.message.Dx4GameJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4MetaGameJson;
import org.dx4.json.message.Dx4PayOutJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.json.message.Dx4PlaceJson;
import org.dx4.json.message.Dx4PlayGameJson;
import org.dx4.json.message.Dx4PlayerProfileJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4ResultJson;
import org.dx4.json.message.Dx4StatusJson;
import org.dx4.json.message.Dx4VersionJson;
import org.dx4.json.message.Dx4WinJson;
import org.dx4.secure.domain.Dx4Profile;
import org.dx4.services.Dx4Services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonServerServices {

	private static final Logger log = Logger.getLogger(JsonServerServices.class);
	private ObjectMapper objectMapper;
	
	public JsonServerServices()
	{
		objectMapper = new ObjectMapper();
	}
	
	@SuppressWarnings("unchecked")
	public Object readJsonObject(String jsonString,@SuppressWarnings("rawtypes") Class clazz)
	{
		try {
			return objectMapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new JsonRuntimeException(e.getMessage() + " - " + jsonString);
		}
	}
	
	public List<Dx4DrawJson> createDx4DrawsJson(List<Dx4DrawResultJson> draws)
	{
		List<Dx4DrawJson> jdraws = new ArrayList<Dx4DrawJson>();
		for (Dx4DrawResultJson draw : draws)
			jdraws.add(createDx4DrawJson(draw));
		return jdraws;
	}
	
	public Dx4DrawJson createDx4DrawJson(Dx4DrawResultJson draw)
	{
		Dx4DrawJson jd = new Dx4DrawJson(); 
		jd.setId(draw.getId());
		jd.setProvider(createProvider(draw.getProvider()));
		jd.setDate(draw.getDate());
		jd.setDrawNo(draw.getDrawNo());
		jd.setFirst(createDx4PlaceJson(draw.getFirstPlace(),draw.getFirstDesc(),draw.getFirstDescCh(),draw.getFirstImage()));
		jd.setSecond(createDx4PlaceJson(draw.getSecondPlace(),draw.getSecondDesc(),draw.getSecondDescCh(),draw.getSecondImage()));
		jd.setThird(createDx4PlaceJson(draw.getThirdPlace(),draw.getThirdDesc(),draw.getThirdDescCh(),draw.getThirdImage()));
		jd.setConsolations(draw.getConsolations());
		jd.setSpecials(draw.getSpecials());
		return jd;
	}
	
	private Dx4ProviderJson createProvider(Dx4ProviderJson provider) {
		
		Dx4ProviderJson cp = new Dx4ProviderJson();
		cp.setCode(provider.getCode());
		cp.setId(provider.getId());
		cp.setName(provider.getName());
		cp.setUrl(provider.getUrl());
		return cp;
	}

	private Dx4PlaceJson createDx4PlaceJson(String number, String desc, String descCh, String image) {
		Dx4PlaceJson pj = new Dx4PlaceJson();
		pj.setNumber(number);
		pj.setDesc(desc);
		pj.setDescCh(descCh);
		pj.setImage(image);
		return pj;
	}

	public Dx4DrawDateRangeJson createDx4DrawsDateRangeJson(List<Date> dateRange) {
		Dx4DrawDateRangeJson ddr = new Dx4DrawDateRangeJson();
		ddr.setStart(dateRange.get(0));
		ddr.setEnd(dateRange.get(1));
		return ddr;
	}
	
	public Dx4ResultJson createDx4MetaGamesJson(List<Dx4MetaGame> dList, Dx4Services dx4Services)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		if (dList.isEmpty())
		{
			result.setStatus(Dx4StatusJson.ERROR);
			result.setMessage("No Upcoming Draws");
			return result;
		}
		
		List<Dx4MetaGameJson> mgs = new ArrayList<Dx4MetaGameJson>();
		for (Dx4MetaGame mg : dList)
			mgs.add(createDx4MetaGameJson(mg,dx4Services));
		
		result.setStatus(Dx4StatusJson.OK);
		result.setResult(mgs);
		return result;
	}
	
	public Dx4ResultJson createDx4MetaGameJsonRes(Dx4MetaGame metaGame,Dx4Services dx4Services)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		if (metaGame==null)
		{
			result.setStatus(Dx4StatusJson.ERROR);
			result.setMessage("No MetaGame");
			return result;
		}
		result.setStatus(Dx4StatusJson.OK);
		result.setResult(createDx4MetaGameJson(metaGame,dx4Services));
		return result;
	}
	
	public Dx4MetaGameJson createDx4MetaGameJson(Dx4MetaGame metaGame, Dx4Services dx4Services)
	{
		Dx4MetaGameJson mgj = new Dx4MetaGameJson();
		mgj.setId(metaGame.getId());
		mgj.setName(metaGame.getName());
		List<Dx4PlayGameJson> pgjs = new ArrayList<Dx4PlayGameJson>();
		for (Dx4PlayGame pg : metaGame.getPlayGamesAvailableForBets())
			pgjs.add(createDx4PlayGameJson(pg,dx4Services));
		mgj.setPlayGames(pgjs);
		mgj.setDescription(metaGame.getDescription());
		List<Dx4GameJson> gjs = new ArrayList<Dx4GameJson>();
		for (Dx4Game g : metaGame.getGames())
			gjs.add(createDx4GameJson(g));
		mgj.setGames(gjs);
		mgj.setProviders(metaGame.getProviders());
		mgj.setGameGroups(Dx4GameGroupJson.values());
		mgj.setImages(metaGame.getImages());
		return mgj;
	}
	
	public Dx4PlayGameJson createDx4PlayGameJson(Dx4PlayGame playGame, Dx4Services dx4Services)
	{
		Dx4PlayGameJson pgj = new Dx4PlayGameJson();
		pgj.setId(playGame.getId());
		pgj.setPlayDate(playGame.getPlayDate());
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(playGame.getPlayDate());
		
		pgj.setPlayedAt(playGame.getPlayedAt());
		pgj.setPlayed(playGame.isPlayed());
		
		for (int i=0; i<playGame.getProviderCodes().length(); i++)
		{
			Dx4ProviderJson pj = dx4Services.getDx4Home().getProviderByCode(playGame.getProviderCodes().charAt(i));
			pgj.getProviders().add(pj.getName());
		}
		
		return pgj;
	}
	
	public Dx4GameJson createDx4GameJson(Dx4Game game)
	{
		Dx4GameJson gj = new Dx4GameJson();
		gj.setId(game.getId());
		gj.setMaxBet(game.getMaxBet());
		gj.setMinBet(game.getMinBet());
		List<Dx4PayOutJson> pos = new ArrayList<Dx4PayOutJson>();
		for (Dx4PayOut po : game.getPayOuts())
			pos.add(createDx4PayOutJson(po));
		gj.setPayOuts(pos);
		gj.setStake(game.getStake());
		gj.setType(game.getGtype());
		return gj;
	}
	
	public Dx4PayOutJson createDx4PayOutJson(Dx4PayOut payOut)
	{
		Dx4PayOutJson po = new Dx4PayOutJson();
		po.setType(payOut.getType());
		po.setPayOut(payOut.getPayOut());
		return po;
	}
	
	public Dx4PlayerProfileJson createDx4PlayerProfileJson(Dx4Profile profile)
	{
		Dx4PlayerProfileJson pj = new Dx4PlayerProfileJson();
		pj.setContact(profile.getContact());
		pj.setEmail(profile.getEmail());
		pj.setIcon(profile.getIcon());
		pj.setNickname(profile.getNickname());
		pj.setPhone(profile.getPhone());
		return pj;
	}
	
	public Dx4AccountJson createDx4AccountJson(Dx4Account account)
	{
		Dx4AccountJson aj = new Dx4AccountJson();
		aj.setBalance(account.getBalance());
		return aj;
	}
	
	public List<Dx4MetaBetJson> createDx4MetaBetsJson(List<Dx4MetaBet> metaBets,Dx4Home dx4Home)
	{
		List<Dx4MetaBetJson> mbjs = new ArrayList<Dx4MetaBetJson>();
		for (Dx4MetaBet mb : metaBets)
			mbjs.add(createDx4MetaBetJson(mb,dx4Home));
		return mbjs;
	}
	
	public List<Dx4BetSummaryJson> createDxBetSummariesJson(List<Dx4MetaBet> metaBets)
	{
		List<Dx4BetSummaryJson> wsjs = new ArrayList<Dx4BetSummaryJson>();
		for (Dx4MetaBet mb : metaBets)
			wsjs.add(createDx4BetSummaryJson(mb));
		return wsjs;
	}
	
	private Dx4BetSummaryJson createDx4BetSummaryJson(Dx4MetaBet mb) {
		Dx4BetSummaryJson bsj = new Dx4BetSummaryJson();
		bsj.setMetaBetId(mb.getId());
		bsj.setMetaGameName(mb.getMetaGame().getName());
		bsj.setPlaced(mb.getPlaced());
		bsj.setPlayed(mb.getPlayed());
		bsj.setPlayDate(mb.getPlayDate());
		bsj.setTotalStake(mb.getTotalStake());
		bsj.setTotalWinStake(totalWinStake(mb));
		bsj.setTotalWin(mb.getTotalWin());
		return bsj;
	}
	
	private double totalWinStake(Dx4MetaBet mb)
	{
		double total = 0.0;
		for (Dx4Win w : mb.getWins())
			total += w.getBet().getStake();
		return total;
	}

	public Dx4BetDetailJson createDx4BetDetailJson(Dx4MetaBet mb,Dx4Home dx4Home) {
		Dx4BetDetailJson bdj = new Dx4BetDetailJson();
		bdj.setBetSummary(createDx4BetSummaryJson(mb));
		
		List<Dx4BetJson> bjs = new ArrayList<Dx4BetJson>();
		for (Dx4Bet b : mb.getBets())
			bjs.add(createDx4BetJson(b,mb));
		bdj.setBets(bjs);
		bdj.setWins(createWinJsons(mb));
		
		return bdj;
	}
	
	public List<Dx4WinJson> createWinJsons(Dx4MetaBet mb)
	{
		List<Dx4WinJson> wjs = new ArrayList<Dx4WinJson>();
		for (Dx4Win w : mb.getWins())
		{
			wjs.add(createDx4WinJson(w,mb));
		}
		return wjs;
	}
	
	public Dx4MetaBetJson createDx4MetaBetJson(Dx4MetaBet metaBet,Dx4Home dx4Home)
	{
		Dx4MetaBetJson mbj = new Dx4MetaBetJson();
		mbj.setId(metaBet.getId());
		List<Dx4BetJson> bjs = new ArrayList<Dx4BetJson>();
		for (Dx4Bet b : metaBet.getBets())
			bjs.add(createDx4BetJson(b,metaBet));
		mbj.setBets(bjs);
		
		mbj.setMetaGameId(metaBet.getMetaGame().getId());
		mbj.setPlaced(metaBet.getPlaced());
		mbj.setPlayed(metaBet.getPlayed());
		mbj.setOutstanding(metaBet.isOutstanding());
		mbj.setTotalStake(metaBet.getTotalStake());
		mbj.setTotalWin(metaBet.getTotalWin());
		if (!metaBet.isOutstanding())
			mbj.setPlayed(metaBet.getPlayGame().getPlayedAt());
		
		mbj.setWins(createWinJsons(metaBet));
		return mbj;
	}
	
	public Dx4BetJson createDx4BetJson(Dx4Bet bet,Dx4MetaBet metaBet)
	{
		Dx4BetJson bj = createDx4BetJson(bet);
		bj.setPlayGameId(metaBet.getPlayGameId());
		bj.setPlayDate(metaBet.getPlayDate());
		return bj;
	}
	
	public static Dx4BetJson createDx4BetJson(Dx4Bet bet)
	{
		Dx4BetJson bj = new Dx4BetJson();
		bj.setId(bet.getId());
		bj.setChoice(bet.getChoice());
		List<String> provs = new ArrayList<String>();
		for (Dx4ProviderJson p : bet.getProviders())
			provs.add(p.getName());
		bj.setProviders(provs);
		bj.setOdds(bet.getOdds());
		
		Dx4GameTypeJson gType = bet.getGame().getGtype();
		if (gType.isBig())
			bj.setBig(bet.getStake());
		else
			bj.setSmall(bet.getStake());
		
		if (gType.equals(Dx4GameTypeJson.D2A) || gType.equals(Dx4GameTypeJson.ABCA) 
				|| gType.equals(Dx4GameTypeJson.D4Small)
					|| (gType.equals(Dx4GameTypeJson.ABCC) 
							|| gType.equals(Dx4GameTypeJson.D4Big)))
		{
			bj.setStakeCombo(Dx4BetStakeCombo.STRAIGHT);
		}
		else
		if (gType.equals(Dx4GameTypeJson.D4BoxSmall) || gType.equals(Dx4GameTypeJson.D4BoxBig))
		{
			bj.setStakeCombo(Dx4BetStakeCombo.BOX);
		}
		else
		if (gType.equals(Dx4GameTypeJson.D4IBoxSmall) ||  gType.equals(Dx4GameTypeJson.D4IBoxBig))
		{
			bj.setStakeCombo(Dx4BetStakeCombo.IBOX);
		}	
		return bj;
	}
	
	public Dx4WinJson createDx4WinJson(Dx4Win win,Dx4MetaBet metaBet)
	{
		Dx4BetJson bet = createDx4BetJson(win.getBet(),metaBet);
		Dx4WinJson wj = new Dx4WinJson();
		Dx4PayOutTypeJson pot = Dx4PayOutTypeJson.valueOf(win.getPlace());
		wj.setPlace(pot.getDesc());
		wj.setBet(bet);
		for (Dx4ProviderJson pj : win.getBet().getProviders())
		{
			if (pj.getCode() == win.getProviderCode())
			{
				wj.setProvider(pj.getName());
				break;
			}
		}
		wj.setResult(win.getResult());
		wj.setWin(win.getWin());
		return wj;
	}
	
	public Dx4VersionJson createDx4VersionJson(String version)
	{
		Dx4VersionJson vj = new Dx4VersionJson();
		vj.setVersionCode(version);
		return vj;
	}
	
	
}
