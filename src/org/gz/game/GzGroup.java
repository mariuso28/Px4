package org.gz.game;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import org.gz.baseuser.GzBaseUser;

public class GzGroup {

	private long id;
	private String name;
	private String description;
	private Date created;
	private String memberId;
	private GzBaseUser member;
	private Map<String,GzPackage> packages = new TreeMap<String,GzPackage>();
	
	public GzGroup()
	{
		setCreated((new GregorianCalendar()).getTime());
	}
	
	public GzGroup(String name,String desc,GzBaseUser member)
	{
		this();
		setName(name);
		setDescription(desc);
		setMember(member);
		setMemberId(member.getMemberId());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	public Map<String,GzPackage> getPackages() {
		return packages;
	}

	public void setPackages(Map<String,GzPackage> packages) {
		this.packages = packages;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public GzBaseUser getMember() {
		return member;
	}


	public void setMember(GzBaseUser member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "GzGroup [id=" + id + ", name=" + name + ", desc=" + description + ", created=" + created + ", memberId="
				+ memberId + ", member=" + member + ", packages=" + packages + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
