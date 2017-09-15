package org.gz.game.persistence;

import org.gz.game.GzPackage;

public interface GzGameDao {
	
	public void storePackage(GzPackage gzPackage);
	public void updatePackage(GzPackage gzPackage);
	public GzPackage getPackageById(long id);

}
