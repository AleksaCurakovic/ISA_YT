package com.springboot.isa.yt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.model.WatchParty;
import com.springboot.isa.yt.repository.WatchPartyRepository;
import com.springboot.isa.yt.service.WatchPartyService;

import jakarta.transaction.Transactional;

@Service
public class WatchPartyServiceImpl implements WatchPartyService {
	
	@Autowired
	private WatchPartyRepository watchPartyRepository;
	
	@Override
	public WatchParty create(WatchParty watchParty) {
		return watchPartyRepository.save(watchParty);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		watchPartyRepository.deleteById(id);;

	}

	@Override
	public List<WatchParty> getAll() {
		return watchPartyRepository.findAll();
	}

	@Override
	public WatchParty get(Long id) {
		return watchPartyRepository.findById(id).orElse(null);
	}


}
