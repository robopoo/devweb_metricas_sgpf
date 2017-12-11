package br.com.sgpf.metrica.bean;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.EntidadeTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.enumeration.BancoDadosImportacaoEnum;
import br.com.sgpf.metrica.enumeration.FormatoEnum;
import br.com.sgpf.metrica.jdbc.ConexaoOracle;
import br.com.sgpf.metrica.jdbc.ConexaoSimples;
import br.com.sgpf.metrica.jdbc.ConexaoSqlServer;
import br.com.sgpf.metrica.jdbc.ConfiguracaoConexaoBD;
import br.com.sgpf.metrica.jdbc.MetadadosBD;

@Name("entidadeBean")
@Scope(ScopeType.CONVERSATION)
public class EntidadeBean extends
AbstractCrudWebBean<EntidadeTO, Long, EntidadeDAO, EntidadeTO> {


	private static final long serialVersionUID = -3752690438982183865L;

	@Out(required = false)
	private EntidadeTO entidadeFiltro;

	private EmpresaTO empresaFiltro;

	@In(create = true)
	@Out(required = false)
	private ConfiguracaoConexaoBD conexaoBanco;

	@In(create = true)
	@Out(required = false)
	private IDataModel<EntidadeTO, Long, EntidadeTO> entidadeDataModel;

	@DataModel(scope=ScopeType.PAGE)
	List<EntidadeTO> entidadeTOlist;

	@DataModelSelection(value = "entidadeTOlist")
	EntidadeTO etidadeTOSelect;

	@DataModel
	List<AtributoTO> atributoTOlist;

	@DataModelSelection(value = "atributoTOlist")
	AtributoTO atributoTOSelect;

	@Out(required = false)
	AtributoTO atributoTONovo;
	
	@Out(required = false)
	AtributoTO atributoTOEditado;

	@Out(required = false)
	private EntidadeTO entidadeTO;

	@In(create = true)
	private EntidadeDAO entidadeDAO;

	@In(required=false)
	EntidadeTO registroDTimp;

	boolean selecionarTodosImportacao;
	
	private BancoDadosImportacaoEnum bancoDadosImportacao;
	
	@Override
	public void preRender() {
		super.preRender();
		this.entidadeFiltro = new EntidadeTO();
		this.entidadeFiltro.setSistemaTO(new SistemaTO());
		this.empresaFiltro = null;
		this.inicializarAtributo();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}


	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.entidadeFiltro = new EntidadeTO();
		this.entidadeFiltro.setSistemaTO(new SistemaTO());
		this.empresaFiltro = null;
		this.inicializarAtributo();
		getListagem().limpar();
	}

	@Override
	protected EntidadeDAO getDao() {
		return entidadeDAO;
	}

	@Override
	protected EntidadeTO getVo() {
		return entidadeTO;
	}

	@Override
	protected EntidadeTO getFiltro() {
		return entidadeFiltro;
	}

	@Override
	protected void setVo(EntidadeTO vo) {
		this.entidadeTO = vo;

	}

	@Override
	protected IDataModel<EntidadeTO, Long, EntidadeTO> getListagem() {
		return entidadeDataModel;
	}

	@Override
	protected void setListagem(IDataModel<EntidadeTO, Long, EntidadeTO> listagem) {
		this.entidadeDataModel = listagem;
	}

	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((EntidadeTO) getSelecao().clone());
		setOperacao(OperacoesEnum.EXCLUSAO);
		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	public String novo() {
		super.novo();
		atributoTOlist = new ArrayList<AtributoTO>();
		inicializarAtributo();

		SistemaTO sistemaTO = new SistemaTO();
		this.entidadeTO.setSistemaTO(sistemaTO);
		sistemaTO.setEmpresaTO(new EmpresaTO());
		this.entidadeTO.setQtTd(0);
		this.entidadeTO.setStatus(StatusEnum.ATIVADO);

		return getInclusaoOutcome();

	}

	public String importar() {
		for (EntidadeTO item : this.entidadeTOlist) {

			if(item.isImportar()){
				item.setSistemaTO(this.entidadeFiltro.getSistemaTO());
				entidadeDAO.insert(item);
			}
		}

		this.addInfoMessageFromResource("operacao.realizada.sucesso") ;
		
		getListagem().limpar();
		setConsultaRealizada(false);
		
		return getSucessOutcome();
	}

	@TransactionExceptionInterceptor
	public void buscarImportacao(){

		if(this.conexaoBanco != null){
			try{
				ConexaoSimples conexaoSimples;
				
				if(BancoDadosImportacaoEnum.ORACLE.equals(this.bancoDadosImportacao)){
					conexaoSimples = new ConexaoOracle(this.conexaoBanco);
				}else{
					conexaoSimples = new ConexaoSqlServer(this.conexaoBanco);
				}
				
				MetadadosBD metadadosBD = new MetadadosBD(conexaoSimples, conexaoSimples.getSchema());
				
				ImportacaoEntidade importacaoBean = new ImportacaoEntidade(metadadosBD);
				
				this.entidadeTOlist = importacaoBean.findAll();
			}catch(SQLException e){
				String erro;
				if(e.getCause() != null){
					erro =  e.getCause().getMessage();
				}else{
					erro =  e.toString();
				}
				addErrorMessageFromResource("erro.conexao.bd", erro);
				this.entidadeTOlist = new ArrayList<EntidadeTO>();
			}

		}else{
			this.entidadeTOlist = new ArrayList<EntidadeTO>();
		}

	}

	public boolean isEntidadeDuplicada(){
		if(this.registroDTimp != null){
			return this.entidadeDAO.countEntidadesByNome(this.registroDTimp.getNmEntidade(), this.entidadeFiltro.getSistemaTO(), null) > 0;
		}else{
			return false;
		}

	}

	public void selecionarTodos(){

		this.selecionarTodosImportacao = !this.selecionarTodosImportacao;

		for(EntidadeTO entidadeTO : entidadeTOlist){
			entidadeTO.setImportar(this.selecionarTodosImportacao);
		}
	}

	@Override
	public String visualizar() {
		super.visualizar();
		atributoTOlist = entidadeTO.getAtributoTOlist();
		if (atributoTOlist == null)
			atributoTOlist = new ArrayList<AtributoTO>();
		return getVisualizacaoOutcome();
	}

	public void ativar() {
		setVo((EntidadeTO) getSelecao().clone());
		getVo().setStatus(StatusEnum.ATIVADO);
		setOperacao(OperacoesEnum.ALTERACAO);
		super.gravar();
	}

	public void desativar() {
		setVo((EntidadeTO) getSelecao().clone());
		getVo().setStatus(StatusEnum.DESATIVADO);
		setOperacao(OperacoesEnum.ALTERACAO);
		super.gravar();
	}

	@Override
	public String gravar() {
		
		boolean erro = false;
		
		if(this.atributoTOlist == null || this.atributoTOlist.isEmpty()){
			addErrorMessageFromResource("msg.erro.entidade.sem.atributo");
			erro = true;
		}

		int qtEntidades = this.entidadeDAO.countEntidadesByNome(this.entidadeTO.getNmEntidade(), this.entidadeTO.getSistemaTO(), this.entidadeTO.getIdEntidade());

		if(qtEntidades > 0){
			addErrorMessageFromResource("msg.erro.entidade.duplicada");
			erro = true;
		}

		if(this.entidadeTO.getNmEntidade().trim().split(" ").length > 1
				|| "".equals(this.entidadeTO.getNmEntidade().trim())){
			addErrorMessageFromResource("msg.erro.entidade.nome.espaco");
			erro = true;
		}
		
		this.entidadeTO.setNmEntidade(this.entidadeTO.getNmEntidade().trim());

		if(erro){
			return null;
		}
		
		validarAtributo();

		entidadeTO.setAtributoTOlist(atributoTOlist);

		super.gravar();
		atributoTOlist = new ArrayList<AtributoTO>();
		
		getListagem().limpar();
		setConsultaRealizada(false);
		
		return getSucessOutcome();
	}
	
	@Override
	public String editar() {
		super.editar();
		if (entidadeTO.getAtributoTOlist() != null) {
			atributoTOlist = entidadeTO.getAtributoTOlist();

		} else {
			atributoTOlist = new ArrayList<AtributoTO>();
		}

		atributoTONovo = new AtributoTO();
		inicializarAtributo();

		return getAlteracaoOutcome();
	}
	
	public void validarAtributo(){
		this.entidadeTO.setQtTd(0);
		
		for (AtributoTO a : atributoTOlist) {
			a.setEntidadeTO(entidadeTO);

			if(SimNaoEnum.SIM.equals(a.getFlReconhecido())){
				this.entidadeTO.addQtTd();
			}
			
			if(FormatoEnum.DATA.equals(a.getTpFormato())){
				a.setNrTamanho(0);
			}
			
			if(!FormatoEnum.NUMERO.equals(a.getTpFormato())
					|| ( FormatoEnum.NUMERO.equals(a.getTpFormato()) && a.getPrecisao() == null )){
				a.setPrecisao(0);
			}
		}
		
	}

	public void editarAtributo() {
		atributoTONovo = (AtributoTO) atributoTOSelect.clone();
		atributoTOEditado = atributoTOSelect;
	}
	
	public void addAtributo() {

		boolean erro = false;
		if (this.atributoTONovo == null || this.atributoTONovo.getNmAtributo().trim().isEmpty()) {
			addErrorMessageFromResource("msg.erro.entidade.atributo.sem.nome");
			erro = true;
		}else{

			for (AtributoTO item : atributoTOlist) {
				if((atributoTOEditado == null || atributoTOEditado != item) && atributoTONovo.getNmAtributo().trim().equals(item.getNmAtributo().trim())){
					addErrorMessageFromResource("msg.erro.entidade.atributo.duplicado");
					erro = true;
					break;
				}
			}
		}
		
		
		if (atributoTONovo == null || atributoTONovo.getDsAtributo().trim().isEmpty()) {

			addErrorMessageFromResource("msg.erro.entidade.atributo.sem.descricao");
			erro = true;
		}

		if(this.atributoTONovo.getNmAtributo().trim().split(" ").length > 1){
			addErrorMessageFromResource("msg.erro.entidade.atributo.nome.espaco");
			erro = true;
		}
		
		this.atributoTONovo.setNmAtributo(this.atributoTONovo.getNmAtributo().trim());
		
		if (!erro) {

			if(atributoTOEditado == null){
				atributoTOlist.add(atributoTONovo);
			}else{
				atributoTOlist.set(atributoTOlist.indexOf(atributoTOEditado), atributoTONovo);
			}
			
			atributoTOEditado = null;
			
			inicializarAtributo();
			validarAtributo();
		}
	}
	
	public void inicializarAtributo(){
		atributoTONovo = new AtributoTO();
		atributoTONovo.setStatus(StatusEnum.ATIVADO);
		atributoTONovo.setFlReconhecido(SimNaoEnum.SIM);
	}

	public void removerAtributo() {
		boolean erro = atributoTOlist.remove(atributoTOSelect);

		atributoTOSelect.setEntidadeTO(null);

		if (!erro) {
			addErrorMessageFromResource("Elemento não localizado");
		}
		
		this.validarAtributo();
	}	
	
	public void ativarAtributo(){
		this.atributoTOSelect.setStatus(StatusEnum.ATIVADO);
	}
	
	public void desativarAtributo(){
		this.atributoTOSelect.setStatus(StatusEnum.DESATIVADO);
	}

	public EmpresaTO getEmpresaFiltro() {
		return empresaFiltro;
	}

	public void setEmpresaFiltro(EmpresaTO empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}

	public EntidadeTO getEntidadeFiltro() {
		return entidadeFiltro;
	}

	public void setEntidadeFiltro(EntidadeTO entidadeFiltro) {
		this.entidadeFiltro = entidadeFiltro;
	}
	public ConfiguracaoConexaoBD getConexaoBanco() {
		return conexaoBanco;
	}

	public void setConexaoBanco(ConfiguracaoConexaoBD conexaoBanco) {
		this.conexaoBanco = conexaoBanco;
	}


	public BancoDadosImportacaoEnum getBancoDadosImportacao() {
		return bancoDadosImportacao;
	}


	public void setBancoDadosImportacao(BancoDadosImportacaoEnum bancoDadosImportacao) {
		this.bancoDadosImportacao = bancoDadosImportacao;
	}

	
}
