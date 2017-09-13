package org.dx4.admin.persistence;

import java.util.Date;

import org.dx4.admin.Dx4Admin;
import org.dx4.admin.Dx4Version;
import org.dx4.home.persistence.Dx4PersistenceException;



public interface Dx4AdminDao{
	
	public void store(Dx4Admin admin);
	public Dx4Admin getAdminByEmail(String email) throws Dx4PersistenceException;
	public void update(Dx4Admin admin) throws Dx4PersistenceException;
	public boolean getScheduledDownTimeSet() throws Dx4PersistenceException;
	public void setScheduledDownTime(boolean set) throws Dx4PersistenceException;
	public void storeScheduledDownTime(Date date) throws Dx4PersistenceException;
	public Date getScheduledDownTime() throws Dx4PersistenceException;
	public Dx4Admin getAdminProperties() throws Dx4PersistenceException;
	public void updateVersion(final Dx4Version version);
	public Dx4Version getVersion();
	public String getVersionCode();
	public void initializeAdminProperties(Dx4Admin admin);
}
