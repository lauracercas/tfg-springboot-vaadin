package com.tfg.springvaadin.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "productos/vinilo", layout = MainView.class)
@PageTitle("Vinilos")
public class ViniloListView extends VerticalLayout{

	private static final long serialVersionUID = 1L;

	ProductoService productoService;
	ArtistaService artistaService;
	
	@Autowired
	public ViniloListView(UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.productoService=productoService;
		this.artistaService=artistaService;
		ProductosView productos = new ProductosView(usuarioService, productoService, artistaService, generoService);
		add(new H2("Vinilos en Venta"),productos);
		


	
	
	}
}
