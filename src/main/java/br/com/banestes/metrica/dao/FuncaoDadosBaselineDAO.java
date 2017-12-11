package br.com.sgpf.metrica.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.entity.BaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;

@Name("funcaoDadosBaselineDAO")
public class FuncaoDadosBaselineDAO extends AbstractDao<FuncaoDadosBaselineTO, Long, FuncaoDadosBaselineTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<FuncaoDadosBaselineTO> findAllBySistemaNotExistsProjeto(ProjetoTO projetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosBaselineTO.findBySistemaAndTipo");

		q.setParameter("sistema", projetoTO.getSistemaTO());
		q.setParameter("projeto", projetoTO);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findTRs(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosBaselineTO.findDescricaoTRs");

		q.setParameter(1, funcaoDadosBaselineTO);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findTDsReferenciadosAplicacao(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosBaselineTO.findDescricaoTDs");

		q.setParameter(1, funcaoDadosBaselineTO);
		
		q.setParameter(2, SimNaoEnum.SIM);

		return q.getResultList();
	}

	/*
	 * Be very careful how you use bulk UPDATE and DELETE. It is possible, depending on the
	 * vendor implementation, to create inconsistencies between the database and entities
	 * that are already being managed by the current persistence context. Vendor
	 * implementations are required only to execute the update or delete directly on the database.
	 * They do not have to modify the state of any currently managed entity. For this
	 * reason, it is recommended that you do these operations within their own transaction
	 * or at the beginning of a transaction (before any entities are accessed that might be
	 * affected by these bulk operations). Alternatively, executing EntityManager.flush() and
	 * EntityManager.clear() before executing a bulk operation will keep you safe.
	 */
	public void deleteFilhos(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {//Deleção em cascata no Banco de Dados

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosBaselineTO.deleteFilhos");

		q.setParameter(1, funcaoDadosBaselineTO);

		q.executeUpdate();
	}
	
	public Integer countFuncoesMesmoNome(BaselineTO baselineTO, FuncaoDadosProjetoTO funcao) {

		Criteria cq = getSession().createCriteria(FuncaoDadosBaselineTO.class);

		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.eq("baselineTO", baselineTO));
		cq.add(Restrictions.eq("nome", funcao.getNome()));
		if(funcao.getFuncaoDadosBaselineTO() != null){
			cq.add(Restrictions.ne("idFuncaoDadosBaseline", funcao.getFuncaoDadosBaselineTO().getIdFuncaoDadosBaseline()));
		}
		
		return (Integer) cq.uniqueResult();
	}

}
