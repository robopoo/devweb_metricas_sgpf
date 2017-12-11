/**
 * 
 */
package br.com.sgpf.metrica.bean;

import java.util.ArrayList;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.dao.ArquivoReferenciadoProjetoDAO;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.dao.TipoRegistroProjetoDAO;
import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.1
 * @since 10/03/2014
 * @time 16:37:25
 */

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Fernando Padrão Silveira <mailto:fernando.silveira@pdcase.com.br>
 * @version 1.0.2
 * @since 18/05/2014
 * @time 16:37:25
 */
@Name("funcaoTransacaoProjetoBean")
@Scope(ScopeType.CONVERSATION)
public class FuncaoTransacaoProjetoBean extends
		AbstractCrudWebBean<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoDAO, FuncaoTransacaoProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3194842241363633177L;

	@Out(required = false)
	private ProjetoTO projetoTO;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@Out(required = false)
	private EmpresaTO empresaTO;

	@In(create = true)
	private ContratoDAO contratoDAO;

	@In(create = true)
	private AnalistaDAO analistaDAO;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	@In(create = true)
	private EntidadeDAO entidadeDAO;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@Out(required = false)
	private SistemaTO sistemaTO;

	@In(create = true)
	@Out(required = true)
	private FuncaoTransacaoController funcaoTransacaoController;

	@In(create = true)
	private TipoRegistroProjetoDAO tipoRegistroProjetoDAO;

	@In(create = true)
	private ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO;

	private Set<String> selecaoProjetos;

	private String projetoSelecionado;

	@Out(required = false)
	private FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO;

	@In(create = true)
	@Out(required = false)
	private IDataModel<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoDataModel;

	@In(create = true)
	private FuncaoBean funcaoBean;

	private String nomeFuncao;

	@In(create = true)
	@Out(required = true)
	private FuncaoProjetoFiltroBean funcaoProjetoFiltroBean;

	public void preRender() {
		super.preRender();
		if (this.funcaoProjetoFiltroBean.getProjetoFiltro() != null) {
			super.buscar();
			funcaoProjetoFiltroBean.atualizarFiltro();
		}
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	public String visualizar() {
		//TODO
		return null;
	}

	@Override
	public String editar() {

		setVo(getSelecao());
		setOperacao(OperacoesEnum.ALTERACAO);

		this.funcaoTransacaoController
				.preRenderForm(this.getVo().getProjetoTO(), this.getVo(), OperacoesEnum.ALTERACAO);

		return getAlteracaoOutcome();
	}

	public String novo() {

		String outcome = super.novo();

		getVo().setArquivoReferenciadoProjetoTOs(new ArrayList<ArquivoReferenciadoProjetoTO>());

		getVo().setProjetoTO(this.funcaoProjetoFiltroBean.getProjetoFiltro());

		getVo().getProjetoTO().getFuncaoTransacaoProjetoTOList().add(getVo());

		this.funcaoTransacaoController.preRenderForm(this.funcaoProjetoFiltroBean.getProjetoFiltro(), getVo(),
				OperacoesEnum.INSERCAO);

		return outcome;
	}

	public void excluir() {
		setVo((FuncaoTransacaoProjetoTO) getSelecao());
		setOperacao(OperacoesEnum.EXCLUSAO);
		this.getVo().getProjetoTO().getFuncaoTransacaoProjetoTOList().remove(this.getVo());
		this.funcaoBean.atualizarTotaisProjeto(true, this.getVo(), this.getVo().getProjetoTO());
		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.funcaoProjetoFiltroBean.setFiltroContrato(null);
		this.funcaoProjetoFiltroBean.setFiltroSistema(null);
		this.funcaoProjetoFiltroBean.setEmpresaFiltro(null);
		this.funcaoProjetoFiltroBean.setProjetoFiltro(null);
		this.funcaoProjetoFiltroBean.setProjetoSelecionado(null);
		getListagem().limpar();
	}

	@Override
	protected boolean limparListagem() {
		return false;
	}

	public SistemaTO getSistemaTO() {
		return sistemaTO;
	}

	public void setSistemaTO(SistemaTO sistemaTO) {
		this.sistemaTO = sistemaTO;
	}

	public FuncaoTransacaoController getFuncaoTransacaoController() {
		return funcaoTransacaoController;
	}

	public void setFuncaoTransacaoController(FuncaoTransacaoController funcaoTransacaoController) {
		this.funcaoTransacaoController = funcaoTransacaoController;
	}

	protected boolean resetForm() {
		return false;
	}

	public String getNomeFuncao() {
		return nomeFuncao;
	}

	public void setNomeFuncao(String nomeFuncao) {
		this.nomeFuncao = nomeFuncao;
	}

	@Override
	protected FuncaoTransacaoProjetoDAO getDao() {
		return this.funcaoTransacaoProjetoDAO;
	}

	@Override
	protected FuncaoTransacaoProjetoTO getVo() {
		return this.funcaoTransacaoProjetoTO;
	}

	@Override
	protected FuncaoTransacaoProjetoTO getFiltro() {
		FuncaoTransacaoProjetoTO ftp = new FuncaoTransacaoProjetoTO();
		ftp.setProjetoTO(this.funcaoProjetoFiltroBean.getProjetoFiltro());
		return ftp;
	}

	@Override
	protected void setVo(FuncaoTransacaoProjetoTO vo) {
		this.funcaoTransacaoProjetoTO = vo;
	}

	@Override
	protected IDataModel<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoTO> getListagem() {
		return this.funcaoTransacaoProjetoDataModel;
	}

	@Override
	protected void setListagem(IDataModel<FuncaoTransacaoProjetoTO, Long, FuncaoTransacaoProjetoTO> listagem) {
		this.funcaoTransacaoProjetoDataModel = listagem;
	}

	public Set<String> getSelecaoProjetos() {
		return selecaoProjetos;
	}

	public void setSelecaoProjetos(Set<String> selecaoProjetos) {
		this.selecaoProjetos = selecaoProjetos;
	}

	public String getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(String projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public FuncaoProjetoFiltroBean getFuncaoProjetoFiltroBean() {
		return funcaoProjetoFiltroBean;
	}

	public void setFuncaoProjetoFiltroBean(FuncaoProjetoFiltroBean funcaoProjetoFiltroBean) {
		this.funcaoProjetoFiltroBean = funcaoProjetoFiltroBean;
	}

}