package org.dx4.bet.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.game.Dx4MetaGame;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.player.Dx4Player;
import org.dx4.utils.Util;
import org.dx4.utils.persistence.PageRowCallbackHandler;

public class MetaBetRowMapperPaginated
{
	private static final Logger log = Logger.getLogger(MetaBetRowMapperPaginated.class);
	
	private int count;
	private int currentPage;
	private int lastPage;
	private int pageSize;
	private Dx4MetaBetDao metaBetDao;
	private Dx4Player player;
	private Dx4BetRetrieverFlag flag;
	private Dx4MetaGame metaGame;
	
	public MetaBetRowMapperPaginated(Dx4MetaBetDao metaBetDao, int pageSize, Dx4Player player, Dx4BetRetrieverFlag flag, Dx4MetaGame metaGame )
	{
		
		setPlayer(player);
		setFlag(flag);
		setMetaGame(metaGame);
		this.metaBetDao = metaBetDao;
		
		count = metaBetDao.getMetaBetsForPlayerCount(player,flag,metaGame); 
		
		currentPage = 0;
		this.pageSize = pageSize;
		lastPage = count/pageSize;
		if (count%pageSize>0)
			lastPage++;
		
		log.trace("Created : " + this.toString());
	}
	
	public List<Dx4MetaBet> getNextPage()
	{
		currentPage++;
		return Util.toList(getPage());
	}

	public List<Dx4MetaBet> getPrevPage()
	{
		if (currentPage>0)
			currentPage--;
		return Util.toList(getPage());
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public List<Dx4MetaBet> getPage(int page)
	{
		currentPage=page;
		return Util.toList(getPage());
	}
	
	private Iterable<Dx4MetaBet> getPage()
	{
		int endIndex = currentPage * pageSize;
		int startIndex = endIndex - pageSize;
		endIndex--;
		if (endIndex > count)
			endIndex = count;
		
		log.trace("Getting page : " + currentPage + " startIndex : " + startIndex + " endIndex: " + endIndex );
		
		Dx4MetaBetRowMapper mapper = new Dx4MetaBetRowMapper();
		PageRowCallbackHandler<Dx4MetaBet> handler = 
					new PageRowCallbackHandler<Dx4MetaBet>( startIndex, endIndex, mapper );
		
		String sql = JdbcMetaBetDaoImpl.getMetaBetsForPlayerString(player,flag,metaGame,false); 
		
		JdbcMetaBetDaoImpl metaBetDaoImpl = (JdbcMetaBetDaoImpl) metaBetDao;
		metaBetDaoImpl.getJdbcTemplate().query(sql,handler);
		Iterable<Dx4MetaBet> results = handler.getResults();
		for (Dx4MetaBet metaBet : results)
		{
			metaBetDaoImpl.populateMetaBet(metaBet);
			metaBet.setMetaGame(metaGame);
			for (Dx4Bet bet : metaBet.getBets())
				bet.setGame(metaGame.getGameById(bet.getGameId()));
		}
		return results;	
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public Dx4MetaBetDao getMetaBetDao() {
		return metaBetDao;
	}

	public void setMetaBetDao(Dx4MetaBetDao metaBetDao) {
		this.metaBetDao = metaBetDao;
	}

	public Dx4Player getPlayer() {
		return player;
	}

	public void setPlayer(Dx4Player player) {
		this.player = player;
	}

	public Dx4BetRetrieverFlag getFlag() {
		return flag;
	}

	public void setFlag(Dx4BetRetrieverFlag flag) {
		this.flag = flag;
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}
	
}
