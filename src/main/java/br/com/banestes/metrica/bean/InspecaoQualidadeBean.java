package br.com.sgpf.metrica.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.dao.BaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ParametroSistemaDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.Aprovacao;
import br.com.sgpf.metrica.entity.BaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.ParametroSistemaTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.enumeration.StatusAvaliacaoEnum;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;

@Name("inspecaoQualidadeBean")
@Scope(ScopeType.CONVERSATION)
public class InspecaoQualidadeBean extends AbstractWebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4522581940494718840L;

	@In(required = true)
	@Out
	private ProjetoTO projetoFiltro;

	@In(create = true)
	private FuncaoDadosBaselineDAO funcaoDadosBaselineDAO;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@In(create = true)
	private FuncaoTransacaoBaselineDAO funcaoTransacaoBaselineDAO;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@In(create = true)
	private ParametroSistemaDAO parametroSistemaDAO;

	@In(create = true)
	private ProjetoDAO projetoDAO;
	
	@In(create = true)
	private BaselineDAO baselineDAO;

	@In(required = false)
	private FuncaoDadosProjetoTO registroFD;

	@In(required = false)
	private FuncaoTransacaoProjetoTO registroFT;

	@Override
	public void preRender() {

		for (FuncaoDadosProjetoTO func : this.projetoFiltro.getFuncaoDadosProjetoTOList()) {
			func.setInspecaoTDs(null);
			func.setInspecaoTRs(null);
		}

		for (FuncaoTransacaoProjetoTO func : this.projetoFiltro.getFuncaoTransacaoProjetoTOList()) {
			func.setInspecaoTDs(null);
			func.setInspecaoARs(null);
		}

	}

	@TransactionExceptionInterceptor
	public String salvar() {

		List<Aprovacao> funcoesTot = new ArrayList<Aprovacao>();

		funcoesTot.addAll(this.projetoFiltro.getFuncaoDadosProjetoTOList());
		funcoesTot.addAll(this.projetoFiltro.getFuncaoTransacaoProjetoTOList());
		funcoesTot.addAll(this.projetoFiltro.getServicoNaoMensuravelTOs());

		if (!validarComentariosRejeicao(this.projetoFiltro.getFuncaoDadosProjetoTOList())
				|| !validarComentariosRejeicao(this.projetoFiltro.getFuncaoTransacaoProjetoTOList())
				|| !validarComentariosRejeicao(this.projetoFiltro.getServicoNaoMensuravelTOs())) {
			return "";
		}

		boolean validado = validarAprovacao(funcoesTot);

		if (validado) {
			this.projetoFiltro.setStatus(StatusProjetoEnum.APROVADO);
			atualizarBaseline();
		} else {
			
			//boolean atualizar = true;
			if (this.projetoFiltro.getFuncaoDadosProjetoTOList() != null) {
				for (FuncaoDadosProjetoTO item : this.projetoFiltro.getFuncaoDadosProjetoTOList()) {
					funcaoDadosProjetoDAO.update(item);
					//atualizar = false;
				}
			}

			if (this.projetoFiltro.getFuncaoTransacaoProjetoTOList() != null) {
				for (FuncaoTransacaoProjetoTO item : this.projetoFiltro.getFuncaoTransacaoProjetoTOList()) {
					funcaoTransacaoProjetoDAO.update(item);
				//	atualizar = false;
				}
			}

			this.projetoFiltro.setStatus(StatusProjetoEnum.REJEITADO);
			//if (atualizar)
				this.projetoDAO.update(this.projetoFiltro);

		}

		addInfoMessageFromResource("operacao.realizada.sucesso");

		this.projetoFiltro = new ProjetoTO();

		return "list";
	}

	//FIXME Rever Solução...
	public void atualizarBaseline() {

		BaselineTO baselineTO;
		
		
		ParametroSistemaTO parametroSistemaTO = this.parametroSistemaDAO.find();

		this.projetoFiltro = super.em.merge(this.projetoFiltro);

		if (projetoFiltro.getSistemaTO().getBaselineTO() == null) {
			baselineTO = new BaselineTO();
			baselineTO.setNrVersao(1);
			projetoFiltro.getSistemaTO().setBaselineTO(baselineTO);
			baselineTO.setSistemaTO(projetoFiltro.getSistemaTO());
			//super.em.persist(baselineTO);
			baselineDAO.insert(baselineTO);
		} else {
			baselineTO =super.em.find(BaselineTO.class, projetoFiltro.getSistemaTO().getBaselineTO().getIdBaseLine());
			//baselineTO = projetoFiltro.getSistemaTO().getBaselineTO();
			baselineTO.setNrVersao(baselineTO.getNrVersao() + 1);
		}
		baselineTO.setValorPFDados(BigDecimal.ZERO);
		baselineTO.setValorPFTransacao(BigDecimal.ZERO);

		FuncaoDadosBaselineTO funcaoDadosBaselineTO;
		for (FuncaoDadosProjetoTO funcao : this.projetoFiltro.getFuncaoDadosProjetoTOList()) {

			if (funcao.getFuncaoDadosBaselineTO() == null) {
				funcaoDadosBaselineTO = FuncaoDadosBaselineTO.valueOf(funcao, baselineTO);
				funcao.setFuncaoDadosBaselineTO(funcaoDadosBaselineTO);
				funcaoDadosBaselineTO.setElementoContagemTO(parametroSistemaTO.getElementoContagemTO());
			} else {
				this.funcaoDadosBaselineDAO.deleteFilhos(funcao.getFuncaoDadosBaselineTO());
				funcao.getFuncaoDadosBaselineTO().updateByProjeto(funcao);
				funcao.getFuncaoDadosBaselineTO().setElementoContagemTO(parametroSistemaTO.getElementoContagemTO());
			}
			funcao.setComentarioRejeicao("");
			funcao.setSituacaoContagem(StatusAvaliacaoEnum.APROVADO);
			funcaoDadosProjetoDAO.update(funcao, false);
			baselineTO.addValorPFDados(funcao.getValorPontoFuncao());
		}

		FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO;
		for (FuncaoTransacaoProjetoTO funcao : this.projetoFiltro.getFuncaoTransacaoProjetoTOList()) {

			if (funcao.getFuncaoTransacaoBaselineTO() == null) {
				funcaoTransacaoBaselineTO = FuncaoTransacaoBaselineTO.valueOf(funcao, baselineTO);
				funcao.setFuncaoTransacaoBaselineTO(funcaoTransacaoBaselineTO);
				funcaoTransacaoBaselineTO.setElementoContagemTO(parametroSistemaTO.getElementoContagemTO());
			} else {
				this.funcaoTransacaoBaselineDAO.deleteFilhos(funcao.getFuncaoTransacaoBaselineTO());
				funcao.getFuncaoTransacaoBaselineTO().updateByProjeto(funcao);
				funcao.getFuncaoTransacaoBaselineTO().setElementoContagemTO(parametroSistemaTO.getElementoContagemTO());
			}
			funcao.setComentarioRejeicao("");
			funcao.setSituacaoContagem(StatusAvaliacaoEnum.APROVADO);
			funcaoTransacaoProjetoDAO.update(funcao, false);
			baselineTO.addValorPFTransacao(funcao.getValorPontoFuncao());
		}
		baselineDAO.update(baselineTO);

		this.projetoFiltro.setServicoNaoMensuravelTOs(null);//Não é tratado atualmente

	}

	public boolean validarComentariosRejeicao(List<? extends Aprovacao> funcoes) {

		if (funcoes != null) {
			for (Aprovacao funcaoTO : funcoes) {
				if (!funcaoTO.isAprovado()
						&& (funcaoTO.getComentarioRejeicao() == null || "".equals(funcaoTO.getComentarioRejeicao()
								.trim()))) {
					addErrorMessageFromResource("label.comentario.rejeicao");
					return false;
				}
			}
		}

		return true;
	}

	public boolean validarAprovacao(List<? extends Aprovacao> funcoes) {
		boolean aprovado = true;

		if (funcoes != null) {
			for (Aprovacao funcaoTO : funcoes) {
				if (funcaoTO.isAprovado()) {
					funcaoTO.setComentarioRejeicao(null);
				} else {
					aprovado = false;
				}
			}
		}
		return aprovado;
	}

	public ProjetoTO getProjetoFiltro() {
		return this.projetoFiltro;
	}

	public BigDecimal getQtPFMensuraveis() {
		return this.getProjetoFiltro().getQtPontoFuncaoMensuravel() != null ? this.getProjetoFiltro()
				.getQtPontoFuncaoMensuravel() : BigDecimal.ZERO;
	}

	public BigDecimal getQtPFServicosNaoMensuraveis() {
		if (this.getProjetoFiltro().getQtPontoFuncaoServicoNaoMensuravel() != null) {
			return this.getProjetoFiltro().getQtPontoFuncaoServicoNaoMensuravel();
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal getQtPFTotal() {
		BigDecimal vlPFMens = BigDecimal.ZERO;
		BigDecimal vlPFNaoMens = BigDecimal.ZERO;

		if (this.getQtPFMensuraveis() != null) {
			vlPFMens = this.getQtPFMensuraveis();
		}

		if (this.getQtPFServicosNaoMensuraveis() != null) {
			vlPFNaoMens = this.getQtPFServicosNaoMensuraveis();
		}

		return vlPFMens.add(vlPFNaoMens);
	}

	public InspecaoFuncao getARs() {
		List<String> arsProj = null;
		List<String> arsBse = null;

		if (this.registroFT == null) {
			return null;
		}

		if (this.registroFT.getInspecaoARs() != null) {
			return this.registroFT.getInspecaoARs();
		}

		arsProj = this.funcaoTransacaoProjetoDAO.findARs(this.registroFT);

		if (this.registroFT.getFuncaoTransacaoBaselineTO() != null) {
			arsBse = this.funcaoTransacaoBaselineDAO.findARs(this.registroFT.getFuncaoTransacaoBaselineTO());
		}

		InspecaoFuncao inspecaoFuncoes = new InspecaoFuncao(arsBse, arsProj);

		this.registroFT.setInspecaoARs(inspecaoFuncoes);

		return inspecaoFuncoes;
	}

	public InspecaoFuncao getTRs() {
		List<String> trsPro = null;
		List<String> trsBse = null;

		if (this.registroFD == null) {
			return null;
		}

		if (this.registroFD.getInspecaoTRs() != null) {
			return this.registroFD.getInspecaoTRs();
		}

		if (registroFD.getFuncaoDadosBaselineTO() != null) {
			trsBse = this.funcaoDadosBaselineDAO.findTRs(this.registroFD.getFuncaoDadosBaselineTO());
		}

		trsPro = this.funcaoDadosProjetoDAO.findTRs(this.registroFD);

		InspecaoFuncao inspecaoFuncoes = new InspecaoFuncao(trsBse, trsPro);

		this.registroFD.setInspecaoTRs(inspecaoFuncoes);

		return inspecaoFuncoes;
	}

	public InspecaoFuncao getTDsFD() {
		List<String> tdsPro = null;
		List<String> tdsBse = null;

		if (this.registroFD.getFuncaoDadosBaselineTO() != null) {
			tdsBse = this.funcaoDadosBaselineDAO.findTDsReferenciadosAplicacao(this.registroFD
					.getFuncaoDadosBaselineTO());
		}

		if (this.registroFD.getInspecaoTDs() != null) {
			return this.registroFD.getInspecaoTDs();
		}

		tdsPro = this.funcaoDadosProjetoDAO.findTDsReferenciadosAplicacao(this.registroFD);

		InspecaoFuncao inspecaoFuncoes = new InspecaoFuncao(tdsBse, tdsPro);

		this.registroFD.setInspecaoTDs(inspecaoFuncoes);

		return inspecaoFuncoes;
	}

	public InspecaoFuncao getTDsFT() {
		List<String> tdsPro = null;
		List<String> tdsBse = null;

		if (this.registroFT == null) {
			return null;
		}

		if (this.registroFT.getInspecaoTDs() != null) {
			return this.registroFT.getInspecaoTDs();
		}

		if (this.registroFT.getFuncaoTransacaoBaselineTO() != null) {
			tdsBse = this.funcaoTransacaoBaselineDAO.findTDsAtravessamFronteira(this.registroFT
					.getFuncaoTransacaoBaselineTO());
		}

		tdsPro = this.funcaoTransacaoProjetoDAO.findTDsAtravessamFronteira(this.registroFT);

		InspecaoFuncao inspecaoFuncoes = new InspecaoFuncao(tdsBse, tdsPro);

		this.registroFT.setInspecaoTDs(inspecaoFuncoes);

		return inspecaoFuncoes;
	}

}