package br.com.sgpf.metrica.dao;

import org.hibernate.Criteria;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.entity.ParametroSistemaTO;

@Name("parametroSistemaDAO")
public class ParametroSistemaDAO extends AbstractDao<ParametroSistemaTO, Long, ParametroSistemaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515406815376634417L;
	
	public ParametroSistemaTO find(){

		Criteria cq = getSession().createCriteria(ParametroSistemaTO.class);

		return (ParametroSistemaTO) cq.uniqueResult();
	}

}
