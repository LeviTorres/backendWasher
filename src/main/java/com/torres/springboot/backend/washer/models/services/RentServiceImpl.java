package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.torres.springboot.backend.washer.models.dao.IRentDao;
import com.torres.springboot.backend.washer.models.entity.Client;
import com.torres.springboot.backend.washer.models.entity.Rent;
import com.torres.springboot.backend.washer.models.entity.Washer;

@Service
public class RentServiceImpl implements IRentService{
	
	@Autowired
	private IRentDao rentDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Rent> findAll() {
		return (List<Rent>)rentDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Rent> findAll(Pageable pageable) {
		return rentDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Rent findById(Long id) {
		return rentDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Rent save(Rent rent) {
		return rentDao.save(rent);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		rentDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAllClients() {
		return rentDao.findAllClients();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Washer> findAllWasher() {
		return rentDao.findAllWashers();
	}

}
