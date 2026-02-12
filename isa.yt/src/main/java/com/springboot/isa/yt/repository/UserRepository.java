package com.springboot.isa.yt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAllByWatchPartyId(Long id);
}
