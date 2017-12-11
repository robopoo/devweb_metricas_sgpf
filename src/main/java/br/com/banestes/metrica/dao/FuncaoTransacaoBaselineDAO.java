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
import br.com.sgpf.metrica.core.util.StringUtil;
import br.com.sgpf.metrica.entity.BaselineTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;

@Name("funcaoTransacaoBaselineDAO")
public class FuncaoTransacaoBaselineDAO extends AbstractDao<FuncaoTransacaoBaselineTO, Long, FuncaoTransacaoBaselineTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<String> findARs(FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoBaselineTO.findDescricaoARs");

		q.setParameter(1, funcaoTransacaoBaselineTO);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findTDsAtravessamFronteira(FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoBaselineTO.findDescricaoTDs");

		q.setParameter(1, funcaoTransacaoBaselineTO.getIdFuncaoTransacaoBaseline());
		q.setParameter(2, SimNaoEnum.SIM.getValue());
		q.setParameter(3, funcaoTransacaoBaselineTO.getIdFuncaoTransacaoBaseline());

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
	public void deleteFilhos(FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {//Deleção em cascata no Banco de Dados

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoBaselineTO.deleteFilhos");

		q.setParameter(1, funcaoTransacaoBaselineTO);

		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<FuncaoTransacaoBaselineTO> findBySistema(Long idSistema) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoBaselineTO.findBySistemaAndTipo");

		q.setParameter("idSistema", idSistema);

		return q.getResultList();
	}

	public Integer countFuncoesMesmoNome(BaselineTO baselineTO, FuncaoTransacaoProjetoTO funcao) {

		Criteria cq = getSession().createCriteria(FuncaoTransacaoBaselineTO.class);

		//		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.eq("baselineTO", baselineTO));
		//		cq.add(Restrictions.eq("nome", funcao.getNome()).ignoreCase());
		if (funcao.getFuncaoTransacaoBaselineTO() != null) {
			cq.add(Restrictions.ne("idFuncaoTransacaoBaseline", funcao.getFuncaoTransacaoBaselineTO()
					.getIdFuncaoTransacaoBaseline()));
		}

		List<FuncaoTransacaoBaselineTO> list = cq.list();
		for (FuncaoTransacaoBaselineTO item : list) {
			if (StringUtil.limparNome(item.getNome()).equals(StringUtil.limparNome(funcao.getNome())))
				return 1;
		}

		return 0;
	}

}
