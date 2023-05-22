package com.tfg.springvaadin.views.cesta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.LinpedidoService;
import com.tfg.springvaadin.service.PedidoService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "cesta", layout = MainView.class)
@PageTitle("Cesta")
public class CestaView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	
	Grid<Producto> cestaGrid = new Grid<>(Producto.class,false);
	List<Producto> cesta = new ArrayList<>();
	ListDataProvider<Producto> dataProvider;
	
	ProductoService productoService;
	PedidoService pedidoService;
	LinpedidoService linPedidoService;
	
	
	String titulo;
	H3 title;
	
	Binder<Producto> binder = new Binder<>(Producto.class);
	Editor<Producto> editor = cestaGrid.getEditor();
	
	private Button btnComprar;
	private Button btnLimpiar;
	
	private NumberField total ;
	
	Dialog dialog;

	@Autowired
	public CestaView(PedidoService pedidoService) {    
		
    	cesta = (List<Producto>) VaadinSession.getCurrent().getSession().getAttribute("cesta");
		Usuario usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");

    	if (cesta==null) {
    		cesta =new ArrayList<>();
    	}
    	
    	Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
		
		cestaGrid.addComponentColumn(producto -> {
		    Button borrar = new Button(VaadinIcon.TRASH.create());
		    borrar.addClassName("borrar");
		    borrar.addClickListener(e -> {
		    	for(Producto p: cesta) {
		    		if(producto.equals(p)) {
		    			cesta.remove(producto);
		    			total.setValue(calcularTotal());
		    			if(cesta.size()==0) {
		    				clearForm();
		    			}else {
		    				cestaGrid.setItems(cesta );
		    				VaadinSession.getCurrent().getSession().setAttribute("cesta",cesta);
		    			}
		    			
		    			
		    		}
		    	}
		    });
		    borrar.setEnabled(!editor.isOpen());
		    editButtons.add(borrar);
		    return borrar;
		});
		
		cestaGrid.setWidth("1200px");
		
		cestaGrid.addColumn(Producto::getNombre).setSortable(true).setWidth("200px")
		.setHeader("Nombre").setKey("nombre");
		
		cestaGrid.addColumn(Producto::getDescripcion).setSortable(true).setWidth("220px")
		.setHeader("Descripcion").setKey("descripcion");
		
		cestaGrid.addColumn(Producto::getTipo).setSortable(true).setWidth("180px")
		.setHeader("Tipo").setKey("tipo");
		
		cestaGrid.addColumn(new NumberRenderer<>(
				Producto::getPrecio, "%(,.2f €",
		        Locale.FRANCE, "0.00 €")).setSortable(true).setWidth("110px")
		.setHeader("Precio").setKey("precio");
		
		cestaGrid.setItems(cesta );
		dataProvider = new ListDataProvider<>(cesta);
		
		cestaGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

		
		
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		
		HorizontalLayout botones = new HorizontalLayout();
		btnComprar = new Button("Comprar");
		btnComprar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLimpiar = new Button("Limpiar");
        botones.add(btnComprar,btnLimpiar);
		
        btnLimpiar.addClickListener(e -> 
        clearForm()
        );
        btnComprar.addClickListener(e -> {
        	dialog=new Dialog();
        	if(!cesta.isEmpty()) {
        		
        		dialog.setCloseOnEsc(false);
		    	dialog.setCloseOnOutsideClick(false);
        		Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> {
        		    dialog.close();
        		});
        		PedidoForm pedidoform = new PedidoForm(pedidoService,this, total.getValue());
        		dialog.add(cancelButton,pedidoform);
        		dialog.open();
        		
        	}
        });
        
        total = new NumberField("Total");
        total.setReadOnly(true);
        total.setPrefixComponent(VaadinIcon.EURO.create());
        total.setValue(calcularTotal());
        
        if(!cesta.isEmpty()) {
        	cestaGrid.setHeight("300px");
        	add(cestaGrid);
        	add(total);
            add(botones);
            
		}else {
			VerticalLayout vacio = new VerticalLayout();
			vacio.add(new H3("La cesta está vacía."));
			vacio.setAlignItems(Alignment.CENTER);
			add(vacio);
		}
		
	}
	
	private void clearForm() {
		VaadinSession.getCurrent().getSession().setAttribute("cesta",null);
		cestaGrid.setItems(new ArrayList<>());
		total.setValue(0d);
		UI.getCurrent().getPage().reload();
    }
	
	private Double calcularTotal() {
		Double total = 0d;
		for(Producto p : cesta) {
			total+=p.getPrecio();
		}
		return total;
	}
	
	public void cerrarCesta() {
		dialog.close();
		clearForm();
		Notification notification = new Notification(
    	        "Pedido hecho correctamente", 2000);
		notification.setPosition(Position.MIDDLE);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    	notification.open();
	}
	
}
