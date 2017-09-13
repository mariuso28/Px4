package org.dx4.config.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private static final Logger log = Logger.getLogger(CustomUserDetailsService.class);
	@Autowired
	private Dx4Services dx4Services;
	@Autowired
	private GzServices gzServices;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("loadUserByUsername email : " + email);
		
		Dx4SecureUser baseUser;
		try
		{
			baseUser = dx4Services.getDx4Home().getByUsername(email, Dx4SecureUser.class);
		}
		catch (PersistenceRuntimeException | Dx4PersistenceException e)
		{
			log.error("Error finding User: " + email + " not found");
			throw new UsernameNotFoundException("Error finding User: " + email);
		}
		
		if (baseUser != null)
		{
			log.info("User : " + baseUser.getEmail() + " found with role :" + baseUser.getRole());
			
			Collection<GrantedAuthority> authorities = getAuthorities(baseUser);
			
			User user = new User(baseUser.getEmail(), baseUser.getPassword(), baseUser.isEnabled(), true, true, true, authorities);
			
			log.info("Using User : " + user.getUsername() + " with authorities :" + authorities);
			return user;
		}	
			
		String memberId = email;
		log.info("trying loadUserByUsername by memberId : " + memberId);
		
		GzBaseUser gzBaseUser;
		try {
			gzBaseUser = gzServices.getGzHome().getBaseUserByMemberId(memberId);
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error finding User: " + memberId + " not found");
			throw new UsernameNotFoundException("Error finding User: " + memberId);
		}
		if (gzBaseUser==null){
			log.error("User : " + memberId + " not found");
			throw new UsernameNotFoundException("User: " + memberId);
		}
		log.info("User : " + gzBaseUser.getMemberId() + " found with role :" + gzBaseUser.getRole());
		
		Collection<GrantedAuthority> authorities = getGzAuthorities(gzBaseUser);
		
		boolean enabled = gzBaseUser.isEnabled();
		User user = new User(gzBaseUser.getMemberId(), gzBaseUser.getPassword(), enabled, true, true, true, authorities);
		
		log.info("Using User : " + user.getUsername() + " with authorities :" + authorities);
		return user;
	}

	private Collection<GrantedAuthority> getGzAuthorities(GzBaseUser gzBaseUser) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		authList.add(new SimpleGrantedAuthority(gzBaseUser.getRole().name()));

		return authList;
	}

	private Collection<GrantedAuthority> getAuthorities(Dx4SecureUser baseUser) {
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		authList.add(new SimpleGrantedAuthority(baseUser.getRole().name()));

		return authList;
	}
}
