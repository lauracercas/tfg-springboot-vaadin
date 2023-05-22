package com.tfg.springvaadin.service;

import com.tfg.springvaadin.model.Usuario;

public interface UsuarioService {
	
	public Usuario saveUsuario(Usuario usuario);
	
	public Usuario getUsuarioLogin(String usuario, String password);
	
	public Usuario getByCodusuario(String usuario);


}
