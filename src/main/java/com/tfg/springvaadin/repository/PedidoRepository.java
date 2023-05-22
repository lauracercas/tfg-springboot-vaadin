package com.tfg.springvaadin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tfg.springvaadin.model.Pedido;


public interface PedidoRepository extends JpaRepository<Pedido, String>,JpaSpecificationExecutor<Pedido>{

	
	List<Pedido> findByCodusuario(String codusuario);
	
	List<Pedido> findByCodpedido(String codpedido);
}