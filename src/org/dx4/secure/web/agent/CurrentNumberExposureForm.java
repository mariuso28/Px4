package org.dx4.secure.web.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBetExpo;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.bet.persistence.MetaBetExpoRowMapperPaginated;
import org.dx4.home.Dx4Home;
import org.springframework.ui.ModelMap;

public class CurrentNumberExposureForm 
{
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CurrentNumberExposureForm.class);
	
	private List<Dx4MetaBetExpo> metaBetExpos;
	private List<Dx4MetaBetExpo> metaBetExposForChoice;
	private List<Dx4MetaBetExpo> metaBetExposForCode;
	private List<String> headers;
	private List<Integer> pos;
	private String sortedBy;
	
	public CurrentNumberExposureForm(Dx4Home dx4Home,
			ModelMap model) {
		
//		Dx4Agent agent = (Dx4Agent) model.get("currUser");
		
		MetaBetExpoRowMapperPaginated currMetaBetExpoPg = (MetaBetExpoRowMapperPaginated) model.get("currMetaBetExpoPg");
		Integer currPage0 = (Integer) model.get("currPage0");
		if (currPage0<0)
			metaBetExpos = currMetaBetExpoPg.getNextPage();
		else
			metaBetExpos = currMetaBetExpoPg.getPage(currPage0); 	
		model.addAttribute("currPage0", currMetaBetExpoPg.getCurrentPage());
		
		List<Dx4MetaBetExpoOrder> ordering = currMetaBetExpoPg.getOrdering();
		headers = new ArrayList<String>();
		pos = new ArrayList<Integer>();
		headers.add(Dx4MetaBetExpoOrder.choice.getDesc());
		pos.add(ordering.indexOf(Dx4MetaBetExpoOrder.choice));
		headers.add(Dx4MetaBetExpoOrder.code.getDesc());
		pos.add(ordering.indexOf(Dx4MetaBetExpoOrder.code));
		headers.add(Dx4MetaBetExpoOrder.tbet.getDesc());
		pos.add(ordering.indexOf(Dx4MetaBetExpoOrder.tbet));
		
		sortedBy = "";
		for (Dx4MetaBetExpoOrder order : ordering)
		{
			sortedBy += order.getDesc() + ",";
		}
		sortedBy = sortedBy.substring(0,sortedBy.length()-1);
		
		Integer currPage1 = (Integer) model.get("currPage1");
		MetaBetExpoRowMapperPaginated currMetaBetExpoForCode;
		if (currPage1<0)
		{
			currMetaBetExpoForCode = initialiseCurrMetaBetExpoPgCode(dx4Home,model,currMetaBetExpoPg);
			metaBetExposForCode = currMetaBetExpoForCode.getNextPage();
		}
		else
		{
			currMetaBetExpoForCode = (MetaBetExpoRowMapperPaginated) model.get("currMetaBetExposForCode");
			metaBetExposForCode = currMetaBetExpoForCode.getPage(currPage1); 	
		}
		model.addAttribute("currPage1", currMetaBetExpoForCode.getCurrentPage());
		
		Integer currPage2 = (Integer) model.get("currPage2");
		MetaBetExpoRowMapperPaginated currMetaBetExpoForChoice;
		if (currPage2<0)
		{
			currMetaBetExpoForChoice = initialiseCurrMetaBetExpoPgChoice(dx4Home,model,currMetaBetExpoPg);
			metaBetExposForChoice = currMetaBetExpoForChoice.getNextPage();
		}
		else
		{
			currMetaBetExpoForChoice = (MetaBetExpoRowMapperPaginated) model.get("currMetaBetExposForChoice");
			metaBetExposForChoice = currMetaBetExpoForChoice.getPage(currPage2); 	
		}
		model.addAttribute("currPage2", currMetaBetExpoForChoice.getCurrentPage());
		
		
	}
	
	private MetaBetExpoRowMapperPaginated initialiseCurrMetaBetExpoPgCode(Dx4Home dx4Home,ModelMap model,MetaBetExpoRowMapperPaginated currMetaBetExpoPg)
	{
		List<Dx4MetaBetExpoOrder> newOrdering = new ArrayList<Dx4MetaBetExpoOrder>();
		newOrdering.add(Dx4MetaBetExpoOrder.tbet);
		newOrdering.add(Dx4MetaBetExpoOrder.code);
		MetaBetExpoRowMapperPaginated metaBetExposForCode = dx4Home.getMetaBetExpoRowMapperPaginated(currMetaBetExpoPg.getAgent(), currMetaBetExpoPg.getPlayGame(), newOrdering,24);	
		model.addAttribute("currMetaBetExposForCode", metaBetExposForCode);
		return metaBetExposForCode;
	}
	
	private MetaBetExpoRowMapperPaginated initialiseCurrMetaBetExpoPgChoice(Dx4Home dx4Home,ModelMap model,MetaBetExpoRowMapperPaginated currMetaBetExpoPg)
	{	
		List<Dx4MetaBetExpoOrder> newOrdering = new ArrayList<Dx4MetaBetExpoOrder>();
		newOrdering.add(Dx4MetaBetExpoOrder.tbet);
		newOrdering.add(Dx4MetaBetExpoOrder.choice);
		MetaBetExpoRowMapperPaginated  metaBetExposForChoice = dx4Home.getMetaBetExpoRowMapperPaginated(currMetaBetExpoPg.getAgent(), currMetaBetExpoPg.getPlayGame(), newOrdering,24);
		model.addAttribute("currMetaBetExposForChoice", metaBetExposForChoice);
		return metaBetExposForChoice;
	}
	
	public void setMetaBetExpos(List<Dx4MetaBetExpo> metaBetExpos) {
		this.metaBetExpos = metaBetExpos;
	}

	public List<Dx4MetaBetExpo> getMetaBetExpos() {
		return metaBetExpos;
	}

	public List<Dx4MetaBetExpo> getMetaBetExposForChoice() {
		return metaBetExposForChoice;
	}

	public void setMetaBetExposForChoice(List<Dx4MetaBetExpo> metaBetExposForChoice) {
		this.metaBetExposForChoice = metaBetExposForChoice;
	}

	public List<Dx4MetaBetExpo> getMetaBetExposForCode() {
		return metaBetExposForCode;
	}

	public void setMetaBetExposForCode(List<Dx4MetaBetExpo> metaBetExposForCode) {
		this.metaBetExposForCode = metaBetExposForCode;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<Integer> getPos() {
		return pos;
	}

	public void setPos(List<Integer> pos) {
		this.pos = pos;
	}

	public void setSortedBy(String sortedBy) {
		this.sortedBy = sortedBy;
	}

	public String getSortedBy() {
		return sortedBy;
	}
	
	
	
	
}
