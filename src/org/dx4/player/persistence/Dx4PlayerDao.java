package org.dx4.player.persistence;

import java.util.List;
import java.util.UUID;

import org.dx4.json.message.Dx4FavouriteJson;
import org.dx4.player.Dx4Player;

public interface Dx4PlayerDao 
{
	public void store(Dx4Player player);
	public Dx4Player getByUsername(String username);
	public Dx4Player getPlayerById(long id);
	public List<Dx4Player> getPlayersByPhone(String phone);
	
	public void storeFavourite(Dx4FavouriteJson favourite,long betId);
	public void deleteFavourite(long favouriteId);
	public List<Dx4FavouriteJson> getFavourites(UUID playerId);
	
}
