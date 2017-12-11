package br.com.sgpf.metrica.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.EntidadeTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.enumeration.CategoriaEnum;

@Name("entidadeDAO")
public class EntidadeDAO extends AbstractDao<EntidadeTO, Long, EntidadeTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1349003980527314690L;

	@Override
	protected void executarPreEventos(EntidadeTO entity, OperacoesEnum operacao) throws DaoException {
		switch (operacao) {
		case INSERCAO:
			entity.setVersaoRegistro(0);
			entity.setStatus(StatusEnum.ATIVADO);
			break;
		case ALTERACAO:
			entity.setVersaoRegistro(entity.getVersaoRegistro() + 1);
			break;
		case EXCLUSAO:
			entity.setSistemaTO(null);
			break;
		case DESATIVACAO:
			entity.setStatus(StatusEnum.DESATIVADO);
			break;
		default:
			break;
		}
	}

	public Integer countEntidadesByNome(String nmEntidade, SistemaTO sistemaTO, Long idEntidade) {

		Criteria cq = getSession().createCriteria(EntidadeTO.class);

		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.eq("nmEntidade", nmEntidade));
		cq.add(Restrictions.eq("sistemaTO", sistemaTO));
		
		if(idEntidade != null){
			cq.add(Restrictions.ne("idEntidade", idEntidade));
		}

		return (Integer) cq.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<EntidadeTO> findAllEntidadeByEmpresa(EmpresaTO empresaTO) {

		Query query = getEntityManager().createNamedQuery("EntidadeTO.findByEmpresa");
		query.setParameter("empresaTO", empresaTO);
		query.setParameter("status", StatusEnum.ATIVADO);
		query.setParameter("negocio", CategoriaEnum.NEGOCIO);
		query.setParameter("referencia", CategoriaEnum.REFERENCIA);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<EntidadeTO> findAllEntidadeBySistema(SistemaTO sistemaTO) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		criterio.add(Restrictions.eq("sistemaTO", sistemaTO));
		criterio.add(Restrictions.eq("status", StatusEnum.ATIVADO));
		criterio.add(Restrictions.in("categoria", new Object[] { CategoriaEnum.NEGOCIO, CategoriaEnum.REFERENCIA }));
		//		criterio.add(Restrictions.gt("qtTd", new Integer(0)));
		criterio.add(Restrictions.sizeGt("atributoTOlist", new Integer(0)));
		criterio.addOrder(Order.asc("nmEntidade"));

		Criteria crit = criterio.getExecutableCriteria(getSession());

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return crit.list();
	}
	
	
	@SuppressWarnings( "unchecked" )
	public List<EntidadeTO> findAllEntidadeDadosDeNegocio(SistemaTO sistemaTO)
	{
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		criterio.add(Restrictions.eq("sistemaTO", sistemaTO));
		criterio.add(Restrictions.eq("status", StatusEnum.ATIVADO));
		criterio.add(Restrictions.in("categoria", new Object[] { CategoriaEnum.NEGOCIO}));
		//		criterio.add(Restrictions.gt("qtTd", new Integer(0)));
		criterio.add(Restrictions.sizeGt("atributoTOlist", new Integer(0)));
		criterio.addOrder(Order.asc("nmEntidade"));

		Criteria crit = criterio.getExecutableCriteria(getSession());

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return crit.list();
	}

}
