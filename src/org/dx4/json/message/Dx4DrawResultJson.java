package org.dx4.json.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dx4.game.payout.Dx4PayOut;

public class Dx4DrawResultJson
{
	private long id;
	private Dx4ProviderJson provider;
	private long providerId;
	private String drawNo;
	private Date date;
	private String firstPlace;
	private String secondPlace;
	private String thirdPlace;
	private String firstDesc;
	private String secondDesc;
	private String thirdDesc;
	private String firstDescCh;
	private String secondDescCh;
	private String thirdDescCh;
	private String firstImage;
	private String secondImage;
	private String thirdImage;
	private List<String> specials;
	private List<String> consolations;
	
	
	private Dx4DrawResultIboxJson drawResultIboxJson;
	
	public Dx4DrawResultJson()
	{	
	}
	
	public Dx4DrawResultJson(Dx4ProviderJson provider)
	{
		this();
		setProvider(provider);
	}
	
	public List<Dx4PayOut> getMatchingPayOuts(List<Dx4PayOut> payOuts,String choice)
	{
		List<Dx4PayOut> matchedPayOuts = new ArrayList<Dx4PayOut>();
		for (Dx4PayOut payOut : payOuts)
		{
			if (payOut.getType().equals(Dx4PayOutTypeJson.First))
			{
				if (firstPlace.endsWith(choice))
					matchedPayOuts.add(payOut);
			}
			else
			if (payOut.getType().equals(Dx4PayOutTypeJson.Second))
			{
				if (secondPlace.endsWith(choice))
					matchedPayOuts.add(payOut);
			}
			else
			if (payOut.getType().equals(Dx4PayOutTypeJson.Third))
			{
				if (thirdPlace.endsWith(choice))
					matchedPayOuts.add(payOut);
			}
			else
			if (payOut.getType().equals(Dx4PayOutTypeJson.Spec))
			{
				for (String number : specials)
				{
					if (number.endsWith(choice))
						matchedPayOuts.add(payOut);
				}
			}
			else
			if (payOut.getType().equals(Dx4PayOutTypeJson.Cons))
			{
				for (String number : consolations)
				{
					if (number.endsWith(choice))
						matchedPayOuts.add(payOut);
				}
			}
		}
		return matchedPayOuts;
	}
	
	public void setProviderId(long providerId) {
		this.providerId = providerId;
	}

	public long getProviderId() {
		return providerId;
	}

	public Dx4ProviderJson getProvider() {
		return provider;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	public String getDrawNo() {
		return drawNo;
	}
	
	public String getFirstPlace() {
		return firstPlace;
	}

	public void setFirstPlace(String firstPlace) {
		this.firstPlace = firstPlace;
	}

	public String getSecondPlace() {
		return secondPlace;
	}

	public void setSecondPlace(String secondPlace) {
		this.secondPlace = secondPlace;
	}

	public String getThirdPlace() {
		return thirdPlace;
	}

	public void setThirdPlace(String thirdPlace) {
		this.thirdPlace = thirdPlace;
	}

	public List<String> getSpecials() {
		return specials;
	}

	public void setSpecials(List<String> specials) {
		this.specials = specials;
	}

	public List<String> getConsolations() {
		return consolations;
	}

	public void setConsolations(List<String> consolations) {
		this.consolations = consolations;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public String getFirstDesc() {
		return firstDesc;
	}

	public void setFirstDesc(String firstDesc) {
		this.firstDesc = firstDesc;
	}

	public String getSecondDesc() {
		return secondDesc;
	}

	public void setSecondDesc(String secondDesc) {
		this.secondDesc = secondDesc;
	}

	public String getThirdDesc() {
		return thirdDesc;
	}

	public void setThirdDesc(String thirdDesc) {
		this.thirdDesc = thirdDesc;
	}

	public String getFirstDescCh() {
		return firstDescCh;
	}

	public void setFirstDescCh(String firstDescCh) {
		this.firstDescCh = firstDescCh;
	}

	public String getSecondDescCh() {
		return secondDescCh;
	}

	public void setSecondDescCh(String secondDescCh) {
		this.secondDescCh = secondDescCh;
	}

	public String getThirdDescCh() {
		return thirdDescCh;
	}

	public void setThirdDescCh(String thirdDescCh) {
		this.thirdDescCh = thirdDescCh;
	}

	public void setProvider(Dx4ProviderJson provider) {
		this.provider = provider;
	}
	
	public String getFirstImage() {
		return firstImage;
	}

	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}

	public String getSecondImage() {
		return secondImage;
	}

	public void setSecondImage(String secondImage) {
		this.secondImage = secondImage;
	}

	public String getThirdImage() {
		return thirdImage;
	}

	public void setThirdImage(String thirdImage) {
		this.thirdImage = thirdImage;
	}

	@Override
	public String toString() {
		return "DrawResult [id=" + id + ", provider=" + provider
				+ ", providerId=" + providerId + ", drawNo=" + drawNo
				+ ", date=" + date + ", firstPlace=" + firstPlace
				+ ", secondPlace=" + secondPlace + ", thirdPlace=" + thirdPlace
				+ ", firstDesc=" + firstDesc + ", secondDesc=" + secondDesc
				+ ", thirdDesc=" + thirdDesc + ", specials=" + specials
				+ ", consolations=" + consolations + "]";
	}

	public Dx4DrawResultIboxJson getDrawResultIboxJson() {
		return drawResultIboxJson;
	}

	public void setDrawResultIboxJson(Dx4DrawResultIboxJson drawResultIboxJson) {
		this.drawResultIboxJson = drawResultIboxJson;
	}


	
}
