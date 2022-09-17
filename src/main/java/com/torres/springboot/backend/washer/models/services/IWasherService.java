package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.torres.springboot.backend.washer.models.entity.Washer;

public interface IWasherService {
	
	public List<Washer> findAll();

	public Page<Washer> findAll(Pageable pageable);
	
	public Washer finById(Long id);
	
	public Washer save(Washer washer);
	
	public void delete(Long id);
}
