package com.springboot.isa.yt.service;

import java.util.List;

import com.springboot.isa.yt.model.WatchParty;

public interface WatchPartyService {
	public WatchParty create(WatchParty watchParty);
	public void delete(Long id);
	public List<WatchParty> getAll();
	public WatchParty get(Long id);
}
