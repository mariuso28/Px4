package org.dx4.secure.rest.anon;

import org.dx4.json.message.Dx4ResultJson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RestAnonController {

	@RequestMapping(value = "/getComingDraws")
	// Dx4ResultJson contains List<Dx4MetaGameJson> if success, message if fail
	public Dx4ResultJson getComingDraws();
	
	@RequestMapping(value = "/drawsByLatest")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByLatest();
	
	@RequestMapping(value = "/getMetaGame")
	// Dx4ResultJson contains Dx4MetaGameJson if success, message if fail
	public Dx4ResultJson getMetaGame();
	
	@RequestMapping(value = "/numberSearchAll")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success, message if fail
	public Dx4ResultJson numberSearchAll(@RequestParam("searchTerm") String searchTerm,
						@RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberViewAll")
	// Dx4ResultJson contains List<Dx4NumberViewJson> if success, message if fail 
	public Dx4ResultJson numberViewAll(@RequestParam("number") String number,@RequestParam("dictionary") Character dictionary);
		
	@RequestMapping(value = "/getProviderByName")
	// Dx4ResultJson contains Dx4ProviderJson if success, message if fail
	public Dx4ResultJson getProviderByName(@RequestParam("name") String name);
	
	@RequestMapping(value = "/getZodiac")
	// Dx4ResultJson contains List of Dx4ZodiacJson if success, message if fail
	public Dx4ResultJson getZodiac(@RequestParam("setNum") int setNum);
	
	@RequestMapping(value = "/getHoroscope")
	// Dx4ResultJson contains List of Dx4HoroscopeJson if success, message if fail
	public Dx4ResultJson getHoroscope();
	
	@RequestMapping(value = "/getTrendingUp")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingUp();
	
	@RequestMapping(value = "/getTrendingDown")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingDown();
	
	@RequestMapping(value = "/getTrendingLatest")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingLatest();
	
	@RequestMapping(value = "/getTrendingLatestPayouts")
	// Dx4ResultJson contains List of Dx4NumberFloatPayoutJson if success, message if fail (limit is max numberfloatpayouts
	public Dx4ResultJson getTrendingLatestPayouts(@RequestParam("limit") int limit);
	
	@RequestMapping(value = "/register")
	// Dx4ResultJson contains info message if success, error message if fail 
	public Dx4ResultJson register(@RequestParam("upstream") String upstream,@RequestParam("email") String email,@RequestParam("password") String password,
							@RequestParam("contact") String contact,@RequestParam("phone") String phone );
	
	@RequestMapping(value = "/getVersion")
	// Dx4ResultJson contains Dx4VersionJson if success, message if fail
	public Dx4ResultJson getVersion();
	
	@RequestMapping(value = "/password")
	// Dx4ResultJson contains info message if success, error message if fail 
	public Dx4ResultJson password(@RequestParam("email") String email);
}
