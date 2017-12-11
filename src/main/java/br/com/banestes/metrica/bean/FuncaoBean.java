/**
 * 
 */
package br.com.sgpf.metrica.bean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.entity.FuncaoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;

@Name("funcaoBean")
@Scope(ScopeType.CONVERSATION)
public class FuncaoBean {

	//	@In(create = true)
	//	private ProjetoDAO projetoDAO;

	public void atualizarTotaisProjeto(boolean exclusao, FuncaoTO funcaoTO, ProjetoTO projetoTO) {
		projetoTO.addQtPontoFuncaoDados(exclusao ? funcaoTO.getValorPontoFuncao().negate() : funcaoTO
				.getValorPontoFuncao());
		projetoTO.addQtPontoFuncaoLocal(exclusao ? funcaoTO.getValorPontoFuncaoLocal().negate() : funcaoTO
				.getValorPontoFuncaoLocal());
		projetoTO.addQtPontoFuncaoMensuravel(exclusao ? funcaoTO.getValorPontoFuncaoLocal().negate() : funcaoTO
				.getValorPontoFuncaoLocal());
	}

}