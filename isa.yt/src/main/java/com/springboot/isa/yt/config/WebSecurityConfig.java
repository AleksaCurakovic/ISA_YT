package com.springboot.isa.yt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.springboot.isa.yt.auth.RestAuthenticationEntryPoint;
import com.springboot.isa.yt.auth.TokenAuthenticationFilter;
import com.springboot.isa.yt.service.impl.CustomUserDetailsService;
import com.springboot.isa.yt.utils.TokenUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
	 @Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;



 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
 	    authProvider.setPasswordEncoder(passwordEncoder());

 	    return authProvider;
 	}
 	
 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}
 	

	@Autowired
	private TokenUtils tokenUtils;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint));
		http.authorizeHttpRequests(req -> req
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/signup",
						"/login",
						"/verify",
						"/getUpload/**",
						"/comments/**",
						"/uploads/**",
						"/getAllUploads",
						"/whoAreYou/**",
						"/getUserUploads/**",
						"/socket/**",
						"/watchparty/list").permitAll()
				.anyRequest().authenticated());
		
		http.cors(cors -> {});
		http.csrf(csrf -> csrf.disable());
		
		http.addFilterBefore(new TokenAuthenticationFilter(tokenUtils,  userDetailsService()), BasicAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());
		
		return http.build();
	}
	
	
}
