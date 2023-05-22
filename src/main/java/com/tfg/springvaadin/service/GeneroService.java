package com.tfg.springvaadin.service;

import java.util.List;

import com.tfg.springvaadin.model.Genero;

public interface GeneroService {
	
	public List<Genero> findAll();
	
	public Genero findByCodgenero(Integer codgenero);
	
	public Genero saveGenero(Genero Genero);
	
	public Integer siguienteCodgenero();
}
