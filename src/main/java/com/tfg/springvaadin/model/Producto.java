package com.tfg.springvaadin.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String codproducto;
	
	private String nombre;
	
	private Double precio;
	
	private String descripcion;
	
	/*
	 * Nuevo con etiquetas
	 * Nuevo sin etiquetas
	 * Muy bueno
	 * Bueno
	 * Usado
	 */
	private String estadoproducto;
	
	/*
	 * CD, DVD, Vinilo, Merchandising
	 */
	private String tipo;
	
	/*
	 * Vendido, Reservado, En venta
	 */
	private String estadopedido;
	
	private Date fechapublicacion;
	
	private Date fechaventa;
	
	private String codusuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codusuario", referencedColumnName = "codusuario",nullable = false,insertable = false,updatable = false)
	private Usuario usuarioref;


	private Integer annio;

	private String titulo;
	private Integer codgenero;
	private Integer codartista;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codartista",referencedColumnName = "codartista", nullable = false,insertable = false,updatable = false)
	private Artista artistaref;
	
	private String urlvideo;
	

	public String getCodproducto() {
		return codproducto;
	}

	public void setCodproducto(String codproducto) {
		this.codproducto = codproducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstadoproducto() {
		return estadoproducto;
	}

	public void setEstadoproducto(String estadoproducto) {
		this.estadoproducto = estadoproducto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstadoPedido() {
		return estadopedido;
	}

	public void setEstadopedido(String estadopedido) {
		this.estadopedido = estadopedido;
	}

	public Date getFechapublicacion() {
		return fechapublicacion;
	}

	public void setFechapublicacion(Date fechapublicacion) {
		this.fechapublicacion = fechapublicacion;
	}

	public Date getFechaventa() {
		return fechaventa;
	}

	public void setFechaventa(Date fechaventa) {
		this.fechaventa = fechaventa;
	}

	public String getCodusuario() {
		return codusuario;
	}

	public void setCodusuario(String codusuario) {
		this.codusuario = codusuario;
	}

	public Usuario getUsuarioref() {
		return usuarioref;
	}

	public void setUsuarioref(Usuario usuarioref) {
		this.usuarioref = usuarioref;
	}

	public Integer getAnnio() {
		return annio;
	}

	public void setAnnio(Integer annio) {
		this.annio = annio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getGenero() {
		return codgenero;
	}

	public void setGenero(Integer codgenero) {
		this.codgenero = codgenero;
	}

	public Integer getCodartista() {
		return codartista;
	}

	public void setCodartista(Integer codartista) {
		this.codartista = codartista;
	}

	public Artista getArtistaref() {
		return artistaref;
	}

	public void setArtistaref(Artista artistaref) {
		this.artistaref = artistaref;
	}
	
	

	public String getUrlvideo() {
		return urlvideo;
	}

	public void setUrlvideo(String urlvideo) {
		this.urlvideo = urlvideo;
	}

	public Producto() {
		
	}
	
	@Override
	public String toString() {
		return (getNombre()!=null?getNombre():"");
	} 
}
