package com.tfg.springvaadin.views;

import java.util.List;

import com.tfg.springvaadin.auth.AuthService;
import com.tfg.springvaadin.auth.AuthorizedRoute;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.tfg.springvaadin.service.UsuarioService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;



@Route(value="")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@CssImport(value = "./themes/tfgspringvaadin/styles.css")
@CssImport(value = "./themes/tfgspringvaadin/menu-bar-submenu.css", themeFor= "vaadin-context-menu-overlay")
public class MainView extends AppLayout {

	private static final long serialVersionUID = 5302972992204327329L;
	
	private final MenuBar menubar;
	
	UsuarioService usuarioService;
	
	ProductoService productoService;
	
	ArtistaService artistaService;
	
	GeneroService generoService;
	
	AuthService authService;
	
	private static final String GRISHEADER = "#d5def1";
	
	
	public MainView(UsuarioService usuarioService,ProductoService productoService,ArtistaService artistaService,
			AuthService authService,GeneroService generoService) {
		this.productoService=productoService;
		this.usuarioService=usuarioService;
		this.artistaService=artistaService;
		this.authService = authService;
		this.generoService=generoService;

		HorizontalLayout header = createHeaderLogo();
        menubar=createMenuBar();
        menubar.getStyle().set("background-color","white");
        menubar.getStyle().set("border-radius","calc(var(--lumo-size-m) / 2)");
        menubar.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
   
        HorizontalLayout menu = createMenu();
        
        addToNavbar(createTopBar(header, menu));
                
        UI.getCurrent().navigate("home");
	}

	private HorizontalLayout createMenu() {
		HorizontalLayout menu = new HorizontalLayout();
        
        Button logout = new Button("Cerrar Sesión");
        logout.getStyle().set("background-color","white");
        logout.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
        Usuario user = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");
		if(user!=null) {
			logout.setVisible(true);
		}else {
			logout.setVisible(false);
		}
        
        
        Button themeButton = new Button(VaadinIcon.MOON_O.create());
        themeButton.getStyle().set("background-color","white");
        themeButton.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
        themeButton.addClickListener(e -> {
        	ThemeList themeList = UI.getCurrent().getElement().getThemeList(); // 

            if (themeList.contains(Lumo.DARK)) { // 
              themeList.remove(Lumo.DARK);
              
              themeButton.setIcon(VaadinIcon.MOON_O.create());
              menubar.getStyle().set("background-color","white");
              menubar.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
              themeButton.getStyle().set("background-color","white");
              themeButton.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
              logout.getStyle().set("background-color","white");
              logout.getStyle().set("--lumo-primary-text-color","hsl(214, 0%, 14%)");
              menubar.getStyle().set("--lumo-primary-color-10pct", "var(--lumo-tint-5pct)");
              themeButton.getStyle().set("--lumo-primary-color-10pct", "var(--lumo-tint-5pct)");
              logout.getStyle().set("--lumo-primary-color-10pct", GRISHEADER);
            } else {
              themeList.add(Lumo.DARK);
              themeButton.setIcon(VaadinIcon.SUN_O.create());
              menubar.getStyle().set("background-color","hsl(214, 0%, 14%)");
              menubar.getStyle().set("--lumo-primary-text-color","white");
              menubar.getElement().getThemeList().remove("--lumo-primary-color-10pct");
              themeButton.getStyle().set("background-color","hsl(214, 0%, 14%)");
              themeButton.getStyle().set("--lumo-primary-text-color","white");
              logout.getStyle().set("background-color","hsl(214, 0%, 14%)");
              logout.getStyle().set("--lumo-primary-text-color","white");
            }
        });
        
        logout.addClickListener(event -> {
        	limpiarVariablesSesion();
        	UI.getCurrent().navigate("home");
        	UI.getCurrent().getPage().reload();
        	
        });
        
        menu.add(menubar,themeButton,logout);
        
        return menu;
	}
	
	private HorizontalLayout createHeaderLogo() {
 
        Image logo = new Image("images/2handmusicLogoColores.png", "Logo");
        logo.setWidth("200px");
        logo.setId("logo");
        
        HorizontalLayout header = new HorizontalLayout(logo);

        logo.getElement().getStyle().set("margin-right", "auto");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.CENTER);
        
        header.setPadding(true);
        header.setSpacing(true);
        
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setId("header");
        return header;
	}

	public void limpiarVariablesSesion() {
		VaadinSession.getCurrent().getSession().setAttribute("usuarioSession",null);
		VaadinSession.getCurrent().getSession().setAttribute("cesta",null);
		VaadinSession.getCurrent().getSession().setAttribute("tipo",null);
		VaadinSession.getCurrent().getSession().setAttribute("producto",null);
		VaadinSession.getCurrent().getSession().setAttribute("accion",null);
    	VaadinSession.getCurrent().getSession().setAttribute("codpedido",null);

	}
	
	public MenuBar createMenuBar() {
		Usuario user = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");

		List<AuthorizedRoute> routes =this.authService.getAuthorizedRoutes(user!=null?user.getRole():null);

        final MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);
        menuBar.getStyle().set("max-width", "100%");
        for(AuthorizedRoute r : routes) {
        	if(("home").equals(r.getRoute())){
        		MenuItem homeView =menuBar.addItem("Inicio");
        		homeView.addClickListener(e->{
        			homeView.getUI().ifPresent(ui -> ui.navigate("home"));
                });
        		
        	}else if(("productos").equals(r.getRoute())){
        		MenuItem productosView = menuBar.addItem("Productos");
                SubMenu productosSubMenu = productosView.getSubMenu();

                MenuItem vinilos = productosSubMenu.addItem("Vinilos");
                vinilos.addClickListener(e->{
                	VaadinSession.getCurrent().getSession().setAttribute("tipo","Vinilo");
                	getUI().ifPresent(ui -> ui.navigate("productos/vinilo"));
                });
                MenuItem cd = productosSubMenu.addItem("CDs");
                cd.addClickListener(e->{
                	VaadinSession.getCurrent().getSession().setAttribute("tipo","CD");
                	getUI().ifPresent(ui -> ui.navigate("productos/cd"));
                });
                MenuItem dvd = productosSubMenu.addItem("DVDs");
                dvd.addClickListener(e->{
                	VaadinSession.getCurrent().getSession().setAttribute("tipo","DVD");
                	getUI().ifPresent(ui -> ui.navigate("productos/dvd"));
                });
                MenuItem merchan = productosSubMenu.addItem("Merchandising");
                merchan.addClickListener(e->{
                	VaadinSession.getCurrent().getSession().setAttribute("tipo","Merchandising");
                	getUI().ifPresent(ui -> ui.navigate("productos/merchan"));
                });
        	}else if(("login").equals(r.getRoute())){
        		 MenuItem usuarioView = menuBar.addItem("Usuario");
        	        usuarioView.addClickListener(e->{
        	        	usuarioView.getUI().ifPresent(ui -> ui.navigate("login"));
        	        });
        	}else if(("cuenta").equals(r.getRoute())){
        		VaadinSession.getCurrent().getSession().setAttribute("tipo",null);
        		MenuItem micuentaView =menuBar.addItem("Mi Cuenta");
                micuentaView.addClickListener(e->{
                	micuentaView.getUI().ifPresent(ui -> ui.navigate("cuenta"));
                });
        	}else if(("producto").equals(r.getRoute())){
        		MenuItem productoView =menuBar.addItem("Subir Producto");
                productoView.addClickListener(e->{
                	VaadinSession.getCurrent().getSession().setAttribute("accion", "A");
                	productoView.getUI().ifPresent(ui -> ui.navigate("producto"));
                });
        	}else if(("cesta").equals(r.getRoute())){
        		MenuItem cestaView =menuBar.addItem(VaadinIcon.CART.create());
        		cestaView.addClickListener(e->{
                	cestaView.getUI().ifPresent(ui -> ui.navigate("cesta"));
                });
        	}
        }

        return menuBar;
    }


	private VerticalLayout createTopBar(HorizontalLayout header, HorizontalLayout menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.getStyle().set("--lumo-base-color", GRISHEADER);
        
        layout.getStyle().set("--lumo-primary-color-10pct", "var(--lumo-tint-5pct)");
        layout.getThemeList().add("dark");

        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(header, menu);
        layout.setId("navbar");
        return layout;
    }
	

}
