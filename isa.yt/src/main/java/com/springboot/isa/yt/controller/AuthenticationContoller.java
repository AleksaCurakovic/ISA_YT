package com.springboot.isa.yt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.isa.yt.dto.JwtAuthenticationRequestDTO;
import com.springboot.isa.yt.dto.ProfileDTO;
import com.springboot.isa.yt.dto.UserRequestDTO;
import com.springboot.isa.yt.dto.UserTokenStateDTO;
import com.springboot.isa.yt.model.User;
import com.springboot.isa.yt.service.EmailService;
import com.springboot.isa.yt.service.UserService;
import com.springboot.isa.yt.utils.TokenUtils;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationContoller {
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RateLimiterRegistry rateLimiterRegistry;
	
	@PostMapping("/login")
	public ResponseEntity<UserTokenStateDTO> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequestDTO authenticationRequest, HttpServletResponse response, HttpServletRequest request) {
		
		var config = rateLimiterRegistry.rateLimiter("standard").getRateLimiterConfig();
		String ip = request.getRemoteAddr();
		RateLimiter perIpLimiter = rateLimiterRegistry.rateLimiter("login:" + ip, config);

        try {
            RateLimiter.waitForPermission(perIpLimiter);
        } catch (RequestNotPermitted ex) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(null);
        }
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		if (!user.isEnabled()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
		}
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody UserRequestDTO userRequest, UriComponentsBuilder ucBuilder) {
		User existUserName = this.userService.findByUsername(userRequest.getUsername());
		User existUserEmail = this.userService.findByEmail(userRequest.getEmail());

		if (existUserName != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
        						.body(null);
		}
		if (existUserEmail != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
								.body(null);
		} 		
		String emailToken = tokenUtils.generateToken(userRequest.getEmail());
		emailService.sendVerificationEmail(userRequest.getEmail(), userRequest.getUsername(), emailToken);
		User user = this.userService.register(userRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	@GetMapping("/verify")
    public void verifyUser(@RequestParam("token") String token, HttpServletResponse response) throws IOException
    {
		String email = tokenUtils.getSubjectFromToken(token);
		User user = userService.findByEmail(email);
        
        if (user.isEnabled()) {
        	 response.sendRedirect("http://localhost:4200/login?alreadyVerified=true");
             return;
        }
        
        user.setEnabled(true);
        userService.save(user);
        response.sendRedirect("http://localhost:4200/login?verified=true");
        						
    }
	
	@GetMapping("/whoAreYou/{userName}")
	public ProfileDTO whoAreYou(@PathVariable String userName){
		User user = userService.findByUsername(userName);
		ProfileDTO profile;
		if (user != null)
		{
			profile = new ProfileDTO(user);
		}
		else {
			profile = null;
		}
		return profile;
	}
	
	@GetMapping("/whoAmI")
	public ProfileDTO whoAmI(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		ProfileDTO profile;
		if (user != null)
		{
			profile = new ProfileDTO(user);
		}
		else {
			profile = null;
		}
		return profile;
	}
}
