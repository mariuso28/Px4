package org.gz.web.adm;

import java.io.Serializable;

import org.gz.baseuser.GzProfile;
import org.gz.baseuser.GzRole;

public class GzMemberCommand implements Serializable {

	private static final long serialVersionUID = 6773672023960627190L;
	private GzProfile profile;
	private String vPassword;
	private GzRole role;
	private boolean enabled;
	private String credit;
	private String betCommission;
	private String winCommission;
	private String memberToChangeCode;
	private String search;
	
	public GzMemberCommand()
	{
	}
	
	public String getvPassword() {
		return vPassword;
	}

	public void setvPassword(String vPassword) {
		this.vPassword = vPassword;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBetCommission() {
		return betCommission;
	}

	public void setBetCommission(String betCommission) {
		this.betCommission = betCommission;
	}

	public String getWinCommission() {
		return winCommission;
	}

	public void setWinCommission(String winCommission) {
		this.winCommission = winCommission;
	}

	public GzProfile getProfile() {
		return profile;
	}

	public void setProfile(GzProfile profile) {
		this.profile = profile;
	}

	public String getMemberToChangeCode() {
		return memberToChangeCode;
	}

	public void setMemberToChangeCode(String memberToChangeCode) {
		this.memberToChangeCode = memberToChangeCode;
	}

	public GzRole getRole() {
		return role;
	}

	public void setRole(GzRole role) {
		this.role = role;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}


}
