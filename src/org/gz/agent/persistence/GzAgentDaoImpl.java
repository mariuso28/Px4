package org.gz.agent.persistence;

import org.apache.log4j.Logger;

import org.gz.agent.GzAgent;
import org.gz.baseuser.GzRole;
import org.gz.baseuser.persistence.GzBaseUserDaoImpl;
import org.gz.home.persistence.GzPersistenceException;


public class GzAgentDaoImpl extends GzBaseUserDaoImpl implements GzAgentDao 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAgentDaoImpl.class);

	@Override
	public void store(GzAgent agent) throws GzPersistenceException
	{
		super.storeBaseUser(agent);
	}
		
	@Override
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException {
		
		String code = super.getCodeForEmail(email);
		@SuppressWarnings("rawtypes")
		Class clazz = GzRole.getRoleClassForCode(code);
		GzAgent agent = (GzAgent) super.getBaseUserByEmail(email,clazz);
		if (agent == null)
			return null;
		
		super.getDownstreamForParent(agent);
		return agent;
	}

	@Override
	public GzAgent getAgentByCode(String code) throws GzPersistenceException {
		
		GzAgent agent = (GzAgent) getBaseUserByCode(code);
		return agent;
	}
}
