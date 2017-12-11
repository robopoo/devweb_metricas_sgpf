package br.com.sgpf.metrica.dao;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.ContratoTO;

@Name("contratoDAO")
public class ContratoDAO extends AbstractDao<ContratoTO, Long, ContratoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 773330544778151031L;

	/**
	 * 
	 */
	

	@Override
	protected void executarPreEventos(ContratoTO entity, OperacoesEnum operacao)
			throws DaoException {
		switch (operacao) {
		case INSERCAO:
			entity.setIdContrato(null);
			break;
		case EXCLUSAO:
			entity.setEmpresaTO(null);
			break;
		default:
			break;
		}
	}

}
