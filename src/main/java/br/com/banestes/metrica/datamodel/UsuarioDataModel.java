package br.com.sgpf.metrica.datamodel;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.UsuarioDAO;
import br.com.sgpf.metrica.entity.UsuarioTO;

@Deprecated
@Name("usuarioDataModel")
@Scope(ScopeType.CONVERSATION)
public class UsuarioDataModel extends PaginatedDataModel<UsuarioTO, Long, UsuarioDAO, UsuarioTO> {

	private static final long serialVersionUID = -8553646380861920765L;

	private UsuarioTO filtro;

	@In(create = true)
	private UsuarioDAO usuarioDAO;

	@Override
	public UsuarioTO getFiltro() {
		return filtro;
	}

	@Override
	public void setFiltro(UsuarioTO filtro) {
		this.filtro = filtro;
	}

	@Override
	public UsuarioDAO getControlador() {
		return usuarioDAO;
	}

	@Override
	public void setControlador(UsuarioDAO service) {
		this.usuarioDAO = service;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] { Order.asc("nome") };
	}

}
