package com.torres.springboot.backend.washer.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	private final static String DIRECTORY_UPLOAD = "uploads";
	
	@Override
	public Resource load(String pictureName) throws MalformedURLException {
		Path rutaArchivo = getPath(pictureName);
		
		log.info(rutaArchivo.toString());
		
		Resource recurso = new UrlResource(rutaArchivo.toUri());
		
		if(!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-user.png").toAbsolutePath();
			
			recurso = new UrlResource(rutaArchivo.toUri());

			log.error("Error no se pudo cargar la imagen" + pictureName);
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile archive) throws IOException {
		String nombreArchivo = UUID.randomUUID().toString() + "_" + archive.getOriginalFilename().replace(" ", "");
		
		Path rutaArchivo = getPath(nombreArchivo);
		
		log.info(rutaArchivo.toString());
		
		Files.copy(archive.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean delete(String pictureName) {
		if(pictureName != null && pictureName.length() > 0) {
			Path rutaFotoAnterior = Paths.get("uploads").resolve(pictureName).toAbsolutePath();
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String pictureName) {
		return Paths.get(DIRECTORY_UPLOAD).resolve(pictureName).toAbsolutePath();
	}

}
