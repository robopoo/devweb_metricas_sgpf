package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.AtributoDAO;
import br.com.sgpf.metrica.entity.AtributoTO;

@Name("atributoDataModel")
@Scope(ScopeType.CONVERSATION)
public class AtributoDataModel extends
		PaginatedDataModel<AtributoTO, Long, AtributoDAO, AtributoTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private AtributoTO filtro;

	@In(create = true)
	private AtributoDAO atributoDAO;

	@Override
	public AtributoTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(AtributoTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public AtributoDAO getControlador() {
		return atributoDAO;
	}

	@Override
	public void setControlador(AtributoDAO service) {
		this.atributoDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("dsAtributo") };
	}

}
