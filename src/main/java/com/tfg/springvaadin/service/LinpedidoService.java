package com.tfg.springvaadin.service;

import java.util.List;

import com.tfg.springvaadin.model.Linpedido;

public interface LinpedidoService {
	
	public List<Linpedido> findAll();
	
	List<Linpedido> findByCodpedido(String codpedido);
	
	List<Linpedido> findByCodpedidoAndCodlinpedido(String codpedido,String codlinpedido);
	
	public Linpedido saveLinpedido(Linpedido pedido);

}
