package br.com.sgpf.metrica.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.FatorEquivalenciaTO;

@Name("fatorEquivalenciaDAO")
public class FatorEquivalenciaDAO extends
	AbstractDao<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> {

    private static final long serialVersionUID = 837314308947929975L;

    @Override
    protected void executarPreEventos(FatorEquivalenciaTO entity,
	    OperacoesEnum operacao) throws DaoException {
	switch (operacao) {
	case INSERCAO:
	    entity.setIdFatorEquivalencia(null);
	    break;
	case EXCLUSAO:
	    entity.setContratoTO(null);
	    break;
	default:
	    break;
	}
    }

    @SuppressWarnings("unchecked")
    public void publicar(FatorEquivalenciaTO fator) {
	List<FatorEquivalenciaTO> fatores = getSession()
		.createCriteria(FatorEquivalenciaTO.class)
		.add(Restrictions.isNull("dtVigenciaFim"))
		.add(Restrictions.isNotNull("dtVigenciaInicio"))
		.addOrder(Order.desc("dtVigenciaInicio"))
		.add(Restrictions.eq("contratoTO", fator.getContratoTO()))
		.setMaxResults(1).list();

	System.out.println("list = " + fatores);

	if (fatores != null && !fatores.isEmpty()) {
	    FatorEquivalenciaTO f = fatores.get(0);
	    f.setDtVigenciaFim(new Date());
	    update(fator);

	}

    }
    
    @Override
    protected void preencherOptionalCriteria(DetachedCriteria criterio, FatorEquivalenciaTO filter) {
    	
    	if(filter.getContratoTO() != null 
    			&& filter.getContratoTO().getEntityId() == null
    			&& filter.getContratoTO().getEmpresaTO() != null
    			&& filter.getContratoTO().getEmpresaTO().getIdEmpresa() != null){
    		criterio.createAlias("contratoTO", "ctr", JoinFragment.INNER_JOIN);
    		criterio.add(Restrictions.eq("ctr.empresaTO", filter.getContratoTO().getEmpresaTO()));
    	}
    	
	}
}
