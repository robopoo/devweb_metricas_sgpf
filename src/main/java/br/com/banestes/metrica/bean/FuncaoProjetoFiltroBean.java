/**
 * 
 */
package br.com.sgpf.metrica.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.util.JSFUtil;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("funcaoProjetoFiltroBean")
@Scope(ScopeType.CONVERSATION)
public class FuncaoProjetoFiltroBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3194842241363633177L;
	
	private static final String SESSION_ATT_NAME = "idProjetoFiltroSession";
	
	private EmpresaTO empresaFiltro;

	private ContratoTO filtroContrato;

	private SistemaTO filtroSistema;

	private ProjetoTO projetoFiltro;
	
	@In(create = true)
    private ProjetoDAO projetoDAO;

	private List<ProjetoTO> projetosSistemaContrato;

	private List<ProjetoTO> projetosFiltrados;

	private Set<String> selecaoProjetos;

	private String projetoSelecionado;
	
	public FuncaoProjetoFiltroBean(){
		
	}

	@Create()
	public void atualizarFiltro(){

		Long idProjeto = (Long) JSFUtil.getSessionAttribute(SESSION_ATT_NAME);

		if(idProjeto != null){
			ProjetoTO projetoTO = projetoDAO.findById(idProjeto);
			this.setEmpresaFiltro(projetoTO.getSistemaTO().getEmpresaTO());
			this.setFiltroContrato(projetoTO.getContratoTO());
			this.setFiltroSistema(projetoTO.getSistemaTO());
			this.setProjetoFiltro(projetoTO);
			this.setProjetoSelecionado(projetoTO.getProjeto());
			this.carregarProjetos();
			this.carregarSubProjetos();
		}

	}
	
	
	public void carregarProjetos() {
		this.selecaoProjetos = new HashSet<String>();
		
		this.projetosSistemaContrato = null;

		if (this.filtroSistema != null && this.filtroContrato != null) {

			this.projetosSistemaContrato = projetoDAO.findAllBySistemaContrato(
					this.filtroSistema, this.filtroContrato); 

			for (ProjetoTO proj : this.projetosSistemaContrato) {
				this.selecaoProjetos.add(proj.getProjeto());
			}
		}
	}

	public void carregarSubProjetos() {

		if (this.projetoSelecionado != null && !"".equals(this.projetoSelecionado)) {

			this.projetosFiltrados = new ArrayList<ProjetoTO>();

			for (ProjetoTO proj : this.projetosSistemaContrato) {
				if(this.projetoSelecionado.equals(proj.getProjeto())){
					this.projetosFiltrados.add(proj);
				}
			}
		}
	}
	
	public void carregarProjetoSessao(){
		JSFUtil.setSessionAttribute(SESSION_ATT_NAME, this.projetoFiltro.getIdProjeto());
		JSFUtil.addInfoMessageFromResource("msg.projeto.sessao.armazenar");
	}
	
	public void limparProjetoSessao(){
		JSFUtil.setSessionAttribute(SESSION_ATT_NAME, null);
		this.projetoFiltro = null;
		this.filtroContrato = null;
		this.filtroSistema = null;
		this.selecaoProjetos = null;
		this.empresaFiltro = null;
		JSFUtil.addInfoMessageFromResource("msg.projeto.sessao.limpar");
	}
	
	public SistemaTO getFiltroSistema() {
		return filtroSistema;
	}

	public void setFiltroSistema(SistemaTO filtroSistema) {
		this.filtroSistema = filtroSistema;
	}

	/**
	 * @return the empresaFiltro
	 */
	public EmpresaTO getEmpresaFiltro() {
		return empresaFiltro;
	}

	/**
	 * @param empresaFiltro
	 *            the empresaFiltro to set
	 */
	public void setEmpresaFiltro(EmpresaTO empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}

	public ContratoTO getFiltroContrato() {
		return filtroContrato;
	}

	public void setFiltroContrato(ContratoTO filtroContrato) {
		this.filtroContrato = filtroContrato;
	}

	public List<ProjetoTO> getProjetosFiltrados() {
		return projetosFiltrados;
	}

	public void setProjetosFiltrados(List<ProjetoTO> projetosFiltrados) {
		this.projetosFiltrados = projetosFiltrados;
	}

	public void setSelecaoProjetos(Set<String> selecaoProjetos) {
		this.selecaoProjetos = selecaoProjetos;
	}

	public Set<String> getSelecaoProjetos() {
		return selecaoProjetos;
	}

	public String getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(String projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public ProjetoTO getProjetoFiltro() {
		return projetoFiltro;
	}

	public void setProjetoFiltro(ProjetoTO projetoFiltro) {
		this.projetoFiltro = projetoFiltro;
	}

	public ProjetoDAO getProjetoDAO() {
		return projetoDAO;
	}

	public void setProjetoDAO(ProjetoDAO projetoDAO) {
		this.projetoDAO = projetoDAO;
	}


}