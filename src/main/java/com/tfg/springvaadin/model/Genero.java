package com.tfg.springvaadin.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "generos")
public class Genero implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer codgenero;
	
	private String nombre;
	
	
	
	
	public Integer getCodgenero() {
		return codgenero;
	}
	public void setCodgenero(Integer codgenero) {
		this.codgenero = codgenero;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Genero() {
		
	}
	
	@Override
	public String toString() {
		return (getNombre()!=null?getNombre():"");
	} 
	
	
	
	
}
