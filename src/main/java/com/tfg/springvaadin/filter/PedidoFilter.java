package com.tfg.springvaadin.filter;

import java.io.Serializable;
import java.util.Date;

public class PedidoFilter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codpedidoeq;
	
	private String codusuarioeq;
	
	private Date fechage;
	
	private Double importege;

	public String getCodpedidoeq() {
		return codpedidoeq;
	}

	public void setCodpedidoeq(String codpedidoeq) {
		this.codpedidoeq = codpedidoeq;
	}

	public String getCodusuarioeq() {
		return codusuarioeq;
	}

	public void setCodusuarioeq(String codusuarioeq) {
		this.codusuarioeq = codusuarioeq;
	}

	public Date getFechage() {
		return fechage;
	}

	public void setFechage(Date fechage) {
		this.fechage = fechage;
	}

	public Double getImportege() {
		return importege;
	}

	public void setImportege(Double importege) {
		this.importege = importege;
	}

	
	

}
