package org.dx4.agent.persistence;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.persistence.JdbcDx4SecureUserDaoImpl;

public class JdbcDx4AgentDaoImpl extends JdbcDx4SecureUserDaoImpl implements Dx4AgentDao 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(JdbcDx4AgentDaoImpl.class);

	@Override
	public void store(Dx4Agent agent)
	{
		try
		{
			super.storeBaseUser(agent);
		}
		catch (Dx4PersistenceException e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException("storing agent : ",e.getMessage());
		}
	}
		
	@Override
	public Dx4Agent getAgentByEmail(String email) throws Dx4PersistenceException {
		
		String code = super.getCodeForEmail(email);
		@SuppressWarnings("rawtypes")
		Class clazz = Dx4Role.getRoleClassForCode(code);
		Dx4Agent agent = (Dx4Agent) super.getBaseUserByEmail(email,clazz);
		if (agent == null)
			return null;
		
//		super.getDownstreamForParent(agent);
		return agent;
	}

	@Override
	public Dx4Agent getAgentByCode(String code) throws Dx4PersistenceException {
		
		Dx4Agent agent = (Dx4Agent) getBaseUserByCode(code);
		return agent;
	}
	
}
