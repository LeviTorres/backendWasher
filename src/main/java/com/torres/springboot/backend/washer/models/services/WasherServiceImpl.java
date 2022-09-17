package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.torres.springboot.backend.washer.models.dao.IWhaserDao;
import com.torres.springboot.backend.washer.models.entity.Washer;

@Service
public class WasherServiceImpl implements IWasherService{
	@Autowired
	private IWhaserDao washerDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Washer> findAll() {
		return (List<Washer>)washerDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Washer> findAll(Pageable pageable) {
		return washerDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Washer finById(Long id) {
		return washerDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Washer save(Washer washer) {
		return washerDao.save(washer);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		washerDao.deleteById(id);
	}
	
	

}
