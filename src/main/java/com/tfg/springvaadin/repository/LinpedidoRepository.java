package com.tfg.springvaadin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tfg.springvaadin.model.Linpedido;


public interface LinpedidoRepository extends JpaRepository<Linpedido, String>,JpaSpecificationExecutor<Linpedido>{

	List<Linpedido> findByCodpedidoAndCodlinpedido(String codpedido,String codlinpedido);
	
	List<Linpedido> findByCodpedido(String codpedido);
}