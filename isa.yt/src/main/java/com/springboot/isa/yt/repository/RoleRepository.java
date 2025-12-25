package com.springboot.isa.yt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
}
