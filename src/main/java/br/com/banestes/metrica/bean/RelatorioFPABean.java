package br.com.sgpf.metrica.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.enumeration.TipoRelatorioEnum;
import br.com.sgpf.metrica.core.util.DateUtil;
import br.com.sgpf.metrica.dao.BaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ParametroSistemaDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.BaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoDadosTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.ServicoNaoMensuravelTO;
import br.com.sgpf.metrica.entity.util.FuncaoTOCompare;
import br.com.sgpf.metrica.excel.fpa.CabecalhoPlanilhaFPA;
import br.com.sgpf.metrica.excel.fpa.FuncaoPlanilhaFPA;
import br.com.sgpf.metrica.excel.fpa.PlanilaFPAsgpf;
import br.com.sgpf.metrica.excel.fpa.ServicoNaoMensuravelPlanilhaFPA;

@Name("relatorioFPABean")
@Scope(ScopeType.CONVERSATION)
public class RelatorioFPABean extends AbstractWebBean {
		
	private static final long serialVersionUID = 7378236478364638231L;

	@In(create = true)
	private FuncaoDadosBaselineDAO funcaoDadosBaselineDAO;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@In(create = true)
	private FuncaoTransacaoBaselineDAO funcaoTransacaoBaselineDAO;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@In(required = false)
	private FuncaoDadosTO registroFD;

	@In(required = false)
	private FuncaoTransacaoTO registroFT;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@In(create = true)
	private BaselineDAO baselineDAO;
	
	@In(create = true)
	private ParametroSistemaDAO parametroSistemaDAO;

	TipoContagem tipoContagem;

	TipoRelatorio tipoRelatorio;

	@DataModel
	private List<? extends FuncaoTransacaoTO> funcoesTrans;

	@DataModel
	private List<? extends FuncaoDadosTO> funcoesDados;

	@DataModel
	private List<? extends ServicoNaoMensuravelTO> servicosNaoMensuraveis;

	private TipoRelatorioEnum formatoRelatorio;

	private byte[] fileRelatorio;

	@In(create = true)
	private FuncaoProjetoFiltroBean funcaoProjetoFiltroBean;

	private Date dtCriacao = new Date();

	public RelatorioFPABean() {
	}

	public void preRender() {
	}

	public String pesquisar() {

		if (TipoContagem.PROJETO.equals(this.tipoContagem)) {
			if (this.funcaoProjetoFiltroBean.getProjetoFiltro() == null) {
				addErrorMessageFromResource(
						"javax.faces.component.UIInput.REQUIRED",
						getMessageFromResource("label.projeto"));
				return null;
			}
			if (this.funcaoProjetoFiltroBean.getProjetoFiltro().getSubProjeto() == null) {
				addErrorMessageFromResource(
						"javax.faces.component.UIInput.REQUIRED",
						getMessageFromResource("label.subprojeto"));
				return null;
			}
		}

		if (this.isProjeto()) {
			this.funcoesDados = this.funcaoProjetoFiltroBean.getProjetoFiltro()
					.getFuncaoDadosProjetoTOList();
			this.funcoesTrans = this.funcaoProjetoFiltroBean.getProjetoFiltro()
					.getFuncaoTransacaoProjetoTOList();
			this.servicosNaoMensuraveis = this.funcaoProjetoFiltroBean
					.getProjetoFiltro().getServicoNaoMensuravelTOs();
		} else {
			if (this.funcaoProjetoFiltroBean.getFiltroSistema().getBaselineTO() != null) {
				this.funcoesDados = this.funcaoProjetoFiltroBean
						.getFiltroSistema().getBaselineTO()
						.getFuncaoDadosBaselineTOs();
				this.funcoesTrans = this.funcaoProjetoFiltroBean
						.getFiltroSistema().getBaselineTO()
						.getFuncaoTransacaoBaselineTOs();
			}
		}
		if (this.funcoesDados != null)
			Collections.sort(this.funcoesDados, new FuncaoTOCompare());
		if (this.funcoesTrans != null)
			Collections.sort(this.funcoesTrans, new FuncaoTOCompare());

		return "relatorio";
	}

	public void updateValuesPf()
	{
		ProjetoTO projeto = this.funcaoProjetoFiltroBean.getProjetoFiltro().updateValorPf();
		
		projetoDAO.update(projeto);	
	}
	
	public BigDecimal getQtPFMensuraveis() {
				
		 
		
		if (this.isProjeto()) {
			updateValuesPf();
			return this.funcaoProjetoFiltroBean.getProjetoFiltro()
					.getQtPontoFuncaoMensuravel() != null ? this.funcaoProjetoFiltroBean
					.getProjetoFiltro().getQtPontoFuncaoMensuravel()
					: BigDecimal.ZERO;
		} else {
			if (this.funcaoProjetoFiltroBean.getFiltroSistema().getBaselineTO() == null
					|| this.funcaoProjetoFiltroBean.getFiltroSistema()
							.getBaselineTO().getQtPFTotal() == null) {
				return BigDecimal.ZERO;
			}
			
			BaselineTO baselineTO =super.em.find(BaselineTO.class, this.funcaoProjetoFiltroBean.getFiltroSistema()
					.getBaselineTO().getIdBaseLine());
		
			baselineTO.updateBaselineValues();
			
			//super.em.merge(baselineTO);
			baselineDAO.update(baselineTO);
			
			
			return this.funcaoProjetoFiltroBean.getFiltroSistema()
					.getBaselineTO().getQtPFTotal();
		}
	}

	public BigDecimal getQtPFServicosNaoMensuraveis() {
		if (this.isProjeto()
				&& this.funcaoProjetoFiltroBean.getProjetoFiltro()
						.getQtPontoFuncaoServicoNaoMensuravel() != null) {
			return this.funcaoProjetoFiltroBean.getProjetoFiltro()
					.getQtPontoFuncaoServicoNaoMensuravel();
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

	public boolean isProjeto() {
		return TipoContagem.PROJETO.equals(this.tipoContagem);
	}

	public void exportar() {
		this.formatoRelatorio = null;
		this.fileRelatorio = null;

		CabecalhoPlanilhaFPA cabecalhoPlanilhaFPA;

		if (isProjeto()) {
			cabecalhoPlanilhaFPA = this.funcaoProjetoFiltroBean
					.getProjetoFiltro();
		} else {
			cabecalhoPlanilhaFPA = this.funcaoProjetoFiltroBean
					.getFiltroSistema();
		}

		if (this.funcoesTrans == null) {
			this.funcoesTrans = new ArrayList<FuncaoTransacaoTO>();
		}
		if (this.funcoesDados == null) {
			this.funcoesDados = new ArrayList<FuncaoDadosTO>();
		}

		List<FuncaoPlanilhaFPA> funcoes = new ArrayList<FuncaoPlanilhaFPA>();
		funcoes.addAll(this.funcoesTrans);
		funcoes.addAll(this.funcoesDados);

		PlanilaFPAsgpf planilha;

		if (this.isProjeto()) {
			List<ServicoNaoMensuravelPlanilhaFPA> snm = new ArrayList<ServicoNaoMensuravelPlanilhaFPA>();
			snm.addAll(this.servicosNaoMensuraveis);
			planilha = new PlanilaFPAsgpf(cabecalhoPlanilhaFPA, funcoes,
					snm);
		} else {
			planilha = new PlanilaFPAsgpf(cabecalhoPlanilhaFPA, funcoes);
		}

		ExternalContext servletContext = getFacesContext().getExternalContext();

		InputStream is = servletContext
				.getResourceAsStream("/WEB-INF/planilhapadrao/FPA_NumSSI_NumOS.xls");

		this.fileRelatorio = planilha.export(is);
		this.formatoRelatorio = TipoRelatorioEnum.XLS;

	}

	public void imprimir() {

		try {
			ServletContext context = (ServletContext) getExternalContext()
					.getContext();
			String arquivo;

			JREmptyDataSource eds = new JREmptyDataSource(1);

			CabecalhoRelatorio cabecalho = new CabecalhoRelatorio();

			Map<String, Object> parametros = new HashMap<String, Object>();

			arquivo = context.getRealPath("report/fpa/reportFPA.jasper");

			if (this.isProjeto()) {
				cabecalho.setAnalistaFornecedor(funcaoProjetoFiltroBean
						.getProjetoFiltro().getResponsavelPrincipalClienteTO()
						.getNomeAnalista());
				cabecalho.setDtCriacao(DateUtil.dateToString(
						funcaoProjetoFiltroBean.getProjetoFiltro()
								.getDtCriacao(), "dd/MM/yyyy"));
				cabecalho.setFornecedor(funcaoProjetoFiltroBean
						.getProjetoFiltro().getResponsavelPrincipalClienteTO()
						.getNomeAnalista());
				cabecalho.setPfFuncoesMensuraveis(this.getQtPFMensuraveis());
				cabecalho.setPfServicosNaoMensuraveis(this
						.getQtPFServicosNaoMensuraveis());
				cabecalho.setPfTotal(this.getQtPFTotal());
				cabecalho.setProjeto(this.funcaoProjetoFiltroBean
						.getProjetoFiltro().getProjeto());
				cabecalho.setSistema(this.funcaoProjetoFiltroBean
						.getFiltroSistema().getSistema());
				cabecalho.setSubProjeto(this.funcaoProjetoFiltroBean
						.getProjetoFiltro().getSubProjeto());
				parametros.put("FL_PROJETO", true);
			} else {
				cabecalho.setSistema(this.funcaoProjetoFiltroBean
						.getFiltroSistema().getSistema());
				cabecalho.setFornecedor(funcaoProjetoFiltroBean
						.getFiltroSistema().getFornecedor());
				cabecalho.setPfTotal(this.getQtPFTotal());
				parametros.put("FL_PROJETO", false);
			}

			if (TipoRelatorio.DETALHADO.equals(this.tipoRelatorio)) {
				parametros.put("FL_DETALHADO", true);
			} else {
				parametros.put("FL_DETALHADO", false);
			}

			JRBeanCollectionDataSource jrds;

			jrds = new JRBeanCollectionDataSource(this.funcoesDados);
			parametros.put("DS_FUNCAO_DADOS", jrds);
			jrds = new JRBeanCollectionDataSource(this.funcoesTrans);
			parametros.put("DS_FUNCAO_TRANSACAO", jrds);
			jrds = new JRBeanCollectionDataSource(this.servicosNaoMensuraveis);
			parametros.put("DS_SNM", jrds);

			parametros.put("SUBREPORT_DIR",
					context.getRealPath("report/fpa/subreport/"));
			parametros.put("TEMPLATE_DIR",
					context.getRealPath("report/templates/"));
			parametros.put("CABECALHO", cabecalho);
			parametros.put("IMG_LOGO",
					context.getRealPath("/img/logo_pd_metrica.png"));
			parametros.put("DATA_EMISSAO",
					DateUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
			parametros.put("USUARIO_EMISSAO", getUsuarioEmissaoRelatorio());

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperRunManager.runReportToPdfStream(new FileInputStream(new File(
					arquivo)), baos, parametros, eds);

			String nome = this.isProjeto() ? cabecalho.getProjeto() : cabecalho
					.getSistema();
			nome = getFileName();

			downloadReport(nome, baos.toByteArray(), TipoRelatorioEnum.PDF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void obterImpressao() {

		String nomePlanilha = getFileName();

		if (this.fileRelatorio != null) {
			downloadReport(nomePlanilha, this.fileRelatorio,
					this.formatoRelatorio);
		}
		this.tipoRelatorio = null;
		this.fileRelatorio = null;
	}

	private String getFileName() {
		if (this.isProjeto()) {
			return String.format("FPA_%s_%s", this.funcaoProjetoFiltroBean
					.getProjetoFiltro().getProjeto(),
					this.funcaoProjetoFiltroBean.getProjetoFiltro()
							.getSubProjeto());
		} else {
			return String.format("FPA_%s", this.funcaoProjetoFiltroBean
					.getFiltroSistema().getSistema());
		}
	}

	public String getARs() {
		List<String> ars = null;

		if (this.registroFT == null) {
			return null;
		}

		if (this.registroFT.getDescricaoARsTRs() == null) {

			if (this.isProjeto()) {
				ars = this.funcaoTransacaoProjetoDAO
						.findARs((FuncaoTransacaoProjetoTO) this.registroFT);
			} else {
				ars = this.funcaoTransacaoBaselineDAO
						.findARs((FuncaoTransacaoBaselineTO) this.registroFT);
			}

			this.registroFT.setDescricaoARsTRs(getDesc(ars));
		}

		return this.registroFT.getDescricaoARsTRs();
	}

	public String getTRs() {
		List<String> trs = null;

		if (this.registroFD == null) {
			return null;
		}

		if (this.registroFD.getDescricaoARsTRs() == null) {

			if (this.isProjeto()) {
				trs = this.funcaoDadosProjetoDAO
						.findTRs((FuncaoDadosProjetoTO) this.registroFD);
			} else {
				trs = this.funcaoDadosBaselineDAO
						.findTRs((FuncaoDadosBaselineTO) this.registroFD);
			}

			this.registroFD.setDescricaoARsTRs(getDesc(trs));
		}

		return this.registroFD.getDescricaoARsTRs();
	}

	public String getTDsFD() {
		List<String> tds;

		if (this.registroFD == null) {
			return null;
		}

		if (this.registroFD.getDescricaoTDs() == null) {
			if (this.isProjeto()) {
				tds = this.funcaoDadosProjetoDAO
						.findTDsReferenciadosAplicacao((FuncaoDadosProjetoTO) this.registroFD);
			} else {
				tds = this.funcaoDadosBaselineDAO
						.findTDsReferenciadosAplicacao((FuncaoDadosBaselineTO) this.registroFD);
			}

			this.registroFD.setDescricaoTDs(getDesc(tds));

		}

		return this.registroFD.getDescricaoTDs();
	}

	public String getTDsFT() {
		List<String> tds = new ArrayList<String>();

		if (this.registroFT == null) {
			return null;
		}

		if (this.registroFT.getDescricaoTDs() == null) {
			if (this.isProjeto()) {
				tds = this.funcaoTransacaoProjetoDAO
						.findTDsAtravessamFronteira((FuncaoTransacaoProjetoTO) this.registroFT);
			} else {
				tds = this.funcaoTransacaoBaselineDAO
						.findTDsAtravessamFronteira((FuncaoTransacaoBaselineTO) this.registroFT);
			}

			this.registroFT.setDescricaoTDs(getDesc(tds));
		}

		return this.registroFT.getDescricaoTDs();
	}

	private String getDesc(List<String> listDesc) {
		StringBuilder result = new StringBuilder(" ");

		if (listDesc != null) {

			int i = 1;
			for (String ar : listDesc) {
				result.append(ar);

				if (i < listDesc.size()) {
					result.append(", ");
				}

				i++;
			}

		}

		return result.toString();
	}

	public List<? extends FuncaoTransacaoTO> getFuncoesTrans() {
		return funcoesTrans;
	}

	public void setFuncoesTrans(List<FuncaoTransacaoTO> funcoesTrans) {
		this.funcoesTrans = funcoesTrans;
	}

	public List<? extends FuncaoDadosTO> getFuncoesDados() {
		return funcoesDados;
	}

	public void setFuncoesDados(List<FuncaoDadosTO> funcoesDados) {
		this.funcoesDados = funcoesDados;
	}

	public TipoContagem getTipoContagem() {
		return tipoContagem;
	}

	public void setTipoContagem(TipoContagem tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public List<? extends ServicoNaoMensuravelTO> getServicosNaoMensuraveis() {
		return servicosNaoMensuraveis;
	}

	public void setServicosNaoMensuraveis(
			List<? extends ServicoNaoMensuravelTO> servicosNaoMensuraveis) {
		this.servicosNaoMensuraveis = servicosNaoMensuraveis;
	}

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public TipoContagem[] getTiposContagem() {
		return TipoContagem.values();
	}

	public TipoRelatorio[] getTiposRelatorio() {
		return TipoRelatorio.values();
	}

	public FuncaoProjetoFiltroBean getFuncaoProjetoFiltroBean() {
		return funcaoProjetoFiltroBean;
	}

	public void setFuncaoProjetoFiltroBean(
			FuncaoProjetoFiltroBean funcaoProjetoFiltroBean) {
		this.funcaoProjetoFiltroBean = funcaoProjetoFiltroBean;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

}

enum TipoContagem {
	BASELINE("Baseline"), PROJETO("Projeto");

	private String desc;

	private TipoContagem(String desc) {
		this.desc = desc;
	}

	public String getDescricao() {
		return desc;
	}

}

enum TipoRelatorio {
	CONSOLIDADO("Consolidado"), DETALHADO("Detalhado");

	private String desc;

	private TipoRelatorio(String desc) {
		this.desc = desc;
	}

	public String getDescricao() {
		return desc;
	}

}
