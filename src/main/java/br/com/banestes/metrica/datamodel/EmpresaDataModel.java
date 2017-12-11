package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.EmpresaDAO;
import br.com.sgpf.metrica.entity.EmpresaTO;

@Name("empresaDataModel")
@Scope(ScopeType.CONVERSATION)
public class EmpresaDataModel extends
		PaginatedDataModel<EmpresaTO, Long, EmpresaDAO, EmpresaTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private EmpresaTO filtro;

	@In(create = true)
	private EmpresaDAO empresaDAO;

	@Override
	public EmpresaTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(EmpresaTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public EmpresaDAO getControlador() {
		return empresaDAO;
	}

	@Override
	public void setControlador(EmpresaDAO service) {
		this.empresaDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("nmEmpresa") };
	}

}
