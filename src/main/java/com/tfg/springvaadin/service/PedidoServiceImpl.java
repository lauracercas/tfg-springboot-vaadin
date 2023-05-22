package com.tfg.springvaadin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.springvaadin.model.Linpedido;
import com.tfg.springvaadin.model.Pedido;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.repository.PedidoRepository;
import com.tfg.springvaadin.spec.PedidoSpecification;

@Service("pedidoService")
@Transactional
public class PedidoServiceImpl implements PedidoService{

	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	LinpedidoService linpedidoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ProductoService productoService;
	
	@Override
	public List<Pedido> findAll(){
		return pedidoRepository.findAll();
	}
	

	@Override
	public List<Pedido> findByCodusuario(String codusuario) {
		return pedidoRepository.findByCodusuario(codusuario);
	}
	
	@Override
	public List<Pedido> findByCodpedido(String codpedido) {
		return pedidoRepository.findByCodpedido(codpedido);
	}


	@Override
	public Pedido savePedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}
	
	@Override
	public List<Pedido> findPedidos(PedidoSpecification spec) {
		return pedidoRepository.findAll(spec);
	}
	
	@Override
	public void crearPedido(Pedido pedido,List<Producto> cesta, Usuario usuario) {
		List<Pedido> pedidos = findAll();
		Integer numpedido = pedidos.size()+1;
		pedido.setCodpedido("PED-"+numpedido);
		pedido.setCodusuario(usuario.getCodusuario());
		pedido.setFecha(new Date());
		savePedido(pedido);
		Double total = 0d;
		int cont =0;
		for(Producto producto : cesta) {
			cont++;
			Linpedido linea = new Linpedido();
			linea.setCodpedido(pedido.getCodpedido());
			total+=producto.getPrecio();
			linea.setCodlinpedido("PED"+numpedido+"-LIN-"+cont);
			linea.setCodproducto(producto.getCodproducto());
			linea.setPrecio(producto.getPrecio());
			linpedidoService.saveLinpedido(linea);
			
			Usuario usuarioProducto = usuarioService.getByCodusuario(producto.getCodusuario());
			usuarioProducto.setSaldo(usuario.getSaldo()!=null?(usuario.getSaldo()+producto.getPrecio()):producto.getPrecio());
			usuarioService.saveUsuario(usuarioProducto);
			
			producto.setEstadopedido("Vendido");
			producto.setFechaventa(new Date());
			productoService.saveProducto(producto);
		}
		pedido.setImporte(total);
		savePedido(pedido);
	}

	
	
	
}
