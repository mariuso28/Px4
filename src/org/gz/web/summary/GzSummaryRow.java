package org.gz.web.summary;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.gz.baseuser.GzBaseUser;
import org.gz.json.GzGameType;

public class GzSummaryRow {

	private UUID id;
	private GzBaseUser member;
	private Map<Integer,GzSummaryEntry> entries;
	private Map<String,GzSummaryRow> subMembers = new TreeMap<String,GzSummaryRow>();
	private boolean expanded;
	
	public GzSummaryRow()
	{		
		setId(UUID.randomUUID());
		entries = new TreeMap<Integer,GzSummaryEntry>();
		for (GzGameType gt : GzGameType.values())
			entries.put(gt.getIndex(), new GzSummaryEntry());
	}
	
	public GzSummaryRow(GzBaseUser member)
	{	
		this();
		setMember(member);
	}

	public GzBaseUser getMember() {
		return member;
	}

	public void setMember(GzBaseUser member) {
		this.member = member;
	}

	public Map<Integer, GzSummaryEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<Integer, GzSummaryEntry> entries) {
		this.entries = entries;
	}

	public Map<String,GzSummaryRow> getSubMembers() {
		return subMembers;
	}

	public void setSubMembers(Map<String,GzSummaryRow> subMembers) {
		this.subMembers = subMembers;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	
}
