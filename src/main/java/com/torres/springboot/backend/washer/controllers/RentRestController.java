package com.torres.springboot.backend.washer.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torres.springboot.backend.washer.models.entity.Client;
import com.torres.springboot.backend.washer.models.entity.Rent;
import com.torres.springboot.backend.washer.models.entity.Washer;
import com.torres.springboot.backend.washer.models.services.IRentService;

@CrossOrigin(origins = {"http://localhost:4000"})
@RestController
@RequestMapping("/api")
public class RentRestController {
	
	@Autowired
	private IRentService rentService;
	
	@GetMapping("/rents")
	public List<Rent> index(){
		return rentService.findAll();
	}
	
	@GetMapping("/rents/page/{page}")
	public Page<Rent> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);
		return rentService.findAll(pageable);
	}
	
	@GetMapping("/rents/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Rent rent = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			rent = rentService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error when querying the database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(rent == null) {
			response.put("message", "The Rent ID: ".concat(id.toString().concat(" does not exist in the database")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Rent>(rent, HttpStatus.OK);
	}
	
	@PostMapping("/rents")
	public ResponseEntity<?> create(@Valid @RequestBody Rent rent, BindingResult result){
		Rent rentNew = null;
		
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> {
							return "El campo '" + err.getField() + "' "+ err.getDefaultMessage();
						})
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			rentNew = rentService.save(rent);
		} catch (DataAccessException e) {
			response.put("message", "Error when performing insert to database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The rent has been created successfully");
		response.put("rent", rentNew);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/rents/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Rent rent, BindingResult result, @PathVariable Long id){
		Rent rentActual = rentService.findById(id);
		Rent rentUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> {
							return "El campo '" + err.getField() + "' "+ err.getDefaultMessage();
						})
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(rentActual == null) {
			response.put("message", " Error: Cannot be edited, the Rent ID ".concat(id.toString().concat(" does not exist in the database")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			rentActual.setStatusRent(rent.getStatusRent());
			rentActual.setCreateAt(rent.getCreateAt());
			rentActual.setClient(rent.getClient());
			rentActual.setWasher(rent.getWasher());
			
			rentUpdated = rentService.save(rentActual);
		}catch(DataAccessException e) {
			response.put("message", "Error updating washer in database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The rent has been created successfully");
		response.put("rent", rentUpdated);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/rents/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String,Object> response = new HashMap<>();
		
		try {			
			rentService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "Error removing rent from database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The rent has been removed successfully");
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/rents/clients")
	public List<Client> listClients(){
		return rentService.findAllClients();
	}
	
	@GetMapping("/rents/washers")
	public List<Washer> listWashers(){
		return rentService.findAllWasher();
	}
}
