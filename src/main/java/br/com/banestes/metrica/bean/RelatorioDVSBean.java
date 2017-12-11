package br.com.sgpf.metrica.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.TipoRelatorioEnum;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.DescricaoAtributoTD;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.TipoRegistroProjetoTO;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;
import br.com.sgpf.metrica.view.FuncaoDadosProjetoVO;
import br.com.sgpf.metrica.view.FuncaoTransacaoProjetoVO;
import br.com.sgpf.metrica.word.dvs.DVSDocument;

@Name("relatorioDVSBean")
@Scope(ScopeType.CONVERSATION)
public class RelatorioDVSBean extends AbstractWebBean {

	private static final long serialVersionUID = 4622855304901327330L;

	@Out(required = false)
	private List<ContratoTO> contratosTOlist;

	@Out(required = false)
	private List<SistemaTO> sistemaTOlist;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@Out
	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListIncluidas = new ArrayList<FuncaoTransacaoProjetoVO>();

	@Out
	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListAlteras = new ArrayList<FuncaoTransacaoProjetoVO>();

	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListExcluidas = new ArrayList<FuncaoTransacaoProjetoVO>();

	@Out
	private List<FuncaoDadosProjetoVO> funcoesDadosVOListIncluidas = new ArrayList<FuncaoDadosProjetoVO>();

	@Out
	private List<FuncaoDadosProjetoVO> funcoesDadosVOListAlteras = new ArrayList<FuncaoDadosProjetoVO>();

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListExcluidas = new ArrayList<FuncaoDadosProjetoVO>();

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListNaoMensuraveis = new ArrayList<FuncaoDadosProjetoVO>();

	private List<ProjetoTO> selecaoProjetos;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@Out(required = false)
	private ProjetoTO projetoTO;

	@Out(required = false)
	private boolean consultou;

	@In(create = true)
	private FuncaoProjetoFiltroBean funcaoProjetoFiltroBean;

	//	private List<ServicoNaoMensuravelTO> servicosNaoMensuraveis;

	public void limpar() {
		consultou = false;
		this.funcaoProjetoFiltroBean.setEmpresaFiltro(null);
		this.funcaoProjetoFiltroBean.setEmpresaFiltro(null);
		this.funcaoProjetoFiltroBean.setProjetoFiltro(null);
		this.funcaoProjetoFiltroBean.setFiltroSistema(null);
		this.funcaoProjetoFiltroBean.setProjetoSelecionado(null);
	}

	public void gerarDoc() {

		ServletContext context = (ServletContext) getExternalContext().getContext();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String arquivo = context.getRealPath("template/SSI_2.dotx");

		DVSDocument.Builder builder = new DVSDocument.Builder(arquivo, baos);
		builder.doProjeto(this.projetoTO);

		if (funcoesTransacaoVOListIncluidas != null)
			builder.comTransacoesIncluidas(funcoesTransacaoVOListIncluidas);

		if (funcoesTransacaoVOListAlteras != null)
			builder.comTransacoesAlteradas(funcoesTransacaoVOListAlteras);

		if (funcoesTransacaoVOListExcluidas != null)
			builder.comTransacoesExcluidas(funcoesTransacaoVOListExcluidas);

		if (funcoesDadosVOListIncluidas != null)
			builder.incluirFuncoesDados(funcoesDadosVOListIncluidas);

		if (funcoesDadosVOListAlteras != null)
			builder.incluirFuncoesDadosAlteradas(funcoesDadosVOListAlteras);

		if (funcoesDadosVOListExcluidas != null)
			builder.incluirFuncoesDadosExcluidas(funcoesDadosVOListExcluidas);

		if (funcoesDadosVOListNaoMensuraveis != null)
			builder.comFuncaoDadosNaoMensuraveis(funcoesDadosVOListNaoMensuraveis);

		DVSDocument dvsDocument = builder.instanciar();
		try {
			dvsDocument.gerarDvs();
			downloadReport("DVS_" + projetoTO.getProjeto() + "_" + projetoTO.getSubProjeto(), baos.toByteArray(),
					TipoRelatorioEnum.DOC);
		} catch (Exception e) {
			this.logger.error(this, e, "");
			addErrorMessageFromResource("Erro ao criar o documento word");
		}

	}

	public void gerarRelatorio() {

		try {
			ServletContext context = (ServletContext) getExternalContext().getContext();
			String arquivo = context.getRealPath("report/dvsReport.jasper");

			List<FuncaoTransacaoProjetoVO> list = new ArrayList<FuncaoTransacaoProjetoVO>();
			if (funcoesTransacaoVOListIncluidas != null)
				list.addAll(funcoesTransacaoVOListIncluidas);

			if (funcoesTransacaoVOListAlteras != null)
				list.addAll(funcoesTransacaoVOListAlteras);

			JRDataSource jrds = new JRBeanCollectionDataSource(list);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperRunManager.runReportToPdfStream(new FileInputStream(new File(arquivo)), baos,
					new HashMap<Object, Object>(), jrds);

			downloadReport("arquivo", baos.toByteArray(), TipoRelatorioEnum.PDF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void consultar() {
		this.projetoTO = this.funcaoProjetoFiltroBean.getProjetoFiltro();

		// ==================================
		// Funções de TRANSAÇÃO incluídas e alteradas
		// ==================================

		List<FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoTOList = projetoTO.getFuncaoTransacaoProjetoTOList();

		funcoesTransacaoVOListIncluidas = new ArrayList<FuncaoTransacaoProjetoVO>(funcaoTransacaoProjetoTOList.size());
		funcoesTransacaoVOListAlteras = new ArrayList<FuncaoTransacaoProjetoVO>(funcaoTransacaoProjetoTOList.size());
		funcoesTransacaoVOListExcluidas = new ArrayList<FuncaoTransacaoProjetoVO>(funcaoTransacaoProjetoTOList.size());
		FuncaoTransacaoProjetoVO.Builder builder = null;
		List<DescricaoAtributoTD> tds;
		List<String> ARs;

		for (FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO : funcaoTransacaoProjetoTOList) {

			boolean isElementoQuantidade = funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE;

			builder = new FuncaoTransacaoProjetoVO.Builder();
			builder.comNome(funcaoTransacaoProjetoTO.getNome());
			builder.comJustificativaSe(funcaoTransacaoProjetoTO.getJustificativaSe());
			builder.comDescricao(funcaoTransacaoProjetoTO.getDescricao());
			builder.comArquivosReferenciados(funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs());
			builder.comDadosDerivados(funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs());
			builder.comComplexidade(funcaoTransacaoProjetoTO.getComplexidadeTP());
			builder.doTipo(funcaoTransacaoProjetoTO.getFuncaoTransacaoTP());
			builder.comAR(funcaoTransacaoProjetoTO.getQtArquivoReferenciado());
			builder.comTD(funcaoTransacaoProjetoTO.getQtTipoDados());

			if (!isElementoQuantidade) {
				tds = funcaoTransacaoProjetoDAO.findAtributo(funcaoTransacaoProjetoTO);
				builder.comTipoDados(tds.toString().replace("[", "").replace("]", ""));
				builder.comTipoDadosTemp(tds);

				ARs = funcaoTransacaoProjetoDAO.findARs(funcaoTransacaoProjetoTO);
				builder.referenciaOsArquivos(ARs.toString().replace("[", "").replace("]", ""));
			} else {
				builder.setElementoQtdade(true);
				builder.quantidade(funcaoTransacaoProjetoTO.getQtItem());
			}

			if (funcaoTransacaoProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.INCLUSAO) {
				builder.doTipo("Incluídas");
				funcoesTransacaoVOListIncluidas.add(builder.instanciar());
			} else if (funcaoTransacaoProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.ALTERACAO) {
				builder.doTipo("Alteradas");
				builder.comManutencao(funcaoTransacaoProjetoTO.getDescricaoManutencao());
				funcoesTransacaoVOListAlteras.add(builder.instanciar());
			} else if (funcaoTransacaoProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.EXCLUSAO) {
				builder.doTipo("Excluídas");
				funcoesTransacaoVOListExcluidas.add(builder.instanciar());
			}

		}

		Collections.sort(funcoesTransacaoVOListIncluidas);
		Collections.sort(funcoesTransacaoVOListAlteras);
		Collections.sort(funcoesTransacaoVOListExcluidas);

		// ==================================
		// Funções de DADOS incluídas e alteradas
		// ==================================

		List<FuncaoDadosProjetoTO> funcaoDadosProjetoTOList = projetoTO.getFuncaoDadosProjetoTOList();

		funcoesDadosVOListAlteras = new ArrayList<FuncaoDadosProjetoVO>();
		funcoesDadosVOListIncluidas = new ArrayList<FuncaoDadosProjetoVO>();
		funcoesDadosVOListExcluidas = new ArrayList<FuncaoDadosProjetoVO>();
		funcoesDadosVOListNaoMensuraveis = new ArrayList<FuncaoDadosProjetoVO>();

		FuncaoDadosProjetoVO.Builder funcaoDadosProjetoVOBuilder;

		List<TipoRegistroProjetoTO> tipoRegistroProjetoTOs;
		List<AtributoTO> atributoTOlist;

		for (FuncaoDadosProjetoTO funcaoDadosProjetoTO : funcaoDadosProjetoTOList) {
			funcaoDadosProjetoVOBuilder = new FuncaoDadosProjetoVO.Builder();
			funcaoDadosProjetoVOBuilder.comNome(funcaoDadosProjetoTO.getNome());
			funcaoDadosProjetoVOBuilder.comDescricao(funcaoDadosProjetoTO.getDescricao());
			funcaoDadosProjetoVOBuilder.comManutencao(funcaoDadosProjetoTO.getDescricaoManutencao());
			funcaoDadosProjetoVOBuilder.comElementoContagem(funcaoDadosProjetoTO.getElementoContagemTO());
			funcaoDadosProjetoVOBuilder.quantidadePontoFuncao(funcaoDadosProjetoTO.obterValorPontoFuncao());

			tipoRegistroProjetoTOs = funcaoDadosProjetoTO.getTipoRegistroProjetoTOs();
			atributoTOlist = new ArrayList<AtributoTO>();

			TipoDadosFuncaoDadosProjetoTO next;
			for (TipoRegistroProjetoTO tipoRegistroProjetoTO : tipoRegistroProjetoTOs) {
				//				atributoTOlist.addAll(tipoRegistroProjetoTO.getEntidadeTO().getAtributoTOlist());

				Iterator<TipoDadosFuncaoDadosProjetoTO> iterator = tipoRegistroProjetoTO
						.getTipoDadosFuncaoDadosProjetoTOs().iterator();
				while (iterator.hasNext()) {
					next = iterator.next();
					if (next.getFlReferenciaAplicacao() == SimNaoEnum.SIM)
						atributoTOlist.add(next.getAtributoTO());
				}
			}
			funcaoDadosProjetoVOBuilder.possuiOsAtributos(atributoTOlist);

			if (funcaoDadosProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.INCLUSAO) {
				funcoesDadosVOListIncluidas.add(funcaoDadosProjetoVOBuilder.instanciar());
			} else if (funcaoDadosProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.ALTERACAO) {
				funcoesDadosVOListAlteras.add(funcaoDadosProjetoVOBuilder.instanciar());
			} else if (funcaoDadosProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.EXCLUSAO) {
				funcoesDadosVOListExcluidas.add(funcaoDadosProjetoVOBuilder.instanciar());
			} else if (funcaoDadosProjetoTO.getElementoContagemTO().getIndItemMensuravel() == TipoItemMensuraveisEnum.NAO_MENSURAVEL) {
				funcoesDadosVOListNaoMensuraveis.add(funcaoDadosProjetoVOBuilder.instanciar());
			}
		}
		Collections.sort(funcoesDadosVOListAlteras);
		Collections.sort(funcoesDadosVOListIncluidas);
		Collections.sort(funcoesDadosVOListExcluidas);
		Collections.sort(funcoesDadosVOListNaoMensuraveis);

		consultou = true;
	}

	public void selecionarEmpresa() {
		if (this.funcaoProjetoFiltroBean.getEmpresaFiltro() != null) {
			this.contratosTOlist = this.funcaoProjetoFiltroBean.getEmpresaFiltro().getContratoTOlist();
			this.sistemaTOlist = this.funcaoProjetoFiltroBean.getEmpresaFiltro().getSistemaTOlist();
		}
	}

	public List<SistemaTO> getSistemaTOlist() {
		return sistemaTOlist;
	}

	public void setSistemaTOlist(List<SistemaTO> sistemaTOlist) {
		if (sistemaTOlist == null)
			sistemaTOlist = new ArrayList<SistemaTO>();
		this.sistemaTOlist = sistemaTOlist;
	}

	public List<ContratoTO> getContratosTOlist() {
		return contratosTOlist;
	}

	public void setContratosTOlist(List<ContratoTO> contratosTOlist) {
		this.contratosTOlist = contratosTOlist;
	}

	public List<ProjetoTO> getSelecaoProjetos() {
		return selecaoProjetos;
	}

	public void setSelecaoProjetos(List<ProjetoTO> selecaoProjetos) {
		this.selecaoProjetos = selecaoProjetos;
	}

	public ProjetoTO getProjetoTO() {
		return projetoTO;
	}

	public void setProjetoTO(ProjetoTO projetoTO) {
		this.projetoTO = projetoTO;
	}

	public List<FuncaoTransacaoProjetoVO> getFuncoesTransacaoVOListIncluidas() {
		return funcoesTransacaoVOListIncluidas;
	}

	public void setFuncoesTransacaoVOListIncluidas(List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListIncluidas) {
		this.funcoesTransacaoVOListIncluidas = funcoesTransacaoVOListIncluidas;
	}

	public List<FuncaoTransacaoProjetoVO> getFuncoesTransacaoVOListAlteras() {
		return funcoesTransacaoVOListAlteras;
	}

	public void setFuncoesTransacaoVOListAlteras(List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListAlteras) {
		this.funcoesTransacaoVOListAlteras = funcoesTransacaoVOListAlteras;
	}

	public List<FuncaoDadosProjetoVO> getFuncoesDadosVOListIncluidas() {
		return funcoesDadosVOListIncluidas;
	}

	public void setFuncoesDadosVOListIncluidas(List<FuncaoDadosProjetoVO> funcoesDadosVOListIncluidas) {
		this.funcoesDadosVOListIncluidas = funcoesDadosVOListIncluidas;
	}

	public List<FuncaoDadosProjetoVO> getFuncoesDadosVOListAlteras() {
		return funcoesDadosVOListAlteras;
	}

	public void setFuncoesDadosVOListAlteras(List<FuncaoDadosProjetoVO> funcoesDadosVOListAlteras) {
		this.funcoesDadosVOListAlteras = funcoesDadosVOListAlteras;
	}

	public boolean isConsultou() {
		return consultou;
	}

	public void setConsultou(boolean consultou) {
		this.consultou = consultou;
	}

	public List<FuncaoDadosProjetoVO> getFuncoesDadosVOListNaoMensuraveis() {
		return funcoesDadosVOListNaoMensuraveis;
	}

	public List<FuncaoDadosProjetoVO> getFuncoesDadosVOListExcluidas() {
		return funcoesDadosVOListExcluidas;
	}

	public FuncaoProjetoFiltroBean getFuncaoProjetoFiltroBean() {
		return funcaoProjetoFiltroBean;
	}

	public void setFuncaoProjetoFiltroBean(FuncaoProjetoFiltroBean funcaoProjetoFiltroBean) {
		this.funcaoProjetoFiltroBean = funcaoProjetoFiltroBean;
	}

	public List<FuncaoTransacaoProjetoVO> getFuncoesTransacaoVOListExcluidas() {
		return funcoesTransacaoVOListExcluidas;
	}

	/*public List<ServicoNaoMensuravelTO> getServicosNaoMensuraveis() {
		return servicosNaoMensuraveis;
	}

	public void setServicosNaoMensuraveis(List<ServicoNaoMensuravelTO> servicosNaoMensuraveis) {
		this.servicosNaoMensuraveis = servicosNaoMensuraveis;
	}
	*/

}
