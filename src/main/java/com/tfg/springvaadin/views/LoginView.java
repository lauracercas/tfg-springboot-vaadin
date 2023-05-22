package com.tfg.springvaadin.views;


import org.springframework.beans.factory.annotation.Autowired;

import com.tfg.springvaadin.auth.AuthService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login")
public class LoginView extends HorizontalLayout {
	
	private static final long serialVersionUID = 1L;
	
	private UsuarioForm usuarioForm;
	
	@Autowired
    public LoginView(UsuarioService usuarioService,AuthService autenticacionService) {

    	this.setAlignItems(Alignment.CENTER);
    	this.setMargin(true);
    	this.setSpacing(true);
        VerticalLayout loginForm = new VerticalLayout();
        loginForm.setAlignItems(Alignment.CENTER);

    	
        LoginForm login = new LoginForm();
        login.setI18n(createSpanishI18n());
        login.setForgotPasswordButtonVisible(false);
        login.addLoginListener(e -> {

        	if(Boolean.TRUE.equals(autenticacionService.authenticate(e.getUsername(), e.getPassword()))){
        		UI.getCurrent().navigate("home");
                UI.getCurrent().getPage().reload();
        	}else {
        		login.setError(true);
        	}

    	    
    	});
        loginForm.add(login);
        
        VerticalLayout registro = new VerticalLayout();
        registro.setPadding(true);
        registro.setAlignItems(Alignment.BASELINE);
        H2 titlereg = new H2();
        titlereg.add(new Icon(VaadinIcon.USER_CHECK));
        titlereg.add(new Text("Crear Cuenta"));
        registro.add(titlereg);
        
        usuarioForm = new UsuarioForm(usuarioService,"R");
        registro.add(usuarioForm);
        add(loginForm,new HorizontalLayout(registro));
        
       
    }
    
    private LoginI18n createSpanishI18n() {
        LoginI18n i18n = LoginI18n.createDefault();
        
        i18n.setHeader(new LoginI18n.Header());
        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Acceda a su cuenta");
        i18n.getForm().setSubmit("Iniciar sesión");
        i18n.getForm().setPassword("Contraseña");
        i18n.getErrorMessage().setTitle("Usuario/Contraseña inválidos");
        i18n.getErrorMessage()
                .setMessage("");
        return i18n;
    }
}
