package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("sistemaDataModel")
@Scope(ScopeType.CONVERSATION)
public class SistemaDataModel extends
		PaginatedDataModel<SistemaTO, Long, SistemaDAO, SistemaTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private SistemaTO filtro;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	@Override
	public SistemaTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(SistemaTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public SistemaDAO getControlador() {
		return sistemaDAO;
	}

	@Override
	public void setControlador(SistemaDAO service) {
		this.sistemaDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("dsSistema") };
	}

}
