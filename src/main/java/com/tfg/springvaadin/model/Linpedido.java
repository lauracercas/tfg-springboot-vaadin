package com.tfg.springvaadin.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "linpedidos")
public class Linpedido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codpedido;
	
	@Id
	private String codlinpedido;
	
	private String codproducto;
	
	private Double precio;
	
	public String getCodpedido() {
		return codpedido;
	}
	public void setCodpedido(String codpedido) {
		this.codpedido = codpedido;
	}
	
	public String getCodlinpedido() {
		return codlinpedido;
	}
	public void setCodlinpedido(String codlinpedido) {
		this.codlinpedido = codlinpedido;
	}
	public String getCodproducto() {
		return codproducto;
	}
	public void setCodproducto(String codproducto) {
		this.codproducto = codproducto;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Linpedido() {
		
	}
}
