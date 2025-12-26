package com.springboot.isa.yt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.model.Role;
import com.springboot.isa.yt.repository.RoleRepository;
import com.springboot.isa.yt.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	  @Autowired
	  private RoleRepository roleRepository;

	  @Override
	  public Role findById(Long id) {
	    Role auth = this.roleRepository.findById(id).orElse(null);
	    return auth;
	  }

	  @Override
	  public List<Role> findByName(String name) {
		List<Role> roles = this.roleRepository.findByName(name);
	    return roles;
	  }
}