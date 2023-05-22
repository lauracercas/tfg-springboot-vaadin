package com.tfg.springvaadin.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Artista;
import com.tfg.springvaadin.model.Genero;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "productosh", layout = MainView.class)
@PageTitle("Productos")
public class HomeProductosView extends VerticalLayout{

	private static final long serialVersionUID = 1L;

	Grid<Producto> productoGrid = new Grid<>(Producto.class,false);
	List<Producto> productos = new ArrayList<Producto>();
	ListDataProvider<Producto> dataProvider;
	
	ProductoService productoService;
	ArtistaService artistaService;
	GeneroService generoService;
	
	TextField nombreFilter;
	TextField descripcionFilter;
	ComboBox<String> tipoFilter;
	NumberField precioFilter ;
	DatePicker fechaPubFilter;
	IntegerField  annioFilter ;
	ComboBox<Artista> artistaFilter;
	ComboBox<Genero> generoFilter;
	ComboBox<String> estadoFilter;
	
	String titulo;
	H3 title;
	
	Binder<Producto> binder = new Binder<>(Producto.class);
	Editor<Producto> editor = productoGrid.getEditor();
	
	String tipo;
	Usuario usuario;
	
	
	
	@Autowired
	public HomeProductosView(String tipo, UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.productoService=productoService;
		this.artistaService=artistaService;
		this.generoService=generoService;
		usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");

		productos = this.productoService.findByTipoAndFechaventaNullOrderByFechapublicacion(tipo);
		if(productos.size()>4) {
			productos = productos.subList(0, 3);
		}
		
		
		Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
		productoGrid.setHeight("300px");
		productoGrid.addComponentColumn(producto -> {
		    Button info = new Button(VaadinIcon.INFO_CIRCLE.create());
		    info.addClassName("view");
		    info.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		    info.addClickListener(e -> {
		    	Dialog dialog = new Dialog();
		    	VaadinSession.getCurrent().getSession().setAttribute("producto",producto);
		    	dialog.setCloseOnEsc(false);
		    	dialog.setCloseOnOutsideClick(false);
		    	
		    	Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
        		    dialog.close();

        		    
        		    productos = this.productoService.findByTipoAndFechaventaNullOrderByFechapublicacion(tipo);
        			if(productos.size()>4) {
        				productos = productos.subList(0, 3);
        			}
        			
        		    productoGrid.setItems(productos);
        		    VaadinSession.getCurrent().getSession().setAttribute("producto",null);
        		    VaadinSession.getCurrent().getSession().setAttribute("accion",null);
        		});
		    	cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

		    	
		    	VaadinSession.getCurrent().getSession().setAttribute("accion", "C");
		    	dialog.add(cancelButton,new ProductoForm(productoService, artistaService,
		    			generoService));
		    	
		    	        		
        		dialog.add();
		    	dialog.open();
		    });
		    info.setEnabled(!editor.isOpen());
		    editButtons.add(info);
		    return info;
		});
		if(usuario!=null && this.tipo==null) {
			productoGrid.addComponentColumn(producto -> {
			    Button shop = new Button(VaadinIcon.CART.create());
			    shop.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			    shop.addClassName("shop");
			    shop.addClickListener(e -> {

			    	List<Producto> cesta = (List<Producto>) VaadinSession.getCurrent().getSession().getAttribute("cesta");
			    	if (cesta==null) {
			    		cesta =new ArrayList<>();
			    	}
			    	
			    	if(this.usuario.getCodusuario().equals(producto.getCodusuario())){
			    		Notification notification = new Notification(
			        	        "Este producto es tuyo no puedes añadirlo en la cesta", 2000);
			    		notification.setPosition(Position.MIDDLE);
			    		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			        	notification.open();
			    	}else if(Boolean.FALSE.equals(isProductoCesta(producto,cesta))) {
			    		cesta.add(producto);
				    	VaadinSession.getCurrent().getSession().setAttribute("cesta",cesta);
				    	Notification notification = new Notification(
			        	        "Producto añadido en la Cesta", 2000);
			    		notification.setPosition(Position.MIDDLE);
			    		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			        	notification.open();
			    	}else {
			    		Notification notification = new Notification(
			        	        "El producto ya está en la lista de la compra", 2000);
			    		notification.setPosition(Position.MIDDLE);
			    		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			        	notification.open();
			    	}
			    	
			    	
			    	
			    });
			    shop.setEnabled(!editor.isOpen());
			    editButtons.add(shop);
			    return shop;
			});
		}
		
		
		productoGrid.addColumn(Producto::getNombre).setSortable(true).setWidth("200px")
		.setHeader("Nombre").setKey("nombre");
		
		productoGrid.addColumn(Producto::getDescripcion).setSortable(true).setWidth("220px")
		.setHeader("Descripcion").setKey("descripcion");

		productoGrid.addColumn(new NumberRenderer<>(
				Producto::getPrecio, "%(,.2f €",
		        Locale.FRANCE, "0.00 €")).setSortable(true).setWidth("110px")
		.setHeader("Precio").setKey("precio");
		
		
		
		productoGrid.addColumn(producto -> { 
	        Artista artista = artistaService.findByCodartista(producto.getCodartista());
	        return artista == null ? "" : artista.getNombre();
	    }).setSortable(true).setWidth("200px")
		.setHeader("Artista").setKey("codartista");
	
		
		productoGrid.setItems(productos );
		dataProvider = new ListDataProvider<>(productos);

		
		productoGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		
		
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		if(!productos.isEmpty()) {
			add(productoGrid);
		}else {
			add(new Span("No hay ningún "+tipo+" disponible"));
		}

	}
    
    private Boolean isProductoCesta(Producto producto, List<Producto> cesta) {
    	Boolean resultado=false;
    	for(Producto p: cesta) {
    		if(producto.equals(p)) {
    			resultado=true;
    		}
    	}
    	return resultado;
    }
}
