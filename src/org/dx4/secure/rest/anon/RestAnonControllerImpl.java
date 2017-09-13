package org.dx4.secure.rest.anon;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4ResultJson;
import org.dx4.json.message.Dx4ZodiacJson;
import org.dx4.json.message.NumberPrizeJson;
import org.dx4.json.server.JsonServerServices;
import org.dx4.services.Dx4Services;
import org.dx4.services.RestServices;
import org.dx4.services.RestServicesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anon")
public class RestAnonControllerImpl implements RestAnonController{
	
	private static final Logger log = Logger.getLogger(RestAnonControllerImpl.class);
	
	@Autowired
	private Dx4Services dx4Services;
	@Autowired
	JsonServerServices jsonServerServices;
	@Autowired
	RestServices restServices;
	
	@Override
	@RequestMapping(value = "/getComingDraws")
	// Dx4ResultJson contains List<Dx4MetaGameJson> if success, message if fail
	public Dx4ResultJson getComingDraws()
	{
		return jsonServerServices.createDx4MetaGamesJson(restServices.createDx4MetaGamePlayList(),dx4Services);
	}
	
	@Override
	@RequestMapping(value = "/drawsByLatest")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByLatest()
	{
		Dx4ResultJson result = new Dx4ResultJson();
		
		try
		{
			result.success(jsonServerServices.createDx4DrawsJson(restServices.getDrawsLatest()));
		}
		catch (RestServicesException e)
		{
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/getMetaGame")
	// Dx4ResultJson contains Dx4MetaGameJson if success, message if fail
	public Dx4ResultJson getMetaGame()
	{
		return jsonServerServices.createDx4MetaGameJsonRes(restServices.createDx4MetaGame(),dx4Services);
	}
	
	@Override
	@RequestMapping(value = "/numberViewAll")
	// Dx4ResultJson contains List<Dx4NumberViewJson> if success, message if fail 
	public Dx4ResultJson numberViewAll(@RequestParam("number") String number,@RequestParam("dictionary") Character dictionary){
	
		return restServices.numberViewAll(number, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/numberSearchAll")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success, message if fail
	public Dx4ResultJson numberSearchAll(@RequestParam("searchTerm") String searchTerm,@RequestParam("dictionary") Character dictionary)
	{
		return restServices.numberSearchAll(searchTerm, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/getProviderByName")
	// Dx4ResultJson contains Dx4ProviderJson if success, message if fail
	public Dx4ResultJson getProviderByName(@RequestParam("name") String name) {
		
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			Dx4ProviderJson provider = restServices.getProviderByName(name);
			result.success(provider);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@RequestMapping(value = "/getZodiac")
	// Dx4ResultJson contains List of Dx4ZodiacJson if success, message if fail
	public Dx4ResultJson getZodiac(@RequestParam("setNum") int setNum)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<Dx4ZodiacJson> zodiacs = restServices.getZodiac(setNum);
			result.success(zodiacs);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@RequestMapping(value = "/getHoroscope")
	// Dx4ResultJson contains List of Dx4HoroscopeJson if success, message if fail
	public Dx4ResultJson getHoroscope()
	{
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<Dx4HoroscopeJson> horoscopes = restServices.getHoroscope();
			result.success(horoscopes);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}

	@Override
	@RequestMapping(value = "/getTrendingUp")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingUp() {
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<NumberPrizeJson> trending = restServices.getTrending(true);
			result.success(trending);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getTrendingDown")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingDown() {
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<NumberPrizeJson> trending = restServices.getTrending(false);
			result.success(trending);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getTrendingLatest")
	// Dx4ResultJson contains List of NumberPrizeJson if success, message if fail
	public Dx4ResultJson getTrendingLatest() {
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<NumberPrizeJson> trending = restServices.getTrendingLatest(false);
			result.success(trending);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@RequestMapping(value = "/getTrendingLatestPayouts")
	// Dx4ResultJson contains List of Dx4NumberFloatPayoutJson if success, message if fail (limit is max numberfloatpayouts
	public Dx4ResultJson getTrendingLatestPayouts(@RequestParam("limit") int limit)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			List<Dx4NumberFloatPayoutJson> trending = restServices.getTrendingLatestPayouts(limit);
			result.success(trending);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}
	
	@Override
	@RequestMapping(value = "/register")
	// Dx4ResultJson contains info message if success, error message if fail 
	public Dx4ResultJson register(@RequestParam("upstream") String upstream,@RequestParam("email") String email,@RequestParam("password") String password,
							@RequestParam("contact") String contact,@RequestParam("phone") String phone )
	{
		log.info("Registering : " + email + " with:" + upstream);
		Dx4ResultJson result = new Dx4ResultJson();
		try {
			String message = restServices.register(upstream,email,password,contact,phone);
			log.info(message);
			result.success(message);
		} catch (RestServicesException e) {
			log.error(e.getMessage());
			result.fail(e.getMessage());
		}		
		return result;
	}

	@Override
	public Dx4ResultJson getVersion() {
		Dx4ResultJson result = new Dx4ResultJson();
		
		try
		{
			String version = dx4Services.getDx4Home().getVersionCode();
			result.success(jsonServerServices.createDx4VersionJson(version));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/password")
	// Dx4ResultJson contains info message if success, error message if fail 
	public Dx4ResultJson password(@RequestParam("email") String email) {
		
		Dx4ResultJson result = new Dx4ResultJson();
		String errMsg = dx4Services.tryResetPassword(email);
		if (!errMsg.isEmpty())
		{
			log.error("error trying reset password : " + errMsg);
			result.fail(errMsg);
			return result;
		}
		
		String infoMsg = "Your password has been reset, please check your inbox and login to change.";
		result.success(infoMsg);
		return result;
	}
}
