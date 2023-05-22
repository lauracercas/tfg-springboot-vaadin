package com.tfg.springvaadin.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainView.class)
@PageTitle("Inicio")
public class HomeView extends SplitLayout {
	
	private static final long serialVersionUID = 1L;

	private IFrame iFrame1= new IFrame();
	private IFrame iFrame2= new IFrame();
	private IFrame iFrame3= new IFrame();

	@Autowired
	public HomeView(UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {   
		VerticalLayout productosView = new VerticalLayout();
		productosView.add(new H2("Últimos Productos"));
		
		Accordion accordion = new Accordion();
		
		
		HomeProductosView topVinilos = new HomeProductosView("Vinilo",usuarioService,  productoService,
	    		 artistaService, generoService );
		
		accordion.add("Vinilos",topVinilos );
		
		HomeProductosView topCds = new HomeProductosView("CD",usuarioService,  productoService,
	    		 artistaService, generoService );
		accordion.add("CDs",topCds );
		
		HomeProductosView topDVDs = new HomeProductosView("DVD",usuarioService,  productoService,
	    		 artistaService, generoService );
		accordion.add("DVDs",topDVDs );
		HomeProductosView topMerchan = new HomeProductosView("Merchandising",usuarioService,  productoService,
	    		 artistaService, generoService );
		accordion.add("Merchandising",topMerchan );
		
		accordion.setWidthFull();
		accordion.setWidth("850px");
		productosView.add(accordion);
		accordion.close();
		addToPrimary(productosView);
		
		VerticalLayout videosView = new VerticalLayout();
		
		List<Producto> videos = productoService.findByUrlvideoNotNull();
		if(!videos.isEmpty()) {
			configurarVideo(videos.get(0).getUrlvideo(), iFrame1);
			videosView.add(iFrame1);
			
		}
		videosView.setWidth("900px");
		addToSecondary(videosView);
		
		this.setHeight("100%");
		
		
		
	}
	
	private void configurarVideo(String video,IFrame iFrame) {
    	String part1= video.substring(0,8);
		String url=null;
		if("https://".equals(part1)) {
			url=video.substring(32,43);
		}else {
			part1= video.substring(0,16);
			if("www.youtube.com/".equals(part1)) {
				url=video.substring(24,35);
			}
		}
		
		if(url!=null) {
			iFrame.setSrc("https://www.youtube.com/embed/"+url);
	        iFrame.setHeight("315px");
	        iFrame.setWidth("560px");
	        iFrame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
	        iFrame.getElement().setAttribute("allowfullscreen", true);
	        iFrame.getElement().setAttribute("frameborder", "0");
	        iFrame.setVisible(true);
		}else {
			iFrame.setVisible(false);
		}
    }
	
}
