package com.tfg.springvaadin.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.repository.ProductoRepository;
import com.tfg.springvaadin.spec.ProductoSpecification;

@Service("productoService")
@Transactional
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	ProductoRepository productoRepository;
	
	@Override
	public List<Producto> findAll(){
		return productoRepository.findAll();
	}
	
	@Override
	public List<Producto> findProductos(ProductoSpecification spec) {
		return productoRepository.findAll(spec);
	}
	
	@Override
	public List<String> getTipos(){
		return Arrays.asList("CD", "Vinilo", "DVD", "Merchandising");
	}

	@Override
	public List<Producto> findByCodusuario(String codusuario) {
		return productoRepository.findByCodusuario(codusuario);
	}
	
	@Override
	public String generarCodProducto(String tipo) {
		String codigo= "";
		long count = productoRepository.countByTipo(tipo);
		if("CD".equals(tipo)) {
			codigo="CD-"+(count+1);
		}else if("Vinilo".equals(tipo)) {
			codigo="V-"+(count+1);
		}else if("DVD".equals(tipo)) {
			codigo="DVD-"+(count+1);
		}else if("Merchandising".equals(tipo)) {
			codigo="M-"+(count+1);
		}
		return codigo;
	}
	
	@Override
	public Producto saveProducto(Producto producto) {
		return productoRepository.save(producto);
	}
	
//	@Override
//	public String getCodtipo(String tipo){
//		String codigo=null;
//		if("CD".equals(tipo)) {
//			codigo=TipoProductoEnum.CD.getValor();
//		}else if("Vinilo".equals(tipo)) {
//			codigo=TipoProductoEnum.VINILO.getValor();
//		}else if("DVD".equals(tipo)) {
//			codigo=TipoProductoEnum.DVD.getValor();
//		}else if("Merchandising".equals(tipo)) {
//			codigo=TipoProductoEnum.MERCHAN.getValor();
//		}
//		return codigo;
//	}

	@Override
	public List<Producto> findByTipoAndFechaventaNull(String tipo) {
		return productoRepository.findByTipoAndFechaventaNull(tipo);
	}
	
//	@Override
//	public String getNombreTipoProducto(String valor) {
//		String nombre="";
//		if(TipoProductoEnum.CD.getValor().equals(valor)) {
//			nombre="CD";
//		}else if(TipoProductoEnum.VINILO.getValor().equals(valor)) {
//			nombre="Vinilo";
//		}else if(TipoProductoEnum.DVD.getValor().equals(valor)) {
//			nombre="DVD";
//		}else if(TipoProductoEnum.MERCHAN.getValor().equals(valor)) {
//			nombre="Merchandising";
//		}
//		return nombre;
//	}
	
	@Override
	public List<Producto> findProductosLineaPedidos(String codpedido) {
		return productoRepository.findProductosLineaPedidos(codpedido);
	}
	
	@Override
	public void deleteProducto(Producto producto) {
		productoRepository.delete(producto);
	}

	@Override
	public List<Producto> findByTipoAndFechaventaNullOrderByFechapublicacion(String tipo) {
		return productoRepository.findByTipoAndFechaventaNullOrderByFechapublicacion(tipo);
	}

	@Override
	public List<Producto> findByUrlvideoNotNull() {
		return productoRepository.findByUrlvideoNotNull();
	}

	
	
}
