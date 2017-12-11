package br.com.sgpf.metrica.dao;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.AtributoTO;

@Name("atributoDAO")
public class AtributoDAO extends AbstractDao<AtributoTO, Long, AtributoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515406815376634417L;

	@Override
	protected void executarPreEventos(AtributoTO entity, OperacoesEnum operacao)
			throws DaoException {
		switch (operacao) {
		case INSERCAO:
			entity.setIdAtributo(null);
			break;
		case EXCLUSAO:
			entity.setEntidadeTO(null);
			break;
		default:
			break;
		}
	}

}
