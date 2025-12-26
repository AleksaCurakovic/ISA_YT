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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.isa.yt.dto.JwtAuthenticationRequestDTO;
import com.springboot.isa.yt.dto.UserRequestDTO;
import com.springboot.isa.yt.dto.UserTokenStateDTO;
import com.springboot.isa.yt.model.User;
import com.springboot.isa.yt.service.EmailService;
import com.springboot.isa.yt.service.UserService;
import com.springboot.isa.yt.utils.TokenUtils;

import exception.ResourceConflictException;
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
	
	@PostMapping("/login")
	public ResponseEntity<UserTokenStateDTO> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequestDTO authenticationRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody UserRequestDTO userRequest, UriComponentsBuilder ucBuilder) throws Exception {
		User existUserName = this.userService.findByUsername(userRequest.getUsername());
		User existUserEmail = this.userService.findByEmail(userRequest.getEmail());

		if (existUserName != null) {
			throw new ResourceConflictException(userRequest.getId(), "Username already in use");
		}
		
		if (existUserEmail != null) {
			throw new ResourceConflictException(userRequest.getId(), "Email already in use");
		}
		 
		
		String emailToken = tokenUtils.generateToken(userRequest.getEmail());
		emailService.sendVerificationEmail(userRequest.getEmail(), userRequest.getUsername(), emailToken);
		User user = this.userService.register(userRequest);
		
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token, HttpServletResponse response) throws IOException
    {
		String email = tokenUtils.getSubjectFromToken(token);
		User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found.");
        }
        
        if (user.isEnabled()) {
        	return ResponseEntity.status(HttpStatus.CONFLICT)
        						.body("User already enabled");
        }
        
        user.setEnabled(true);
        userService.save(user);
        response.sendRedirect("http://localhost:4200/login?verified=true");
        return null;
        						
    }
}
