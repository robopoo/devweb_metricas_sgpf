package br.com.sgpf.metrica.dao;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.ElementoContagemTO;

@Name("elementoContagemDAO")
public class ElementoContagemDAO extends AbstractDao<ElementoContagemTO, Long, ElementoContagemTO> {

	private static final long serialVersionUID = 3582126462455373732L;

	/*public List<ElementoContagemTO> findAllElementoContagemByContrato(ContratoTO contratoTO) {
		ElementoContagemTO filter = new ElementoContagemTO();
		filter.setFatorEquivalenciaTO(new FatorEquivalenciaTO());
		filter.getFatorEquivalenciaTO().setContratoTO(contratoTO);
		filter.getFatorEquivalenciaTO().setDtVigenciaFim(null);

		return findByCriteria(filter, null, null, new Order[] { Order.asc("codElementoContagem") });
	}*/
	
	public List<ElementoContagemTO> findAllElementoContagemByContrato(ContratoTO contratoTO) {
		
		Query query = getEntityManager().createNamedQuery("findAllElementoContagemByContrato");
		query.setParameter("contratoTO", contratoTO);
		return query.getResultList();
	}

	@Override
	protected void executarPreEventos(ElementoContagemTO entity, OperacoesEnum operacao) throws DaoException {
		switch (operacao) {
		case INSERCAO:
			entity.setIdElementoContagem(null);
			break;
		case EXCLUSAO:
			entity.setFatorEquivalenciaTO(null);
			break;
		default:
			break;
		}
	}

}
