package com.tfg.springvaadin.views;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.filter.ProductoFilter;
import com.tfg.springvaadin.model.Artista;
import com.tfg.springvaadin.model.Genero;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.tfg.springvaadin.spec.ProductoSpecification;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = "productos", layout = MainView.class)
@PageTitle("Productos")
public class ProductosView extends VerticalLayout{

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
	ComboBox<String> estadoPedidoFilter;
	ComboBox<String> estadoFilter;
	
	String titulo;
	H3 title;
	
	Binder<Producto> binder = new Binder<>(Producto.class);
	Editor<Producto> editor = productoGrid.getEditor();
	
	String tipo;
	Usuario usuario;
	
	
	
	@Autowired
	public ProductosView(UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.productoService=productoService;
		this.artistaService=artistaService;
		this.generoService=generoService;
		
		usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");
    	tipo = (String) VaadinSession.getCurrent().getSession().getAttribute("tipo");
 
		
		if(tipo!=null) {
		    productos = this.productoService.findByTipoAndFechaventaNull(tipo);
	    }else if(usuario!=null) {
			productos = this.productoService.findByCodusuario(usuario.getCodusuario());
		}else{
	    	productos = this.productoService.findAll();
	    }
		
		Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
		
		if(tipo==null) {
			productoGrid.addComponentColumn(producto -> {
			    Button edit = new Button(VaadinIcon.EDIT.create());
			    edit.addClassName("edit");
			    edit.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			    edit.addClickListener(e -> {
			    	Dialog dialog = new Dialog();
			    	VaadinSession.getCurrent().getSession().setAttribute("producto",producto);
			    	dialog.setCloseOnEsc(false);
			    	dialog.setCloseOnOutsideClick(false);
			    	
			    	//arriba izquierda, ver como ponerlo a la derecha arriba??
			    	Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
	        		    dialog.close();
	        		    if(tipo!=null) {
		        		    productos = this.productoService.findByTipoAndFechaventaNull(tipo);
	        		    }else if(usuario!=null) {
	        				productos = this.productoService.findByCodusuario(usuario.getCodusuario());
	        			}else{
	        		    	productos = this.productoService.findAll();
	        		    }
	        		    productoGrid.setItems(productos);
	        		    VaadinSession.getCurrent().getSession().setAttribute("producto",null);
	        		    VaadinSession.getCurrent().getSession().setAttribute("accion",null);
	        		});
			    	cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

			    	VaadinSession.getCurrent().getSession().setAttribute("accion", "E");
			    	dialog.add(cancelButton,new ProductoForm(productoService, artistaService,
			    			generoService));
			    	
			    	        		
	        		dialog.add();
			    	dialog.open();
			    });
			    edit.setEnabled(!editor.isOpen());
			    editButtons.add(edit);
			    return edit;
			});
			
			productoGrid.addComponentColumn(producto -> {
			    Button delete = new Button(VaadinIcon.TRASH.create());
			    delete.addClassName("delete");
			    delete.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			    delete.addClickListener(e -> {
			    	
			    	Dialog dialog = new Dialog();
			    	dialog.add(new Text("¿Estás seguro que quiere eliminar este producto?"));
			    	dialog.setCloseOnEsc(false);
			    	dialog.setCloseOnOutsideClick(false);

			    	Button confirmButton = new Button("Eliminar", event -> {
			    		this.borrarProducto(producto);
			    	    dialog.close();
			    	});
			    	confirmButton.setThemeName(Lumo.DARK);
			    	Button cancelButton = new Button("Cancelar", event -> {
			    	    dialog.close();
			    	    if(tipo!=null) {
		        		    productos = this.productoService.findByTipoAndFechaventaNull(tipo);
	        		    }else if(usuario!=null) {
	        				productos = this.productoService.findByCodusuario(usuario.getCodusuario());
	        			}else{
	        		    	productos = this.productoService.findAll();
	        		    }
	        		    productoGrid.setItems(productos);
	        		    VaadinSession.getCurrent().getSession().setAttribute("producto",null);
	        		    VaadinSession.getCurrent().getSession().setAttribute("accion",null);
			    	});
			    	
			    	cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

			    	dialog.add(new Div( confirmButton, cancelButton));
			    	
			    	dialog.open();
			    });
			    delete.setEnabled(!editor.isOpen());
			    editButtons.add(delete);
			    return delete;
			});
		}else {
			productoGrid.addComponentColumn(producto -> {
			    Button info = new Button(VaadinIcon.INFO_CIRCLE.create());
			    info.addClassName("view");
			    info.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			    info.addClickListener(e -> {
			    	Dialog dialog = new Dialog();
			    	VaadinSession.getCurrent().getSession().setAttribute("producto",producto);
			    	dialog.setCloseOnEsc(false);
			    	dialog.setCloseOnOutsideClick(false);
			    	
			    	//arriba izquierda, ver como ponerlo a la derecha arriba??
			    	Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
	        		    dialog.close();
	        		    if(tipo!=null) {
		        		    productos = this.productoService.findByTipoAndFechaventaNull(tipo);
	        		    }else if(usuario!=null) {
	        				productos = this.productoService.findByCodusuario(usuario.getCodusuario());
	        			}else{
	        		    	productos = this.productoService.findAll();
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
			
		}
		if(usuario!=null && this.tipo!=null) {
			productoGrid.addComponentColumn(producto -> {
			    Button shop = new Button(VaadinIcon.CART.create());
			    shop.addClassName("shop");
			    shop.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
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
			        	        "El producto ya está en la Cesta", 2000);
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
		
		if(this.tipo==null) {
			productoGrid.addColumn(Producto::getTipo).setSortable(true).setWidth("180px")
			.setHeader("Tipo").setKey("tipo");
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
		
		productoGrid.addColumn(bean -> formatter.format(bean.getFechapublicacion()))
	    .setComparator(
            (o1, o2) -> o1.getFechapublicacion().compareTo(o2.getFechapublicacion()))
	    .setSortable(true).setWidth("200px")
	    .setHeader("Fecha Publicacion").setKey("fechapublicacion");
		
		productoGrid.addColumn(Producto::getAnnio).setSortable(true).setWidth("110px")
		.setHeader("Año").setKey("annio");

		
		productoGrid.addColumn(producto -> { 
	        Artista artista = artistaService.findByCodartista(producto.getCodartista());
	        return artista == null ? "" : artista.getNombre();
	    }).setSortable(true).setWidth("200px")
		.setHeader("Artista").setKey("codartista");

		
		productoGrid.addColumn(producto -> { 
	        Genero genero = generoService.findByCodgenero(producto.getGenero());
	        return genero == null ? "" : genero.getNombre();
	    }).setSortable(true).setWidth("170px")
		.setHeader("Genero").setKey("genero");
		
		productoGrid.addColumn(Producto::getEstadoproducto).setSortable(true).setWidth("180px")
		.setHeader("Producto").setKey("estadoproducto");
		
		if(usuario!=null) {
			productoGrid.addColumn(Producto::getEstadoPedido).setSortable(true).setWidth("180px")
			.setHeader("Estado").setKey("estadopedido");
		}
		
		
		
		productoGrid.setItems(productos );
		dataProvider = new ListDataProvider<>(productos);
		
		
		productoGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		configurarFiltros();
		
		
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		add(productoGrid);


	
	}
	
	private void configurarFiltros() {
        HeaderRow headerRow = productoGrid.appendHeaderRow();

        nombreFilter = new TextField();
        
        descripcionFilter = new TextField();
        
        tipoFilter = new ComboBox<>();
        tipoFilter.setItems(productoService.getTipos());
        
        precioFilter = new NumberField();
        precioFilter.setPlaceholder("Desde");
        
        fechaPubFilter = new DatePicker();
        fechaPubFilter.setPlaceholder("Desde");
        
        annioFilter = new IntegerField();
        annioFilter.setPlaceholder("Desde");
        
        artistaFilter = new ComboBox<>();
        artistaFilter.setItems(artistaService.findAll());
        
        generoFilter = new ComboBox<>();
        generoFilter.setItems(this.generoService.findAll());
        
        estadoFilter = new ComboBox<>();
        estadoFilter.setItems("Nuevo con etiquetas", "Nuevo sin etiquetas", 
    			"Muy bueno","Bueno","Usado");
        
        estadoPedidoFilter = new ComboBox<>();
        estadoPedidoFilter.setItems("Vendido", "Reservado", "En venta");
        
        for (final Column<Producto> column : productoGrid.getColumns()) {

	        if(("nombre").equals(column.getKey())) {
	        	nombreFilter.setClearButtonVisible(true);
        		nombreFilter.setValueChangeMode(ValueChangeMode.LAZY);
        		nombreFilter.setWidth("185px");
        		nombreFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(nombreFilter);
        	}else if(("descripcion").equals(column.getKey())) {
        		descripcionFilter.setClearButtonVisible(true);
        		descripcionFilter.setWidth("185px");
        		descripcionFilter.setValueChangeMode(ValueChangeMode.LAZY);
        		descripcionFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(descripcionFilter);
        	}else if(("precio").equals(column.getKey())) {
        		precioFilter.setClearButtonVisible(true);
        		precioFilter.setWidth("95px");
        		precioFilter.setValueChangeMode(ValueChangeMode.LAZY);
        		precioFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(precioFilter);
        	}else if(this.tipo==null && ("tipo").equals(column.getKey())) {
        		tipoFilter.setWidth("140px");
        		tipoFilter.setClearButtonVisible(true);
        		tipoFilter.addValueChangeListener(event -> 
        		this.filtrarListado()
        		);
        		headerRow.getCell(column).setComponent(tipoFilter);
        	}
        	else if(("fechapublicacion").equals(column.getKey())) {
        		fechaPubFilter.setWidth("160px");
        		fechaPubFilter.setClearButtonVisible(true);
        		fechaPubFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(fechaPubFilter);
        	}else if(("annio").equals(column.getKey())) {
        		annioFilter.setClearButtonVisible(true);
        		annioFilter.setWidth("95px");
        		annioFilter.setValueChangeMode(ValueChangeMode.LAZY);
        		annioFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(annioFilter);
        	}else if(("codartista").equals(column.getKey())) {
        		artistaFilter.setClearButtonVisible(true);
        		artistaFilter.setWidth("185px");
        		artistaFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(artistaFilter);
        	}else if(("genero").equals(column.getKey())) {
        		generoFilter.setClearButtonVisible(true);
        		generoFilter.setWidth("155px");
        		headerRow.getCell(column).setComponent(generoFilter);
        		generoFilter.addValueChangeListener(event -> this.filtrarListado());
        	}else if(("estadoproducto").equals(column.getKey())) {
        		estadoFilter.setWidth("140px");
        		estadoFilter.setClearButtonVisible(true);
        		estadoFilter.addValueChangeListener(event -> 
        		this.filtrarListado()
        		);
        		headerRow.getCell(column).setComponent(estadoFilter);
        	}else if(this.usuario!=null && ("estadopedido").equals(column.getKey())) {
        		estadoPedidoFilter.setWidth("140px");
        		estadoPedidoFilter.setClearButtonVisible(true);
        		estadoPedidoFilter.addValueChangeListener(event -> 
        		this.filtrarListado()
        		);
        		headerRow.getCell(column).setComponent(estadoPedidoFilter);
        	}
        	
        }
        
    }
    
    private void filtrarListado(){
    	ProductoFilter productoFilter = new ProductoFilter();
    	productoFilter.setNombreilike(nombreFilter.getValue());
    	productoFilter.setDescripcionilike(descripcionFilter.getValue());
    	productoFilter.setPrecioge(precioFilter.getValue());
    	if(this.tipo!=null) {
    		productoFilter.setTipoeq(this.tipo);
    	}else {
    		productoFilter.setTipoeq(tipoFilter.getValue());
    	}
    	if(this.usuario!=null) {
    		productoFilter.setCodusuarioeq(usuario.getCodusuario());
    		productoFilter.setEstadoPedidoeq(estadoPedidoFilter.getValue());
    	}else {
    		productoFilter.setEstadoPedidonoteq("Vendido");
    	}
    	
    	LocalDate fecha = fechaPubFilter.getValue();
    	ZoneId defaultZoneId = ZoneId.systemDefault();
    	Date date = fecha!=null?Date.from(fecha.atStartOfDay(defaultZoneId).toInstant()):null;
    	productoFilter.setFechapublicacionge(date);
    	productoFilter.setAnnioge(annioFilter.getValue());
    	//cambiar a artista
    	productoFilter.setArtistaeq(artistaFilter.getValue()!=null?artistaFilter.getValue().getCodartista():null);
    	productoFilter.setGeneroeq(generoFilter.getValue()!=null?generoFilter.getValue().getCodgenero():null);
    	productoFilter.setEstadoProductoeq(estadoFilter.getValue());
    	
    	ProductoSpecification productoSpec = new ProductoSpecification(productoFilter);

    	
    	productoGrid.setItems(productoService.findProductos(productoSpec));
    	
    }
    
    private void borrarProducto(Producto producto) {
    	try {
    		this.productoService.deleteProducto(producto);
    		Span content = new Span("El producto se ha eliminado correctamente");
    		Notification notification = new Notification(content);
    		notification.setDuration(2000);
    		notification.setPosition(Position.MIDDLE);
    		productos = this.productoService.findAll();
    		productoGrid.setItems(productos);
		    notification.open();
    	}catch (Exception e) {
    		Notification notification = new Notification(
    				"Error al borrar el producto", 3000);
    		notification.setPosition(Position.MIDDLE);
        	notification.open();
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
