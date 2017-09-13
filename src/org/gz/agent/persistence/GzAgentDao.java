package org.gz.agent.persistence;

import org.gz.agent.GzAgent;
import org.gz.home.persistence.GzPersistenceException;

public interface GzAgentDao{

	public void store(GzAgent agent) throws GzPersistenceException;
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException;
	public GzAgent getAgentByCode(String code) throws GzPersistenceException;
	
}
