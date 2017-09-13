package org.dx4.secure.web.agent;

import java.io.Serializable;

import org.dx4.secure.web.member.UserLogonForm;

public class AgentLogonForm extends UserLogonForm implements Serializable{

	private static final long serialVersionUID = 6045513668844808119L;
	AgentLogon agentLogon;
	
	public AgentLogon getAgentLogon() {
		return (AgentLogon) getUserLogon();
	}
	public void setAgentLogon(AgentLogon agentLogon) {
		setUserLogon(agentLogon);
	}
}

