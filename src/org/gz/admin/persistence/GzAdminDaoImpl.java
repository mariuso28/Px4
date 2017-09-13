package org.gz.admin.persistence;

import org.apache.log4j.Logger;

import org.gz.admin.GzAdmin;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.baseuser.persistence.GzBaseUserDaoImpl;
import org.gz.home.persistence.GzPersistenceException;


public class GzAdminDaoImpl extends GzBaseUserDaoImpl implements GzAdminDao 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAdminDaoImpl.class);

	@Override
	public void store(GzAdmin agent) throws GzPersistenceException
	{
		super.storeBaseUser(agent);
	}
		
	@Override
	public GzAdmin getAdminByMemberId(String memberId) throws GzPersistenceException {
		
		GzBaseUser bu = (GzBaseUser) super.getBaseUserByMemberId(memberId);
		if (bu == null || !bu.getRole().equals(GzRole.ROLE_GZADMIN))
			return null;
		
		GzAdmin admin = (GzAdmin) bu;
		super.getDownstreamForParent(admin);
		return admin;
	}

	@Override
	public GzAdmin getAdminByCode(String code) throws GzPersistenceException {
		
		GzAdmin admin = (GzAdmin) getBaseUserByCode(code);
		return admin;
	}
}
