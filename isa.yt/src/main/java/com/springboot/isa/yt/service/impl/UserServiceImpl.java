package com.springboot.isa.yt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.dto.UserRequestDTO;
import com.springboot.isa.yt.model.Role;
import com.springboot.isa.yt.model.User;
import com.springboot.isa.yt.repository.UserRepository;
import com.springboot.isa.yt.service.RoleService;
import com.springboot.isa.yt.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElseGet(null);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User register(UserRequestDTO userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setAddress(userRequest.getAddress());
		u.setEnabled(false);
		u.setEmail(userRequest.getEmail());
		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);
		
		return this.userRepository.save(u);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByUsername(email);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
