package com.tfg.springvaadin.views;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.LinpedidoService;
import com.tfg.springvaadin.service.PedidoService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "cuenta", layout = MainView.class)
@PageTitle("Mi Cuenta")
public class CuentaView extends HorizontalLayout {

	
	private static final long serialVersionUID = 1L;
	
	private Tab tabDatos;
	private Tab tabProductos;
	private Tab tabPedidos;
	
	private Tabs tabList;
	
	private UsuarioForm usuarioForm;
	private ProductosView productos;
	private PedidosView pedidos;
	HorizontalLayout layout;
	
	@Autowired
    public CuentaView(UsuarioService usuarioService,ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService,
    		PedidoService pedidoService, LinpedidoService linPedidoService) {
    	
		VaadinSession.getCurrent().getSession().setAttribute("tipo",null);
		
    	setMargin(true);
    	setPadding(true);
    	setSpacing(true);
    	tabDatos = new Tab("Datos");
    	tabProductos = new Tab("Productos");
    	tabPedidos = new Tab("Pedidos");

    	tabList = new Tabs(false,tabDatos, tabProductos, tabPedidos);
    	tabList.setOrientation(Orientation.VERTICAL);
    	tabList.setSelectedTab(tabDatos);
    	
    	
    	Label espacio = new Label("");
    	espacio.setWidth("20%");
    	
    	
    	layout= new HorizontalLayout();
    	
		usuarioForm = new UsuarioForm(usuarioService,"C");
		usuarioForm.setWidth("40%");
		layout.setAlignItems(Alignment.CENTER);
		
		
		
		productos =new ProductosView(usuarioService, productoService, artistaService, generoService);
		layout.add(productos);
		pedidos = new PedidosView(pedidoService,linPedidoService,
				usuarioService,productoService,artistaService,generoService);
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(tabDatos,usuarioForm);
		tabsToPages.put(tabProductos,productos);
		tabsToPages.put(tabPedidos,pedidos);
		
		tabList.addSelectedChangeListener(e->{
			borrarComponentes();
			if(tabList.getSelectedTab().equals(tabDatos)) {
				espacio.setWidth("20%");
				add(usuarioForm);
			}else if(tabList.getSelectedTab().equals(tabProductos)) {
				espacio.setWidth("2%");
				add(productos);
			}else if(tabList.getSelectedTab().equals(tabPedidos)) {
				espacio.setWidth("10%");
				add(pedidos);
			}
		});
		
		add(tabList);
		add(espacio);
		add(usuarioForm);

    }
    
    private void borrarComponentes() {
    	getChildren().forEach(child->{
			if(child.equals(usuarioForm)) {
				remove(usuarioForm);
			}else if(child.equals(pedidos)) {
				remove(pedidos);
			}else if(child.equals(productos)) {
				remove(productos);
			}
		});
    }

}
