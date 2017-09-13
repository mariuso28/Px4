package org.dx4.secure.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dx4.account.Dx4Account;
import org.dx4.game.Dx4GameGroup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Dx4SecureUser implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1325020168810495143L;
	private UUID id;
	private String parentCode;
	private String contact;
	private String email;
	private String phone;
	private String password;
	private String nickname;
	private Dx4Account account; 
	private String code;
	private boolean enabled;
	private Dx4Role role;
	private List<Dx4Role> authorities = new ArrayList<Dx4Role>();
	private String session;
	private List<Dx4SecureUser> members  = new ArrayList<Dx4SecureUser>();
	private String icon;
	private Dx4SecureUser parent;
	private boolean systemMember;						// house bankers etc.
	private long leafInstance;
	private long seqId;
	private Dx4GameGroup gameGroup;
	
	public Dx4SecureUser()
	{
		
	}
	
	public Dx4SecureUser(String email)
	{
		setEmail(email);
		setEnabled(true);
	}
	
	public void copyProfileValues(Dx4Profile profile)
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		setPassword(encoder.encode(profile.getPassword()));
		setContact(profile.getContact());
		setNickname(profile.getNickname());
		setIcon(profile.getIcon());
		setPhone(profile.getPhone());
	}
	
	public Dx4Profile createProfile()
	{
		Dx4Profile profile = new Dx4Profile();
		profile.setNickname(getNickname());
		profile.setPhone(getPhone());
		profile.setContact(getContact());
		profile.setEmail(getEmail());
		profile.setIcon(getIcon());
		return profile;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Dx4SecureUser bu = (Dx4SecureUser) obj;
		return this.getId().equals(bu.getId());
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public List<Dx4SecureUser> getMembers() {
		return members;
	}

	public void setMembers(List<Dx4SecureUser> members) {
		this.members = members;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Dx4SecureUser getParent() {
		return parent;
	}

	public void setParent(Dx4SecureUser parent) {
		this.parent = parent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public boolean isSystemMember() {
		return systemMember;
	}

	public void setSystemMember(boolean systemMember) {
		this.systemMember = systemMember;
	}

	public long getLeafInstance() {
		return leafInstance;
	}

	public void setLeafInstance(long leafInstance) {
		this.leafInstance = leafInstance;
	}

	public Dx4Account getAccount() {
		return account;
	}

	public void setAccount(Dx4Account account) {
		this.account = account;
	}

	public Dx4Role getRole() {
		return role;
	}

	public void setRole(Dx4Role role) {
		this.role = role;
	}

	public List<Dx4Role> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Dx4Role> authorities) {
		this.authorities = authorities;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public Dx4GameGroup getGameGroup() {
		return gameGroup;
	}

	public void setGameGroup(Dx4GameGroup gameGroup) {
		this.gameGroup = gameGroup;
	}

}
