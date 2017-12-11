package br.com.sgpf.metrica.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("sistemaDAO")
public class SistemaDAO extends AbstractDao<SistemaTO, Long, SistemaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515406815376634417L;

	@SuppressWarnings("unchecked")
	public List<SistemaTO> findAllSistemaByProjetoEmpresaSemSistemaAtual(ProjetoTO filter) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getClasse());
		criteria.add(Restrictions.eq("empresaTO", filter.getSistemaTO().getEmpresaTO()));
		criteria.add(Restrictions.ne("idSistema", filter.getSistemaTO().getIdSistema()));
		criteria.addOrder(Order.asc("cdSistema")).addOrder(Order.asc("dsSistema"));

		return criteria.getExecutableCriteria(getSession()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public List<SistemaTO> findAllSistemaByProjetoEmpresa(ProjetoTO filter) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getClasse());
		criteria.add(Restrictions.eq("empresaTO", filter.getSistemaTO().getEmpresaTO()));
		criteria.addOrder(Order.asc("cdSistema")).addOrder(Order.asc("dsSistema"));

		return criteria.getExecutableCriteria(getSession()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public Integer countSistemasByCodigo(String cdSistema, EmpresaTO empresaTO, Long idSistema) {

		Criteria cq = getSession().createCriteria(SistemaTO.class);

		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.eq("cdSistema", cdSistema));
		cq.add(Restrictions.eq("empresaTO", empresaTO));

		if (idSistema != null) {
			cq.add(Restrictions.ne("idSistema", idSistema));
		}

		return (Integer) cq.uniqueResult();
	}

	@Override
	protected void executarPreEventos(SistemaTO entity, OperacoesEnum operacao) throws DaoException {
		switch (operacao) {
		case INSERCAO:
			entity.setIdSistema(null);
			break;
		case EXCLUSAO:
			entity.setEmpresaTO(null);
			break;
		default:
			break;
		}
	}

}
