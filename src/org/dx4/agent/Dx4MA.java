package org.dx4.agent;

import java.util.List;

public class Dx4MA extends Dx4Agent 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4106857975191630484L;
	private List<Dx4Agent> agents;
	
	
	public void setAgents(List<Dx4Agent> agents) {
		this.agents = agents;
	}
	public List<Dx4Agent> getAgents() {
		return agents;
	}
}
