package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.FatorEquivalenciaDAO;
import br.com.sgpf.metrica.entity.FatorEquivalenciaTO;

@Name("fatorEquivalenciaDataModel")
@Scope(ScopeType.CONVERSATION)
public class FatorEquivalenciaDataModel extends
		PaginatedDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaDAO, FatorEquivalenciaTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private FatorEquivalenciaTO filtro;

	@In(create = true)
	private FatorEquivalenciaDAO fatorEquivalenciaDAO;

	@Override
	public FatorEquivalenciaTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(FatorEquivalenciaTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public FatorEquivalenciaDAO getControlador() {
		return fatorEquivalenciaDAO;
	}

	@Override
	public void setControlador(FatorEquivalenciaDAO service) {
		this.fatorEquivalenciaDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] {  };
	}

}
