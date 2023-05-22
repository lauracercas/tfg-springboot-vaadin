package com.tfg.springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.springvaadin.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	Usuario getByCodusuarioAndPassword(String usuario, String password);
	
	Usuario getByCodusuario(String usuario);

}