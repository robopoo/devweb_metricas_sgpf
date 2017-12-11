package br.com.sgpf.metrica.bean;

import java.math.BigDecimal;
import java.util.Date;

public class CabecalhoRelatorio {

	
	private String fornecedor;
	
	private String projeto;
	
	private String subProjeto;
	
	private String sistema;
	
	private String analistaFornecedor;
	
	private BigDecimal pfTotal;
	
	private BigDecimal pfServicosNaoMensuraveis;
	
	private BigDecimal pfFuncoesMensuraveis;
	
	private String dtCriacao;

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getSubProjeto() {
		return subProjeto;
	}

	public void setSubProjeto(String subProjeto) {
		this.subProjeto = subProjeto;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getAnalistaFornecedor() {
		return analistaFornecedor;
	}

	public void setAnalistaFornecedor(String analistaFornecedor) {
		this.analistaFornecedor = analistaFornecedor;
	}

	public BigDecimal getPfTotal() {
		return pfTotal;
	}

	public void setPfTotal(BigDecimal pfTotal) {
		this.pfTotal = pfTotal;
	}

	public BigDecimal getPfServicosNaoMensuraveis() {
		return pfServicosNaoMensuraveis;
	}

	public void setPfServicosNaoMensuraveis(BigDecimal pfServicosNaoMensuraveis) {
		this.pfServicosNaoMensuraveis = pfServicosNaoMensuraveis;
	}

	public BigDecimal getPfFuncoesMensuraveis() {
		return pfFuncoesMensuraveis;
	}

	public void setPfFuncoesMensuraveis(BigDecimal pfFuncoesMensuraveis) {
		this.pfFuncoesMensuraveis = pfFuncoesMensuraveis;
	}

	public String getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(String dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	
	
	
}
