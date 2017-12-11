/**
 * 
 */
package br.com.sgpf.metrica.datamodel;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.datamodel.PaginatedDataModel;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.ProjetoTO;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 28/02/2014
 * @time 14:33:09
 */

@Name("projetoDataModel")
@Scope(ScopeType.CONVERSATION)
public class ProjetoDataModel extends PaginatedDataModel<ProjetoTO, Long, ProjetoDAO, ProjetoTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4130044100655233201L;
	private ProjetoTO filtro;
	
	@In(create = true)
	private ProjetoDAO projetoDAO;

	/* (non-Javadoc)
	 * @see br.com.sgpf.metrica.core.datamodel.PaginatedDataModel#getFiltro()
	 */
	@Override
	public ProjetoTO getFiltro() {
		return filtro;
	}

	/* (non-Javadoc)
	 * @see br.com.sgpf.metrica.core.datamodel.PaginatedDataModel#setFiltro(br.com.sgpf.metrica.core.entity.EntityModel)
	 */
	@Override
	public void setFiltro(ProjetoTO filtro) {
		this.filtro = filtro;
		
	}

	/* (non-Javadoc)
	 * @see br.com.sgpf.metrica.core.datamodel.PaginatedDataModel#getControlador()
	 */
	@Override
	public ProjetoDAO getControlador() {
		return projetoDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.sgpf.metrica.core.datamodel.PaginatedDataModel#setControlador(br.com.sgpf.metrica.core.dao.AbstractDao)
	 */
	@Override
	public void setControlador(ProjetoDAO controlador) {
		this.projetoDAO = controlador;
		
	}

}
