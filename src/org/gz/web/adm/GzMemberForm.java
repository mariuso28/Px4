package org.gz.web.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gz.baseuser.GzRole;

public class GzMemberForm implements Serializable {

	private static final long serialVersionUID = -9110917883160354671L;
	private GzMemberCommand command;
	private GzMemberCommand inCompleteCommand;
	private boolean adminOnly;
	private String errMsg;
	private String infoMsg;
	private List<GzRole> roles; 
	private Map<String,GzMemberSummary> members = new  TreeMap<String,GzMemberSummary>();
	private List<GzMemberSummary> flatMembers = new ArrayList<GzMemberSummary>();
	private List<GzMemberSummary> chooseMembers = new ArrayList<GzMemberSummary>();
	private GzMemberSummary memberSummary;
	private double maxCredit;
	
	
	public GzMemberForm()
	{
	}

	public GzMemberCommand getCommand() {
		return command;
	}

	public void setCommand(GzMemberCommand command) {
		this.command = command;
	}

	public GzMemberCommand getInCompleteCommand() {
		return inCompleteCommand;
	}

	public void setInCompleteCommand(GzMemberCommand inCompleteCommand) {
		this.inCompleteCommand = inCompleteCommand;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public boolean isAdminOnly() {
		return adminOnly;
	}

	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	public GzMemberSummary getMemberSummary() {
		return memberSummary;
	}

	public void setMemberSummary(GzMemberSummary memberSummary) {
		this.memberSummary = memberSummary;
	}

	public List<GzRole> getRoles() {
		return roles;
	}

	public void setRoles(List<GzRole> roles) {
		this.roles = roles;
	}

	public List<GzMemberSummary> getFlatMembers() {
		return flatMembers;
	}

	public void setFlatMembers(List<GzMemberSummary> flatMembers) {
		this.flatMembers = flatMembers;
	}

	public double getMaxCredit() {
		return maxCredit;
	}

	public void setMaxCredit(double maxCredit) {
		this.maxCredit = maxCredit;
	}

	public List<GzMemberSummary> getChooseMembers() {
		return chooseMembers;
	}

	public void setChooseMembers(List<GzMemberSummary> chooseMembers) {
		this.chooseMembers = chooseMembers;
	}

	public Map<String,GzMemberSummary> getMembers() {
		return members;
	}

	public void setMembers(Map<String,GzMemberSummary> members) {
		this.members = members;
	}

}
