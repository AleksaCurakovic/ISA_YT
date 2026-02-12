package com.springboot.isa.yt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.isa.yt.model.User;
import com.springboot.isa.yt.model.WatchParty;
import com.springboot.isa.yt.service.UserService;
import com.springboot.isa.yt.service.WatchPartyService;

@RestController
public class WatchPartyController {
	
	@Autowired
	private WatchPartyService watchPartyService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/watchparty/list")
	public ResponseEntity<List<WatchParty>> getAll(){
		return ResponseEntity.ok(watchPartyService.getAll());
	}
	
	@PostMapping("/watchparty/create")
	public ResponseEntity<WatchParty> create(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		User realUser = userService.findByUsername(user.getUsername());
		WatchParty watchParty = watchPartyService.create(new WatchParty(user.getUsername()));
		realUser.setWatchParty(watchParty);
		userService.save(realUser);
		return ResponseEntity.ok(watchParty);
	}
	
	@DeleteMapping("/watchparty/delete/{id}")
	public void delete(@PathVariable Long id){
		userService.removeUsersFromParty(id);
		watchPartyService.delete(id);
	}
	
	@GetMapping("/watchparty/get")
	public ResponseEntity<WatchParty> get(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		User realUser = userService.findByUsername(user.getUsername());
		if (realUser.getWatchParty() == null)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.ok(realUser.getWatchParty());
	}
	
	@PatchMapping("/watchparty/leave")
	public ResponseEntity<?> leave(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		User realUser = userService.findByUsername(user.getUsername());
		realUser.setWatchParty(null);
		userService.save(realUser);
		return ResponseEntity.ok(null);

	}
	
	@PatchMapping("/watchparty/join/{id}")
	public ResponseEntity<WatchParty> join(@PathVariable Long id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		User realUser = userService.findByUsername(user.getUsername());
		WatchParty watchParty = watchPartyService.get(id);
		realUser.setWatchParty(watchParty);
		userService.save(realUser);
		return ResponseEntity.ok(watchParty);
	}
	
	
	
}
