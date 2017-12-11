package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;

@Name("funcaoTransacaoProjetoDataModel")
@Scope(ScopeType.CONVERSATION)
public class FuncaoTransacaoProjetoDataModel extends
		PaginatedDataModel<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoDAO, FuncaoTransacaoProjetoTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private FuncaoTransacaoProjetoTO filtro;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@Override
	public FuncaoTransacaoProjetoTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(FuncaoTransacaoProjetoTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public FuncaoTransacaoProjetoDAO getControlador() {
		return this.funcaoTransacaoProjetoDAO;
	}

	@Override
	public void setControlador(FuncaoTransacaoProjetoDAO service) {
		this.funcaoTransacaoProjetoDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("nome") };
	}

}
