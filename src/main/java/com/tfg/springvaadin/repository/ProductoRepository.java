package com.tfg.springvaadin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tfg.springvaadin.model.Producto;


public interface ProductoRepository extends JpaRepository<Producto, String>,JpaSpecificationExecutor<Producto>{

	
	List<Producto> findByCodusuario(String codusuario);
	
	long countByTipo(String tipo);
	
	List<Producto> findByTipoAndFechaventaNull(String tipo);
	
	@Query("select p from Producto p, Linpedido l "
			+ "where l.codpedido =:codpedido and l.codproducto=p.codproducto")
	List<Producto> findProductosLineaPedidos(@Param("codpedido") String codpedido);
	
	List<Producto> findByTipoAndFechaventaNullOrderByFechapublicacion(String tipo);
	
	List<Producto> findByUrlvideoNotNull();


}