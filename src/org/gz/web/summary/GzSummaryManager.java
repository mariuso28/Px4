package org.gz.web.summary;

import java.util.List;
import java.util.UUID;

import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;

public class GzSummaryManager {

	public GzSummaryManager()
	{
	}
	
	public GzSummaryMembers createGzSummaryMembers(GzHome home,GzBaseUser superior) throws GzPersistenceException
	{
		List<GzSummaryEntry> entries = home.getSummaryEntries(superior);
		GzSummaryMembers gms = new GzSummaryMembers(superior);
		for (GzSummaryEntry entry : entries)
		{
			GzSummaryRow sr = gms.getRows().get(entry.getMemberId());
			if (sr == null)
			{
				GzBaseUser bu = home.getBaseUserByMemberId(entry.getMemberId());
				sr = new GzSummaryRow(bu);
				gms.getRowMap().put(sr.getId(), sr);
				gms.getRows().put(entry.getMemberId(), sr);
			}
			sr.getEntries().put(entry.getGameType().getIndex(), entry);
		}
		
		return gms;
	}
	
	public void collapseRow(GzHome home,GzSummaryMembers gms,String rowId) throws GzPersistenceException
	{
		UUID uId = UUID.fromString(rowId);
		GzSummaryRow sr = gms.getRowMap().get(uId);
		if (sr==null)
			return;
		sr.setExpanded(false);
		sr.getSubMembers().clear();
	}
	
	public void expandRow(GzHome home,GzSummaryMembers gms,String rowId) throws GzPersistenceException
	{
		UUID uId = UUID.fromString(rowId);
		GzSummaryRow sr = gms.getRowMap().get(uId);
		if (sr==null)
			return;
		sr.setExpanded(true);
		List<GzSummaryEntry> entries = home.getSummaryEntries(sr.getMember());
		for (GzSummaryEntry entry : entries)
		{
			GzSummaryRow subRow = sr.getSubMembers().get(entry.getMemberId());
			if (subRow == null)
			{
				GzBaseUser bu = home.getBaseUserByMemberId(entry.getMemberId());
				subRow = new GzSummaryRow(bu);
				gms.getRowMap().put(subRow.getId(),subRow);
				sr.getSubMembers().put(entry.getMemberId(),subRow);
			}
			subRow.getEntries().put(entry.getGameType().getIndex(), entry);
		}
	}
	
	
}
