package com.torres.springboot.backend.washer.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.torres.springboot.backend.washer.models.entity.Washer;
import com.torres.springboot.backend.washer.models.services.IUploadFileService;
import com.torres.springboot.backend.washer.models.services.IWasherService;

@CrossOrigin(origins= {"http://localhost:4000"})
@RestController
@RequestMapping("/api")
public class WasherRestController {
	
	@Autowired
	private IWasherService washerService;
	
	@Autowired
	private IUploadFileService uploadService;
	
	@GetMapping("/washers")
	public List<Washer>index(){
		return washerService.findAll();
	}
	
	@GetMapping("/washers/page/{page}")
	public Page<Washer> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);
		return washerService.findAll(pageable);
	}
	
	@GetMapping("/washers/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		Washer washer = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			washer = washerService.finById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error when querying the database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(washer == null) {
			response.put("message", "The Washer ID: ".concat(id.toString().concat(" does not exist in the database")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Washer>(washer, HttpStatus.OK);
	}
	
	@PostMapping("/washers")
	public ResponseEntity<?> create(@Valid @RequestBody Washer washer, BindingResult result){
		Washer washerNew = null;
		
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
			washerNew = washerService.save(washer);
		} catch (DataAccessException e) {
			response.put("message", "Error when performing insert to database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The washer has been created successfully");
		response.put("washer", washerNew);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/washers/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Washer washer, BindingResult result, @PathVariable Long id){
		Washer washerActual = washerService.finById(id);
		Washer washerUpdated = null;
		
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
		
		if(washerActual == null) {
			response.put("message", " Error: Cannot be edited, the Client ID ".concat(id.toString().concat(" does not exist in the database")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			washerActual.setBrand(washer.getBrand());
			washerActual.setColor(washer.getColor());
			washerActual.setSpecs(washer.getSpecs());
			washerActual.setStatus(washer.getStatus());
			washerActual.setCreateAt(washer.getCreateAt());
			
			washerUpdated = washerService.save(washerActual);
		}catch(DataAccessException e) {
			response.put("message", "Error updating washer in database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The washer has been created successfully");
		response.put("washer", washerUpdated);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/washers/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String,Object> response = new HashMap<>();
		
		try {
			Washer washer = washerService.finById(id);
			String nombreFotoAnterior = washer.getPicture();
			
			uploadService.delete(nombreFotoAnterior);
			washerService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "Error removing washer from database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The washer has been removed successfully");
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/washers/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id ){
		Map<String, Object> response = new HashMap<String, Object>();
		Washer washer = washerService.finById(id);
		if(!archivo.isEmpty()) {
			
			String nombreArchivo = null;
			try {
				nombreArchivo =  uploadService.copy(archivo);
			} catch (IOException e) {
				response.put("message", "Error al subir la imagen de la maquina ");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = washer.getPicture();
			
			uploadService.delete(nombreFotoAnterior);
			
			
			washer.setPicture(nombreArchivo);
			washerService.save(washer);
			
			response.put("washer", washer);
			response.put("message", "Has subido correctamente la imagen:" + nombreArchivo);
		}
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{pictureName:.+}")
	public ResponseEntity<Resource> showPicture(@PathVariable String pictureName){
		
		Resource recurso = null;
		
		try {
			recurso = uploadService.load(pictureName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		org.springframework.http.HttpHeaders cabecera = new org.springframework.http.HttpHeaders();
		cabecera.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	
	
}
