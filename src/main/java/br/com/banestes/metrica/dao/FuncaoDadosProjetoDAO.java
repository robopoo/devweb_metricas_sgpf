package br.com.sgpf.metrica.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoDadosTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("funcaoDadosProjetoDAO")
public class FuncaoDadosProjetoDAO extends AbstractDao<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<String> findTRs(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosProjetoTO.findDescricaoTRs");

		q.setParameter(1, funcaoDadosProjetoTO);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findTDsReferenciadosAplicacao(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosProjetoTO.findDescricaoTDs");

		q.setParameter(1, funcaoDadosProjetoTO);
		
		q.setParameter(2, SimNaoEnum.SIM);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<FuncaoDadosTO> findAllFuncaoDadosBySistema(
			SistemaTO sistemaTO) {
		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoDadosProjetoTO.findBySistema");

		q.setParameter(1, sistemaTO);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FuncaoDadosTO> findByProjeto(
			ProjetoTO projetoTO, String nome) {
		
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		
		criterio.add(Restrictions.eq("projetoTO", projetoTO));
		criterio.add(Restrictions.eq("nome", nome));

		Criteria crit = criterio.getExecutableCriteria(getSession());
		return crit.list();
	}

	
	
}
