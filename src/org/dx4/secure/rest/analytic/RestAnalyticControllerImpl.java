package org.dx4.secure.rest.analytic;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4DrawDateRangeJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.dx4.json.message.Dx4NumberViewJson;
import org.dx4.json.message.Dx4ResultJson;
import org.dx4.json.server.JsonServerServices;
import org.dx4.services.Dx4Services;
import org.dx4.services.RestServices;
import org.dx4.services.RestServicesException;
import org.dx4.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anal")
public class RestAnalyticControllerImpl implements RestAnalyticController {
	
	private static final Logger log = Logger.getLogger(RestAnalyticController.class);
	
	@Autowired
	private Dx4Services dx4Services;
	@Autowired
	JsonServerServices jsonServerServices;
	@Autowired
	RestServices restServices;
	
	@Override
	@RequestMapping(value = "/drawsByDateStr")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByDateStr(@RequestParam("date") String date){
		
		Date dd = DateUtil.dateFromYYmmDDString(date);
		
		Dx4ResultJson result = new Dx4ResultJson();
		if (dd==null)
		{
			log.error("invalid date string - format should be yyyy-mm-dd");
			result.fail("invalid date string - format should be yyyy-mm-dd");
			return result;
		}
		
		try
		{
			result.success(jsonServerServices.createDx4DrawsJson(restServices.getDrawsByDate(dd)));
		}
		catch (RestServicesException e)
		{
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/drawsByDate")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByDate(@RequestParam("date") Date date)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		
		try
		{
			result.success(jsonServerServices.createDx4DrawsJson(restServices.getDrawsByDate(date)));
		}
		catch (RestServicesException e)
		{
			result.fail(e.getMessage());
		}
		return result;
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
	
	@Override
	@RequestMapping(value = "/drawsDateRange")
	public Dx4DrawDateRangeJson drawsDateRange(){
	
		List<Date> dateRange = dx4Services.getDx4Home().getDrawResultsDateRange();
		return jsonServerServices.createDx4DrawsDateRangeJson(dateRange);
	}
	
	@Override
	@RequestMapping(value = "/numberSearch")
	public List<Dx4NumberPageElementJson> numberSearch(@RequestParam("searchTerm") String searchTerm,
					@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate, @RequestParam("dictionary") Character dictionary){
	
		return restServices.numberSearch(searchTerm, startDate, endDate, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/numberSearchStr")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success message if fail
	public Dx4ResultJson numberSearchStr(@RequestParam("searchTerm") String searchTerm,
			@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate, @RequestParam("dictionary") Character dictionary){
	
		Date sd = DateUtil.dateFromYYmmDDString(startDate);
		Date ed = DateUtil.dateFromYYmmDDString(endDate);
		Dx4ResultJson result = new Dx4ResultJson();
		if (sd == null || ed ==null)
		{
			log.error("invalid date string - format should be yyyy-mm-dd");
			result.fail("invalid date string - format should be yyyy-mm-dd");
			return result;
		}
		
		try
		{
			List<Dx4NumberPageElementJson> search = restServices.numberSearch(searchTerm,sd, ed, dictionary);
			result.success(search);
			return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			result.fail("Number Search failed - " + e.getMessage());
			return result;
		}
	}
	
	@RequestMapping(value = "/numberSearchAll")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success, message if fail
	public Dx4ResultJson numberSearchAll(@RequestParam("searchTerm") String searchTerm,@RequestParam("dictionary") Character dictionary)
	{
		return restServices.numberSearchAll(searchTerm, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/numberViewStr")
	// Dx4ResultJson contains Dx4NumberViewJson if success, message if fail 
	public Dx4ResultJson numberViewStr(@RequestParam("number") String number,
			@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate, @RequestParam("dictionary") Character dictionary){
	
		Date sd = DateUtil.dateFromYYmmDDString(startDate);
		Date ed = DateUtil.dateFromYYmmDDString(endDate);
		Dx4ResultJson result = new Dx4ResultJson();
		if (sd == null || ed ==null)
		{
			log.error("invalid date string - format should be yyyy-mm-dd");
			result.fail("invalid date string - format should be yyyy-mm-dd");
			return result;
		}
		
		result.success(restServices.getNumberView(number, sd, ed, dictionary));
		return result;
	}
	
	@Override
	@RequestMapping(value = "/numberView")
	public Dx4NumberViewJson numberView(@RequestParam("number") String number,
			@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate, @RequestParam("dictionary") Character dictionary){
		
		return restServices.getNumberView(number, startDate, endDate, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/numberViewAll")
	// Dx4ResultJson contains List<Dx4NumberViewJson> if success, message if fail 
	public Dx4ResultJson numberViewAll(@RequestParam("number") String number, @RequestParam("dictionary") Character dictionary){
	
		return restServices.numberViewAll(number, dictionary);
	}
	
	@Override
	@RequestMapping(value = "/numberRevenuesStr")
	// Dx4ResultJson contains List<Dx4NumberRevenueJson> if success, message if fail 
	public Dx4ResultJson getNumberRevenuesStr(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate){
		
		Date sd = DateUtil.dateFromYYmmDDString(startDate);
		Date ed = DateUtil.dateFromYYmmDDString(endDate);
		Dx4ResultJson result = new Dx4ResultJson();
		if (sd == null || ed ==null)
		{
			log.error("invalid date string - format should be yyyy-mm-dd");
			result.fail("invalid date string - format should be yyyy-mm-dd");
			return result;
		}
		
		result.success(restServices.getNumberRevenues(sd, ed));
		return result;
	}
	
	@Override
	@RequestMapping(value = "/numberRevenues")
	public List<Dx4NumberRevenueJson> getNumberRevenues(@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate){
		
		return restServices.getNumberRevenues(startDate, endDate);
	}
	
	@Override
	@RequestMapping(value = "/sameDayDraws")
	public List<Date> getExternalSameDayGameDates()
	{
		return restServices.getExternalSameDayGameDates();
	}
}
