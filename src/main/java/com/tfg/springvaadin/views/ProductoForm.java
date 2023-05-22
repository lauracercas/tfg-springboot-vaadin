package com.tfg.springvaadin.views;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

//import com.google.common.io.Files;
import com.tfg.springvaadin.model.Artista;
import com.tfg.springvaadin.model.Genero;
import com.tfg.springvaadin.model.Producto;
import com.tfg.springvaadin.model.Usuario;
import com.tfg.springvaadin.service.ArtistaService;
import com.tfg.springvaadin.service.GeneroService;
import com.tfg.springvaadin.service.ProductoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;

@Route(value = "producto",layout = MainView.class)
@CssImport(value = "./themes/tfgspringvaadin/vaadin-combo-box-styles.css", themeFor= "vaadin-combo-box-overlay")
public class ProductoForm extends VerticalLayout {

	private ProductoService productoService;
	private ArtistaService artistaService;
	private GeneroService generoService;

	
	private static final long serialVersionUID = 1L;
	private TextField nombre;
	private NumberField precio;
	private TextArea descripcion;
	private ComboBox<String> estado;
	private ComboBox<String> tipo;
	private IntegerField annio;
	private TextField titulo;
	private ComboBox<Genero> genero;
	private ComboBox<Artista> artista;
	private TextField urlvideo;
	
	private Button btnGuardar;
    private Button btnLimpiar;
    
    private Artista artistaNuevo= new Artista();
    private Genero generoNuevo=new Genero();


   
    private Binder<Producto> binder = new Binder<>(Producto.class);
    private Producto producto = new Producto();
    
    private static final String CAMPOOBLIGATORIO = "El campo es obligatorio";
    
    private Boolean edicion=false;
    
    private IFrame iFrame;
    
    Div output = new Div();
    private MemoryBuffer buffer = new MemoryBuffer();
    private Upload upload = new Upload(buffer);
    
    private byte[] bytes;
    public static final String PERSISTENCE_UNIT_NAME = "mysqldatasource"; 
    private EntityManager em;
    private EntityManagerFactory emf;
    private Query q;
    
    ByteArrayOutputStream pngContent;
    
    private Image imagen = new Image();
    
    private VerticalLayout imageContainer = new VerticalLayout();
    
    
    @Autowired
    public ProductoForm(ProductoService productoService,
    		ArtistaService artistaService,GeneroService generoService){
    	VerticalLayout vertical = new VerticalLayout();
    	this.productoService=productoService;
    	this.artistaService=artistaService;
    	this.generoService=generoService;
    	
    	Producto productoEditar = (Producto) VaadinSession.getCurrent().getSession().getAttribute("producto");
    	
    	String accion = (String) VaadinSession.getCurrent().getSession().getAttribute("accion");
    	
    	FormLayout form = new FormLayout();
    	//responsive steps si es mas pequeño la pantalla que tal las columnas son x
    	form.setResponsiveSteps(
    	           new ResponsiveStep("600px", 3));
    	form.setWidth("1000px");
 
    	
    	if(!("C").equals(accion)) {
    		nombre = new TextField("¿Que quieres vender?");
    	}else {
    		nombre = new TextField("Nombre Producto");
    	}
    	nombre.setPlaceholder("En pocas palabras...");
    	nombre.setRequired(true);
    	
    	precio = new NumberField("Precio");
    	precio.setRequiredIndicatorVisible(true);
    	precio.setPrefixComponent(new Icon(VaadinIcon.EURO));
    	
    	descripcion = new TextArea("Descripcion");
    	descripcion.setPlaceholder("Escribe algo sobre lo que quieres vender");
    	descripcion.setMaxLength(100);
    	
    	estado = new ComboBox<>("Nuevo con etiquetas", "Nuevo sin etiquetas", 
    			"Muy bueno","Bueno","Usado");
    	estado.setLabel("Estado");
    	estado.setClearButtonVisible(true);
    	estado.setRequired(true);
    	
    	tipo = new ComboBox<>();
    	tipo.setLabel("Tipo");
    	tipo.setItems(productoService.getTipos());
    	tipo.setClearButtonVisible(true);
    	tipo.setRequired(true);
    	tipo.setRequiredIndicatorVisible(true);
    	
        
    	annio = new IntegerField("Año");
    	annio.setHelperText("Año en que salió al mercado");
    	
    	titulo = new TextField("Título");
    	titulo.setVisible(false);
    	
    	tipo.addValueChangeListener(event ->{
    		if(!("Merchandising").equals(event.getValue())&& event.getValue()!=null) {
    			titulo.setVisible(true);
    			titulo.setPlaceholder("Título del disco");
    		}else {
    			titulo.setVisible(false);
    		}
    	});
    	
    	genero = new ComboBox<>();
    	genero.setLabel("Género Musical");
    	genero.setHelperText("Selecione un género musical del listado o escriba una nuevo" );
    	genero.setItems(generoService.findAll());
    	genero.addCustomValueSetListener(event ->{
    		generoNuevo.setNombre(!event.getDetail().trim().isEmpty()?event.getDetail():null);
    		genero.setValue(generoNuevo);
    	});
    	
    	artista = new ComboBox<>();
    	artista.setLabel("Artista");
    	artista.setHelperText("Selecione un artista del listado o escriba una nuevo" );
    	artista.setItems(artistaService.findAll());
    	artista.addCustomValueSetListener(event ->{
    		artistaNuevo.setNombre(!event.getDetail().trim().isEmpty()?event.getDetail():null);
    		artista.setValue(artistaNuevo);
    	});
    	iFrame = new IFrame();
    	iFrame.setVisible(false);
    	
    	urlvideo=new TextField("Video");
    	urlvideo.setClearButtonVisible(true);
    	urlvideo.setHelperText("Introduce una url de un video de Youtube");
    	urlvideo.addValueChangeListener(e ->{
    		if(!e.getValue().isEmpty() && e.getValue().length()>16) {
    			configurarVideo(e.getValue());
    		}else {
    			iFrame.setVisible(false);
    		}
    	});
    	
    	upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");
        //upload.setSizeFull();
        
        upload.addSucceededListener(event -> {
            try {
                // The image can be jpg png or gif, but we store it always as png file in this example
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream());
                 pngContent = new ByteArrayOutputStream();
                 
                ImageIO.write(inputImage, "png", pngContent);
                imagen=generateImage(pngContent.toByteArray());
                imagen.setWidthFull();
                imageContainer.removeAll();
                imageContainer.add(imagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            imageContainer.removeAll();
            output.removeAll();
            showOutput(event.getErrorMessage(), component, output);
        });

        upload.getElement().addEventListener("file-remove", event -> {
            output.removeAll();
            imageContainer.removeAll();
        });

    	form.add(nombre,precio);
    	form.setColspan(nombre, 2);
    	form.add(estado,tipo,annio);
    	
    	form.add(genero,artista,new EmptyFormLayoutItem(),titulo);
    	
    	form.setColspan(titulo, 3);
    	form.add(descripcion);
    	form.setColspan(descripcion, 3);
    	
    	HorizontalLayout multimedia = new HorizontalLayout();
    	
    	VerticalLayout video = new VerticalLayout();
    	video.add(urlvideo,iFrame);
    	VerticalLayout imagenLayout = new VerticalLayout();
    	imagenLayout.add(upload,imageContainer);
    	
    	video.setWidth("600px");
    	urlvideo.setWidth("500px");
    	iFrame.setWidth("400px");
    	imagenLayout.setWidth("600px");
    	upload.setWidth("400px");
    	imageContainer.setWidth("340px");
    	
    	multimedia.add(video,imagenLayout);
    	
    	
    	
    	HorizontalLayout botones = new HorizontalLayout();
        btnGuardar = new Button("Guardar");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLimpiar = new Button("Limpiar");
        botones.add(btnGuardar,btnLimpiar);

        vertical.setWidth("1200px");
        vertical.setAlignItems(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        vertical.add(form,multimedia,botones);
        add(vertical);
        
       
        btnLimpiar.addClickListener(e -> clearForm());
        btnGuardar.addClickListener(e -> {
        	if (binder.writeBeanIfValid(producto)) {
        		guardarDatos(accion);
            } else {
                binder.validate();
            }
        });
        
        configurarValidaciones();
        
        if(productoEditar!=null) {
        	edicion=true;
        	producto=productoEditar;
        }
        configurarFormEdicion(productoEditar,accion);
    	
    }
    
    private void configurarValidaciones() {
    	
		binder.forField(nombre).asRequired(CAMPOOBLIGATORIO)
        .bind(Producto::getNombre, Producto::setNombre);
		
		binder.forField(precio).asRequired(CAMPOOBLIGATORIO)
        .bind(Producto::getPrecio, Producto::setPrecio);
		
		binder.forField(estado).asRequired(CAMPOOBLIGATORIO)
        .bind(Producto::getEstadoproducto, Producto::setEstadoproducto);
		
		binder.forField(tipo).asRequired(CAMPOOBLIGATORIO)
        .bind(Producto::getTipo, Producto::setTipo);
		
		binder.forField(annio)
		.withValidator(annio -> annio==null || annio>0,"El año debe ser mayor que 0")
        .bind(Producto::getAnnio, Producto::setAnnio);
		

    	binder.forField(descripcion).bind(Producto::getDescripcion, Producto:: setDescripcion);

		
		binder.forField(urlvideo)
		.withValidator(url -> this.isVideoValido(url),"La url no es válida")
        .bind(Producto::getUrlvideo, Producto::setUrlvideo);
		

		
	}

    private void clearForm() {
    	genero.setValue(null);
    	artista.setValue(null);
    	titulo.setValue(null);
    	imageContainer.removeAll();
    	output.removeAll();
    	artista.setItems(artistaService.findAll());
    	genero.setItems(generoService.findAll());
        binder.setBean(new Producto());
    }
    
    

    
    public void guardarDatos(String accion) {
    	try {
    		
    		if(artista.getValue()!=null && artista.getValue().getCodartista()!=null) {
    			producto.setCodartista(artista.getValue().getCodartista());
    		}else if(artistaNuevo.getNombre()!=null) {
    			artistaNuevo.setCodartista(artistaService.siguienteCodartista());
    			artistaService.saveArtista(artistaNuevo);
    			producto.setCodartista(artistaNuevo.getCodartista());
    		}
    		if(genero.getValue()!=null && genero.getValue().getCodgenero()!=null) {
    			producto.setGenero(genero.getValue().getCodgenero());
    		}else if(generoNuevo.getNombre()!=null){
    			generoNuevo.setCodgenero(generoService.siguienteCodgenero());
    			generoService.saveGenero(generoNuevo);
    			producto.setGenero(generoNuevo.getCodgenero());
    		}
    		if(producto.getCodproducto()==null) {
        		producto.setCodproducto(productoService.generarCodProducto(producto.getTipo()));

    		}
    		if(producto.getDescripcion().trim().isEmpty()) {
    			producto.setDescripcion(null);
    		}
    		
    		if(producto.getEstadoPedido()==null) {
    			producto.setEstadopedido("En Venta");
    		}
    		if(producto.getFechapublicacion()==null) {
    			producto.setFechapublicacion(new Date());
    		}
    		if(producto.getUrlvideo().trim().isEmpty()) {
    			producto.setUrlvideo(null);
    		}
    		
    		if(("A").equals(accion)) {
    			Usuario user = (Usuario) VaadinSession.getCurrent().getSession().getAttribute("usuarioSession");
    			producto.setCodusuario(user.getCodusuario());
    		}

    		productoService.saveProducto(producto);
    		
    		if(pngContent!=null) {
    			Path directorio = Paths.get("src//main//resources//META-INF//resources//images");

        		FileOutputStream output = new FileOutputStream(directorio+"/"+producto.getCodproducto()+".png");
                output.write(pngContent.toByteArray());
                output.close();
    		}

        	Notification notification = new Notification(
        	        "Guardado Correcto", 3000);
        	notification.setPosition(Position.MIDDLE);
        	notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        	notification.open();
        	UI.getCurrent().navigate("producto");
        	UI.getCurrent().getPage().reload();

        	
        	
    	}catch (Exception e) {
    		Notification notification = new Notification(
        	        "Error al guardar los datos, contacte con el administrador", 3000);
        	notification.setPosition(Position.TOP_START);
        	notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        	notification.open();
		}
    }
    
    public void configurarFormEdicion(Producto producto, String accion) {
		if (producto != null) {
			binder.setBean(producto);
			genero.setValue(generoService.findByCodgenero(producto.getGenero()));
			artista.setValue(artistaService.findByCodartista(producto.getCodartista()));
			btnLimpiar.setVisible(false);

			File file = new File("C:\\Users\\spica\\Documents\\Laura\\tfgspringvaadin\\src\\main\\resources\\META-INF\\resources\\images\\"+producto.getCodproducto() + ".png");
			if (file.isFile()) {
				imagen = new Image("images/" + producto.getCodproducto() + ".png", "imagen");

			} else {
				imagen = null;
			}

			imageContainer.removeAll();
			if(imagen!=null) {
				imagen.setWidthFull();
				imageContainer.add(imagen);
			}
		}
		
        
    	 if(("C").equals(accion)) {
    		 nombre.setReadOnly(true);
    		 precio.setReadOnly(true);
    		 descripcion.setReadOnly(true);
    		 estado.setReadOnly(true);
    		 tipo.setReadOnly(true);
    		 annio.setReadOnly(true);
    		 titulo.setReadOnly(true);
    		 genero.setReadOnly(true);
    		 genero.setHelperText(null);
    		 artista.setReadOnly(true);
    		 artista.setHelperText(null);
    		 btnGuardar.setVisible(false);
    		 urlvideo.setReadOnly(true);
    		 upload.setVisible(false);
    	 }else {
    		 nombre.setReadOnly(false);
    		 precio.setReadOnly(false);
    		 descripcion.setReadOnly(false);
    		 estado.setReadOnly(false);
    		 tipo.setReadOnly(false);
    		 annio.setReadOnly(false);
    		 titulo.setReadOnly(false);
    		 genero.setReadOnly(false);
    		 genero.setHelperText("Selecione un género musical del listado o escriba una nuevo" );
    		 artista.setHelperText("Selecione un artista del listado o escriba una nuevo" );
    		 artista.setReadOnly(false);
    		 btnGuardar.setVisible(true);
    		 urlvideo.setReadOnly(false);
    		 upload.setVisible(true);

    	 }
    }
    
    private void configurarVideo(String video) {
    	String part1= video.substring(0,8);
		String url=null;
		if("https://".equals(part1)) {
			url=video.substring(32,43);
		}else {
			part1= video.substring(0,16);
			if("www.youtube.com/".equals(part1)) {
				url=video.substring(24,35);
			}
		}
		
		if(url!=null) {
			iFrame.setSrc("https://www.youtube.com/embed/"+url);
	        iFrame.setHeight("315px");
	        iFrame.setWidth("560px");
	        iFrame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
	        iFrame.getElement().setAttribute("allowfullscreen", true);
	        iFrame.getElement().setAttribute("frameborder", "0");
	        iFrame.setVisible(true);
		}else {
			urlvideo.setValue(null);
			iFrame.setVisible(false);
		}
    }
    
    private Boolean isVideoValido(String video) {
    	Boolean valido=video.isEmpty();
    	if(video.length()>8) {
    		String part1= video.substring(0,8);
    		if("https://".equals(part1)) {
    			valido=true;
    		}else {
    			part1= video.substring(0,16);
    			if("www.youtube.com/".equals(part1)) {
    				valido=true;
    			}
    			
    		}
    	}
		return valido;
    }


      
      

      private void showOutput(String text, Component content,
              HasComponents outputContainer) {
          HtmlComponent p = new HtmlComponent(Tag.P);
          p.getElement().setText(text);
          outputContainer.add(p);
          outputContainer.add(content);
      }
      
      public Image generateImage(byte[] image) {
    	    StreamResource sr = new StreamResource("user", () ->  {
    	        return new ByteArrayInputStream(image);
    	    });
    	    Image i = new Image(sr, "profile-picture");
    	    return i;
    	}
      

    
}
