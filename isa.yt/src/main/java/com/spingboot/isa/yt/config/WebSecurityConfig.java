package com.spingboot.isa.yt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(req -> req
				.requestMatchers(
						"/",
						"/home",
						"/signup*",
						"/login/"
					).permitAll()
				.anyRequest().authenticated());
		
		http.cors(cors -> cors.configure(http));
		http.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
	
	
}
