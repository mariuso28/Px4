package org.dx4.secure.rest.play;

import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4ResultJson;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RestPlayController {

	@RequestMapping(value = "/getProfile")
	// Dx4ResultJson contains Dx4PlayerProfileJson if success, message if fail
	public Dx4ResultJson getProfile(OAuth2Authentication auth);

	@RequestMapping(value = "/getAccount")
	// Dx4ResultJson contains Dx4PlayerProfileJson if success, message if fail
	public Dx4ResultJson getAccount(OAuth2Authentication auth);

	@RequestMapping(value = "/getComingDraws")
	// Dx4ResultJson contains List<Dx4MetaGameJson> if success, message if fail
	public Dx4ResultJson getComingDraws(OAuth2Authentication auth);
		
	@RequestMapping(value = "/getBetSummaries")
	// Dx4ResultJson contains List<Dx4BetSummaryJson> if success, message if fail
	public Dx4ResultJson getBetSummaries(OAuth2Authentication auth,@RequestParam("retrieveFlag") Dx4BetRetrieverFlag retrieveFlag);
	
	@RequestMapping(value = "/getBetDetail")
	// Dx4ResultJson contains Dx4BetDetailJson if success, message if fail
	public Dx4ResultJson getBetDetail(OAuth2Authentication auth,@RequestParam("metaBetId") long metaBetId);
	
	@RequestMapping(value = "/emailBetDetail")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson emailBetDetail(OAuth2Authentication auth,@RequestParam("metaBetId") long metaBetId);
	
	@RequestMapping(value = "/placeMetaBet")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson placeMetaBet(OAuth2Authentication auth,@RequestBody() Dx4MetaBetJson metaBetBasketJson);
	
	@RequestMapping(value = "/usePrevMetaBet")
	// Dx4ResultJson contains Dx4MetaBetJson if success, message if fail
	public Dx4ResultJson usePrevMetaBet(OAuth2Authentication auth,@RequestParam("metaBetId") long metaBetId);
	
	@RequestMapping(value = "/makeDeposit")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson makeDeposit(OAuth2Authentication auth,@RequestParam("amount") double amount);
	
	@RequestMapping(value = "/makeWithrawal")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson makeWithrawal(OAuth2Authentication auth,@RequestParam("amount") double amount);
	
	@RequestMapping(value = "/shake")
	// Dx4ResultJson contains the number as String if success, message if fail
	public Dx4ResultJson shake(OAuth2Authentication auth,@RequestParam("playGameId") long playGameId,@RequestParam("gameType") Dx4GameTypeJson gameType);
	
	@RequestMapping(value = "/getProviderByName")
	// Dx4ResultJson contains Dx4ProviderJson if success, message if fail
	public Dx4ResultJson getProviderByName(OAuth2Authentication auth,@RequestParam("name") String name);
	
	@RequestMapping(value = "/getFavourites")
	// Dx4ResultJson contains Dx4ProviderJson if success, message if fail
	public Dx4ResultJson getFavourites(OAuth2Authentication auth);
	
	@RequestMapping(value = "/storeFavourite")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson storeFavourite(OAuth2Authentication auth,@RequestParam("betId") long betId,@RequestParam("description") String description);
	
	@RequestMapping(value = "/deleteFavourite")
	// Dx4ResultJson empty if success, message if fail
	public Dx4ResultJson deleteFavourite(OAuth2Authentication auth,@RequestParam("favouriteId") long favouriteId);
	
	@RequestMapping(value = "/getNumberFloatPayoutJson")
	// Dx4ResultJson contains Dx4NumberFloatPayoutJson if success, message if fail
	public Dx4ResultJson getNumberFloatPayoutJson(OAuth2Authentication auth,@RequestParam("number") String number);
	
	@RequestMapping(value = "/getNumberFloatRefreshTime")
	// Dx4ResultJson contains Dx4NumberFloatRefreshTimeJson if success, message if fail
	public Dx4ResultJson getNumberFloatPayoutJson(OAuth2Authentication auth);
	
}
