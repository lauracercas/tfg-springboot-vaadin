package com.tfg.springvaadin.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.tfg.springvaadin.filter.ProductoFilter;
import com.tfg.springvaadin.model.Producto;
import com.google.common.base.Strings;

public class ProductoSpecification implements Specification<Producto>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ProductoFilter  filter;
	
	public ProductoSpecification(ProductoFilter filter) {
		super();
		this.filter = filter;
	}	
	
	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */	
	public ProductoFilter getFilter() {
		return this.filter;
		
	}
	
	@Override
	public Predicate toPredicate(Root<Producto> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate p = cb.and();
		addNombreExpression(root, cb, p);
		addDescripcionExpression(root, cb, p);
		addPrecioExpression(root, cb, p);
		addFechapublicacionExpression(root, cb, p);
		addAnnioExpression(root, cb, p);
		addTipoExpression(root, cb, p);
		addArtistaExpression(root, cb, p);
		addGeneroExpression(root, cb, p);
		addUsuarioExpression(root, cb, p);
		addEstadoPedidoExpression(root, cb, p);
		addEstadoProductoExpression(root, cb, p);
		
		return p;
	}
	
	protected void addNombreExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (!Strings.isNullOrEmpty(filter.getNombreilike())){
            p.getExpressions().add(cb.and(cb.like(cb.lower(root.get("nombre")), "%" + filter.getNombreilike().toLowerCase().trim() + "%")));
        }
    }
	
	protected void addDescripcionExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (!Strings.isNullOrEmpty(filter.getDescripcionilike())){
            p.getExpressions().add(cb.and(cb.like(cb.lower(root.get("descripcion")), "%" + filter.getDescripcionilike().toLowerCase().trim() + "%")));
        }
    }
	
	protected void addPrecioExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getPrecioge() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("precio"), filter.getPrecioge())));
        }
    }
	
	protected void addFechapublicacionExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getFechapublicacionge() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("fechapublicacion"), filter.getFechapublicacionge())));
        } 
    }
	
	protected void addAnnioExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getAnnioge() != null) {
            p.getExpressions().add(cb.and(cb.greaterThanOrEqualTo(root.get("annio"), filter.getAnnioge())));
        } 
    }
	
	protected void addTipoExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getTipoeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("tipo"), filter.getTipoeq())));
        }
    }
	
	protected void addArtistaExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getArtistaeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("codartista"), filter.getArtistaeq())));
        }
    }
	
	protected void addGeneroExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getGeneroeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("codgenero"), filter.getGeneroeq())));
        }
    }
	
	protected void addUsuarioExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getCodusuarioeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("codusuario"), filter.getCodusuarioeq())));
        }
    }
	
	protected void addEstadoPedidoExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getEstadoPedidonoteq() != null) {
            p.getExpressions().add(cb.and(cb.notEqual(root.get("estadopedido"), filter.getEstadoPedidonoteq())));
        }
		
		if (filter.getEstadoPedidoeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("estadopedido"), filter.getEstadoPedidoeq())));
        }
    }
	
	protected void addEstadoProductoExpression(Root<Producto> root, CriteriaBuilder cb, Predicate p) {
		if (filter.getEstadoProductonoteq() != null) {
            p.getExpressions().add(cb.and(cb.notEqual(root.get("estadoproducto"), filter.getEstadoProductonoteq())));
        }
		
		if (filter.getEstadoProductoeq() != null) {
            p.getExpressions().add(cb.and(cb.equal(root.get("estadoproducto"), filter.getEstadoProductoeq())));
        }
    }
	
	

}
