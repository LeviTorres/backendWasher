package com.torres.springboot.backend.washer.models.services;

import com.torres.springboot.backend.washer.models.entity.User;

public interface IUserService {
	
	public User findByUsername(String username);
}
