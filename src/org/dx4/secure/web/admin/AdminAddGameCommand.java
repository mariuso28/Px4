package org.dx4.secure.web.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.game.Dx4MetaGame;

public class AdminAddGameCommand implements Serializable{

	private static final long serialVersionUID = -206190295367616757L;
	private Dx4MetaGame metaGame;
	private List<String> useProviders;
	
	public AdminAddGameCommand()
	{
		metaGame = new Dx4MetaGame();
		useProviders = new ArrayList<String>();
	}


	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}


	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setUseProviders(List<String> useProviders) {
		this.useProviders = useProviders;
	}


	public List<String> getUseProviders() {
		return useProviders;
	}


	@Override
	public String toString() {
		return "AdminAddGameCommand [metaGame=" + metaGame + "]";
	}



	
}
