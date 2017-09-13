package org.dx4.bet.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4MetaBetExpo;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.game.Dx4PlayGame;
import org.dx4.utils.Util;
import org.dx4.utils.persistence.PageRowCallbackHandler;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class MetaBetExpoRowMapperPaginated
{
	private static final Logger log = Logger.getLogger(MetaBetExpoRowMapperPaginated.class);
	
	private int count;
	private int currentPage;
	private int lastPage;
	private int pageSize;
	private Dx4MetaBetDao metaBetDao;
	private Dx4PlayGame playGame;
	private Dx4Agent agent;
	private List<Dx4MetaBetExpoOrder> ordering;
	
	public MetaBetExpoRowMapperPaginated(Dx4MetaBetDao metaBetDao, int pageSize, Dx4PlayGame playGame,Dx4Agent agent,List<Dx4MetaBetExpoOrder> ordering )
	{
		
		setPlayGame(playGame);
		setAgent(agent);
		setOrdering(ordering);
		this.metaBetDao = metaBetDao;
		
		count = (int) metaBetDao.getDx4MetaBetExposCount(agent,playGame,ordering);
		
		currentPage = 0;
		this.pageSize = pageSize;
		lastPage = count/pageSize;
		if (count%pageSize>0)
			lastPage++;
		
		log.trace("Created : " + this.toString());
	}
	
	public List<Dx4MetaBetExpo> getNextPage()
	{
		currentPage++;
		return Util.toList(getPage());
	}

	public List<Dx4MetaBetExpo> getPrevPage()
	{
		if (currentPage>0)
			currentPage--;
		return Util.toList(getPage());
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public List<Dx4MetaBetExpo> getPage(int page)
	{
		currentPage=page;
		return Util.toList(getPage());
	}
	
	private Iterable<Dx4MetaBetExpo> getPage()
	{
		int endIndex = currentPage * pageSize;
		int startIndex = endIndex - pageSize;
		endIndex--;
		if (endIndex > count)
			endIndex = count;
		
		log.info("Getting page : " + currentPage + " startIndex : " + startIndex + " endIndex: " + endIndex );
		
		@SuppressWarnings("rawtypes")
		BeanPropertyRowMapper mapper = BeanPropertyRowMapper.newInstance(Dx4MetaBetExpo.class);
		@SuppressWarnings("unchecked")
		PageRowCallbackHandler<Dx4MetaBetExpo> handler = 
					new PageRowCallbackHandler<Dx4MetaBetExpo>( startIndex, endIndex, mapper );
		
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		String sql = Dx4MetaBetExposureSupport.getSqlString(playDate.toString(),agent.getRole(),agent.getCode(),ordering);
		
		JdbcMetaBetDaoImpl metaBetDaoImpl = (JdbcMetaBetDaoImpl) metaBetDao;
		metaBetDaoImpl.getJdbcTemplate().query(sql,handler);
		Iterable<Dx4MetaBetExpo> results = handler.getResults();
		
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

	public Dx4PlayGame getPlayGame() {
		return playGame;
	}

	public void setPlayGame(Dx4PlayGame playGame) {
		this.playGame = playGame;
	}

	public Dx4Agent getAgent() {
		return agent;
	}

	public void setAgent(Dx4Agent agent) {
		this.agent = agent;
	}

	public List<Dx4MetaBetExpoOrder> getOrdering() {
		return ordering;
	}

	public void setOrdering(List<Dx4MetaBetExpoOrder> ordering) {
		this.ordering = ordering;
	}

	
}
