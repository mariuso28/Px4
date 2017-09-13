package org.gz.admin.persistence;

import org.gz.admin.GzAdmin;
import org.gz.home.persistence.GzPersistenceException;

public interface GzAdminDao{

	public void store(GzAdmin agent) throws GzPersistenceException;
	public GzAdmin getAdminByMemberId(String memberId) throws GzPersistenceException;
	public GzAdmin getAdminByCode(String code) throws GzPersistenceException;
	
}
