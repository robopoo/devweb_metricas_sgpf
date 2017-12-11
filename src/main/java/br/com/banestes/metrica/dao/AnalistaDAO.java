package br.com.sgpf.metrica.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.entity.AnalistaTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.enumeration.TipoAnalistaEnum;

@Name("analistaDAO")
public class AnalistaDAO extends AbstractDao<AnalistaTO, Long, AnalistaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515406815376634417L;

	// public List<AnalistaTO> findAllAnalistasPDCase(){
	// AnalistaTO filtro = new AnalistaTO();
	// // filtro.setAnalistaInterno(1L);
	//
	// return findByCriteria(filtro, null, null, new Order[]
	// {Order.asc("nomeAnalista")});
	// }

	@SuppressWarnings("unchecked")
	public List<AnalistaTO> findAllAnalistasPDCase() {

		Criteria cq = getSession().createCriteria(AnalistaTO.class);
		cq.addOrder(Order.asc("nomeAnalista"));
		cq.createAlias("empresaTO", "emp")
				.add(Restrictions.eq("emp.flFornecedor", SimNaoEnum.SIM))
				.add(Restrictions.eq("tpAnalista", TipoAnalistaEnum.ANALISTA))
				.list();

		return cq.list();

	}

	public List<AnalistaTO> findAllAnalistaByEmpresa(EmpresaTO empresaFiltro) {
		AnalistaTO filter = new AnalistaTO();
		filter.setEmpresaTO(empresaFiltro);
		filter.setTpAnalista(TipoAnalistaEnum.ANALISTA);

		return findByCriteria(filter, null, null,
				new Order[] { Order.asc("nomeAnalista") });
	}
	
	public List<AnalistaTO> findAllGestorByEmpresa(EmpresaTO empresaFiltro) {
		AnalistaTO filter = new AnalistaTO();
		filter.setEmpresaTO(empresaFiltro);
		filter.setTpAnalista(TipoAnalistaEnum.GESTOR);
		return findByCriteria(filter, null, null,
				new Order[] { Order.asc("nomeAnalista") });
	}
	
	
}
