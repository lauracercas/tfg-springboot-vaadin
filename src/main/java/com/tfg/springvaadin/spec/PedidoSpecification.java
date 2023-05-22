package com.tfg.springvaadin.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.google.common.base.Strings;
import com.tfg.springvaadin.filter.PedidoFilter;
import com.tfg.springvaadin.model.Pedido;

public class PedidoSpecification implements Specification<Pedido>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PedidoFilter  filter;
	
	public PedidoSpecification(PedidoFilter filter) {
		super();
		this.filter = filter;
	}	
	
	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */	
	public PedidoFilter getFilter() {
		return this.filter;
		
	}
	
	@Override
	public Predicate toPredicate(Root<Pedido> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate p = cb.and();
		addCodpedidoExpression(root, cb, p);
		addCodusuarioExpression(root, cb, p);
		addImporteExpression(root, cb, p);
		addFechaExpression(root, cb, p);
		
		return p;
	}
	
	
	protected void addImporteExpression(Root<Pedido> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getImportege() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("importe"), filter.getImportege())));
		}
	}
	
	protected void addFechaExpression(Root<Pedido> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getFechage() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("fecha"), filter.getFechage())));
        } 
    }
	
	
	protected void addCodpedidoExpression(Root<Pedido> root, CriteriaBuilder cb, Predicate p) {
		if (!Strings.isNullOrEmpty(filter.getCodpedidoeq())) {
            p.getExpressions().add(cb.and(cb.equal(root.get("codpedido"), filter.getCodpedidoeq())));
        }
    }
	
	protected void addCodusuarioExpression(Root<Pedido> root, CriteriaBuilder cb, Predicate p) {
		if (!Strings.isNullOrEmpty(filter.getCodusuarioeq())) {
            p.getExpressions().add(cb.and(cb.equal(root.get("codusuario"), filter.getCodusuarioeq())));
        }
    }
	
	
	

}
