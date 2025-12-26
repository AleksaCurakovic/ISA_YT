package com.springboot.isa.yt.service;

import java.util.List;

import com.springboot.isa.yt.dto.UserRequestDTO;
import com.springboot.isa.yt.model.User;

public interface UserService {
	User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequestDTO userRequest);
}
