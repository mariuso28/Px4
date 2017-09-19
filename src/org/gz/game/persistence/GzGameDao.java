package org.gz.game.persistence;

import java.util.Map;

import org.gz.game.GzGroup;
import org.gz.game.GzPackage;

public interface GzGameDao {
	
	public void storePackage(GzPackage gzPackage);
	public void updatePackage(GzPackage gzPackage);
	public GzPackage getPackageById(long id);
	public void storeGroup(GzGroup group);
	public void addPackageToGroup(GzGroup group,GzPackage gzPackage);
	public Map<String, GzGroup> getGroups(String memberId);
}
