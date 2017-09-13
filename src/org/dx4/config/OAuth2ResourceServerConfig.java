package org.dx4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/Px4/api/**")
			.authorizeRequests()
			
			.antMatchers(
					"/Px4/api/a/**",
					"/Px4/api/anon/**"
					)
			.permitAll()
						
			.anyRequest()
			.access("hasRole('ROLE_PLAY')");
		;
    }

}