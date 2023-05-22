package com.tfg.springvaadin.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.UsuarioService;
import com.tfg.springvaadin.views.CdListView;
import com.tfg.springvaadin.views.CuentaView;
import com.tfg.springvaadin.views.DvdListView;
import com.tfg.springvaadin.views.HomeView;
import com.tfg.springvaadin.views.LoginView;
import com.tfg.springvaadin.views.MainView;
import com.tfg.springvaadin.views.MerchanListView;
import com.tfg.springvaadin.views.ProductoForm;
import com.tfg.springvaadin.views.ViniloListView;
import com.tfg.springvaadin.views.cesta.CestaView;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

@Service
public class AuthService {

    public class AuthException extends Exception {

    }

    private final UsuarioService usuarioService;
    //private final MailSender mailSender;

    public AuthService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Boolean authenticate(String codusuario, String password) {
        Usuario usuario = usuarioService.getUsuarioLogin(codusuario, password);
        if (usuario != null ) {
        	VaadinSession.getCurrent().getSession().setAttribute("usuarioSession",usuario);
            //VaadinSession.getCurrent().setAttribute(Usuario.class, usuario);
//            UI.getCurrent().navigate("home");
//            UI.getCurrent().getPage().reload();
            return true;
            
            //createRoutes(usuario.getRole());
        } else {
//        	Notification notification = new Notification(
//    				"Error: usuario y/o contraseña no son válidos", 2000);
//    		notification.setPosition(Position.MIDDLE);
//        	notification.open();
        	return false;
        }
    }

    private void createRoutes(String role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainView.class));
        
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(String role) {
        ArrayList<AuthorizedRoute> routes = new ArrayList<>();

        if ("USER".equals(role)) {
            //routes.add(new AuthorizedRoute("login", "Login", LoginView.class));
        	routes.add(new AuthorizedRoute("home", "Inicio", HomeView.class));
        	routes.add(new AuthorizedRoute("productos", "Productos", null));
            routes.add(new AuthorizedRoute("merchan", "Merchandising", MerchanListView.class));
            routes.add(new AuthorizedRoute("dvd", "DVD", DvdListView.class));
            routes.add(new AuthorizedRoute("cd", "CD", CdListView.class));
            routes.add(new AuthorizedRoute("vinilo", "Vinilo", ViniloListView.class));
            routes.add(new AuthorizedRoute("cuenta", "Cuenta", CuentaView.class));
            routes.add(new AuthorizedRoute("producto", "Subir Producto", ProductoForm.class));
            routes.add(new AuthorizedRoute("cesta", "Cesta", CestaView.class));

        } else if ("ADMIN".equals(role)) {
        	//routes.add(new AuthorizedRoute("login", "Login", LoginView.class));
        	routes.add(new AuthorizedRoute("home", "Inicio", HomeView.class));
        	routes.add(new AuthorizedRoute("productos", "Productos", null));
            routes.add(new AuthorizedRoute("merchan", "Merchandising", MerchanListView.class));
            routes.add(new AuthorizedRoute("dvd", "DVD", DvdListView.class));
            routes.add(new AuthorizedRoute("cd", "CD", CdListView.class));
            routes.add(new AuthorizedRoute("vinilo", "Vinilo", ViniloListView.class));
            //routes.add(new AuthorizedRoute("cuenta", "Cuenta", CuentaView.class));
            
        }else {
        	routes.add(new AuthorizedRoute("home", "Inicio", HomeView.class));
        	routes.add(new AuthorizedRoute("productos", "Productos", null));
            routes.add(new AuthorizedRoute("merchan", "Merchandising", MerchanListView.class));
            routes.add(new AuthorizedRoute("dvd", "DVD", DvdListView.class));
            routes.add(new AuthorizedRoute("cd", "CD", CdListView.class));
            routes.add(new AuthorizedRoute("vinilo", "Vinilo", ViniloListView.class));
            routes.add(new AuthorizedRoute("login", "Login", LoginView.class));
        }

        return routes;
    }


}
