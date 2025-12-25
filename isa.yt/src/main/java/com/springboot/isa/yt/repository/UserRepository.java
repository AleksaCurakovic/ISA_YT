package com.springboot.isa.yt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
