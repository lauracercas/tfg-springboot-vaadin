package com.tfg.springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.springvaadin.model.Genero;


public interface GeneroRepository extends JpaRepository<Genero, String>{
	
	Genero findByCodgenero(Integer codgenero);

}