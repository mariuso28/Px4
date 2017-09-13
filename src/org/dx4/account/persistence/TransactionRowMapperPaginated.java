package org.dx4.account.persistence;


import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.utils.Util;
import org.dx4.utils.persistence.PageRowCallbackHandler;


public class TransactionRowMapperPaginated extends TransactionRowMapper
{
	private static final Logger log = Logger.getLogger(TransactionRowMapperPaginated.class);
	
	private int count;
	private int currentPage;
	private int lastPage;
	private int pageSize;
	private Dx4AccountDao accountDao;
	private long userId;
	
	public TransactionRowMapperPaginated(Dx4AccountDao accountDao, long userId, int pageSize )
	{
		this.accountDao = accountDao;
		count = accountDao.getAccountActivityDetailsCount( userId );
		currentPage = 0;
		this.userId = userId;
		this.pageSize = pageSize;
		lastPage = count/pageSize;
		if (count%pageSize>0)
			lastPage++;
		log.info("Created : " + this.toString());
	}
	
	
	public List<Dx4Transaction> getNextPage()
	{
		if (currentPage<lastPage)		
			currentPage++;
		return Util.toList(getPage());
	}

	public List<Dx4Transaction> getPrevPage()
	{
		if (currentPage>0)
			currentPage--;
		return Util.toList(getPage());
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public List<Dx4Transaction> getPage(int page)
	{
		currentPage=page;
		return Util.toList(getPage());
	}
	
	private Iterable<Dx4Transaction> getPage()
	{
		int endIndex = currentPage * pageSize;
		int startIndex = endIndex - pageSize;
		endIndex--;
		if (endIndex > count)
			endIndex = count;
		
		log.info("Getting page : " + currentPage + " startIndex : " + startIndex + " endIndex: " + endIndex );
		
		TransactionRowMapper mapper = new TransactionRowMapper();
		PageRowCallbackHandler<Dx4Transaction> handler = 
					new PageRowCallbackHandler<Dx4Transaction>( startIndex, endIndex, mapper );
		
		String sql = "SELECT * FROM XTRANSACTION WHERE USERID=" + userId + " ORDER BY ID DESC"; 
	
		log.info(sql);
		JdbcDx4AccountDaoImpl accountDaoImpl = (JdbcDx4AccountDaoImpl) accountDao;
		accountDaoImpl.getJdbcTemplate().query(sql,handler);
		return handler.getResults();	
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


	
}

