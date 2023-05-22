package com.tfg.springvaadin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tfg.springvaadin.model.Artista;
import com.tfg.springvaadin.repository.ArtistaRepository;

@Service
public class ArtistaServiceImpl implements ArtistaService{

	
	@Autowired
	ArtistaRepository artistaRepository;
	
	@Override
	public List<Artista> findAll(){
		return artistaRepository.findAll();
	}

	@Override
	public Artista findByCodartista(Integer codartista) {
		return artistaRepository.findByCodartista(codartista);
	}

	@Override
	public Artista saveArtista(Artista artista) {
		return artistaRepository.save(artista);
	}
	
	@Override
	public Integer siguienteCodartista() {
		List<Artista> artistas = artistaRepository.findAll(Sort.by(Sort.Direction.ASC, "codartista"));
		Integer codigoNuevo;
		if(!artistas.isEmpty()) {
			codigoNuevo=artistas.get(artistas.size()-1).getCodartista()+1;
		}else {
			codigoNuevo=1;
		}
		return codigoNuevo;
	}
	
	
}
