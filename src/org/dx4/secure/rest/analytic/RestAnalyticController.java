package org.dx4.secure.rest.analytic;

import java.util.Date;
import java.util.List;

import org.dx4.json.message.Dx4DrawDateRangeJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.dx4.json.message.Dx4NumberViewJson;
import org.dx4.json.message.Dx4ResultJson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RestAnalyticController {

	@RequestMapping(value = "/drawsByDateStr")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByDateStr(@RequestParam("date") String date);
	
	@RequestMapping(value = "/drawsByDate")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByDate(@RequestParam("date") Date date);
	
	@RequestMapping(value = "/drawsByLatest")
	// Dx4ResultJson contains List<Dx4DrawJson> if success message if fail
	public Dx4ResultJson drawsByLatest();
	
	@RequestMapping(value = "/drawsDateRange")
	public Dx4DrawDateRangeJson drawsDateRange();
	
	@RequestMapping(value = "/numberSearch")
	public List<Dx4NumberPageElementJson> numberSearch(@RequestParam("searchTerm") String searchTerm,
					@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate, @RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberSearchStr")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success, message if fail
	public Dx4ResultJson numberSearchStr(@RequestParam("searchTerm") String searchTerm,
			@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate, @RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberSearchAll")
	// Dx4ResultJson contains List<Dx4NumberPageElementJson> if success, message if fail
	public Dx4ResultJson numberSearchAll(@RequestParam("searchTerm") String searchTerm,@RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberViewStr")
	// Dx4ResultJson contains Dx4NumberViewJson if success, message if fail 
	public Dx4ResultJson numberViewStr(@RequestParam("number") String number,
			@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate, @RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberViewStr")
	public Dx4NumberViewJson numberView(@RequestParam("number") String number,
			@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate, @RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberViewAll")
	// Dx4ResultJson contains List<Dx4NumberViewJson> if success, message if fail 
	public Dx4ResultJson numberViewAll(@RequestParam("number") String number, @RequestParam("dictionary") Character dictionary);
	
	@RequestMapping(value = "/numberRevenuesStr")
	// Dx4ResultJson contains List<Dx4NumberRevenueJson> if success, message if fail 
	public Dx4ResultJson getNumberRevenuesStr(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate);
	
	@RequestMapping(value = "/numberRevenues")
	public List<Dx4NumberRevenueJson> getNumberRevenues(@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate);
	
	@RequestMapping(value = "/sameDayDraws")
	public List<Date> getExternalSameDayGameDates();
}
