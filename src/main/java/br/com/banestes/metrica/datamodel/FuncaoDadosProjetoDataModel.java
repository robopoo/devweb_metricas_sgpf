package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;

@Name("funcaoDadosProjetoDataModel")
@Scope(ScopeType.CONVERSATION)
public class FuncaoDadosProjetoDataModel extends
		PaginatedDataModel<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoDAO, FuncaoDadosProjetoTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private FuncaoDadosProjetoTO filtro;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@Override
	public FuncaoDadosProjetoTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(FuncaoDadosProjetoTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public FuncaoDadosProjetoDAO getControlador() {
		return this.funcaoDadosProjetoDAO;
	}

	@Override
	public void setControlador(FuncaoDadosProjetoDAO service) {
		this.funcaoDadosProjetoDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("nome") };
	}

}
