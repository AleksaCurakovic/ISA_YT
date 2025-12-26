package com.springboot.isa.yt.service;

public interface EmailService {
	void sendVerificationEmail(String recepient, String username, String token);
}
