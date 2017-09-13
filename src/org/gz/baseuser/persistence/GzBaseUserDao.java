package org.gz.baseuser.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import org.gz.account.persistence.GzAccountDao;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.persistence.GzPersistenceException;

public interface GzBaseUserDao extends GzAccountDao{

	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzBaseUser getBaseUserByEmail(String email, @SuppressWarnings("rawtypes") Class clazz) throws GzPersistenceException;
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException;
	public List<String> getMemberCodes(GzBaseUser baseUser) throws GzPersistenceException;
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException;
	public Double getDownStreamCredit(GzBaseUser user, GzBaseUser parent);
	public void getDownstreamForParent(GzBaseUser parent);
	public void setAsSystemMember(GzBaseUser user) throws GzPersistenceException;
	public void storeImage(String email,MultipartFile data, String contentType) throws GzPersistenceException;
	public byte[] getImage(final String email) throws GzPersistenceException;
	public String getEmailForId(UUID id) throws GzPersistenceException;
	public void setDefaultPasswordForAll(String encoded);
	public void updateLeafInstance(GzBaseUser bu);
	public GzBaseUser getBaseUserByMemberId(String memberId) throws GzPersistenceException;
	public List<GzBaseUser> search(GzBaseUser user, String term, String type) throws GzPersistenceException;

}
