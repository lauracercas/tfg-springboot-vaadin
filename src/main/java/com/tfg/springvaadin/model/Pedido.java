package com.tfg.springvaadin.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String codpedido;
	
	private String codusuario;
	
	private Date fecha;
	
	private Double importe;
	
	
	private String correo;
	private Integer telefono;
	private String nombre;
	private String apellidos;
	private String direccion;
	private Integer codpostal;
	private String ciudad;
	private String provincia;

	private String titular;
	private Integer numtarjeta;
	private String cvc;
	private Integer mes;
	private Integer annio;
	
	public String getCodpedido() {
		return codpedido;
	}
	public void setCodpedido(String codpedido) {
		this.codpedido = codpedido;
	}

	public String getCodusuario() {
		return codusuario;
	}

	public void setCodusuario(String codusuario) {
		this.codusuario = codusuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Pedido() {
		
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Integer getTelefono() {
		return telefono;
	}
	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Integer getCodpostal() {
		return codpostal;
	}
	public void setCodpostal(Integer codpostal) {
		this.codpostal = codpostal;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public Integer getNumtarjeta() {
		return numtarjeta;
	}
	public void setNumtarjeta(Integer numtarjeta) {
		this.numtarjeta = numtarjeta;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAnnio() {
		return annio;
	}
	public void setAnnio(Integer annio) {
		this.annio = annio;
	}
	
	
}
