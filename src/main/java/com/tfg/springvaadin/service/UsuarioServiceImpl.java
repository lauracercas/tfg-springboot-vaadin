package com.tfg.springvaadin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.repository.UsuarioRepository;



@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario getUsuarioLogin(String usuario, String password) {
		return usuarioRepository.getByCodusuarioAndPassword(usuario, password);
	}

	@Override
	public Usuario getByCodusuario(String usuario) {
		return usuarioRepository.getByCodusuario(usuario);
	}

}
