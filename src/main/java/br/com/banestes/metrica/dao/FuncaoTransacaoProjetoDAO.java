package br.com.sgpf.metrica.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.entity.DescricaoARsProjeto;
import br.com.sgpf.metrica.entity.DescricaoAtributoTD;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;

@Name("funcaoTransacaoProjetoDAO")
public class FuncaoTransacaoProjetoDAO extends AbstractDao<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<String> findARs(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoProjetoTO.findDescricaoARs");

		q.setParameter(1, funcaoTransacaoProjetoTO);

		List<DescricaoARsProjeto> listDescs = q.getResultList();

		return DescricaoARsProjeto.toList(listDescs);
	}

	@SuppressWarnings("unchecked")
	public List<String> findTDsAtravessamFronteira(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoProjetoTO.findDescricaoTDs");

		q.setParameter(1, funcaoTransacaoProjetoTO.getIdFuncaoTransacaoProjeto());
		q.setParameter(2, SimNaoEnum.SIM.getValue());
		q.setParameter(3, funcaoTransacaoProjetoTO.getIdFuncaoTransacaoProjeto());

		return q.getResultList();
	}

	public void deleteArquivosRefAndOutrosTD(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("TipoDadosFuncaoTransacaoProjetoTO.deleteByFuncaoTransacao");
		q.setParameter(1, funcaoTransacaoProjetoTO);
		q.executeUpdate();

		q = entityManager.createNamedQuery("ArquivoReferenciadoProjetoTO.deleteByFuncaoTransacao");
		q.setParameter(1, funcaoTransacaoProjetoTO);
		q.executeUpdate();
		
		q = entityManager.createNamedQuery("TipoDadosDERFuncaoTransacaoProjetoTO.deleteByFuncaoTransacao");
		q.setParameter(1, funcaoTransacaoProjetoTO);
		q.executeUpdate();

	}

	public List<DescricaoAtributoTD> findAtributo(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {

		EntityManager entityManager = super.getEntityManager();

		Query q = entityManager.createNamedQuery("FuncaoTransacaoProjetoTO.findAtributosTDs");

		q.setParameter(1, funcaoTransacaoProjetoTO.getIdFuncaoTransacaoProjeto());
		q.setParameter(2, SimNaoEnum.SIM.getValue());
		q.setParameter(3, funcaoTransacaoProjetoTO.getIdFuncaoTransacaoProjeto());
		List resultList = q.getResultList();
		List<DescricaoAtributoTD> result = new ArrayList<DescricaoAtributoTD>(resultList.size());
		DescricaoAtributoTD td = null;
		for (Object object : resultList) {
			Object[] values = (Object[]) object;
			td = new DescricaoAtributoTD((String) values[0], ((BigDecimal) values[1]).intValue());
			result.add(td);
		}
		return result;
	}

}
