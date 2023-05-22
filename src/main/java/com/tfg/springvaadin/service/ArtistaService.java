package com.tfg.springvaadin.service;

import java.util.List;

import com.tfg.springvaadin.model.Artista;

public interface ArtistaService {
	
	public List<Artista> findAll();
	
	public Artista findByCodartista(Integer codartista);
	
	public Artista saveArtista(Artista artista);
	
	public Integer siguienteCodartista();
}
