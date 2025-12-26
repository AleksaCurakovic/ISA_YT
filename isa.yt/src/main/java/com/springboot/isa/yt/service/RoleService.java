package com.springboot.isa.yt.service;

import java.util.List;

import com.springboot.isa.yt.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
