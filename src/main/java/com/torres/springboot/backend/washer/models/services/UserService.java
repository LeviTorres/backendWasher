package com.torres.springboot.backend.washer.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.torres.springboot.backend.washer.models.dao.IUserDao;
import com.torres.springboot.backend.washer.models.entity.User;

@Service
public class UserService implements IUserService, UserDetailsService{
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private IUserDao userDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userDao.findByUsername(username);
		
		if(user == null) {
			logger.error("Error en el login: no existe el usuario en el sistema");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario en el sistema");
		}
		
		List<GrantedAuthority> authorities = user.getRoles()
												.stream()
												.map(role -> new SimpleGrantedAuthority(role.getName()))
												.peek(authority -> logger.info("Role " + authority.getAuthority()))
												.collect(Collectors.toList()); 
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

}
