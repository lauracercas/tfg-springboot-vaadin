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

@Route(value = "productos/cd", layout = MainView.class)
@PageTitle("Cds")
public class CdListView extends VerticalLayout{

	private static final long serialVersionUID = 1L;

	ProductoService productoService;
	ArtistaService artistaService;
	
	@Autowired
	public CdListView(UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.productoService=productoService;
		this.artistaService=artistaService;
		ProductosView productos = new ProductosView(usuarioService, productoService, artistaService, generoService);
		add(new H2("CDs en Venta"),productos);
		


	
	
	}
}
