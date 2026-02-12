package com.springboot.isa.yt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.WatchParty;

public interface WatchPartyRepository extends JpaRepository<WatchParty, Long> {
	public void deleteByHost(String host);

}
