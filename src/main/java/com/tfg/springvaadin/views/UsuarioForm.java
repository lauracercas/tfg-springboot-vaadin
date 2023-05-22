package com.tfg.springvaadin.views;


import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;

public class UsuarioForm extends FormLayout {

	UsuarioService usuarioService;
	
	
	private static final long serialVersionUID = 1L;
	private TextField nombre;
	private TextField codusuario;
	private TextField apellido1;
	private TextField apellido2;
    private EmailField email;
    private PasswordField password ;
    private NumberField saldo;
    private Button btnGuardar;
    private Button btnLimpiar;

    
   
    private Binder<Usuario> binder = new Binder<>(Usuario.class);
    private Usuario usuario = new Usuario();

    
    private static final String CAMPOOBLIGATORIO = "El campo es obligatorio";

    @Autowired
    public UsuarioForm(UsuarioService usuarioService, String origen) {
    	this.usuarioService=usuarioService;
 
        this.setResponsiveSteps(new ResponsiveStep("10em", 2, LabelsPosition.TOP));
         
        codusuario = new TextField("Usuario");
        codusuario.setRequired(true);
        
        nombre = new TextField("Nombre");
        nombre.setRequired(true);
        
        apellido1 = new TextField("Primer apellido");
        apellido1.setRequired(true);
        
        apellido2 = new TextField("Segundo apellido");
        
        email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);
        
        password = new PasswordField("Contraseña");
        password.setRequiredIndicatorVisible(true);
        
        saldo = new NumberField("Saldo");
        saldo.setPrefixComponent(new Icon(VaadinIcon.EURO));

        
        btnGuardar = new Button("Guardar");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLimpiar = new Button("Limpiar");
        
        configurarCampos(origen);
        
        add(codusuario,nombre, apellido1, apellido2,email, password,saldo);
        add(new HorizontalLayout(btnGuardar,btnLimpiar));
        
        
        configurarValidaciones();
        
        btnLimpiar.addClickListener(e -> clearForm());
        btnGuardar.addClickListener(e -> {
        	if(usuarioExiste() && ("R").equals(origen)) {
        		Dialog dialog = new Dialog();
        		dialog.add(new Text("Ya existe un usuario con este codigo de usuario"));

        		dialog.setWidth("400px");
        		dialog.setHeight("150px");

        		Button cancelButton = new Button("Cerrar", event -> {
        		    dialog.close();
        		});      
		    	cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        		dialog.add(new Div(cancelButton));
        		dialog.open();
        		codusuario.setValue("");
        	}else if (binder.writeBeanIfValid(usuario)) {
        		guardarDatos(origen);
            } else {
                binder.validate();
            }
        }
        
        
        
        );
        
        
    }

	private void configurarValidaciones() {
		binder.forField(nombre).asRequired(CAMPOOBLIGATORIO)
				.bind(Usuario::getNombre, Usuario::setNombre);
        
        binder.forField(email)
                .withValidator(new StringLengthValidator(
                        CAMPOOBLIGATORIO,1, null))
                .withValidator(new EmailValidator("Por favor introduzca un email válido"))
                .bind(Usuario::getCorreo, Usuario::setCorreo);
        
        binder.forField(nombre)
	        .withValidator(new StringLengthValidator(
	                CAMPOOBLIGATORIO, 1, null))
	        .bind(Usuario::getNombre, Usuario::setNombre);
        
        binder.forField(apellido1)
        .withValidator(new StringLengthValidator(
                CAMPOOBLIGATORIO, 1, null))
        .bind(Usuario::getApellido1, Usuario::setApellido1);
        
        binder.forField(apellido2)
        .bind(Usuario::getApellido2, Usuario::setApellido2);
        
        binder.forField(codusuario)
	        .withValidator(new StringLengthValidator(
	                CAMPOOBLIGATORIO, 1, null))
	        .bind(Usuario::getCodusuario, Usuario::setCodusuario);
        
        binder.forField(password)
	        .asRequired(CAMPOOBLIGATORIO)
	        .withValidator(new StringLengthValidator(
	                "La contraseña debe tener entre 6 y 15 caracteres", 6, 15))
	        .bind(Usuario::getPassword, Usuario::setPassword);
        
        binder.forField(saldo)
        .bind(Usuario::getSaldo, Usuario::setSaldo);
	}

    private void clearForm() {
        binder.setBean(new Usuario());
    }

    
    public void guardarDatos(String origen) {
    	try {
    		if(("R").equals(origen)) {
    			usuario.setRole("USER");
    			usuario.setSaldo(0d);
        	}
    		
    		usuario.setApellido2(apellido2.getValue().trim().isEmpty()?null:apellido2.getValue());
        	usuarioService.saveUsuario(usuario);
        	Notification notification = new Notification(
        	        "Guardado Correcto", 2000);
        	notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    		notification.setPosition(Position.MIDDLE);
        	notification.open();
        	VaadinSession.getCurrent().getSession().setAttribute("usuarioSession",usuario);
        	if(("R").equals(origen)) {
        		UI.getCurrent().navigate("cuenta");
        		UI.getCurrent().getPage().reload();
            	
        	}
        	
            
    	}catch (Exception e) {
    		Notification notification = new Notification(
    				"Error al guardar los datos", 2000);
    		notification.setPosition(Position.MIDDLE);
        	notification.open();
		}
    	
    	
    }
    
    private void configurarCampos(String origen) {
    	Usuario user = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");
    	if(user!=null) {
    		usuario=user;
    		binder.setBean(user);
    	}
    	if(("R").equals(origen)) {
    		codusuario.setEnabled(true);
    		nombre.setEnabled(true);
    		apellido1.setEnabled(true);
    		apellido2.setEnabled(true);
    		email.setEnabled(true);
    		password.setEnabled(true);
    		saldo.setEnabled(false);
    		saldo.setValue(0d);
    	}else {
    		codusuario.setReadOnly(true);
    		nombre.setEnabled(true);
    		apellido1.setEnabled(true);
    		apellido2.setEnabled(true);
    		email.setReadOnly(true);
    		password.setEnabled(true);
    		saldo.setReadOnly(true);
    		btnLimpiar.setVisible(false);
    	}
    }
    
    private boolean usuarioExiste() {
    	return usuarioService.getByCodusuario(codusuario.getValue())!=null;
    }
 
}
