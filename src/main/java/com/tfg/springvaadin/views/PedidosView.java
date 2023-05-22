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

import com.tfg.springvaadin.filter.PedidoFilter;
import com.tfg.springvaadin.model.Pedido;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.LinpedidoService;
import com.tfg.springvaadin.service.PedidoService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.tfg.springvaadin.spec.PedidoSpecification;
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

@Route(value = "pedidos", layout = MainView.class)
@PageTitle("Pedidos")
public class PedidosView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;

	Grid<Pedido> pedidoGrid = new Grid<>(Pedido.class,false);
	List<Pedido> pedidos = new ArrayList<>();
	ListDataProvider<Pedido> dataProvider;
	
	ProductoService productoService;
	PedidoService pedidoService;
	LinpedidoService linPedidoService;
	
	TextField codpedidoFilter;
	NumberField importeFilter ;
	DatePicker fechaFilter;
	
	String titulo;
	H3 title;
	
	Binder<Pedido> binder = new Binder<>(Pedido.class);
	Editor<Pedido> editor = pedidoGrid.getEditor();
	
	Usuario usuario;
	
	@Autowired
	public PedidosView(PedidoService pedidoService,LinpedidoService linPedidoService,
			UsuarioService usuarioService, ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService) {
		this.pedidoService=pedidoService;
		this.productoService=productoService;
		this.linPedidoService=linPedidoService;
		
		usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");

		
		pedidos = this.pedidoService.findByCodusuario(usuario.getCodusuario());
		
		Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
		
		pedidoGrid.addComponentColumn(pedido -> {
		    Button info = new Button(VaadinIcon.INFO_CIRCLE.create());
		    info.addClassName("view");
		    info.addClickListener(e -> {
		    	Dialog dialog = new Dialog();
		    	VaadinSession.getCurrent().getSession().setAttribute("codpedido",pedido.getCodpedido());
		    	dialog.setCloseOnEsc(false);
		    	dialog.setCloseOnOutsideClick(false);
		    	
		    	Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
        		    dialog.close();
        		    pedidoGrid.setItems(pedidos);
        		    VaadinSession.getCurrent().getSession().setAttribute("producto",null);
        		    VaadinSession.getCurrent().getSession().setAttribute("accion",null);
        		});
		    	cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

		    	dialog.add(cancelButton,new LineasPedidoView(pedidoService,linPedidoService,usuarioService,
		    			productoService,artistaService,generoService));
		    	
        		dialog.add();
		    	dialog.open();
		    });
		    info.setEnabled(!editor.isOpen());
		    editButtons.add(info);
		    return info;
		});
		pedidoGrid.setWidth("700px");
		
		pedidoGrid.addColumn(Pedido::getCodpedido).setSortable(true).setWidth("200px")
		.setHeader("Num Pedido").setKey("codpedido");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
		
		pedidoGrid.addColumn(bean -> formatter.format(bean.getFecha()))
	    .setComparator(
            (o1, o2) -> o1.getFecha().compareTo(o2.getFecha()))
	    .setSortable(true).setWidth("200px")
	    .setHeader("Fecha").setKey("fecha");
		
		pedidoGrid.addColumn(new NumberRenderer<>(
				Pedido::getImporte, "%(,.2f €",
		        Locale.FRANCE, "0.00 €")).setSortable(true).setWidth("130px")
		.setHeader("Total Importe").setKey("importe");
		
		pedidoGrid.setItems(pedidos );
		dataProvider = new ListDataProvider<>(pedidos);
		
		pedidoGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		configurarFiltros();
		
		
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		if(pedidos!=null && !pedidos.isEmpty()) {
			add(pedidoGrid);
		}else {
			VerticalLayout vacio = new VerticalLayout();
			vacio.add(new H3("No se ha realizado ningún pedido aún"));
			vacio.setAlignItems(Alignment.CENTER);
			add(vacio);
		}
		
		
	}
	
	private void configurarFiltros() {
        HeaderRow headerRow = pedidoGrid.appendHeaderRow();

        codpedidoFilter = new TextField();
        codpedidoFilter.setPlaceholder("Num Pedido");
        
        importeFilter = new NumberField();
        importeFilter.setPlaceholder("Desde");
        
        fechaFilter = new DatePicker();
        fechaFilter.setPlaceholder("Desde");
        
        for (final Column<Pedido> column : pedidoGrid.getColumns()) {

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
		PedidoFilter pedidoFilter = new PedidoFilter();
		pedidoFilter.setCodusuarioeq(usuario.getCodusuario());
    	pedidoFilter.setCodpedidoeq(codpedidoFilter.getValue());
    	pedidoFilter.setImportege(importeFilter.getValue());
    	LocalDate fecha = fechaFilter.getValue();
    	ZoneId defaultZoneId = ZoneId.systemDefault();
    	Date date = fecha!=null?Date.from(fecha.atStartOfDay(defaultZoneId).toInstant()):null;
    	pedidoFilter.setFechage(date);
    	
    	PedidoSpecification pedidoSpec = new PedidoSpecification(pedidoFilter);

    	pedidoGrid.setItems(pedidoService.findPedidos(pedidoSpec));
    }
	
}
