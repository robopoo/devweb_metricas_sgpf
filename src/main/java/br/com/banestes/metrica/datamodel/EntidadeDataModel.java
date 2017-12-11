package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.entity.EntidadeTO;

@Name("entidadeDataModel")
@Scope(ScopeType.CONVERSATION)
public class EntidadeDataModel extends
		PaginatedDataModel<EntidadeTO, Long, EntidadeDAO, EntidadeTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private EntidadeTO filtro;

	@In(create = true)
	private EntidadeDAO entidadeDAO;

	@Override
	public EntidadeTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(EntidadeTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public EntidadeDAO getControlador() {
		return entidadeDAO;
	}

	@Override
	public void setControlador(EntidadeDAO service) {
		this.entidadeDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("dsEntidade") };
	}

}
