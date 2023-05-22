package com.tfg.springvaadin.views.cesta;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Pedido;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.PedidoService;
import com.tfg.springvaadin.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;

@Route(value = "pagar", layout = MainView.class)
@PageTitle("Resumen Cesta")
@CssImport(value = "./themes/tfgspringvaadin/vaadin-select-styles.css", themeFor= "vaadin-select-overlay")
public class PedidoForm extends HorizontalLayout {

	PedidoService pedidoService;
	
	CestaView cestaview;
	
	
	private static final long serialVersionUID = 1L;
	// Campos Envio Formulario
	private EmailField correo;
	private IntegerField telefono;
	private TextField nombre;
	private TextField apellidos;
	private TextField direccion;
	private IntegerField codpostal;
	private TextField ciudad;
	private TextField provincia;

	// Campos Pago
	private TextField titular;
	private IntegerField numtarjeta;
	private PasswordField cvc;
	private Select<Integer> mes;
	private Select<Integer> annio;

	private Button btnPagar;
	private Button btnCancelar;

    
   
    private Binder<Pedido> binder = new Binder<>(Pedido.class);
    private Pedido pedido = new Pedido();
    private Usuario usuario;
    private List<Producto> cesta;
    //private BeanValidationBinder<Usuario> binder = new BeanValidationBinder<>(Usuario.class);

    private NumberField totalField;
    private NumberField saldoDisponible;
    private NumberField saldoUsado;
    
    
    
    Grid<Producto> resumenGrid = new Grid<>(Producto.class,false);
	ListDataProvider<Producto> dataProvider;
    
    private static final String CAMPOOBLIGATORIO = "El campo es obligatorio";
    
	DecimalFormat df = new DecimalFormat("#.##");
	
	private Double maximoSaldo;


    @Autowired
    public PedidoForm(PedidoService pedidoService, CestaView cestaview, Double total) {
    	this.pedidoService=pedidoService;
    	this.cestaview=cestaview;
    	usuario = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");
    	cesta = (List<Producto>) VaadinSession.getCurrent().getSession().getAttribute("cesta");
    	
    	
    	FormLayout form = new FormLayout();
    	form.setResponsiveSteps( new ResponsiveStep("600px", 3));
    	
    	creacionCamposForm(total);
    	
        configurarUsuario();
        
        H3 envio = new H3("Envío");
        H3 pago = new H3("Pago");
        
        form.add(envio);
        form.setColspan(envio, 3);
        form.add(correo,telefono);
    	form.setColspan(correo, 2);
    	form.add(nombre,apellidos);
    	form.setColspan(apellidos, 2);
    	form.add(direccion);
    	form.setColspan(direccion, 3);
    	form.add(codpostal,ciudad,provincia);
    	
    	form.add(pago);
        form.add(titular);
    	form.setColspan(titular, 3);
    	form.add(numtarjeta,cvc);
    	form.setColspan(numtarjeta, 2);
    	form.add(mes,annio, new EmptyFormLayoutItem());
        
        
        
        HorizontalLayout botones = new HorizontalLayout();
        btnPagar = new Button("Pagar",VaadinIcon.LOCK.create());
        btnPagar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnCancelar = new Button("Cancelar");
        botones.add(btnPagar,totalField);

        this.setWidth("1100px");
        this.setHeight("700px");
       
        
        H4 saldoTitulo = new H4("Si tiene saldo disponible puede usarlo al pagar");
        
        HorizontalLayout saldos = new HorizontalLayout();
        saldos.add(saldoDisponible, saldoUsado);
        
        //add(saldoTitulo,saldos,form);
        
        VerticalLayout resumenView = new VerticalLayout();
        H3 resumenTitulo = new H3("Resumen Pedido");
        resumenGrid.addColumn(Producto::getNombre).setSortable(true).setWidth("200px")
		.setHeader("Nombre").setKey("nombre");
        
        resumenGrid.addColumn(new NumberRenderer<>(
				Producto::getPrecio, "%(,.2f €",
		        Locale.FRANCE, "0.00 €")).setSortable(true).setWidth("110px")
		.setHeader("Precio").setKey("precio");
        
        resumenGrid.setItems(cesta );
        resumenGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
		        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        resumenGrid.setWidthFull();
        resumenGrid.setHeightFull();
        resumenView.add(resumenTitulo,resumenGrid,saldoTitulo,saldos,totalField,btnPagar);
        
        form.setWidth("700px");
        resumenView.setWidth("400px");
        
        add(form,resumenView);
        configurarValidaciones();
        
        configurarBotones();
        
        
        
    }

	private void configurarBotones() {
		btnCancelar.addClickListener(e -> {
        	
        });
        btnPagar.addClickListener(e -> {
        	if (binder.writeBeanIfValid(pedido)) {
        		pagar();
        		this.cestaview.cerrarCesta();
        		
            } else {
                binder.validate();
            }
        });
	}

	private void creacionCamposForm(Double total) {
		totalField = new NumberField("Total");
    	totalField.setReadOnly(true);
    	totalField.setPrefixComponent(VaadinIcon.EURO.create());
    	totalField.setValue(total);
    	totalField.setMin(0);
    	
    	saldoDisponible = new NumberField("Saldo disponible");
    	saldoDisponible.setReadOnly(true);
    	saldoDisponible.setPrefixComponent(VaadinIcon.EURO.create());
    	saldoDisponible.setValue(usuario.getSaldo()!=null?usuario.getSaldo():0d);
    	
    	
    	saldoUsado = new NumberField("Saldo que desea usar");
    	saldoUsado.setValue(0d);
    	saldoUsado.setHasControls(true);
    	saldoUsado.setPrefixComponent(VaadinIcon.EURO.create());
    	saldoUsado.setMin(0); 
    	maximoSaldo=calcularMax(total);
    	saldoUsado.setMax(maximoSaldo);
    	saldoUsado.setSizeFull();
    	
    	saldoUsado.addValueChangeListener(event->{
    		if(event.getValue()<=maximoSaldo) {
        		totalField.setValue(Double.parseDouble((df.format(total-event.getValue())).replace(',', '.')));
    		}else {
    			saldoUsado.setValue(maximoSaldo);
    			Notification notification = new Notification(
	        	        "Saldo supera el total del pedido", 2000);
	    		notification.setPosition(Position.MIDDLE);
	        	notification.open();
    		}

    	});
    	
        
        correo = new EmailField("Email");
        correo.setClearButtonVisible(true);
        correo.setRequiredIndicatorVisible(true);
        correo.setErrorMessage("Por favor introduzca un correo electrónico válido");
        
    	telefono = new IntegerField("Teléfono");
    	telefono.setErrorMessage("Por favor introduzca un numero de teléfono válido");
    	telefono.setClearButtonVisible(true);
    	telefono.setRequiredIndicatorVisible(true);

    	
    	nombre = new TextField("Nombre");
    	nombre.setRequired(true);
    	nombre.setClearButtonVisible(true);
    	apellidos = new TextField("Apellidos");
    	apellidos.setRequired(true);
    	apellidos.setClearButtonVisible(true);
    	direccion = new TextField("Dirección");
    	direccion.setRequired(true);
    	direccion.setClearButtonVisible(true);
    	codpostal= new IntegerField("Código Postal");
    	codpostal.setRequiredIndicatorVisible(true);
    	codpostal.setErrorMessage("Por favor introduzca un código de postal válido");
    	codpostal.setClearButtonVisible(true);
    	ciudad= new TextField("Ciudad");
    	ciudad.setRequired(true);
    	ciudad.setClearButtonVisible(true);
    	provincia= new TextField("Provincia");
    	provincia.setRequired(true);
    	provincia.setClearButtonVisible(true);

    	// Campos Pago
    	titular= new TextField("Nombre del titular");
    	titular.setRequired(true);
    	titular.setClearButtonVisible(true);
    	numtarjeta= new IntegerField("Número de tarjeta");
    	numtarjeta.setRequiredIndicatorVisible(true);
    	numtarjeta.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
    	numtarjeta.setPlaceholder("1234 5678 9123 4567");
    	//numtarjeta.setErrorMessage("Por favor introduzca un numero de tarjeta válido");
    	numtarjeta.setClearButtonVisible(true);
    	cvc= new PasswordField("Código de seguridad");
    	cvc.setPlaceholder("CVV/CVC");
    	cvc.setRequiredIndicatorVisible(true);
    	cvc.setClearButtonVisible(true);
    	mes = new Select<>();
    	mes.setRequiredIndicatorVisible(true);
    	mes.setLabel("Mes");
    	mes.setPlaceholder("mm");
    	mes.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    	annio = new Select<>();
    	annio.setLabel("Año");
    	annio.setPlaceholder("aa");
    	annio.setRequiredIndicatorVisible(true);
    	annio.setItems(20, 21, 22, 23, 24, 25);
	}
    
    private void configurarUsuario(){
    	nombre.setValue(usuario.getNombre());
    	apellidos.setValue(usuario.getApellido1()+" "+usuario.getApellido2());
    	correo.setValue(usuario.getCorreo());
    }

	private void configurarValidaciones() {
		binder.forField(correo).asRequired(CAMPOOBLIGATORIO)
        .withValidator(new EmailValidator("Por favor introduzca un email válido"))
        .bind(Pedido::getCorreo, Pedido::setCorreo);
		
		
		binder.forField(telefono).asRequired(CAMPOOBLIGATORIO)
        .withValidator(num -> String.valueOf(num).trim().length()==9,"Por favor introduzca un numero de teléfono válido")
		.bind(Pedido::getTelefono, Pedido::setTelefono);
        
        
        binder.forField(nombre).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getNombre, Pedido::setNombre);
        
        binder.forField(apellidos).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getApellidos, Pedido::setApellidos);
        
        binder.forField(direccion).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getDireccion, Pedido::setDireccion);
        
        binder.forField(codpostal).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getCodpostal, Pedido::setCodpostal);
        
        binder.forField(ciudad).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getCiudad, Pedido::setCiudad);
        
        binder.forField(provincia).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getProvincia, Pedido::setProvincia);
        
        binder.forField(titular).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getTitular, Pedido::setTitular);
        
//        binder.forField(numtarjeta)
//        .asRequired(CAMPOOBLIGATORIO)
//        .withValidator(num -> String.valueOf(num).trim().length()==16,"Por favor introduzca un numero de tarjeta válido")
//	    .bind(Pedido::getNumtarjeta, Pedido::setNumtarjeta);
        
//		
//        binder.forField(numtarjeta)
//        .asRequired(CAMPOOBLIGATORIO)
//	    .bind(Pedido::getNumtarjeta, Pedido::setNumtarjeta);
//        
        binder.forField(cvc).asRequired(CAMPOOBLIGATORIO)
        .withValidator(c -> c.trim().length()==3,"El código de seguridad debe tener 3 caractéres")
	    .bind(Pedido::getCvc, Pedido::setCvc);
        
        binder.forField(mes).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getMes, Pedido::setMes);
        
        binder.forField(annio).asRequired(CAMPOOBLIGATORIO)
	    .bind(Pedido::getAnnio, Pedido::setAnnio);
	}
	
	public void pagar() {
		usuario.setSaldo(usuario.getSaldo()-saldoUsado.getValue());
		pedidoService.crearPedido(pedido,cesta,usuario);
		
	}
	
	private Double calcularMax(Double total) {
		return saldoDisponible.getValue()<total?saldoDisponible.getValue():Math.floor(total);
	}



 
}
