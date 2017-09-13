package org.dx4.secure.web.admin;

import java.util.ArrayList;
import java.util.List;

import org.dx4.game.Dx4MetaGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4ProviderJson;



public class AdminAddGameForm 
{
	AdminAddGameCommand command;
	private String message;
	private Dx4GameTypeJson gameType;
	private List<String> providers;
	private List<Boolean> useProviders;
	
	public AdminAddGameForm()
	{
	}
	
	public AdminAddGameForm(Dx4Home dx4Home,Dx4MetaGame currMetaGame)
	{
		command = new AdminAddGameCommand();
		List<Dx4ProviderJson> providerList = dx4Home.getProviders();
		providers = new ArrayList<String>();
		for (Dx4ProviderJson provider : providerList)
			providers.add(provider.getName());
		useProviders = new ArrayList<Boolean>();
		for (Dx4ProviderJson provider : providerList)
		{
			if (currMetaGame.getProviders().contains(provider.getName()))
				useProviders.add(true);
			else
				useProviders.add(false);
		}
	}
	
	public AdminAddGameCommand getCommand() {
		return command;
	}

	public void setCommand(AdminAddGameCommand command) {
		this.command = command;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
		this.gameType = gameType;
	}

	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public List<String> getProviders() {
		return providers;
	}

	public void setUseProviders(List<Boolean> useProviders) {
		this.useProviders = useProviders;
	}

	public List<Boolean> getUseProviders() {
		return useProviders;
	}
	
}
