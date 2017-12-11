package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.entity.ContratoTO;

@Name("ContratoDataModel")
@Scope(ScopeType.CONVERSATION)
public class ContratoDataModel extends
		PaginatedDataModel<ContratoTO, Long, ContratoDAO, ContratoTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private ContratoTO filtro;

	@In(create = true)
	private ContratoDAO contratoDAO;

	@Override
	public ContratoTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(ContratoTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public ContratoDAO getControlador() {
		return contratoDAO;
	}

	@Override
	public void setControlador(ContratoDAO service) {
		this.contratoDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("descricaoContrato") };
	}

}
