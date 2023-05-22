package com.tfg.springvaadin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.springvaadin.model.Linpedido;
import com.tfg.springvaadin.repository.LinpedidoRepository;

@Service("linpedidoService")
@Transactional
public class LinpedidoServiceImpl implements LinpedidoService{

	@Autowired
	LinpedidoRepository linpedidoRepository;
	
	@Override
	public List<Linpedido> findAll(){
		return linpedidoRepository.findAll();
	}
	
	@Override
	public List<Linpedido> findByCodpedido(String codpedido) {
		return linpedidoRepository.findByCodpedido(codpedido);
	}
	
	@Override
	public List<Linpedido> findByCodpedidoAndCodlinpedido(String codpedido,String codlinpedido) {
		return linpedidoRepository.findByCodpedidoAndCodlinpedido(codpedido,codlinpedido);
	}

	@Override
	public Linpedido saveLinpedido(Linpedido linpedido) {
		return linpedidoRepository.save(linpedido);
	}

}
