package org.dx4.external.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExternalComingDate {
	
	private Date date;
	private List<String> providers;
	
	public ExternalComingDate()
	{
		setProviders(new ArrayList<String>());
	}

	public String getProviderCodes()
	{
		String pc = "";
		for (String provider : providers)
		{
			if (provider.toLowerCase().contains("magnum"))
				pc += "M";
			else
			if (provider.toLowerCase().contains("damacai"))
				pc += "K";
			else
			if (provider.toLowerCase().contains("toto"))
				pc += "T";
			else
			if (provider.toLowerCase().contains("singapore 4d"))
				pc += "S";
			else
			if (provider.toLowerCase().contains("sabah 88"))
				pc += "8";
			else
			if (provider.toLowerCase().contains("sandakan"))
				pc += "D";
			else
			if (provider.toLowerCase().contains("cash sweep"))
				pc += "C";
		}
		return pc;
	}
	
	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public List<String> getProviders() {
		return providers;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "ExternalComingDate [date=" + date + ", providers=" + providers
				+ "]";
	}

}
