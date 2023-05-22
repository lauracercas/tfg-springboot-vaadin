package com.tfg.springvaadin.service;

import java.util.List;

import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.spec.ProductoSpecification;

public interface ProductoService {
	
	public List<Producto> findAll();
	
	public List<Producto> findProductos(ProductoSpecification spec);
	
	public List<String> getTipos();
	
	List<Producto> findByCodusuario(String codusuario);
	
	public String generarCodProducto(String tipo);
	
	public Producto saveProducto(Producto producto);
	
	//public String getCodtipo(String tipo);
	
	List<Producto> findByTipoAndFechaventaNull(String tipo);
	
	//public String getNombreTipoProducto(String valor);
	
	List<Producto> findProductosLineaPedidos(String codpedido);
	
	void deleteProducto(Producto producto);
	
	List<Producto> findByTipoAndFechaventaNullOrderByFechapublicacion(String tipo);
	
	List<Producto> findByUrlvideoNotNull();

}
