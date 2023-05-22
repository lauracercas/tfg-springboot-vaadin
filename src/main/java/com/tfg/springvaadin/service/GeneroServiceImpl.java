package com.tfg.springvaadin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tfg.springvaadin.model.Genero;
import com.tfg.springvaadin.repository.GeneroRepository;

@Service
public class GeneroServiceImpl implements GeneroService{

	
	@Autowired
	GeneroRepository generoRepository;
	
	@Override
	public List<Genero> findAll(){
		return generoRepository.findAll();
	}

	@Override
	public Genero findByCodgenero(Integer codgenero) {
		return generoRepository.findByCodgenero(codgenero);
	}

	@Override
	public Genero saveGenero(Genero Genero) {
		return generoRepository.save(Genero);
	}
	
	@Override
	public Integer siguienteCodgenero() {
		List<Genero> generos = generoRepository.findAll(Sort.by(Sort.Direction.ASC, "codgenero"));
		Integer codigoNuevo;
		if(!generos.isEmpty()) {
			codigoNuevo=generos.get(generos.size()-1).getCodgenero()+1;
		}else {
			codigoNuevo=1;
		}
		return codigoNuevo;
	}
	
	
}
