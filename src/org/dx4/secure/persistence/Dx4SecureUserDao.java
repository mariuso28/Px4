package org.dx4.secure.persistence;

import java.util.List;
import java.util.UUID;

import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.web.multipart.MultipartFile;

public interface Dx4SecureUserDao{

	public void storeBaseUser(Dx4SecureUser baseUser) throws Dx4PersistenceException;
	public Dx4SecureUser getBaseUserByEmail(String email, @SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException;
	public Dx4SecureUser getBaseUserByCode(String code) throws Dx4PersistenceException;
	public Dx4SecureUser getBaseUserBySeqId(long id,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException;
	public Dx4SecureUser getBaseUserBySeqId(long id) throws Dx4PersistenceException;
	public List<Dx4SecureUser> getBaseUsersByRole(final Dx4Role role) throws Dx4PersistenceException;
	public String getBaseUserEmailBySeqId(long id) throws Dx4PersistenceException;
	public List<String> getMemberCodes(Dx4SecureUser baseUser) throws Dx4PersistenceException;
	public void updateBaseUserProfile(Dx4SecureUser baseUser) throws Dx4PersistenceException;
	public void getDownstreamForParent(Dx4SecureUser parent);
	public void setAsSystemMember(Dx4SecureUser user) throws Dx4PersistenceException;
	public void storeImage(String email,MultipartFile data, String contentType) throws Dx4PersistenceException;
	public byte[] getImage(final String email) throws Dx4PersistenceException;
	public String getEmailForId(UUID id) throws Dx4PersistenceException;
	public void setDefaultPasswordForAll(String encoded);
	public void updateLeafInstance(Dx4SecureUser bu);
	public String getCodeForSeqId(long id) throws Dx4PersistenceException;
	public void deleteMember(Dx4SecureUser user) throws Dx4PersistenceException;
	public Long getSeqIdForId(UUID id);

}
