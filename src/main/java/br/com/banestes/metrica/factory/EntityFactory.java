package br.com.sgpf.metrica.factory;

import java.util.List;

import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.dao.AtributoDAO;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.dao.ElementoContagemDAO;
import br.com.sgpf.metrica.dao.EmpresaDAO;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.dao.FatorEquivalenciaDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.dao.UsuarioDAO;
import br.com.sgpf.metrica.entity.AnalistaTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("entityFactory")
public class EntityFactory {

    @In(create = true)
    private UsuarioDAO usuarioDAO;

    @In(create = true)
    private EmpresaDAO empresaDAO;

    @In(create = true)
    private ContratoDAO contratoDAO;
    
    @In(create = true)
    private SistemaDAO sistemaDAO;

    @In(create = true)
    private FatorEquivalenciaDAO fatorEquivalenciaDAO;

    @In(create = true)
    private ElementoContagemDAO elementoContagemDAO;

    @In(create = true)
    private EntidadeDAO entidadeDAO;

    @In(create = true)
    private AtributoDAO atributoDAO;
    
    @In(create = true)
    private AnalistaDAO analistaDAO;

    @Factory(value = "factoryEmpresas", scope = ScopeType.CONVERSATION)
    public List<EmpresaTO> getAllEmpresas() {
	return empresaDAO.findByCriteria(null, null, null,
		new Order[] { Order.asc("nmEmpresa") });
    }


    @Factory(value = "factoryEmpresasAtiva", scope = ScopeType.CONVERSATION)
    public List<EmpresaTO> getAllEmpresasAtivas() {
	EmpresaTO filtro = new EmpresaTO();
	filtro.setStEmpresa(StatusEnum.ATIVADO);
	return empresaDAO.findByCriteria(filtro, null, null,
		new Order[] { Order.asc("nmEmpresa") });
    }
    
    @Factory(value = "factorySistemas", scope = ScopeType.CONVERSATION)
    public List<SistemaTO> getAllSistemas() {
    	return sistemaDAO.findByCriteria(null, null, null,
    			new Order[] { Order.asc("cdSistema") });
    }
    
    @Factory(value = "factoryAnalistasPDCase", scope = ScopeType.CONVERSATION)
    public List<AnalistaTO> getAllAnalistasPDCase(){
    	return analistaDAO.findAllAnalistasPDCase();
    }
    
//    @Factory(value = "factoryContratoByEmpresa", scope = ScopeType.CONVERSATION)
//    public List<ContratoTO> getAllContratosByEmpresa() {
//	ContratoTO filtro = new ContratoTO();
////	filtro.setEmpresaTO(null);
//	return contratoDAO.findByCriteria(filtro, null, null,
//		new Order[] { Order.asc("codigoContrato") });
//    }

}