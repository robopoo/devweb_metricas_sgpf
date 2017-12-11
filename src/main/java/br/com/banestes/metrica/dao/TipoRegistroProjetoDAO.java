package br.com.sgpf.metrica.dao;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.TipoRegistroProjetoTO;

@Name("tipoRegistroProjetoDAO")
public class TipoRegistroProjetoDAO extends AbstractDao<TipoRegistroProjetoTO, Long, TipoRegistroProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void deleteByFuncaoDados(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {

		Query query = getEntityManager().createNamedQuery("TipoRegistroProjetoTO.deleteTipoRegistroByFuncaoDados");
		query.setParameter(1, funcaoDadosProjetoTO);
		query.executeUpdate();

		query = getEntityManager().createNamedQuery("TipoRegistroProjetoTO.deleteByFuncaoDados");
		query.setParameter(1, funcaoDadosProjetoTO);
		query.executeUpdate();

	}

}
