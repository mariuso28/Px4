package org.dx4.secure.rest.play;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.home.persistence.Dx4DuplicateKeyException;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4FavouriteJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4NumberFloatRefreshTimeJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4ResultJson;
import org.dx4.json.server.JsonServerServices;
import org.dx4.player.Dx4Player;
import org.dx4.services.Dx4Services;
import org.dx4.services.RestServices;
import org.dx4.services.RestServicesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/play")
public class RestPlayControllerImpl implements RestPlayController{
	
	private static final Logger log = Logger.getLogger(RestPlayControllerImpl.class);
	
	@Autowired
	private Dx4Services dx4Services;
	@Autowired
	JsonServerServices jsonServerServices;
	@Autowired
	RestServices restServices;

	@Override
	@RequestMapping(value = "/getProfile")
	// Dx4ResultJson contains Dx4PlayerProfileJson if success, message if fail
	public Dx4ResultJson getProfile(OAuth2Authentication auth){
		
		
		String email = ((User)auth.getPrincipal()).getUsername();
		
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result.success(jsonServerServices.createDx4PlayerProfileJson(player.createProfile()));
		return result;
	}
	
	private Dx4Player getPlayer(String email,Dx4ResultJson result)
	{
		Dx4Player player = dx4Services.getDx4Home().getPlayerByUsername(email);
		if (player == null)
		{
			log.error("Player : " + email + " not found");
			result.fail("Player : " + email + " not found");	
		}
		return player;
	}
	
	@Override
	@RequestMapping(value = "/getAccount")
	// Dx4ResultJson contains Dx4PlayerProfileJson if success, message if fail
	public Dx4ResultJson getAccount(OAuth2Authentication auth){
		String email = ((User)auth.getPrincipal()).getUsername();
		
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result.success(jsonServerServices.createDx4AccountJson(player.getAccount()));
		return result;
	}

	@Override
	@RequestMapping(value = "/getComingDraws")
	// Dx4ResultJson contains List<Dx4MetaGameJson> if success, message if fail
	public Dx4ResultJson getComingDraws(OAuth2Authentication auth)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		return jsonServerServices.createDx4MetaGamesJson(restServices.createDx4MetaGamePlayList(player),dx4Services);
	}
	
	@Override
	@RequestMapping(value = "/getBetDetail")
	// Dx4ResultJson contains Dx4BetDetailJson if success, message if fail
	public Dx4ResultJson getBetDetail(OAuth2Authentication auth, long metaBetId) {
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		Dx4MetaBet mb = dx4Services.getDx4Home().getMetaBetById(player, metaBetId);
	
		if (mb==null)
			result.fail("Metabet for id : " + metaBetId + " not found.");
		else
			result.success(jsonServerServices.createDx4BetDetailJson(mb,dx4Services.getDx4Home()));
		
		return result;
	}
	
	@RequestMapping(value = "/emailBetDetail")
	// Dx4ResultJson message confirmation if success, message if fail
	public Dx4ResultJson emailBetDetail(OAuth2Authentication auth,@RequestParam("metaBetId") long metaBetId)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		Dx4MetaBet mb = dx4Services.getDx4Home().getMetaBetById(player, metaBetId);
	
		if (mb==null)
		{
			result.fail("Metabet for id : " + metaBetId + " not found.");
			return result;
		}
		try
		{
			dx4Services.sendMetaBetEmail(player, mb);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.fail("Bet details could not be sent - please try later.");
			return result;
		}
		result.success("Bet details successfully sent - please check inbox");
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getBetSummaries")
	// Dx4ResultJson contains List<Dx4BetSummaryJson> if success, message if fail
	public Dx4ResultJson getBetSummaries(OAuth2Authentication auth,@RequestParam("retrieveFlag") Dx4BetRetrieverFlag retrieveFlag)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result.success(jsonServerServices.createDxBetSummariesJson(restServices.getMetaBetsForPlayer(player,retrieveFlag)));
		return result;
	}
	
	@Override
	@RequestMapping(value = "/placeMetaBet")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson placeMetaBet(OAuth2Authentication auth,@RequestBody() Dx4MetaBetJson metaBetJson)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		try {		
	//		log.info("Placing metabet :" + metaBetJson);
			String invalidChoices = restServices.placeMetaBet(player,metaBetJson,jsonServerServices);
			if (!invalidChoices.isEmpty())
				result.setMessage(invalidChoices);
			result.success();
		}
		catch (Dx4DuplicateKeyException e)
		{
			log.warn("Duplicate metabetuuid");
			result.warn("Duplicate metabetuuid");
		}
		catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/usePrevMetaBet")
	// Dx4ResultJson contains Dx4MetaBetJson if success, message if fail
	public Dx4ResultJson usePrevMetaBet(OAuth2Authentication auth,@RequestParam("metaBetId") long metaBetId)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			Dx4MetaBet metaBet = restServices.usePrevMetaBet(player,metaBetId);
			result.success(jsonServerServices.createDx4MetaBetJson(metaBet,dx4Services.getDx4Home()));
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/makeDeposit")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson makeDeposit(OAuth2Authentication auth,@RequestParam("amount") double amount)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			restServices.makeDeposit(player,amount);
			result.success();
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/makeWithrawal")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson makeWithrawal(OAuth2Authentication auth,@RequestParam("amount") double amount)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			restServices.makeWithdrawl(player,amount);
			result.success();
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/shake")
	// Dx4ResultJson contains the number as String if success, message if fail
	public Dx4ResultJson shake(OAuth2Authentication auth,@RequestParam("playGameId") long playGameId,@RequestParam("gameType") Dx4GameTypeJson gameType)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			String number = restServices.getRandom(playGameId,gameType);
			result.success(number);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}

	@Override
	@RequestMapping(value = "/getProviderByName")
	// Dx4ResultJson contains Dx4ProviderJson if success, message if fail
	public Dx4ResultJson getProviderByName(OAuth2Authentication auth, @RequestParam("name") String name) {
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			Dx4ProviderJson provider = restServices.getProviderByName(name);
			result.success(provider);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}

	@Override
	@RequestMapping(value = "/getFavourites")
	// Dx4ResultJson contains List<Dx4FavouriteJson> if success, message if fail
	public Dx4ResultJson getFavourites(OAuth2Authentication auth)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			List<Dx4FavouriteJson> favourites = restServices.getFavourites(player);
			result.success(favourites);
		} catch (Exception e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/storeFavourite")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson storeFavourite(OAuth2Authentication auth,@RequestParam("betId") long betId,@RequestParam("description")  String description)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			Dx4FavouriteJson fj = new Dx4FavouriteJson();
			fj.setDescription(description);
			fj.setPlayerId(player.getId());
			dx4Services.getDx4Home().storeFavourite(fj,betId);
			result.success();
		} catch (Exception e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/deleteFavourite")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson deleteFavourite(OAuth2Authentication auth,@RequestParam("favouriteId") long favouriteId)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			dx4Services.getDx4Home().deleteFavourite(favouriteId);
			result.success();
		} catch (Exception e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}

	@Override
	@RequestMapping(value = "/getNumberFloatPayoutJson")
	// Dx4ResultJson contains Dx4NumberFloatPayoutJson if success, message if fail
	public Dx4ResultJson getNumberFloatPayoutJson(OAuth2Authentication auth,@RequestParam("number") String number)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			Dx4NumberFloatPayoutJson nfp = dx4Services.getFloatPayoutMgr().getDx4NumberFloatPayoutJson(player,number);
			result.success(nfp);
		} catch (Exception e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@RequestMapping(value = "/getNumberFloatRefreshTime")
	// Dx4ResultJson contains Dx4NumberFloatRefreshTime if success, message if fail
	public Dx4ResultJson getNumberFloatPayoutJson(OAuth2Authentication auth)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		Dx4ResultJson result = new Dx4ResultJson();
		Dx4Player player = getPlayer(email,result);
		if (player == null)
			return result;
		
		result = new Dx4ResultJson();
		try {
			Dx4NumberFloatRefreshTimeJson nfr = new Dx4NumberFloatRefreshTimeJson();
			nfr.setRefreshTimeInSecs(dx4Services.getFloatPayoutMgr().getCurrentFloatNumberRefreshTime());
			result.success(nfr);
		} catch (Exception e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
}
