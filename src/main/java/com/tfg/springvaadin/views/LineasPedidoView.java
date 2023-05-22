package com.tfg.springvaadin.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.LinpedidoService;
import com.tfg.springvaadin.service.PedidoService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "lineaspedidos", layout = MainView.class)
@PageTitle("Pedidos")
public class LineasPedidoView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;

	Grid<Producto> linpedidoGrid = new Grid<>(Producto.class,false);
	List<Producto> linpedido = new ArrayList<>();
	ListDataProvider<Producto> dataProvider;
	
	ProductoService productoService;
	PedidoService pedidoService;
	LinpedidoService linPedidoService;
	
	TextField codpedidoFilter;
	NumberField importeFilter ;
	DatePicker fechaFilter;
	
	String titulo;
	H3 title;
	
	Binder<Producto> binder = new Binder<>(Producto.class);
	Editor<Producto> editor = linpedidoGrid.getEditor();
	
	
	
	@Autowired
	public LineasPedidoView(PedidoService pedidoService,LinpedidoService linPedidoService,
			UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.pedidoService=pedidoService;
		this.productoService=productoService;
		this.linPedidoService=linPedidoService;
		
		String codpedido = (String) VaadinSession.getCurrent().getSession().getAttribute("codpedido");
		
		linpedido = this.productoService.findProductosLineaPedidos(codpedido);
		
		Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
		
		linpedidoGrid.addComponentColumn(producto -> {
		    Button info = new Button(VaadinIcon.INFO_CIRCLE.create());
		    info.addClassName("view");
		    info.addClickListener(e -> {
		    	Dialog dialog = new Dialog();
		    	VaadinSession.getCurrent().getSession().setAttribute("producto",producto);
		    	dialog.setCloseOnEsc(false);
		    	dialog.setCloseOnOutsideClick(false);
		    	
		    	Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
        		    dialog.close();
        		    linpedidoGrid.setItems(linpedido);
        		    VaadinSession.getCurrent().getSession().setAttribute("producto",null);
        		    VaadinSession.getCurrent().getSession().setAttribute("accion", null);
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
		
		linpedidoGrid.setWidth("1200px");
		
		linpedidoGrid.addColumn(Producto::getNombre).setSortable(true).setWidth("200px")
		.setHeader("Nombre").setKey("nombre");
		
		linpedidoGrid.addColumn(Producto::getDescripcion).setSortable(true).setWidth("220px")
		.setHeader("Descripcion").setKey("descripcion");
		
		linpedidoGrid.addColumn(new NumberRenderer<>(
				Producto::getPrecio, "%(,.2f €",
		        Locale.FRANCE, "0.00 €")).setSortable(true).setWidth("110px")
		.setHeader("Precio").setKey("precio");
		
		linpedidoGrid.addColumn(Producto::getTipo).setSortable(true).setWidth("180px")
		.setHeader("Tipo").setKey("tipo");
		
		
		linpedidoGrid.setItems(linpedido );
		dataProvider = new ListDataProvider<>(linpedido);
		
		linpedidoGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		configurarFiltros();
		
		
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		add(linpedidoGrid);
		
	}
	
	private void configurarFiltros() {
        HeaderRow headerRow = linpedidoGrid.appendHeaderRow();

        codpedidoFilter = new TextField();
        codpedidoFilter.setPlaceholder("Num Pedido");
        
        importeFilter = new NumberField();
        importeFilter.setPlaceholder("Desde");
        
        fechaFilter = new DatePicker();
        fechaFilter.setPlaceholder("Desde");
        
        for (final Column<Producto> column : linpedidoGrid.getColumns()) {

	        if(("codpedido").equals(column.getKey())) {
	        	codpedidoFilter.setClearButtonVisible(true);
	        	codpedidoFilter.setValueChangeMode(ValueChangeMode.LAZY);
	        	codpedidoFilter.setWidth("185px");
	        	codpedidoFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(codpedidoFilter);
        	}else if(("importe").equals(column.getKey())) {
        		importeFilter.setClearButtonVisible(true);
        		importeFilter.setWidth("95px");
        		importeFilter.setValueChangeMode(ValueChangeMode.LAZY);
        		importeFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(importeFilter);
        	}else if(("fecha").equals(column.getKey())) {
        		fechaFilter.setWidth("160px");
        		fechaFilter.setClearButtonVisible(true);
        		fechaFilter.addValueChangeListener(event -> this.filtrarListado());
        		headerRow.getCell(column).setComponent(fechaFilter);
        	}
        	
        }
        
    }
	
	private void filtrarListado(){
		//TODO
    }
	
}
