package com.tfg.springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.springvaadin.model.Artista;


public interface ArtistaRepository extends JpaRepository<Artista, String>{
	
	Artista findByCodartista(Integer codartista);

}