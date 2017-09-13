package org.dx4.agent.persistence;

import org.dx4.agent.Dx4Agent;
import org.dx4.home.persistence.Dx4PersistenceException;

public interface Dx4AgentDao{

	public void store(Dx4Agent agent);
	public Dx4Agent getAgentByEmail(String email) throws Dx4PersistenceException; 
	public Dx4Agent getAgentByCode(String code) throws Dx4PersistenceException;
}
