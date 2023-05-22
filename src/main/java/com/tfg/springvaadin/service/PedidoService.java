package com.tfg.springvaadin.service;

import java.util.List;

import com.tfg.springvaadin.model.Pedido;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.spec.PedidoSpecification;

public interface PedidoService {
	
	public List<Pedido> findAll();
	
	List<Pedido> findByCodusuario(String codusuario);
	
	List<Pedido> findByCodpedido(String codpedido);
	
	public Pedido savePedido(Pedido pedido);
	
	List<Pedido> findPedidos(PedidoSpecification spec);
	
	public void crearPedido(Pedido pedido,List<Producto> cesta, Usuario usuario);

}
