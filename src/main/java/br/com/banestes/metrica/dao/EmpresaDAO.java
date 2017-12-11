package br.com.sgpf.metrica.dao;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.EmpresaTO;

@Name("empresaDAO")
public class EmpresaDAO extends AbstractDao<EmpresaTO, Long, EmpresaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515406815376634417L;
	
	@Override
	protected void executarPreEventos(EmpresaTO entity, OperacoesEnum operacao) throws DaoException {
		switch (operacao) {
			case INSERCAO :
				entity.setIdEmpresa(null);
				break;
			default :
				break;
		}
	}

//	@Override
//	public void delete(EmpresaTO entity) throws DaoException {
//		super.update(entity, OperacoesEnum.EXCLUSAO);
//	}

}
