package br.com.sgpf.metrica.factory;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.enumeration.BancoDadosImportacaoEnum;
import br.com.sgpf.metrica.enumeration.CategoriaEnum;
import br.com.sgpf.metrica.enumeration.FormatoEnum;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;
import br.com.sgpf.metrica.enumeration.TipoAnalistaEnum;
import br.com.sgpf.metrica.enumeration.TipoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoEntidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoDadosProjetoEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;
import br.com.sgpf.metrica.enumeration.TipoPerfilEnum;

@Name("enumFactory")
public class EnumFactory implements Serializable {

	private static final long serialVersionUID = -6554938963405369747L;

	@Factory(value = "statusEnum", scope = ScopeType.APPLICATION)
	public StatusEnum[] getStatus() {
		return StatusEnum.values();
	}

	@Factory(value = "simNaoEnum", scope = ScopeType.APPLICATION)
	public SimNaoEnum[] getSimNao() {
		return SimNaoEnum.values();
	}

	@Factory(value = "tipoPerfilEnum", scope = ScopeType.APPLICATION)
	public TipoPerfilEnum[] getTipoPerfil() {
		return TipoPerfilEnum.values();
	}

	@Factory(value = "categoriaEnum", scope = ScopeType.APPLICATION)
	public CategoriaEnum[] getCategorial() {
		return CategoriaEnum.values();
	}

	@Factory(value = "formatoEnum", scope = ScopeType.APPLICATION)
	public FormatoEnum[] getFormato() {
		return FormatoEnum.values();
	}

	@Factory(value = "tipoEntidadeEnum", scope = ScopeType.APPLICATION)
	public TipoEntidadeEnum[] getTipoEntidade() {
		return TipoEntidadeEnum.values();
	}

	@Factory(value = "tipoElementoContagemEnum", scope = ScopeType.APPLICATION)
	public TipoElementoContagemEnum[] getTipoElementoContagem() {
		return TipoElementoContagemEnum.values();
	}

	@Factory(value = "statusProjetoEnum", scope = ScopeType.APPLICATION)
	public StatusProjetoEnum[] getStatusProjeto() {
		return StatusProjetoEnum.values();
	}

	@Factory(value = "tipoContagemEnum", scope = ScopeType.APPLICATION)
	public TipoContagemEnum[] getTipoContagem() {
		return TipoContagemEnum.values();
	}

	@Factory(value = "tipoFuncaoDadosProjetoEnum", scope = ScopeType.APPLICATION)
	public TipoFuncaoDadosProjetoEnum[] getTipoFuncaoDadosProjeto() {
		return TipoFuncaoDadosProjetoEnum.values();
	}

	@Factory(value = "tipoFuncaoTransacaoProjetoEnum", scope = ScopeType.APPLICATION)
	public TipoFuncaoTransacaoEnum[] getTipoFuncaoTransacaoProjeto() {
		return TipoFuncaoTransacaoEnum.values();
	}

	@Factory(value = "tipoItemMensuraveisEnum", scope = ScopeType.APPLICATION)
	public TipoItemMensuraveisEnum[] getTipoItemMensuraveisEnum() {
		return TipoItemMensuraveisEnum.values();
	}

	@Factory(value = "bancoDadosImportacao", scope = ScopeType.APPLICATION)
	public BancoDadosImportacaoEnum[] getOpcoesBancosDadosImportacao() {
		return BancoDadosImportacaoEnum.values();
	}

	@Factory(value = "tipoAnalista", scope = ScopeType.APPLICATION)
	public TipoAnalistaEnum[] getTipoAnalistaEnum() {
		return TipoAnalistaEnum.values();
	}

}
