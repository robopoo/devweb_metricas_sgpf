package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.entity.AnalistaTO;

@Name("analistaDataModel")
@Scope(ScopeType.CONVERSATION)
public class AnalistaDataModel extends
		PaginatedDataModel<AnalistaTO, Long, AnalistaDAO, AnalistaTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private AnalistaTO filtro;

	@In(create = true)
	private AnalistaDAO analistaDAO;

	@Override
	public AnalistaTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(AnalistaTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public AnalistaDAO getControlador() {
		return analistaDAO;
	}

	@Override
	public void setControlador(AnalistaDAO service) {
		this.analistaDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] {  Order.asc("nomeAnalista") };
	}

}
