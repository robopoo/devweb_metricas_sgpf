/**
 * 
 */
package br.com.sgpf.metrica.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.entity.AnalistaTO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 28/02/2014
 * @time 14:34:06
 */

@Name("projetoDAO")
public class ProjetoDAO extends AbstractDao<ProjetoTO, Long, ProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7364725513664836636L;

	@Override
	protected void preencherOptionalCriteria(DetachedCriteria criterio, ProjetoTO filter) {
		if (filter.getContratoTO() == null) {
			criterio.createAlias("contratoTO", "contrato", DetachedCriteria.INNER_JOIN);
			criterio.add(Restrictions.eq("contrato.empresaTO", filter.getEmpresaTransient()));
		}

		if (filter.getSistemaTO() == null) {
			criterio.createAlias("sistemaTO", "sistema", DetachedCriteria.INNER_JOIN);
			criterio.add(Restrictions.eq("sistema.empresaTO", filter.getEmpresaTransient()));
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProjetoTO> findAllBySistemaContrato(SistemaTO sistemaTO, ContratoTO contratoTO) {

		Criteria cq = getSession().createCriteria(ProjetoTO.class);

		cq.add(Restrictions.eq("sistemaTO", sistemaTO));
		cq.add(Restrictions.eq("contratoTO", contratoTO));

		return cq.list();

	}

	public Integer countProjetosByAnalista(AnalistaTO analistaTO) {

		Criteria cq = getSession().createCriteria(ProjetoTO.class);

		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.disjunction().add(Restrictions.eq("responsavelPDCaseTO", analistaTO))
				.add(Restrictions.eq("responsavelPrincipalClienteTO", analistaTO))
				.add(Restrictions.eq("responsavelEnvolvidoClientTO", analistaTO))
				.add(Restrictions.eq("gestorPrincipalTO", analistaTO))
				.add(Restrictions.eq("gestorEnvolvidoTO", analistaTO)));

		return (Integer) cq.uniqueResult();
	}

	public int ExisteProjetosByNmProjetoNmSubProjeto(String projeto, String subprojeto, SistemaTO sistemaTO) {

		Criteria cq = getSession().createCriteria(ProjetoTO.class);

		cq.setProjection(Projections.rowCount());

		cq.add(Restrictions.eq("projeto", projeto));
		cq.add(Restrictions.eq("subProjeto", subprojeto));
		cq.add(Restrictions.eq("sistemaTO", sistemaTO));

		int count = (Integer) cq.uniqueResult();

		return count;
	}

}
