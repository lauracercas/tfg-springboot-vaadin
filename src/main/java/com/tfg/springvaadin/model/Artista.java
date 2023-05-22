package com.tfg.springvaadin.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "artistas")
public class Artista implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer codartista;
	
	private String nombre;
	
	
	
	
	public Integer getCodartista() {
		return codartista;
	}
	public void setCodartista(Integer codartista) {
		this.codartista = codartista;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Artista() {
		
	}
	
	@Override
	public String toString() {
		return (getNombre()!=null?getNombre():"");
	} 
	
	
	
	
}
