/**
 * 
 */
package br.com.sgpf.metrica.bean;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.vo.MensagemErro;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.dao.ArquivoReferenciadoProjetoDAO;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.dao.TipoRegistroProjetoDAO;
import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.entity.TipoRegistroProjetoTO;
import br.com.sgpf.metrica.enumeration.TipoFuncaoDadosProjetoEnum;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 26/02/2014
 */

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
@Name("funcaoDadosProjetoBean")
@Scope(ScopeType.CONVERSATION)
public class FuncaoDadosProjetoBean extends
		AbstractCrudWebBean<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoDAO, FuncaoDadosProjetoTO> {

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
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@Out(required = false)
	private SistemaTO sistemaTO;

	@In(create = true)
	@Out(required = true)
	private FuncaoDadosController funcaoDadosController;

	@In(create = true)
	private TipoRegistroProjetoDAO tipoRegistroProjetoDAO;

	@In(create = true)
	private ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO;

	@Out(required = false)
	private FuncaoDadosProjetoTO funcaoDadosProjetoTO;

	@In(create = true)
	@Out(required = false)
	private IDataModel<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoTO> funcaoDadosProjetoDataModel;

	private String nomeFuncao;

	@In(create = true)
	private FuncaoBean funcaoBean;

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

	public void valueChangedTipoFuncaoDados(ValueChangeEvent event) {
		funcaoDadosController.atualizaSistemaEntidadeListas((TipoFuncaoDadosProjetoEnum) event.getNewValue());
	}

	public String editar() {

		setVo(getSelecao());
		setOperacao(OperacoesEnum.ALTERACAO);

		this.funcaoDadosController.preRenderForm(this.getVo().getProjetoTO(), this.getVo(), OperacoesEnum.ALTERACAO);

		return getAlteracaoOutcome();
	}

	public String novo() {

		String outcome = super.novo();

		getVo().setTipoRegistroProjetoTOs(new ArrayList<TipoRegistroProjetoTO>());

		getVo().setProjetoTO(this.funcaoProjetoFiltroBean.getProjetoFiltro());

		this.funcaoDadosController.preRenderForm(this.funcaoProjetoFiltroBean.getProjetoFiltro(), getVo(),
				OperacoesEnum.INSERCAO);

		return outcome;
	}

	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((FuncaoDadosProjetoTO) getSelecao());
		setOperacao(OperacoesEnum.EXCLUSAO);

		ArquivoReferenciadoProjetoTO filtro = new ArquivoReferenciadoProjetoTO();
		filtro.setFuncaoDadosProjetoTO(getVo());

		int qtArqs = arquivoReferenciadoProjetoDAO.getCount(filtro);

		if (qtArqs > 0) {
			throw new BusinessException(new MensagemErro("msg.arquivo.referenciado.associado"));
		}

		this.getVo().getProjetoTO().getFuncaoDadosProjetoTOList().remove(this.getVo());

		this.funcaoBean.atualizarTotaisProjeto(true, this.getVo(), this.getVo().getProjetoTO());

		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	public SistemaTO getSistemaTO() {
		return sistemaTO;
	}

	public void setSistemaTO(SistemaTO sistemaTO) {
		this.sistemaTO = sistemaTO;
	}

	public FuncaoDadosController getFuncaoDadosController() {
		return funcaoDadosController;
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

	protected boolean resetForm() {
		return false;
	}

	@Override
	protected FuncaoDadosProjetoDAO getDao() {
		return this.funcaoDadosProjetoDAO;
	}

	@Override
	protected FuncaoDadosProjetoTO getVo() {
		return this.funcaoDadosProjetoTO;
	}

	@Override
	protected FuncaoDadosProjetoTO getFiltro() {
		FuncaoDadosProjetoTO fdp = new FuncaoDadosProjetoTO();
		fdp.setProjetoTO(this.funcaoProjetoFiltroBean.getProjetoFiltro());
		return fdp;
	}

	@Override
	protected void setVo(FuncaoDadosProjetoTO vo) {
		this.funcaoDadosProjetoTO = vo;
	}

	@Override
	protected IDataModel<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoTO> getListagem() {
		return this.funcaoDadosProjetoDataModel;
	}

	@Override
	protected void setListagem(IDataModel<FuncaoDadosProjetoTO, Long, FuncaoDadosProjetoTO> listagem) {
		this.funcaoDadosProjetoDataModel = listagem;

	}

	public FuncaoDadosProjetoTO getFuncaoDadosProjetoTO() {
		return funcaoDadosProjetoTO;
	}

	public void setFuncaoDadosProjetoTO(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {
		this.funcaoDadosProjetoTO = funcaoDadosProjetoTO;
	}

	public String getNomeFuncao() {
		return nomeFuncao;
	}

	public void setNomeFuncao(String nomeFuncao) {
		this.nomeFuncao = nomeFuncao;
	}

	public FuncaoProjetoFiltroBean getFuncaoProjetoFiltroBean() {
		return funcaoProjetoFiltroBean;
	}

	public void setFuncaoProjetoFiltroBean(FuncaoProjetoFiltroBean funcaoProjetoFiltroBean) {
		this.funcaoProjetoFiltroBean = funcaoProjetoFiltroBean;
	}

}